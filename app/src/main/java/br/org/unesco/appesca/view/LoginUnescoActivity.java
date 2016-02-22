package br.org.unesco.appesca.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.util.ArrayList;
import java.util.List;

import br.org.unesco.appesca.R;
import br.org.unesco.appesca.dao.UsuarioDAO;
import br.org.unesco.appesca.model.Identity;
import br.org.unesco.appesca.model.Usuario;
import br.org.unesco.appesca.rest.model.RespAutenticacaoREST;
import br.org.unesco.appesca.util.ConnectionNetwork;
import br.org.unesco.appesca.util.ConstantesREST;
import cz.msebera.android.httpclient.Header;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginUnescoActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {


    private static final int REQUEST_READ_CONTACTS = 0;


    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    public static boolean ENCERRAR_APP = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    efetuarLogin();
//                    return true;
//                }
//                return false;
//            }
//        });

        Button btnEntrar = (Button) findViewById(R.id.email_sign_in_button);
        btnEntrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                efetuarLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(ENCERRAR_APP){
            ENCERRAR_APP = false;
            finish();
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * @author yesus
     */
    private void efetuarLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            //Se tiver internet sempre vai no servidor.
            if(ConnectionNetwork.verifiedInternetConnection(this)){
                loginNoServidor( email,  password);
                return;
            }else {//LOGIN LOCAL
                UsuarioDAO usuarioDAO = new UsuarioDAO(LoginUnescoActivity.this);

                List<Usuario> lista = usuarioDAO.listAll();
                if (lista != null && lista.size() > 0) {
                    for (Usuario usr : lista) {
                        if (((usr.getLogin() != null && usr.getLogin().equals(email))
                                ||
                                (usr.getEmail() != null && usr.getEmail().equals(email))
                        )
                                &&
                                usr.getSenha().equals(password)
                                ) { //LOGIN CORRETO

                            Identity.setUsuarioLogado(usr);
                            Intent intent = new Intent(LoginUnescoActivity.this, PrincipalUnescoActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Bem vindo ao Appesca. Conectado pela base local.", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            new AlertDialog.Builder(LoginUnescoActivity.this)
                                    .setTitle("Appesca")
                                    .setMessage("Não foi possível autenticar este usuário na base local, é possível que a senha tenha sido alterada. Deseja se conectar aos servidores da Appesca?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("Realizar login pela internet", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            loginNoServidor(email, password);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();
                            Identity.setUsuarioLogado(null);
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Não existem usuários na base local. Conecte-se na internet e tente novamente.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * @author yesus
     * @param email
     * @param password
     */
    private void loginNoServidor(String email, String password) {

        if(!ConnectionNetwork.verifiedInternetConnection(this)){
            String mensagemErro = "Seu aparelho está sem conectividade com a internet. Por favor habilite seu WIFI ou rede de celular.";
            Toast toast = Toast.makeText(LoginUnescoActivity.this, mensagemErro, Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        String strURL = ConstantesREST.getURLService(ConstantesREST.AUTHENTICATION_SERVICE) +
                "?login=" + email + "&senha=" + password;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(strURL, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                showProgress(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String xmlRetorno = new String(response).toString();
                XStream xStream = new XStream(new DomDriver());
                Log.i("xmlRetornoLogin", xmlRetorno);

                RespAutenticacaoREST auth = (RespAutenticacaoREST) xStream.fromXML(xmlRetorno);

                if (!auth.isErro()) { //LOGIN CORRETO
                    Identity.setUsuarioLogado(auth.getUsuario());
                    UsuarioDAO usuarioDAO = new UsuarioDAO(LoginUnescoActivity.this);
                    usuarioDAO.save(auth.getUsuario());

                    Intent intent = new Intent(LoginUnescoActivity.this, PrincipalUnescoActivity.class);
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), "Bem vindo ao Appesca", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(LoginUnescoActivity.this)
                            .setTitle("Appesca")
                            .setMessage("Login ou senha inválidos!")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null).show();
                    Identity.setUsuarioLogado(null);
                }

                showProgress(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                showProgress(false);

                mPasswordView.setError("Aconteceu um problema ao conectar.");
                mPasswordView.requestFocus();
            }

            @Override
            public void onRetry(int retryNo) {
                showProgress(false);
            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginUnescoActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}

