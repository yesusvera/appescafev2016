//package br.org.unesco.appesca.view.codigoantigo;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.OvershootInterpolator;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import br.org.unesco.appesca.R;
//import br.org.unesco.appesca.control.FormularioListDetailFragment;
//import br.org.unesco.appesca.floatingbuttonlibrary.FloatingActionButton;
//import br.org.unesco.appesca.floatingbuttonlibrary.FloatingActionMenu;
//import br.org.unesco.appesca.model.Identity;
//import br.org.unesco.appesca.model.ItemMenuLateral;
//import br.org.unesco.appesca.model.MenuContent;
//import br.org.unesco.appesca.util.ConstantesREST;
//
//public class HomeActivity extends AppCompatActivity {
//
//    private FloatingActionMenu menu1;
//
//    private LocationManager locationManager;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle(getTitle());
//        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
//
//        //PREENCHENDO OS DADOS DE LOGIN
//
//        if(Identity.getUsuarioLogado()!=null && Identity.getUsuarioLogado().getNome()!=null) {
//            TextView txtNomeUsuarioLogado = (TextView)findViewById(R.id.txtNomeUsuarioLogado);
//            txtNomeUsuarioLogado.setText(Identity.getUsuarioLogado().getNome());
//        }
//        if(Identity.getUsuarioLogado()!=null && Identity.getUsuarioLogado().getEmail()!=null) {
//            TextView txtEmailUsuarioLogado = (TextView) findViewById(R.id.txtEmailUsuarioLogado);
//            txtEmailUsuarioLogado.setText(Identity.getUsuarioLogado().getEmail());
//        }
//
//        menu1 = (FloatingActionMenu) findViewById(R.id.formulario_floating_menu);
//
//        locationManager = (LocationManager)
//                this.getSystemService(Context.LOCATION_SERVICE);
//
//        final ImageView fotoUsuario = (ImageView)findViewById(R.id.fotoUsuario);
//
//        new AsyncTask<Void, Void, Void>() {
//            Bitmap bitmap;
//
//            @Override
//            protected Void doInBackground(Void... params) {
//
//                try {
//                    URL newurl = new URL(
//                                ConstantesREST.getURLService(ConstantesREST.IMAGEM_USUARIO)
//                                        + "?login=" + Identity.getUsuarioLogado().getLogin() + "&senha=" + Identity.getUsuarioLogado().getSenha());
//                    bitmap=  BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
//                }
//                catch(IOException e ) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                fotoUsuario.setImageBitmap(bitmap);
//                super.onPostExecute(aVoid);
//            }
//        }.execute();
//
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                String latitudeStr = String.valueOf(location.getLatitude());
//                String longitudeStr = String.valueOf(location.getLongitude());
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//
//        long tempoAtualizacao = 0;
//        float distancia = 0;
//
//
//        View recyclerView = findViewById(R.id.formulario_list_situation);
//        assert recyclerView != null;
//        setupRecyclerView((RecyclerView) recyclerView);
//
//        createFloatButton();
//    }
//
//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//    }
//
//    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
//        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(MenuContent.ITEMS));
//    }
//
//    public class SimpleItemRecyclerViewAdapter
//            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
//
//        private final List<ItemMenuLateral> mValues;
//
//        public SimpleItemRecyclerViewAdapter(List<ItemMenuLateral> items) {
//            mValues = items;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.formulario_list_title, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final ViewHolder holder, final int position) {
//            holder.mItem = mValues.get(position);
//            holder.mTitleView.setText(mValues.get(position).title);
//
//            holder.mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openFragment(position);
//                }
//            });
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return mValues.size();
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            public final View mView;
//            public final TextView mTitleView;
//            public ItemMenuLateral mItem;
//
//            public ViewHolder(View view) {
//                super(view);
//                mView = view;
//                mTitleView = (TextView) view.findViewById(R.id.title);
//            }
//
//            @Override
//            public String toString() {
//                return super.toString() + " '" + mTitleView.getText() + "'";
//            }
//        }
//    }
//
//    public void openFragment(int position){
//        Bundle arguments = new Bundle();
//        arguments.putInt(FormularioListDetailFragment.ARG_ITEM_ID, position);
//        FormularioListDetailFragment fragment = new FormularioListDetailFragment();
//        fragment.setArguments(arguments);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.formulario_detail_container, fragment)
//                .commit();
//    }
//
//    protected void createFloatButton() {
//
//        final FloatingActionButton formCamarao = new FloatingActionButton(this);
//        formCamarao.setButtonSize(FloatingActionButton.SIZE_MINI);
//        formCamarao.setLabelText("Formulário Camarão");
//        formCamarao.setImageResource(R.drawable.fab_add);
//
//        final FloatingActionButton formCaranguejo = new FloatingActionButton(this);
//        formCaranguejo.setButtonSize(FloatingActionButton.SIZE_MINI);
//        formCaranguejo.setLabelText("Formulário Caranguejo");
//        formCaranguejo.setImageResource(R.drawable.fab_add);
//
//        final FloatingActionButton formCamaraoRegional = new FloatingActionButton(this);
//        formCamaraoRegional.setButtonSize(FloatingActionButton.SIZE_MINI);
//        formCamaraoRegional.setLabelText("Formulário Camarão Regional");
//        formCamaraoRegional.setImageResource(R.drawable.fab_add);
//
//        menu1.addMenuButton(formCamarao);
//        menu1.addMenuButton(formCaranguejo);
//        menu1.addMenuButton(formCamaraoRegional);
//
//        formCamarao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Context context = v.getContext();
//                Intent intent = new Intent(context, FormularioCamaraoActivity.class);
//
//                context.startActivity(intent);
//            }
//        });
//
//        formCaranguejo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Context context = v.getContext();
//                Intent intent = new Intent(context, FormularioCaranguejoActivity.class);
//
//                context.startActivity(intent);
//            }
//        });
//
//        formCamaraoRegional.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Context context = v.getContext();
//                Intent intent = new Intent(context, FormularioCamaraoRegionalActivity.class);
//
//                context.startActivity(intent);
//            }
//        });
//
//        menu1.setOnMenuButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (menu1.isOpened()) {
//                }
//                menu1.toggle(true);
//            }
//        });
//
//        List<FloatingActionMenu> menus = new ArrayList<>();
//        menus.add(menu1);
//
//        menu1.setClosedOnTouchOutside(true);
//        menu1.setIconAnimated(true);
//        menu1.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
//            @Override
//            public void onMenuToggle(boolean opened) {
//
//            }
//        });
//
//        createCustomAnimation();
//
//        Handler mUiHandler = new Handler();
//        int delay = 400;
//        for (final FloatingActionMenu menu : menus) {
//            mUiHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    menu.showMenuButton(true);
//                }
//            }, delay);
//            delay += 150;
//        }
//
//    }
//
//    private void createCustomAnimation() {
//
//        AnimatorSet set = new AnimatorSet();
//
//        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menu1.getMenuIconView(), "scaleX", 1.0f, 0.2f);
//        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menu1.getMenuIconView(), "scaleY", 1.0f, 0.2f);
//
//        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menu1.getMenuIconView(), "scaleX", 0.2f, 1.0f);
//        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menu1.getMenuIconView(), "scaleY", 0.2f, 1.0f);
//
//        scaleOutX.setDuration(50);
//        scaleOutY.setDuration(50);
//
//        scaleInX.setDuration(150);
//        scaleInY.setDuration(150);
//
//        scaleInX.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                menu1.getMenuIconView().setImageResource(menu1.isOpened()
//                        ? R.drawable.fab_add : R.drawable.fab_add_mini);
//            }
//        });
//
//        set.play(scaleOutX).with(scaleOutY);
//        set.play(scaleInX).with(scaleInY).after(scaleOutX);
//        set.setInterpolator(new OvershootInterpolator(2));
//
//        menu1.setIconToggleAnimatorSet(set);
//    }
//
//}
