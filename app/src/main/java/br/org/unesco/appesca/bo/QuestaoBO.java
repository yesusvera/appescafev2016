package br.org.unesco.appesca.bo;

import android.content.Context;

import java.util.List;

import br.org.unesco.appesca.dao.PerguntaDAO;
import br.org.unesco.appesca.dao.QuestaoDAO;
import br.org.unesco.appesca.dao.RespostaDAO;
import br.org.unesco.appesca.model.Pergunta;
import br.org.unesco.appesca.model.Questao;
import br.org.unesco.appesca.model.Resposta;

/**
 * Created by yesus on 23/02/16.
 */
public class QuestaoBO {

    public void excluirQuestao(Questao questao, Context context){

        if (questao != null) {
            QuestaoDAO questaoDAO = new QuestaoDAO(context);
            RespostaDAO respostaDAO = new RespostaDAO(context);

            PerguntaDAO perguntaDAO = new PerguntaDAO(context);

            List<Pergunta> listaPerguntas = questao.getPerguntas();

            for (Pergunta pergunta : listaPerguntas) {
                List<Resposta> listaRespostas = pergunta.getRespostas();

                for (Resposta resposta : listaRespostas) {
                    respostaDAO.delete(resposta.getId());
                }
                perguntaDAO.deletePerguntaById(pergunta.getId());
            }
            questaoDAO.deleteQuestaoById(questao.getId());

        }
    }

    public boolean temAlgumaResposta(Questao questao, Context context){

        boolean ret = false;

        if(questao!=null){
            RespostaDAO respostaDAO = new RespostaDAO(context);
            PerguntaDAO perguntaDAO = new PerguntaDAO(context);

            List<Pergunta> listaPerguntas = perguntaDAO.findPerguntasByQuestao(questao.getId());

            for (Pergunta pergunta : listaPerguntas) {
                List<Resposta> listaRespostas = respostaDAO.findRespostasByPergunta(pergunta.getId());

                for (Resposta resposta : listaRespostas) {
                    ret = true;
                }
            }
        }

        return ret;
    }
}
