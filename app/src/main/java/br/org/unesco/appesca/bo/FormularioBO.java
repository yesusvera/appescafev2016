package br.org.unesco.appesca.bo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.org.unesco.appesca.dao.FormularioDAO;
import br.org.unesco.appesca.model.Formulario;
import br.org.unesco.appesca.model.Identity;
import br.org.unesco.appesca.model.Pergunta;
import br.org.unesco.appesca.model.Questao;
import br.org.unesco.appesca.model.Resposta;
import br.org.unesco.appesca.rest.model.FormularioREST;
import br.org.unesco.appesca.rest.model.RespEnvioFormulario;
import br.org.unesco.appesca.util.ConstantesREST;
import cz.msebera.android.httpclient.Header;

/**
 * Created by yesus on 23/02/16.
 */
public class FormularioBO {


    public void prepararObjetoFormulario(Formulario form){
        form.setId(null);
        if(form.getListaQuestoes()!=null){
            for(Questao q: form.getListaQuestoes()){
                q.setId(null);
                q.setFormulario(null);
                if(q.getListaPerguntas()!=null){
                    for(Pergunta p: q.getListaPerguntas()){
                        p.setId(null);
                        p.setQuestao(null);
                        if(p.getListaRespostas()!=null){
                            for(Resposta r: p.getListaRespostas()){
                                r.setId(null);
                                r.setPergunta(null);
                            }
                        }
                    }
                }
            }
        }
    }

    public void enviarFormulario(Formulario formTemp,final Formulario formularioOriginal, final Context context){

        prepararObjetoFormulario(formTemp);

        FormularioREST formularioREST = new FormularioREST(formTemp);

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
                Toast.makeText(context, "Enviando o formulário.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String xmlRetorno = new String(response).toString();
                XStream xStream = new XStream(new DomDriver());

                RespEnvioFormulario respEnvioFormulario = (RespEnvioFormulario) xStream.fromXML(xmlRetorno);

                if(!respEnvioFormulario.isErro()) { //LOGIN CORRETO
                    FormularioDAO formularioDAO = new FormularioDAO(context);
                    formularioOriginal.setIdSincronizacao(respEnvioFormulario.getIdSincronizacao());
                    formularioOriginal.setSituacao(1);
                    formularioDAO.save(formularioOriginal);
                }


                Toast.makeText(context, respEnvioFormulario.getMensagemErro(), Toast.LENGTH_LONG).show();
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
