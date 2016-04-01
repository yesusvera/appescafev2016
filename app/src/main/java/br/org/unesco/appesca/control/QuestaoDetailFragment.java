package br.org.unesco.appesca.control;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

import br.org.unesco.appesca.bo.FormularioBO;
import br.org.unesco.appesca.dao.FormularioDAO;
import br.org.unesco.appesca.dao.QuestaoDAO;
import br.org.unesco.appesca.model.Formulario;
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

    private FormularioBO formularioBO = new FormularioBO();

    private QuestaoDAO questaoDAO;
    private FormularioDAO formularioDAO;

    public QuestaoDetailFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        questaoDAO = new QuestaoDAO(QuestaoDetailFragment.this.getContext());
        formularioDAO = new FormularioDAO(QuestaoDetailFragment.this.getContext());

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

        configuraComponentes();
        carregaRespostas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(Integer.valueOf(id_layout_inflate), container, false);
        return rootView;
    }

    public void configuraComponentes(){
        for(int seqPergunta=1; seqPergunta<=59; seqPergunta++) {
            String currentPergunta = ConstantesIdsFormularios.PERGUNTA.concat(String.valueOf(seqPergunta));

            /** RADIOBUTTON **/
            for (int x = 1; x <= 30; x++) { //
                String currentCompoundButton = currentPergunta.concat(ConstantesIdsFormularios.TYPE_RADIO_BUTTON + x);
                View viewComponentTemp = (View) findViewByStringId(currentCompoundButton);

                if(viewComponentTemp==null){
                    currentCompoundButton = currentPergunta.concat(ConstantesIdsFormularios.TYPE_CHECK_BOX + x);
                    viewComponentTemp = (View) findViewByStringId(currentCompoundButton);
                }

                final View viewComponent = viewComponentTemp;

                if (viewComponent != null){
                    final String tag = (String)viewComponent.getTag();
                    if(tag!=null && !tag.isEmpty()){
                        try {
                            final String[] tagComandos;
                            if(tag.contains(",")) {
                                tagComandos = tag.split(",");
                            }else{
                                tagComandos = new String[]{tag};
                            }

                            viewComponent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    comandosPorComponente(tagComandos, viewComponent);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void comandosPorComponente(String[] tagComandos, View viewComponent) {
        for (String comando : tagComandos) {
            final String[] tagSpl = comando.split("->");
            if (tagSpl != null && tagSpl.length == 2) { //RADIOGROUPS
                if (tagSpl[0].contains("_rg")) {
                    RadioGroup rgTmp = (RadioGroup) findViewByStringId(tagSpl[0]);
                    Boolean enabledParam = new Boolean(tagSpl[1]);

                    if (rgTmp != null && enabledParam != null) {
                        int count = rgTmp.getChildCount();
                        ArrayList<RadioButton> listOfRadioButtons = new ArrayList<RadioButton>();
                        for (int i = 0; i < count; i++) {
                            View o = rgTmp.getChildAt(i);
                            if (o instanceof RadioButton) {
                                listOfRadioButtons.add((RadioButton) o);
                            }
                        }
                        for (RadioButton rb : listOfRadioButtons) {
                            if (!enabledParam) {
                                rb.setChecked(false);
                                rb.callOnClick();
                            }
                            rb.setEnabled(enabledParam);
                        }
                        rgTmp.setEnabled(enabledParam);
                    }
                } else if (tagSpl[0].contains(ConstantesIdsFormularios.TYPE_EDIT_TEXT)) { //EDITTEXT
                    EditText editText = (EditText) findViewByStringId(tagSpl[0]);
                    if(tagSpl[1]!=null) {
                        if(tagSpl[1].equalsIgnoreCase("true") || tagSpl[1].equalsIgnoreCase("false")) {
                            Boolean enabledParam = new Boolean(tagSpl[1]);
                            if (editText != null && enabledParam != null) {
                                if (!enabledParam) {
                                    editText.setText("");
                                }
                                editText.setEnabled(enabledParam);
                            }
                        } else if(tagSpl[1].equalsIgnoreCase("this")){
                            if (editText != null && viewComponent instanceof CompoundButton) {
                                Boolean enabledParam= ((CompoundButton) viewComponent).isChecked();
                                if (!enabledParam) {
                                    editText.setText("");
                                }
                                editText.setEnabled(enabledParam);
                            }
                        }
                    }
                }else if(tagSpl[0].contains("_cb_")) { //CHECKBOX
                    if(tagSpl[0].contains("ˆ")){
                        String comandoComposto = tagSpl[0];// EXEMPLO: perg2_cb_1ˆ10->false
                        String [] splitComandoComposto = comandoComposto.split("_");

                        if(splitComandoComposto.length==3){ //1|10ˆ
                            String [] range = splitComandoComposto[2].split("ˆ");
                            if(range.length==2){
                                try {
                                    int i = Integer.valueOf(range[0]);
                                    int max = Integer.valueOf(range[1]);

                                    for( ; i<=max; i++){
                                        try {
                                            CheckBox checkBox = (CheckBox) findViewByStringId(splitComandoComposto[0] + "_" + splitComandoComposto[1] + "_resp" + i);
                                            if (tagSpl[1].equalsIgnoreCase("true") || tagSpl[1].equalsIgnoreCase("false")) {
                                                Boolean enabledParam = new Boolean(tagSpl[1]);
                                                if (checkBox != null && enabledParam != null) {
                                                    if(!enabledParam){
                                                        checkBox.setChecked(false);
                                                        checkBox.callOnClick();
                                                    }
                                                    checkBox.setEnabled(enabledParam);
                                                }
                                            } else if (tagSpl[1].equalsIgnoreCase("this")) {
                                                if (checkBox != null && viewComponent instanceof CompoundButton) {
                                                    Boolean enabledParam = ((CompoundButton) viewComponent).isChecked();
                                                    if(!enabledParam){
                                                        checkBox.setChecked(false);
                                                        checkBox.callOnClick();
                                                    }
                                                    checkBox.setEnabled(enabledParam);
                                                }
                                            }
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }catch (NumberFormatException nfe){
                                    nfe.printStackTrace();
                                }
                            }
                        }
                    }else{
                        CheckBox checkBox = (CheckBox) findViewByStringId(tagSpl[0]);
                        if(tagSpl[1].equalsIgnoreCase("true") || tagSpl[1].equalsIgnoreCase("false")) {
                            Boolean enabledParam = new Boolean(tagSpl[1]);
                            if (checkBox != null && enabledParam != null) {
                                if(!enabledParam){
                                    checkBox.setChecked(false);
                                    checkBox.callOnClick();
                                }
                                checkBox.setEnabled(enabledParam);
                            }
                        } else if(tagSpl[1].equalsIgnoreCase("gone")) {
                            Boolean enabledParam = new Boolean(tagSpl[1]);
                            if (checkBox != null && enabledParam != null) {
                                if(!enabledParam){
                                    checkBox.setChecked(false);
                                    checkBox.setVisibility(View.GONE);
                                    checkBox.callOnClick();
                                }
                                checkBox.setEnabled(enabledParam);
                            }
                        }else if(tagSpl[1].equalsIgnoreCase("this")){
                            if (checkBox != null && viewComponent instanceof CompoundButton) {
                                Boolean enabledParam= ((CompoundButton) viewComponent).isChecked();
                                if(!enabledParam){
                                    checkBox.setChecked(false);
                                    checkBox.callOnClick();
                                }
                                checkBox.setEnabled(enabledParam);
                            }
                        }
                    }

                }else if (tagSpl[0].contains("_et_") && tagSpl[0].contains("ˆ") ) { //EDITTEXT MULTIPLOS
                        String comandoComposto = tagSpl[0];// EXEMPLO: perg2_cb_1ˆ10->false
                        String [] splitComandoComposto = comandoComposto.split("_");

                        if(splitComandoComposto.length==3){ //1|10ˆ
                            String [] range = splitComandoComposto[2].split("ˆ");
                            if(range.length==2){
                                try {
                                    int i = Integer.valueOf(range[0]);
                                    int max = Integer.valueOf(range[1]);

                                    for( ; i<=max; i++){
                                        try {
                                            EditText editText = (EditText) findViewByStringId(splitComandoComposto[0] + "_" + splitComandoComposto[1] + "_resp" + i);
                                            if (tagSpl[1].equalsIgnoreCase("true") || tagSpl[1].equalsIgnoreCase("false")) {
                                                Boolean enabledParam = new Boolean(tagSpl[1]);
                                                if (editText != null && enabledParam != null) {
                                                    if(!enabledParam){
                                                        editText.setText("");

                                                    }
                                                    editText.setEnabled(enabledParam);
                                                }
                                            } else if (tagSpl[1].equalsIgnoreCase("this")) {
                                                if (editText != null && viewComponent instanceof CompoundButton) {
                                                    Boolean enabledParam = ((CompoundButton) viewComponent).isChecked();
                                                    if(!enabledParam){
                                                        editText.setText("");
                                                        editText.setVisibility(viewComponent.getVisibility());
                                                    }
                                                    editText.setEnabled(enabledParam);
                                                }
                                            }
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }catch (NumberFormatException nfe){
                                    nfe.printStackTrace();
                                }
                            }
                        }


                }
            }
        }
    }


    /**
     * Carregar automaticamente qualquer resposta.
     */
    private void carregaRespostas(){
        Questao questao = questaoDAO.findQuestaoByOrdemIdFormulario(ordemQuestao, idformulario);
        Formulario formulario = formularioDAO.findById(idformulario);

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
                            radioButton.callOnClick();
                        }

                         /** CHECKBOX **/
                        String currentCheckBox = currentPergunta.concat(ConstantesIdsFormularios.TYPE_CHECK_BOX + resposta.getOrdem());
                        CheckBox checkBox = (CheckBox) findViewByStringId(currentCheckBox);
                        if (checkBox != null) {
                            checkBox.setChecked(true);
                            checkBox.callOnClick();
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



        //Tratamento e validações para questões específicas
        switch (ordemQuestao)
        {
            case 0: configuraIdentificacaoEntrevisado(formulario.getIdTipoFormulario());break;
            default:
        }

        if(formulario!=null){
            List<Questao> listaQuestoes = questaoDAO.getQuestoesByFormulario(formulario.getId());
            formulario.setListaQuestoes(listaQuestoes);
            switch (formulario.getIdTipoFormulario()){
                case 1: //CAMARAO REGIONAL

                    break;
                case 2: //CARANGUEJO

                    switch (ordemQuestao)
                    {
                        case 53: configuraTabelaB6Q7(formulario);break;
                        case 63: configuraTabelaB6Q9(formulario);break;
                        default:
                    }
                    break;
                case 3: //PITICAIA E BRANCO

                    break;
            }
        }
    }

    public View findViewByStringId(String id){
        return getActivity().findViewById(getResources().getIdentifier(id, "id", getActivity().getPackageName()));
    }

    /**
     * 7. Qual é quantidade média capturada/dia nas marés alta e baixa, nos períodos de inverno e verão, por arte de pesca?
     * Quanto tempo é gasto? Quantos dias são trabalhados? (O dia de pesca se refere tanto ao período diurno quanto noturno)
     * @param formulario
     */
    public void configuraTabelaB6Q7(Formulario formulario){

        Questao q51 = questaoDAO.findQuestaoByOrdemIdFormulario(51, formulario.getId());
        Questao q52 = questaoDAO.findQuestaoByOrdemIdFormulario(52, formulario.getId());

        boolean verao = formularioBO.getResposta(q51, 1, 1)!=null;
        boolean inverno = formularioBO.getResposta(q51, 1, 2)!=null;

        boolean mareMorta = formularioBO.getResposta(q52, 1, 1)!=null;
        boolean lancante = formularioBO.getResposta(q52, 1, 2)!=null;


        if(!inverno){
            String[] strArray = new String[16];
            for(int i=0,x=1;i<strArray.length;i++,x++){
                strArray[i] = "perg"+(x)+"_cb_resp1->gone";
            }
            comandosPorComponente(strArray, null);

            TableLayout tbInverno = (TableLayout)findViewByStringId("tb_inverno");
            tbInverno.setVisibility(View.GONE);
        }


        if(!verao){
            String[] strArray = new String[16];
            for(int i=0,x=17;i<strArray.length;i++,x++){
                strArray[i] = "perg"+(x)+"_cb_resp1->gone";
            }
            comandosPorComponente(strArray, null);

            TableLayout tbVerao = (TableLayout)findViewByStringId("tb_verao");
            tbVerao.setVisibility(View.GONE);
        }


        if(!mareMorta){
            String[] strArray = new String[8];
            for(int i=0,x=1;i<8;i++,x++){
                strArray[i] = "perg"+(x)+"_cb_resp1->false";

                TableRow tbRow = (TableRow) findViewByStringId("tbrow_"+x);
                tbRow.setVisibility(View.GONE);
            }

            comandosPorComponente(strArray, null);

            for(int i=0,x=17;i<8;i++,x++){
                strArray[i] = "perg"+(x)+"_cb_resp1->false";

                TableRow tbRow = (TableRow) findViewByStringId("tbrow_"+x);
                tbRow.setVisibility(View.GONE);
            }

            comandosPorComponente(strArray, null);

        }
//
        if(!lancante){
            String[] strArray = new String[8];
            for(int i=0,x=9;i<8;i++,x++){
                strArray[i] = "perg"+(x)+"_cb_resp1->gone";
                TableRow tbRow = (TableRow) findViewByStringId("tbrow_"+x);
                tbRow.setVisibility(View.GONE);
            }

            comandosPorComponente(strArray, null);

            for(int i=0,x=25;i<8;i++,x++){
                strArray[i] = "perg"+(x)+"_cb_resp1->comandosPorComponente(strArray, null);";
                TableRow tbRow = (TableRow) findViewByStringId("tbrow_"+x);
                tbRow.setVisibility(View.GONE);
            }

            comandosPorComponente(strArray, null);
        }

    }


    /**
     * Desses compradores/consumidores quais são os três principais?
     * (informar se é interno ou externo da comunidade.
     * Na lista dos compradores marcados na questão anterior,
     * deve vir descrito se é da comunidade ou fora)
     */
    public void configuraTabelaB6Q9(final Formulario formulario){

        for(int i=1; i<=3;i++) {

            ImageView imgV = (ImageView) findViewByStringId("imgListaConsumidores" + i);

            final int indexEditText = i;


            if(imgV!=null) {
                imgV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Questao q51 = questaoDAO.findQuestaoByOrdemIdFormulario(62, formulario.getId());

                        Pergunta perg1 = formularioBO.getPerguntaPorOrdem(1, q51);//Na comunidade:
                        Pergunta perg2 = formularioBO.getPerguntaPorOrdem(2, q51);//Fora:


                        final ArrayList<String> consumidoresTexto = new ArrayList<String>();
                        final ArrayList<String> consumidoresLabel = new ArrayList<String>();

                        if(perg1!=null && perg1.getListaRespostas()!=null && perg1.getListaRespostas().size()>0){
                            for(Resposta r: perg1.getListaRespostas()){

                                if(r.getTexto()!=null && !r.getTexto().toLowerCase().contains("outro")) {
                                    consumidoresTexto.add(r.getTexto());
                                    consumidoresLabel.add(r.getTexto() + "(Comunidade)");
                                }
                            }
                        }

                        if(perg2!=null && perg2.getListaRespostas()!=null && perg2.getListaRespostas().size()>0){
                            for(Resposta r: perg2.getListaRespostas()){

                                if(r.getTexto()!=null && !r.getTexto().toLowerCase().contains("outro")) {
                                    consumidoresTexto.add(r.getTexto());
                                    consumidoresLabel.add(r.getTexto() + "(Fora)");
                                }
                            }
                        }

                        final String[] itemsBkp = new String[consumidoresLabel.size()];
                        final String[] items = consumidoresLabel.toArray(itemsBkp);

                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(getActivity());

                        builder.setTitle("Consumidores (Questão 8 - Anterior)")
                                .setItems(items, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        EditText et = (EditText) findViewByStringId("perg1_et_resp" + indexEditText);
                                        et.setText(consumidoresLabel.get(item));
                                    }
                                });


                        builder.create().show();

                    }
                });
            }

        }

    }


    public void validaQ56(int idTipoFormulario){
//        switch (idTipoFormulario) {
//            case 1: //Camarão Regional
//
//                break;
//            case 2: //Caranguejo
//
//                break;
//            case 3: //Piticaia e Branco
//                RadioButton rbTmp = (RadioButton) findViewByStringId("perg1_rb_resp1");
//                rbTmp.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        RadioGroup rgTmp = (RadioGroup) findViewByStringId("perg2_rg");
//                        rgTmp.setEnabled(true);
//                    }
//                });
//
//
//                RadioButton rbTmp2 = (RadioButton) findViewByStringId("perg1_rb_resp2");
//                rbTmp.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        RadioGroup rgTmp = (RadioGroup) findViewByStringId("perg2_rg");
//                        rgTmp.setEnabled(false);
//                    }
//                });
//
//                break;
//        }
    }

    public void configuraIdentificacaoEntrevisado(int idTipoFormulario){
        RadioButton rbMA = (RadioButton) findViewByStringId("perg3_rb_resp1");
        RadioButton rbAP = (RadioButton) findViewByStringId("perg3_rb_resp2");
        RadioButton rbPA = (RadioButton) findViewByStringId("perg3_rb_resp3");

        if(rbMA!=null && rbAP!=null && rbPA!=null) {
             switch (idTipoFormulario) {
                case 1: //Camarão Regional
                    rbMA.setEnabled(false);
                    rbAP.setEnabled(true);
                    rbPA.setEnabled(true);
                    break;
                case 2: //Caranguejo
                    rbMA.setEnabled(false);
                    rbAP.setEnabled(false);
                    rbPA.setEnabled(true);
                    break;
                case 3: //Piticaia e Branco
                    rbMA.setEnabled(true);
                    rbAP.setEnabled(false);
                    rbPA.setEnabled(false);
                    break;
            }
        }
    }
}
