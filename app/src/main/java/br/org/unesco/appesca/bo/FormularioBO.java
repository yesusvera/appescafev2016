package br.org.unesco.appesca.bo;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.org.unesco.appesca.model.Formulario;
import br.org.unesco.appesca.model.Identity;
import br.org.unesco.appesca.rest.model.FormularioREST;
import br.org.unesco.appesca.util.ConstantesREST;
import cz.msebera.android.httpclient.Header;

/**
 * Created by yesus on 23/02/16.
 */
public class FormularioBO {

    public void enviarFormulario(Formulario formulario){

        Log.i("FORMULARIO", formulario.toString());

        FormularioREST formularioREST = new FormularioREST(formulario);

        XStream xStream = new XStream(new DomDriver());
        String xmlFormularioRest = xStream.toXML(formularioREST);

        String strURL = ConstantesREST.getURLService(ConstantesREST.FORMULARIO_INSERIR);

        RequestParams params = new RequestParams();

        params.put("xmlFormularioRest", xmlFormularioRest);
        params.put("login", Identity.getUsuarioLogado().getLogin());
        params.put("senha", Identity.getUsuarioLogado().getSenha());

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(strURL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
//                showProgress(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String xmlRetorno = new String(response).toString();
//                showProgress(false);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
//                showProgress(false);
            }

            @Override
            public void onRetry(int retryNo) {
                //showProgress(false);
            }
        });
    }

}
