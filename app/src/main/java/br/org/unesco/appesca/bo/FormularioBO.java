package br.org.unesco.appesca.bo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.org.unesco.appesca.R;
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

    public void enviarFormulario(Formulario formTemp,final Formulario formularioOriginal, final Context context, final Activity activity){

        final ProgressDialog ringProgressDialog = ProgressDialog.show(activity, "Aguarde ...", "Enviando o formulário para aprovação.", true);
        ringProgressDialog.setCancelable(true);

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
//                Toast.makeText(context, "Enviando o formulário.", Toast.LENGTH_LONG).show();
            }

            public void mensagem(String msg){
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.app_name)
                        .setMessage(msg)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        }).show();

                if(ringProgressDialog!=null && ringProgressDialog.isShowing()) {
                    ringProgressDialog.dismiss();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String xmlRetorno = new String(response).toString();
                XStream xStream = new XStream(new DomDriver());

                RespEnvioFormulario respEnvioFormulario = (RespEnvioFormulario) xStream.fromXML(xmlRetorno);

                if (!respEnvioFormulario.isErro()) { //LOGIN CORRETO
                    FormularioDAO formularioDAO = new FormularioDAO(context);
                    formularioOriginal.setIdSincronizacao(respEnvioFormulario.getIdSincronizacao());
                    formularioOriginal.setSituacao(1);
                    formularioDAO.save(formularioOriginal);
                }


//                Toast.makeText(context, respEnvioFormulario.getMensagemErro(), Toast.LENGTH_LONG).show();

                mensagem(respEnvioFormulario.getMensagemErro());


//                try {
//
//                } catch (Exception e) {
//
//                }
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
                mensagem("Aconteceu alguma falha.");
//                showProgress(false);
            }

            @Override
            public void onRetry(int retryNo) {
                //showProgress(false);
            }
        });
    }





    public Resposta getResposta(int ordemQuestao, int ordemPergunta, int ordemResposta, Formulario formulario){
        Questao questao = getQuestaoPorOrdem(ordemQuestao, formulario);
        Pergunta pergunta = getPerguntaPorOrdem(ordemPergunta, questao);
        Resposta resposta = getRespostaPorOrdem(ordemResposta, pergunta);

        return resposta;
    }

    public Resposta getResposta(Questao questao, int ordemPergunta, int ordemResposta){
        Pergunta pergunta = getPerguntaPorOrdem(ordemPergunta, questao);
        Resposta resposta = getRespostaPorOrdem(ordemResposta, pergunta);

        return resposta;
    }

    public String getRespostaTexto(int ordemQuestao, int ordemPergunta, int ordemResposta, Formulario formulario){
        Questao questao = getQuestaoPorOrdem(ordemQuestao, formulario);
        Pergunta pergunta = getPerguntaPorOrdem(ordemPergunta, questao);
        Resposta resposta = getRespostaPorOrdem(ordemResposta, pergunta);

        if(resposta != null){
            return resposta.getTexto();
        }

        return "";
    }

    public Questao getQuestaoPorOrdem(int ordem, Formulario formulario){
        if(formulario!=null && formulario.getListaQuestoes()!=null && formulario.getListaQuestoes().size() > 0){
            for(Questao questao: formulario.getListaQuestoes()){
                if(questao.getOrdem() == ordem){
                    return questao;
                }
            }
        }

        return null;
    }

    public Pergunta getPerguntaPorOrdem(int ordem, Questao questao){
        if(questao!=null && questao.getListaPerguntas()!=null && questao.getListaPerguntas().size()>0){
            for(Pergunta pergunta: questao.getListaPerguntas()){
                if(pergunta.getOrdem()==ordem){
                    return pergunta;
                }
            }
        }

        return null;
    }

    public Resposta getRespostaPorOrdem(int ordem, Pergunta pergunta){
        if(pergunta!=null && pergunta.getListaRespostas()!=null && pergunta.getListaRespostas().size() > 0 ){
            for(Resposta resposta: pergunta.getListaRespostas()){
                if(resposta.getOrdem()== ordem){
                    return resposta;
                }
            }
        }

        return null;
    }

}
