package br.org.unesco.appesca.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import br.org.unesco.appesca.R;
import br.org.unesco.appesca.control.FormularioListDetailFragment;
import br.org.unesco.appesca.dao.UsuarioDAO;
import br.org.unesco.appesca.model.Identity;
import br.org.unesco.appesca.util.AppescaUtil;
import br.org.unesco.appesca.util.ConnectionNetwork;
import br.org.unesco.appesca.util.ConstantesREST;
import br.org.unesco.appesca.util.DBBitmapUtil;

/**
 * @author yesus
 */
public class PrincipalUnescoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    View cabecalhoNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawer.openDrawer(GravityCompat.START);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cabecalhoNavigationView = navigationView.getHeaderView(0);

        carregarDadosUsuario();

        listarFormularios(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        listarFormularios(0);
    }

    /**
     * Para carregar os dados do usuário no menu de hamburguer.
     */
    private void carregarDadosUsuario(){

        if(Identity.getUsuarioLogado()!=null && Identity.getUsuarioLogado().getEmail()!=null) {
            TextView txtEmailUsuarioLogado = (TextView) cabecalhoNavigationView.findViewById(R.id.txtEmailUsuarioLogado);
            txtEmailUsuarioLogado.setText(Identity.getUsuarioLogado().getEmail());
        }

        if(Identity.getUsuarioLogado()!=null && Identity.getUsuarioLogado().getNome()!=null) {
            TextView txtNomeUsuarioLogado = (TextView) cabecalhoNavigationView.findViewById(R.id.txtNomeUsuarioLogado);
            txtNomeUsuarioLogado.setText(Identity.getUsuarioLogado().getNome());
        }

        if(Identity.getUsuarioLogado()!=null && Identity.getUsuarioLogado().getPerfil()!=null) {
            TextView txtPerfilUsuarioLogado = (TextView) cabecalhoNavigationView.findViewById(R.id.txtPerfilUsuarioLogado);
            txtPerfilUsuarioLogado.setText(Identity.getUsuarioLogado().getPerfil());
        }

        final ImageView fotoUsuario = (ImageView) cabecalhoNavigationView.findViewById(R.id.imgFotoUsuarioLogado);

        if(Identity.getUsuarioLogado()!= null && Identity.getUsuarioLogado().getImagem()==null && ConnectionNetwork.verifiedInternetConnection(this)) {
            new AsyncTask<Void, Void, Void>() {
                Bitmap bitmap;

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        URL newurl = new URL(
                                ConstantesREST.getURLService(ConstantesREST.IMAGEM_USUARIO)
                                        + "?login=" + Identity.getUsuarioLogado().getLogin() + "&senha=" + Identity.getUsuarioLogado().getSenha());
                        bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());


                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    if(bitmap!=null) {
                        fotoUsuario.setImageBitmap(bitmap);
                        fotoUsuario.setVisibility(View.VISIBLE);

                        Identity.getUsuarioLogado().setImagem(DBBitmapUtil.getBytes(bitmap));
                     }
                    new UsuarioDAO(PrincipalUnescoActivity.this).save(Identity.getUsuarioLogado());

//                    super.onPostExecute(aVoid);
                }
            }.execute();
        }else{
            if(Identity.getUsuarioLogado()!=null && Identity.getUsuarioLogado().getImagem()!=null) {
                fotoUsuario.setImageBitmap(DBBitmapUtil.getImage(Identity.getUsuarioLogado().getImagem()));
                fotoUsuario.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            encerrarApp();
        }
    }

    public void encerrarApp(){
        new AlertDialog.Builder(this)
                .setTitle("Appesca")
                .setMessage("Deseja realmente fechar a aplicação?")
                .setIcon(android.R.drawable.ic_dialog_alert)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Identity.setUsuarioLogado(null);recreate();
                        LoginUnescoActivity.ENCERRAR_APP = true;
                        finish();
                        Toast.makeText(getApplicationContext(), "Appesca Encerrado!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }


    public void efetuarOutroLogin(){
        new AlertDialog.Builder(this)
                .setTitle("Appesca")
                .setMessage("Esta sessão será encerrada. Deseja entrar com outro usuário?")
                .setIcon(android.R.drawable.ic_dialog_alert)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Identity.setUsuarioLogado(null);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itemClose) {
            encerrarApp();
        }
        if (id == R.id.itemLogout) {
            efetuarOutroLogin();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_enviados_para_aprovacao) {
            listarFormularios(1);
        } else if (id == R.id.nav_nao_enviados) {
            listarFormularios(2);
        }else if (id == R.id.nav_todos) {
            listarFormularios(0);
        }else if (id == R.id.nav_enviados_aprovados) {
            listarFormularios(3);
        }

        else if(id == R.id.novoCamaraoRegional ){
            abrirFormulario("Camarão Regional", 1);
        }else if(id == R.id.novoCamaraoECaranguejo){
            abrirFormulario("Caranguejo", 2);
        }else if(id == R.id.novoCamaraoEBranco){
            abrirFormulario("Camarão Piticaia e Branco", 3);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private  void abrirFormulario(final String nome, final int tipoFormulario){

        new AlertDialog.Builder(this)
                .setTitle("Appesca")
                .setMessage("Deseja realmente cadastrar uma nova pesquisa?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppescaUtil.limparMemoria();
                        Intent intent = new Intent(PrincipalUnescoActivity.this, FormCamRegActivityNew.class);
                        intent.putExtra(FormCamRegActivityNew.ID_NOME_FORMULARIO, nome);
                        intent.putExtra(FormCamRegActivityNew.ID_TIPO_FORMULARIO, tipoFormulario);
                        PrincipalUnescoActivity.this.startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

    }

    private void listarFormularios(int value) {
        Bundle arguments = new Bundle();
        arguments.putInt(FormularioListDetailFragment.ARG_ITEM_ID, value);
        FormularioListDetailFragment fragment = new FormularioListDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.formulario_detail_container, fragment)
                .commit();
    }
}
