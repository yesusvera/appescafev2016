package br.org.unesco.appesca.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.org.unesco.appesca.R;
import br.org.unesco.appesca.bo.FormularioBO;
import br.org.unesco.appesca.bo.QuestaoBO;
import br.org.unesco.appesca.control.QuestaoDetailFragment;
import br.org.unesco.appesca.dao.FormularioDAO;
import br.org.unesco.appesca.dao.PerguntaDAO;
import br.org.unesco.appesca.dao.QuestaoDAO;
import br.org.unesco.appesca.dao.RespostaDAO;
import br.org.unesco.appesca.model.Formulario;
import br.org.unesco.appesca.model.Identity;
import br.org.unesco.appesca.model.LocResidencia;
import br.org.unesco.appesca.model.Pergunta;
import br.org.unesco.appesca.model.Questao;
import br.org.unesco.appesca.model.Resposta;
import br.org.unesco.appesca.util.AppescaUtil;
import br.org.unesco.appesca.util.ConnectionNetwork;
import br.org.unesco.appesca.util.ConstantesIdsFormularios;

public class FormCamRegActivityNew extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * ID EXTRAS
     */
    public static String ID_FORMULARIO_OPEN = "ID_FORMULARIO_OPEN";
    public static String ID_TIPO_FORMULARIO = "ID_TIPO_FORMULARIO";
    public static String ID_NOME_FORMULARIO = "ID_NOME_FORMULARIO";


    public static Formulario formulario;
    public static LocResidencia locResidencia = null;

    public static int  id_activity_questao_atual = 0;
    public static int posAtual = 0;
    public static int postTempRevert = 0;


    private String nomeFormulario;
    private Integer tipoFormulario;

    private QuestaoDAO questaoDAO;
    private RespostaDAO respostaDAO;
    private PerguntaDAO perguntaDAO;
    private QuestaoBO questaoBO;

    public static int[] arraysIdsMenuLateral;
    public static int[] arrayIdsQuestoes;

    View cabecalhoNavigationView;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras()!=null && getIntent().getExtras().get(ID_FORMULARIO_OPEN)!=null) {
            formulario = (Formulario) getIntent().getExtras().get(ID_FORMULARIO_OPEN);


            if(formulario!=null){
                tipoFormulario = formulario.getIdTipoFormulario();
                nomeFormulario = formulario.getNome();
            }
        }

        if(getIntent().getExtras()!=null && getIntent().getExtras().get(ID_TIPO_FORMULARIO)!=null) {
            tipoFormulario = (Integer) getIntent().getExtras().get(ID_TIPO_FORMULARIO);
        }

        if(getIntent().getExtras()!=null && getIntent().getExtras().get(ID_NOME_FORMULARIO)!=null) {
            nomeFormulario = (String) getIntent().getExtras().get(ID_NOME_FORMULARIO);
        }


        switch (tipoFormulario){
            case 1: setContentView(R.layout.activity_formcamreg);
                    setTitle("Formulário Camarão Regional");
                    arraysIdsMenuLateral = ConstantesIdsFormularios.arraysIdsMenuLateralCamaraoRegional;
                    arrayIdsQuestoes = ConstantesIdsFormularios.arrayIdsFormularioCamaraoRegional;
                    break;
            case 2:
                    setContentView(R.layout.activity_formcaranguejo);
                    setTitle("Formulário Caranguejo");
                    arraysIdsMenuLateral = ConstantesIdsFormularios.arraysIdsMenuLateralCaranguejo;
                    arrayIdsQuestoes = ConstantesIdsFormularios.arrayIdsFormularioCaranguejo;
                    break;
            case 3:
                    setContentView(R.layout.activity_formpiticaiabranco);
                    setTitle("Formulário Camarão Piticaia e Branco");
                    arraysIdsMenuLateral = ConstantesIdsFormularios.arraysIdsMenuLateralPiticaiaEBranco;
                    arrayIdsQuestoes = ConstantesIdsFormularios.arrayIdsFormularioPiticaiaEBranco;
                    break;
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.mipmap.ic_launcher);

//        formulario = null;

        questaoDAO = new QuestaoDAO(FormCamRegActivityNew.this);
        respostaDAO = new RespostaDAO(FormCamRegActivityNew.this);
        perguntaDAO = new PerguntaDAO(FormCamRegActivityNew.this);
        questaoBO = new QuestaoBO(FormCamRegActivityNew.this);

        FloatingActionButton fabRevert = (FloatingActionButton) findViewById(R.id.float_button_menu_back);

        fabRevert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppescaUtil.limparMemoria();
                if (posAtual == 0) {
                    new AlertDialog.Builder(FormCamRegActivityNew.this)
                            .setTitle("Appesca")
                            .setMessage("Você está na primeira questão. Não é possível voltar.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                } else {
                    new AlertDialog.Builder(FormCamRegActivityNew.this)
                            .setTitle("Appesca")
                            .setMessage("Antes de voltar para a questão anterior, o que deseja fazer?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Ir para a questão anterior sem salvar.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    openFragment(arrayIdsQuestoes[--posAtual]);
                                }
                            }).setNegativeButton("Salvar a atual e ir para a anterior.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    salvarQuestaoAtual(false);
//                                    openFragment(arrayIdsQuestoes[--posAtual]);


                                }
                            }).show();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.float_button_menu_save);
        //SALVAR AS RESPOSTAS DA QUESTAO
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction()==MotionEvent.ACTION_UP) {
                    salvarQuestaoAtual(true);
                    AppescaUtil.limparMemoria();
                }

                return true;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(GravityCompat.START);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        cabecalhoNavigationView = navigationView.getHeaderView(0);

        menu = navigationView.getMenu();


        //TODO if(insert){
        inicializaFormulario();
        //}else{//TODO Update{
        //}
        openFragment(R.layout.localizacao_residencia_form);

        configurarAcoesConformeSituacao(fabRevert, fab);
    }

    private void configurarAcoesConformeSituacao(FloatingActionButton fabRevert, FloatingActionButton fab) {
        if(formulario!=null && formulario.getSituacao()>0){
            fab.setVisibility(View.INVISIBLE);
            fabRevert.setVisibility(View.INVISIBLE);
        }
    }


    private void configuraVisibilidadeBotoesNavegacao(boolean enabled){
        FloatingActionButton floatButtonVoltar = (FloatingActionButton) findViewById(R.id.float_button_menu_back);
        FloatingActionButton floatButtonSalvar = (FloatingActionButton) findViewById(R.id.float_button_menu_save);


        if(floatButtonVoltar!=null){
            floatButtonVoltar.setVisibility(enabled?View.VISIBLE:View.INVISIBLE);
        }
        if(floatButtonSalvar!=null){
            floatButtonSalvar.setVisibility(enabled?View.VISIBLE:View.INVISIBLE);
        }
        
    }
    
    
    /**
     * @param direcao true para avançar e false para retroceder
     */
    private void salvarQuestaoAtual(final boolean direcao){

        configuraVisibilidadeBotoesNavegacao(false);

        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        final ProgressDialog ringProgressDialog = ProgressDialog.show(FormCamRegActivityNew.this, "Aguarde ...", "Salvando questão ...", true);

        ringProgressDialog.setCancelable(false);


        new AsyncTask<Void,Void,Integer>(){

            @Override
            protected Integer doInBackground(Void... params) {
                int ordemQuestao = posAtual - 1;
                Questao questao = questaoDAO.findQuestaoByOrdemIdFormulario(ordemQuestao, formulario.getId());

                //PRIMEIRO EXCLUIR TUDO EM CASCATA: RESPOSTAS, PERGUNTAS E QUESTAO. DEPOIS RE-INSERIR.
                if (questao != null) {
                    questaoBO.excluirQuestao(questao);
                    questao = null;
                }

                if (questao == null) {
                    questao = new Questao();
                    questao.setOrdem(ordemQuestao);
                    questao.getFormulario().setId(formulario.getId());
                    questao.setTitulo("Questão " + ordemQuestao);
                    questao = questaoDAO.insertQuestao(questao);

                    questao.setListaPerguntas(buildPerguntaList(questao));

                    if (questao.getListaPerguntas() != null && !questao.getListaPerguntas().isEmpty()) {
                        questaoDAO.updateQuestao(questao);
                    }

                    FormularioBO formularioBO = new FormularioBO();

                    if(questaoBO.temAlgumaResposta(questao) || (posAtual==arrayIdsQuestoes.length-1 && formularioBO.temAudio(formulario) )){
                        return 0;
                    }else{
                        questaoBO.excluirQuestao(questao);
                        return 1;
                    }
                }
                return 0;
            }

            private void abrirQuestao() {
                if(direcao) { //AVANÇAR
                        if (posAtual == arrayIdsQuestoes.length - 1) {
                            posAtual = -1;
                        }
                        openFragment(arrayIdsQuestoes[++posAtual]);
                }else{ //RETROCEDER
                    openFragment(arrayIdsQuestoes[--posAtual]);
                }
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                if(result ==0){
                    new AlertDialog.Builder(FormCamRegActivityNew.this)
                            .setTitle(getString(R.string.app_name))
                            .setMessage("Questão salva na base local.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    abrirQuestao();
                                }
                            })
                            .setNegativeButton("Rever esta questão", null)
                            .setCancelable(false).show();

                    chekListQuestoes();



                }else if(result ==1){
                    new AlertDialog.Builder(FormCamRegActivityNew.this)
                            .setTitle(getString(R.string.app_name))
                            .setMessage("Esta questão não pode ser salva, uma resposta é necessária.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).setCancelable(false).show();
                }
                if(ringProgressDialog!=null && ringProgressDialog.isShowing()) {
                    ringProgressDialog.dismiss();
                }

                configuraVisibilidadeBotoesNavegacao(true);
            }
        }.execute();
    }

    public void capturaLocalizacao() throws SecurityException, Exception{
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        LocationListener listener = new LocationListener() {
            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
//                Toast.makeText(getApplicationContext(), "Status alterado", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderEnabled(String arg0) {
//                Toast.makeText(getApplicationContext(), "Provider Habilitado", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onProviderDisabled(String arg0) {
//                Toast.makeText(getApplicationContext(), "Provider Desabilitado", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLocationChanged(Location location) {
                try {
                    if (formulario != null) {
                        if (formulario.getLatitude() == null && formulario.getLongitude() == null) {
                            formulario.setLatitude(new BigDecimal(location.getLatitude()));
                            formulario.setLongitude(new BigDecimal(location.getLongitude()));

                            FormularioDAO formularioDAO = new FormularioDAO(FormCamRegActivityNew.this);
                            formularioDAO.save(formulario);

                            Toast.makeText(getApplicationContext(), "Localização capturada.", Toast.LENGTH_LONG).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        try {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, listener, null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void inicializaFormulario(){
//        if(getIntent().getExtras()!=null && getIntent().getExtras().get(ID_FORMULARIO_OPEN)!=null) {
//            formulario = (Formulario) getIntent().getExtras().get(ID_FORMULARIO_OPEN);
//        }
        Date dtCriacaoFormulario = new Date();
        if(formulario==null) {
            formulario = new Formulario();

            formulario.setIdTipoFormulario(tipoFormulario);
            formulario.setNome(nomeFormulario);

            formulario.setIdUsuario(Identity.getUsuarioLogado().getId());
            formulario.setDataAplicacao(dtCriacaoFormulario);

            formulario.setSituacao(0);

            FormularioDAO formularioDAO = new FormularioDAO(this);
            formularioDAO.save(formulario);
        }else{
            dtCriacaoFormulario = formulario.getDataAplicacao();
            chekListQuestoes();
        }

        try {
            capturaLocalizacao();
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView txtStatus = (TextView) cabecalhoNavigationView.findViewById(R.id.txtStatus);
        ImageView imgSituacaoFormulario = (ImageView) cabecalhoNavigationView.findViewById(R.id.imgSituacaoFormulario);

        String idSync = formulario.getIdSincronizacao()!=null?formulario.getIdSincronizacao():"";
        switch(formulario.getSituacao()){
            case -1:
                imgSituacaoFormulario.setImageResource(R.drawable.questao_icon_devolvido);
                txtStatus.setText("Devolvido (" + idSync + ")");
                break;
            case 0:
                imgSituacaoFormulario.setImageResource(R.drawable.nao_enviado_icone);
                txtStatus.setText("Não enviado");
                break;
            case 1:
                imgSituacaoFormulario.setImageResource(R.drawable.equipe_icon);
                txtStatus.setText("Em aprovação (" + idSync + ")");
                break;
            case 2:
                imgSituacaoFormulario.setImageResource(R.drawable.enviado_icone);
                txtStatus.setText("Finalizado (" + idSync + ")");
                break;
        }

        TextView txtPesquisador = (TextView) cabecalhoNavigationView.findViewById(R.id.txtPesquisador);
        if (Identity.getUsuarioLogado() != null && Identity.getUsuarioLogado().getNome() != null) {
            txtPesquisador.setText(Identity.getUsuarioLogado().getNome());
        }

        TextView txtDataPesquisa = (TextView) cabecalhoNavigationView.findViewById(R.id.txtDataPesquisa);
        txtDataPesquisa.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dtCriacaoFormulario));

        locResidencia = null;
        id_activity_questao_atual = 0;
        posAtual = 0;
    }


    /**
     * @author yesus
     * Método necessário para assinalar quais questões já foram respondidas.
     */
    private void chekListQuestoes(){
            String lblRespondida = "(Respondida)";
            String lblChecked = "(√)";

            if (formulario != null) {
                List<Questao> listaQuestoes = questaoDAO.getQuestoesByFormulario(formulario.getId());

                for (Questao q : listaQuestoes) {
                    try {
                        int id_item = arraysIdsMenuLateral[q.getOrdem().intValue()+1];

                        MenuItem item = (MenuItem) menu.findItem(id_item);

                        if((q.getOrdem()+1)>1) {
                            item.setIcon(R.drawable.questao_icon_respondida);
                            if(item.getTitle().toString().indexOf(lblRespondida)==-1) {
                                item.setTitle(item.getTitle() + lblRespondida);
                            }
                        }else{
                            if(item.getTitle().toString().indexOf(lblChecked)==-1) {
                                item.setTitle(item.getTitle() + lblChecked);
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }




                ProgressBar progressBar = (ProgressBar)cabecalhoNavigationView.findViewById(R.id.progressBarRespostas);
                TextView txtTextoResposta = (TextView)cabecalhoNavigationView.findViewById(R.id.txtTextoResposta);

                atualizaProgresso(progressBar, txtTextoResposta, listaQuestoes);


                ProgressBar progressBarPrincipal = (ProgressBar)findViewById(R.id.progressBarRespostas);
                TextView txtTextoRespostaPrincipal = (TextView)findViewById(R.id.txtTextoResposta);

                atualizaProgresso(progressBarPrincipal, txtTextoRespostaPrincipal, listaQuestoes);



                if(formulario.getSituacao()<=0) {

                    //Verificando se as questões foram respondidas para alertar sobre envio.
                    if (!entrevistadoEPescadorOuResponsavelFamiliar()) {
                        //Verificar somente até o bloco 4
                        boolean preencheuTodoOBloco4 = true;
                        int posUltQuestaoBloco4 = descobrirPos(R.id.b4_q11);

                        for (int i = 0; i <= posUltQuestaoBloco4; i++) {
                            int id_item = arraysIdsMenuLateral[i];
                            MenuItem item = (MenuItem) menu.findItem(id_item);

                            if (item != null && item.getTitle() != null) {
                                if (item.getTitle().toString().indexOf(lblRespondida) == -1 && item.getTitle().toString().indexOf(lblChecked) == -1) {
                                    preencheuTodoOBloco4 = false;
                                    break;
                                }
                            }
                        }

                        if (preencheuTodoOBloco4) {
                            new AlertDialog.Builder(FormCamRegActivityNew.this)
                                    .setTitle(R.string.app_name)
                                    .setMessage("Você preencheu o formulário até o bloco 4, já pode enviar o formulário.")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.ok, null).show();
                        } else if (posAtual == posUltQuestaoBloco4) {
                            new AlertDialog.Builder(FormCamRegActivityNew.this)
                                    .setTitle(R.string.app_name)
                                    .setMessage("Você precisa preencher o bloco 4 completo, verifique a lista ao lado para ver quais questões ainda não foram respondidas.")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("Ver lista", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                                            if (drawer != null && !drawer.isDrawerOpen(GravityCompat.START)) {
                                                drawer.openDrawer(GravityCompat.START);
                                            }
                                        }
                                    }).show();
                        }

                    } else { // não é pescador
                        //Verificar o formulário completo
                        boolean preencheuOFormularioTodo = true;
                        for (int i = 0; i < arraysIdsMenuLateral.length; i++) {
                            int id_item = arraysIdsMenuLateral[i];
                            MenuItem item = (MenuItem) menu.findItem(id_item);

                            if (item != null && item.getTitle() != null) {
                                if (item.getTitle().toString().indexOf(lblRespondida) == -1 && item.getTitle().toString().indexOf(lblChecked) == -1) {
                                    preencheuOFormularioTodo = false;
                                    break;
                                }
                            }
                        }

                        if (preencheuOFormularioTodo) {
                            new AlertDialog.Builder(FormCamRegActivityNew.this)
                                    .setTitle(R.string.app_name)
                                    .setMessage("Você preencheu o formulário completo, já pode enviar o formulário.")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.ok, null).show();
                        } else if (posAtual == arraysIdsMenuLateral.length - 1) {
                            new AlertDialog.Builder(FormCamRegActivityNew.this)
                                    .setTitle(R.string.app_name)
                                    .setMessage("Você precisa preencher o formulário completo, verifique a lista ao lado para ver quais questões ainda não foram respondidas.")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("Ver lista", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                                            if (drawer != null && !drawer.isDrawerOpen(GravityCompat.START)) {
                                                drawer.openDrawer(GravityCompat.START);
                                            }
                                        }
                                    }).show();
                        }
                    }

                }

            }
    }

    public void atualizaProgresso(ProgressBar progressBar,TextView txtTextoResposta,  List<Questao> listaQuestoes ){
        if(progressBar!=null){
            progressBar.setMax(arraysIdsMenuLateral.length);

            if(listaQuestoes==null || listaQuestoes.size() == 0) {
                progressBar.setProgress(0);
                txtTextoResposta.setText("Nenhuma questão respondida");
            }else {
                progressBar.setProgress(listaQuestoes.size());
                txtTextoResposta.setText((listaQuestoes == null ? 0 : listaQuestoes.size()) + " de " + arraysIdsMenuLateral.length + " respondidas.");
                if(listaQuestoes.size() == arraysIdsMenuLateral.length) {
                    txtTextoResposta.setText(txtTextoResposta.getText() + "\nTodas as questões foram respondidas.\nFormulário pronto para envio");
                }
            }
        }
    }

    private List<Pergunta> buildPerguntaList(Questao questao){
        List<Pergunta> perguntas = new ArrayList<Pergunta>();

        /** PERGUNTAS **/
        for(int seqPergunta=1; seqPergunta<=59; seqPergunta++){
            try {

             String currentPergunta = ConstantesIdsFormularios.PERGUNTA.concat(String.valueOf(seqPergunta));
//           TextView perguntaTextView = (TextView) findViewById(getResources().getIdentifier(currentPergunta ,"id", getPackageName())); //perg1
//
//            if(perguntaTextView != null) {
                List<Resposta> respostas = new ArrayList<Resposta>();

                /** RADIOBUTTON **/
                for (int x = 1; x <= 30; x++) { //
                    String currentRadioButton = currentPergunta.concat(ConstantesIdsFormularios.TYPE_RADIO_BUTTON + x); //1 ou 2 //3 ou 4 //5 e 6
                    RadioButton radioButton = (RadioButton) findViewById(getResources().getIdentifier(currentRadioButton, "id", getPackageName()));

                    if (radioButton != null && radioButton.isChecked()) {
                        Resposta resp = new Resposta();
                        resp.setOpcao(x);
                        resp.setOrdem(x);
                        resp.setTipoComponente("rb");
                        resp.setTexto(radioButton.getText().toString());
                        respostas.add(resp);
                    } else {
                        /** CHECKBOX **/
                        String currentCheckBox = currentPergunta.concat(ConstantesIdsFormularios.TYPE_CHECK_BOX + x);
                        CheckBox checkBox = (CheckBox) findViewById(getResources().getIdentifier(currentCheckBox, "id", getPackageName()));

                        if (checkBox != null && checkBox.isChecked()) {
                            Resposta resp = new Resposta();
                            resp.setOpcao(x);
                            resp.setOrdem(x);
                            resp.setTipoComponente("cb");
                            resp.setTexto(checkBox.getText().toString());
                            respostas.add(resp);
                        } else {
                            /** EDITTEXT **/
                            String currentEditText = currentPergunta.concat(ConstantesIdsFormularios.TYPE_EDIT_TEXT + x);
                            EditText editText = (EditText) findViewById(getResources().getIdentifier(currentEditText, "id", getPackageName()));

                            if (editText != null && !editText.getText().toString().isEmpty()) {
                                Resposta resp = new Resposta();
                                resp.setTexto(editText.getText().toString());
                                resp.setOrdem(x);
                                resp.setTipoComponente("et");
                                resp.setTexto(editText.getText().toString());
                                respostas.add(resp);
                            }
                        }
                    }
                }

                if (respostas != null && respostas.size() > 0) {
                    Pergunta pergunta = perguntaDAO.findPerguntaByOrdemIdQuestao(seqPergunta, questao.getId());

                    if (pergunta == null) {
                        pergunta = new Pergunta();
                        pergunta.setOrdem(seqPergunta);
                        pergunta.setBooleana(false);
                        pergunta.setRespBooleana(false);
                        pergunta.getQuestao().setId(questao.getId());
                        pergunta = perguntaDAO.insertPergunta(pergunta);
                    }

                    for (Resposta resp : respostas) {
                        resp.getPergunta().setId(pergunta.getId());
                        respostaDAO.save(resp);
                    }

                    pergunta.setListaRespostas(respostas);
                    perguntas.add(pergunta);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return perguntas;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            encerrarFormulario();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_formcamreg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itemEnviarFormulario) {
                 enviarFormulario();
        }
            if (id == R.id.itemClose) {
                encerrarFormulario();
            }

        return super.onOptionsItemSelected(item);
    }

    private void enviarFormulario() {

        if(formulario!=null ) {
            if(formulario.getSituacao()==1) {
                new AlertDialog.Builder(FormCamRegActivityNew.this)
                        .setTitle(R.string.app_name)
                        .setMessage("Este formulário está em fase de aprovação. Peça para um coordenador validar no ambiente WEB.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, null).show();
                return;
            }else if(formulario.getSituacao() == 2) {
                new AlertDialog.Builder(FormCamRegActivityNew.this)
                        .setTitle(R.string.app_name)
                        .setMessage("Este formulário já foi enviado e finalizado, não pode ser enviado novamente.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok,null).show();
                return;
            }
        }


        if(!ConnectionNetwork.verifiedInternetConnection(this)){

            new AlertDialog.Builder(FormCamRegActivityNew.this)
                    .setTitle(R.string.app_name)
                    .setMessage("Seu aparelho está sem conectividade com a internet. Por favor habilite seu Wifi ou rede de celular.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok,null).show();
//            String mensagemErro = ;
//            Toast toast = Toast.makeText(FormCamRegActivityNew.this, mensagemErro, Toast.LENGTH_LONG);
//            toast.show();
            return;
        }

        String msgConfirmacao="Este formulário será enviado para aprovação, confirma o envio?";

        if(formulario.getSituacao()==-1){
            msgConfirmacao = "Este formulário devolvido está sendo enviado novamente para a aprovação do coordenador." +
                    "Já realizou todas as correções apontadas pelo coordenador? Confirma o envio?";
        }

        new AlertDialog.Builder(this)
                .setTitle("Appesca")
                .setMessage(msgConfirmacao)
                .setIcon(android.R.drawable.ic_dialog_alert)

                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        boolean enviarSomenteCompleto = false;


                        List<Questao> listaQuestoes = questaoDAO.getQuestoesRespostasByFormulario(formulario.getId());

                        //VALIDACOES QUE IMPEDEM O ENVIO DO FORMULARIO
                        if ((listaQuestoes == null || listaQuestoes.size() == 0)) {

                            Toast.makeText(getApplicationContext(), "Não foi possível enviar. Nenhuma questão foi respondida.", Toast.LENGTH_LONG).show();

                            return;
                        } else if (listaQuestoes.size() < arrayIdsQuestoes.length && enviarSomenteCompleto) {

                            new AlertDialog.Builder(FormCamRegActivityNew.this)
                                    .setTitle("Appesca")
                                    .setMessage("Não foi possível enviar. " +
                                            " Você respondeu somente " + listaQuestoes.size() + " de " + arrayIdsQuestoes.length + " questões." +
                                            "\nVerifique a lista ao lado para validar as questões não respondidas.")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.ok, null).show();

                            return;
                        } else if (listaQuestoes.size() == arrayIdsQuestoes.length || !enviarSomenteCompleto) {

                            FormularioDAO formularioDAO = new FormularioDAO(FormCamRegActivityNew.this);

                            Formulario formTmp = formularioDAO.findById(formulario.getId());
                            formTmp.setListaQuestoes(listaQuestoes);
                            new FormularioBO().enviarFormulario(formTmp, formulario, getApplicationContext(), FormCamRegActivityNew.this);

                        }
                    }
                }). setNegativeButton(android.R.string.no, null). show();
    }

    private void encerrarFormulario() {
        if(formulario!=null && formulario.getSituacao()>0){
            finish();
        }else {

            new AlertDialog.Builder(this)
                    .setTitle("Appesca")
                    .setMessage("As últimas mudanças não salvas serão perdidadas. Deseja encerrar o formulário?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();

        }
    }


    /**
     * @author yesus
     * @param idMenuLateral
     * @return
     */
    private int descobrirPos(int idMenuLateral){
        int i=0;
        for(int idMenu: arraysIdsMenuLateral){
            if(idMenu == idMenuLateral){
                return i;
            }
            i++;
        }
        return 0;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        postTempRevert = posAtual;

        posAtual = descobrirPos(id);
        openFragment(arrayIdsQuestoes[posAtual]);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(final int idLayout) {
        id_activity_questao_atual = idLayout;

        int ordemQuestao = posAtual -1;

        if(!entrevistadoEPescadorOuResponsavelFamiliar() && posAtual > getPosicaoPorIdLayout(R.layout.fcmr_reg_b4_q11)) {
            new AlertDialog.Builder(FormCamRegActivityNew.this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage("O questionário precisa ser respondido apenas até o final do Bloco 4.\nPara responder ao questionário a partir do Bloco 5 o entrevistado deve:\n" +
                            "* Ser responsável pela unidade Familiar (Resposta a última pergunta da Identificação);\n* Ou Possuir como ocupação a pesca de Camarão/Caranguejo (Resposta ao Bloco 2 na questão 1 ou última pergunta da questão 2);")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok, null).show();

            posAtual = postTempRevert;

            MenuItem item = (MenuItem) findViewById(arraysIdsMenuLateral[posAtual]);
            if(item!=null){
                item.setChecked(true);
            }
        }else {
            Bundle arguments = new Bundle();
            arguments.putString(QuestaoDetailFragment.ARG_ITEM_ID, String.valueOf(idLayout));
            arguments.putInt(QuestaoDetailFragment.ARG_QUESTAO_ORDEM, ordemQuestao);
            if (formulario.getId() != null) {
                arguments.putInt(QuestaoDetailFragment.ARG_FORMULARIO_ID, formulario.getId());
            }

            QuestaoDetailFragment fragment = new QuestaoDetailFragment();

            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.questao_detail_container, fragment)
                    .commit();
        }
    }

    /**
     * Regra utilizada para saber se tem que preencher o formulário todo ou somente até o bloco 4.
     * @return
     */

    private boolean entrevistadoEPescadorOuResponsavelFamiliar(){
        Questao qIdent = questaoDAO.findQuestaoByOrdemIdFormulario(getPosicaoPorIdLayout(R.layout.identificacao_entrevistado_form)-1, formulario.getId());
        Questao b2q1 = questaoDAO.findQuestaoByOrdemIdFormulario(getPosicaoPorIdLayout(R.layout.fcmr_reg_b2_q1)-1, formulario.getId());
        Questao b2q2 = questaoDAO.findQuestaoByOrdemIdFormulario(getPosicaoPorIdLayout(R.layout.fcmr_reg_b2_q2)-1, formulario.getId());

        FormularioBO formularioBO = new FormularioBO();

        Resposta responsavelFamiliar = formularioBO.getResposta(qIdent,14,1);
        Resposta pescadorCamarao = formularioBO.getResposta(b2q1, 1, 2);
        Resposta pescadorCamaraob2q2 = formularioBO.getResposta(b2q2, 2,2);

        if(responsavelFamiliar!=null || pescadorCamarao!=null || pescadorCamaraob2q2!=null) {
            return true;
        }else{
            return false;
        }

    }

    private int getPosicaoPorIdLayout(int idLayout){
        if(arrayIdsQuestoes!=null){
            for(int i=0;i<=arrayIdsQuestoes.length;i++){
                if(arrayIdsQuestoes[i]==idLayout){
                    return i;
                }
            }
        }
        return -1;
    }
}