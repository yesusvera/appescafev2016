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

    QuestaoDAO questaoDAO;
    RespostaDAO respostaDAO;
    PerguntaDAO perguntaDAO;

    public QuestaoBO(Context context){
        questaoDAO = new QuestaoDAO(context);
        respostaDAO = new RespostaDAO(context);
        perguntaDAO = new PerguntaDAO(context);
    }

    public void excluirQuestao(Questao questao){

        if (questao != null) {
            List<Pergunta> listaPerguntas = questao.getListaPerguntas();

            for (Pergunta pergunta : listaPerguntas) {
                List<Resposta> listaRespostas = pergunta.getListaRespostas();

                for (Resposta resposta : listaRespostas) {
                    if(resposta!=null && resposta.getId()!=null) {
                        respostaDAO.delete(resposta.getId());
                    }
                }
                perguntaDAO.deletePerguntaById(pergunta.getId());
            }
            questaoDAO.deleteQuestaoById(questao.getId());
        }
    }

    public boolean temAlgumaResposta(Questao questao){
        boolean ret = false;

        if(questao!=null){
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
