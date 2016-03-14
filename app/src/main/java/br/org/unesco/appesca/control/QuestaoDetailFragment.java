package br.org.unesco.appesca.control;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.List;

import br.org.unesco.appesca.dao.QuestaoDAO;
import br.org.unesco.appesca.model.Pergunta;
import br.org.unesco.appesca.model.Questao;
import br.org.unesco.appesca.model.Resposta;
import br.org.unesco.appesca.util.ConstantesIdsFormularios;

public class QuestaoDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_QUESTAO_ORDEM = "questao_ordem";
    public static final String ARG_FORMULARIO_ID = "questao_id";

    private String id_layout_inflate;
    private int ordemQuestao;
    private int idformulario;

    private QuestaoDAO questaoDAO;

    public QuestaoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        questaoDAO = new QuestaoDAO(QuestaoDetailFragment.this.getContext());

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            id_layout_inflate = getArguments().getString(ARG_ITEM_ID);
        }
        if (getArguments().containsKey(ARG_QUESTAO_ORDEM)) {
            ordemQuestao = getArguments().getInt(ARG_QUESTAO_ORDEM);
        }
        if (getArguments().containsKey(ARG_FORMULARIO_ID)) {
            idformulario = getArguments().getInt(ARG_FORMULARIO_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        carregaRespostas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(Integer.valueOf(id_layout_inflate), container, false);
        return rootView;

    }

    /**
     * Carregar automaticamente qualquer resposta.
     */
    private void carregaRespostas(){
        Questao questao = questaoDAO.findQuestaoByOrdemIdFormulario(ordemQuestao, idformulario);

        if(questao!=null){
            List<Pergunta> listaPerguntas = questao.getListaPerguntas();

            for(Pergunta pergunta : listaPerguntas){
                String currentPergunta = ConstantesIdsFormularios.PERGUNTA.concat(String.valueOf(pergunta.getOrdem()));

                List<Resposta> listaRespostas = pergunta.getListaRespostas();

                for(Resposta resposta: listaRespostas) {
                        /** RADIOBUTTON **/
                        String currentRadioButton = currentPergunta.concat(ConstantesIdsFormularios.TYPE_RADIO_BUTTON + resposta.getOrdem()); //1 ou 2 //3 ou 4 //5 e 6
                        RadioButton radioButton = (RadioButton) findViewByStringId(currentRadioButton);
                        if (radioButton != null) {
                            radioButton.setChecked(true);
                        }

                         /** CHECKBOX **/
                        String currentCheckBox = currentPergunta.concat(ConstantesIdsFormularios.TYPE_CHECK_BOX + resposta.getOrdem());
                        CheckBox checkBox = (CheckBox) findViewByStringId(currentCheckBox);
                        if (checkBox != null) {
                            checkBox.setChecked(true);
                        }

                         /** EDITTEXT **/
                        String currentEditText = currentPergunta.concat(ConstantesIdsFormularios.TYPE_EDIT_TEXT + resposta.getOrdem());
                        EditText editText = (EditText) findViewByStringId(currentEditText);
                        if (editText != null) {
                            editText.setText(resposta.getTexto());
                        }
                }
            }
        }
    }

    public View findViewByStringId(String id){
        return getActivity().findViewById(getResources().getIdentifier(id, "id", getActivity().getPackageName()));
    }

}
