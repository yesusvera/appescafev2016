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

import java.util.List;

import br.org.unesco.appesca.R;
import br.org.unesco.appesca.dao.LocalizacaoUsuarioDAO;
import br.org.unesco.appesca.dao.UsuarioDAO;
import br.org.unesco.appesca.model.Identity;
import br.org.unesco.appesca.model.LocalizacaoUsuario;
import br.org.unesco.appesca.model.Usuario;
import br.org.unesco.appesca.rest.model.LocalizacaoREST;
import br.org.unesco.appesca.rest.model.RespEnvioLocalizacao;
import br.org.unesco.appesca.util.ConnectionNetwork;
import br.org.unesco.appesca.util.ConstantesREST;
import cz.msebera.android.httpclient.Header;

/**
 * Created by yesus on 19/04/16.
 */
public class LocalizacaoUsuarioBO {

    public void enviarLocalizacoesPendentes( final Context context, final Activity activity) {

        final LocalizacaoUsuarioDAO localizacaoUsuarioDAO = new LocalizacaoUsuarioDAO(context);
        List<LocalizacaoUsuario> listaLocalizacoes = localizacaoUsuarioDAO.listAll();


        if(!ConnectionNetwork.verifiedInternetConnection(activity) || listaLocalizacoes == null || listaLocalizacoes.size()==0){
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO(context);

        Usuario usrTmp = new Usuario();

        for(LocalizacaoUsuario locUsr: listaLocalizacoes){
            if(locUsr.getUsuario()!=null && locUsr.getUsuario().getId()!=null) {
                if(usrTmp == null || usrTmp.getId() != locUsr.getUsuario().getId()) {
                    usrTmp = usuarioDAO.findById(locUsr.getUsuario().getId());
                }

                if(usrTmp!=null) {
                    Usuario usrFnl = new Usuario();
                    usrFnl.setLogin(usrTmp.getLogin());
                    usrFnl.setEmail(usrTmp.getEmail());

                    locUsr.setUsuario(usrFnl);
                }
            }
        }

        final ProgressDialog ringProgressDialog = ProgressDialog.show(activity, "Aguarde ...", "Enviando as localizações gravadas, aguarde...", true);
        ringProgressDialog.setCancelable(true);

        final LocalizacaoREST localizacaoREST = new LocalizacaoREST();
        localizacaoREST.setListaLocalizacaoUsuario(listaLocalizacoes);

        XStream xStream = new XStream(new DomDriver());
        String xmlLocalizacao = xStream.toXML(localizacaoREST);

        String strURL = ConstantesREST.getURLService(ConstantesREST.LOCALIZACAO_INSERIR);

        RequestParams params = new RequestParams();
        params.put("xmlLocalizacao", xmlLocalizacao);
        params.put("login", Identity.getUsuarioLogado().getLogin());


        AsyncHttpClient client = new AsyncHttpClient();
        client.setResponseTimeout(10 * 10000);
        client.post(strURL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            public void mensagem(String msg) {
                try {
//                    new AlertDialog.Builder(activity)
//                            .setTitle(R.string.app_name)
//                            .setMessage(msg)
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .setPositiveButton(android.R.string.ok, null).show();

                    String mensagemErro = msg;
                    Toast toast = Toast.makeText(activity, mensagemErro, Toast.LENGTH_LONG);
                    toast.show();

                    if (ringProgressDialog != null && ringProgressDialog.isShowing()) {
                        ringProgressDialog.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String xmlRetorno = new String(response).toString();
                XStream xStream = new XStream(new DomDriver());

                RespEnvioLocalizacao respEnvioLocalizacao = (RespEnvioLocalizacao) xStream.fromXML(xmlRetorno);

                if (!respEnvioLocalizacao.isErro()) { //deletando as localizações enviadas
                    for(LocalizacaoUsuario loc: localizacaoREST.getListaLocalizacaoUsuario()) {
                        localizacaoUsuarioDAO.delete(loc.getId());
                    }
                }

                mensagem(respEnvioLocalizacao.getMensagemErro());
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
//                mensagem("Aconteceu alguma falha de conectividade." + "\n" + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onRetry(int retryNo) {
            }
        });
    }
}
