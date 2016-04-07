package br.org.unesco.appesca.control;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.lassana.recorder.AudioRecorder;
import com.github.lassana.recorder.AudioRecorderBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.org.unesco.appesca.R;
import br.org.unesco.appesca.bo.FormularioBO;
import br.org.unesco.appesca.bo.RecorderApplication;
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


    //GRAVACAO

    private static final int REQUEST_CODE_PERMISSIONS = 0x1;

    private Button mStartButton;
    private Button mPauseButton;
    private Button mPlayButton;
    private ImageView mCassetteImage;

    private Uri mAudioRecordUri;
    private String mActiveRecordFileName;

    private AudioRecorder mAudioRecorder;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonStartRecording:
                    v.setEnabled(false);
                    tryStart();
                    break;
                case R.id.buttonPauseRecording:
                    v.setEnabled(false);
                    pause();
                    break;
                case R.id.buttonPlayRecording:
                    play();
                    break;
                default:
                    break;
            }
        }
    };

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
    public void onDestroy() {
        if (mAudioRecorder.isRecording()) {
            mAudioRecorder.cancel();
            getActivity().setResult(Activity.RESULT_CANCELED);
        }
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(Integer.valueOf(id_layout_inflate), container, false);

        //gravacao
//        final RecorderApplication application = RecorderApplication.getApplication(getActivity());
//        mAudioRecorder = application.getRecorder();
        if (mAudioRecorder == null
                || mAudioRecorder.getStatus() == AudioRecorder.Status.STATUS_UNKNOWN) {
            mAudioRecorder = AudioRecorderBuilder.with(getContext())
                    .fileName(getNextFileName())
                    .config(AudioRecorder.MediaRecorderConfig.DEFAULT)
                    .loggable()
                    .build();
//            application.setRecorder(mAudioRecorder);
        }

        mCassetteImage = (ImageView) rootView.findViewById(R.id.image_cassette);

        mStartButton = (Button) rootView.findViewById(R.id.buttonStartRecording);
        if(mStartButton!=null)
            mStartButton.setOnClickListener(mOnClickListener);

        mPauseButton = (Button) rootView.findViewById(R.id.buttonPauseRecording);
        if(mPauseButton!=null)
            mPauseButton.setOnClickListener(mOnClickListener);

        mPlayButton = (Button) rootView.findViewById(R.id.buttonPlayRecording);
        if(mPlayButton!=null)
            mPlayButton.setOnClickListener(mOnClickListener);

        invalidateViews();

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


                }else if(tagSpl[0].contains("tbrow")){
                    TableRow tableRow = (TableRow) findViewByStringId(tagSpl[0]);
                    if(tagSpl[1]!=null && tagSpl[1].equalsIgnoreCase("this")) {
                        if (tableRow != null && viewComponent instanceof CompoundButton) {
                            Boolean enabledParam = ((CompoundButton) viewComponent).isChecked();
                            if (enabledParam != null) {
                                if (enabledParam) {
                                    tableRow.setVisibility(View.VISIBLE);
                                }else{
                                    tableRow.setVisibility(View.GONE);
                                }
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



        //Tratamento e validações para questões genérica
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

                    switch (ordemQuestao)
                    {
                        case  52: configuraTabelaB6Q6_REGIONAL(formulario); break;
                        case  57: configuraTabelaB7Q4_REGIONAL(formulario); break;
                        case  61: configuraTabelaB7Q8_REGIONAL(formulario); break;
                        case  62: configuraTabelaB7Q9_REGIONAL(formulario); break;
                        case  63: configuraTabelaB7Q10_REGIONAL(formulario); break;
                        default:
                    }
                    break;
                case 2: //CARANGUEJO
                    switch (ordemQuestao)
                    {
                        case 53: configuraTabelaB6Q7(formulario);break;
                        case 59: configuraTabelaB6Q5_CARANGUEJO(formulario);break;
                        case 63: configuraTabelaB7Q9(formulario);break;
                        case 64: configuraTabelaB7Q10(formulario);break;
                        case 65: configuraB7Q11(formulario);break;

                        default:
                    }
                    break;
                case 3: //PITICAIA E BRANCO
                    switch (ordemQuestao)
                    {
                        case 50: configuraTabelaB6Q4_PITICAIA(formulario);
                                 carregaRespostasB6Q4_PITICAIA(formulario);
                                    break;
                        case 51: configuraTabelaB6Q4_PITICAIA(formulario); //esta possui os mesmos componentes visuais.
//                                 carregaRespostasB6Q4_PITICAIA(formulario);
                                     break;
                        case 53:
                        case 54:
                                 configuraTabelaB7Q1_PITICAIA(formulario);
                                 break;
                        default:
                    }
                    break;
            }
        }
    }

    public View findViewByStringId(String id){
        return getActivity().findViewById(getResources().getIdentifier(id, "id", getActivity().getPackageName()));
    }

    public void carregaRespostasB6Q4_PITICAIA(Formulario formulario){
        Questao q50 = questaoDAO.findQuestaoByOrdemIdFormulario(50, formulario.getId());

        Resposta veraoResp = formularioBO.getResposta(q50, 1, 1);
        Resposta invernoResp = formularioBO.getResposta(q50, 1, 2);
        Resposta baixaMortaResp = formularioBO.getResposta(q50, 2, 1);
        Resposta grandeAltaResp = formularioBO.getResposta(q50, 2, 2);

        CheckBox veraoCheckBox = (CheckBox)findViewByStringId("perg1_cb_resp1");
        CheckBox invernoCheckBox = (CheckBox)findViewByStringId("perg1_cb_resp2");
        CheckBox baixaMorta = (CheckBox)findViewByStringId("perg2_cb_resp1");
        CheckBox grandeAlta = (CheckBox)findViewByStringId("perg2_cb_resp2");

        veraoCheckBox.setChecked(veraoResp!=null);
        invernoCheckBox.setChecked(invernoResp!=null);
        baixaMorta.setChecked(baixaMortaResp!=null);
        grandeAlta.setChecked(grandeAltaResp!=null);

        veraoCheckBox.callOnClick();
        invernoCheckBox.callOnClick();
        baixaMorta.callOnClick();
        grandeAlta.callOnClick();
    }


    public void configuraTabelaB7Q1_PITICAIA(Formulario formulario){


        for(int i=1; i<=3;i++) {

            ImageView imgV = (ImageView) findViewByStringId("imgListaConsumidores" + i);

            final int indexEditText = i;


            if(imgV!=null) {
                imgV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ArrayList<String> consumidoresTexto = new ArrayList<String>();
                        final ArrayList<String> consumidoresLabel = new ArrayList<String>();


                        for(int i=1;i<=12;i++){
                            CheckBox cb = (CheckBox)findViewByStringId("perg33_cb_resp" + i);
                            if(cb!=null && cb.isChecked()){
                                if(cb.getText()!=null ) {
                                    consumidoresTexto.add(cb.getText().toString());
                                    consumidoresLabel.add(cb.getText().toString() + "(Comunidade)");
                                }
                            }
                        }

                        EditText et33resp14 = (EditText)findViewByStringId("perg33_et_resp14");
                        if(et33resp14!=null && et33resp14.getText().toString()!=null && et33resp14.getText().length()>0){
                            consumidoresTexto.add(et33resp14.getText().toString());
                            consumidoresLabel.add(et33resp14.getText().toString() + "(Comunidade)");
                        }



                        for(int i=15;i<=24;i++){
                            CheckBox cb = (CheckBox)findViewByStringId("perg33_cb_resp" + i);
                            if(cb!=null && cb.isChecked()){
                                if(cb.getText()!=null) {
                                    consumidoresTexto.add(cb.getText().toString());
                                    consumidoresLabel.add(cb.getText().toString() + "(Fora)");
                                }
                            }
                        }

                        EditText et33resp26 = (EditText)findViewByStringId("perg33_et_resp26");
                        if(et33resp26!=null && et33resp26.getText().toString()!=null && et33resp26.getText().length()>0){
                            consumidoresTexto.add(et33resp26.getText().toString());
                            consumidoresLabel.add(et33resp26.getText().toString() + "(Fora)");
                        }



                        final String[] itemsBkp = new String[consumidoresLabel.size()];
                        final String[] items = consumidoresLabel.toArray(itemsBkp);

                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(getActivity());

                        builder.setTitle("Consumidores (1.8 - Anterior)")
                                .setItems(items, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        EditText et = (EditText) findViewByStringId("perg34_et_resp" + indexEditText);
                                        et.setText(consumidoresLabel.get(item));
                                    }
                                });


                        builder.create().show();

                    }
                });
            }

        }
    }

    public void configuraTabelaB6Q4_PITICAIA(Formulario formulario){
        final CheckBox veraoCheckBox = (CheckBox)findViewByStringId("perg1_cb_resp1");
        final CheckBox invernoCheckBox = (CheckBox)findViewByStringId("perg1_cb_resp2");
        CheckBox baixaMorta = (CheckBox)findViewByStringId("perg2_cb_resp1");
        CheckBox grandeAlta = (CheckBox)findViewByStringId("perg2_cb_resp2");

        if(veraoCheckBox!=null){
            veraoCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableLayout tbVerao = (TableLayout)findViewByStringId("tb_verao");
                    if(tbVerao!=null) {
                        if (!((CheckBox) v).isChecked()) {
                            tbVerao.setVisibility(View.GONE);
                        } else {
                            tbVerao.setVisibility(View.VISIBLE);
                        }

                    }
                }
            });
        }
        if(invernoCheckBox!=null){
            invernoCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableLayout tbInverno = (TableLayout)findViewByStringId("tb_inverno");
                    if(tbInverno!=null) {
                        if (!((CheckBox) v).isChecked()) {
                            tbInverno.setVisibility(View.GONE);
                        } else {
                            tbInverno.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }

        if(baixaMorta!=null){
            baixaMorta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean enabled = ((CheckBox) v).isChecked();

                    for(int i=3;i<=10;i++){
                        CheckBox checkArte = (CheckBox)findViewByStringId("perg"+i+"_cb_resp1");
                        if(checkArte!=null){
                            checkArte.setEnabled(enabled);
                            if(!enabled){
                                checkArte.setChecked(false);
                            }
                            checkArte.callOnClick();
                        }
                    }
                    for(int i=19;i<=26;i++){
                        CheckBox checkArte = (CheckBox)findViewByStringId("perg"+i+"_cb_resp1");
                        if(checkArte!=null){
                            checkArte.setEnabled(enabled);
                            if(!enabled){
                                checkArte.setChecked(false);
                            }
                            checkArte.callOnClick();
                        }
                    }
                }
            });
        }


        if(grandeAlta!=null){
            grandeAlta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean enabled = ((CheckBox) v).isChecked();

                    for(int i=11;i<=18;i++){
                        CheckBox checkArte = (CheckBox)findViewByStringId("perg"+i+"_cb_resp1");
                        if(checkArte!=null){
                            checkArte.setEnabled(enabled);
                            if(!enabled){
                                checkArte.setChecked(false);
                            }
                            checkArte.callOnClick();
                        }
                    }

                    for(int i=27;i<=34;i++){
                        CheckBox checkArte = (CheckBox)findViewByStringId("perg"+i+"_cb_resp1");
                        if(checkArte!=null){
                            checkArte.setEnabled(enabled);
                            if(!enabled){
                                checkArte.setChecked(false);
                            }
                            checkArte.callOnClick();
                        }
                    }
                }
            });
        }
        
    }

    public void configuraTabelaB7Q4_REGIONAL(Formulario formulario) {
        Questao q54 = questaoDAO.findQuestaoByOrdemIdFormulario(54, formulario.getId());

        int rowOrder = 1;
        for(int i=1;i<=3;i++){
            for(int j=1;j<=7;j++) {
                boolean resp = formularioBO.getResposta(q54, i, j)!=null;

                if(!resp){
                    TableRow tbRow = (TableRow)findViewByStringId("tbrow_" + rowOrder);
                    if(tbRow!=null){
                        tbRow.setVisibility(View.GONE);
                    }
                }
                rowOrder++;
            }
        }
    }


    public void configuraTabelaB7Q9_REGIONAL(Formulario formulario) {
        Questao q54 = questaoDAO.findQuestaoByOrdemIdFormulario(54, formulario.getId());

        int rowOrder = 1;
        for(int i=1;i<=3;i++){
            for(int j=1;j<=7;j++) {
                boolean resp = formularioBO.getResposta(q54, i, j)!=null;

                if(!resp){
                    TableRow tbRow = (TableRow)findViewByStringId("tbrow_" + rowOrder);
                    if(tbRow!=null){
                        tbRow.setVisibility(View.GONE);
                    }
                }
                rowOrder++;
            }
        }

        Questao q61 = questaoDAO.findQuestaoByOrdemIdFormulario(61, formulario.getId());

        Resposta resp1 = formularioBO.getResposta(q61, 1, 1);
        Resposta resp2 = formularioBO.getResposta(q61,1,2);
        Resposta resp3 = formularioBO.getResposta(q61,1,3);

        if(resp1!=null){
            TextView comprador1Txt = (TextView)findViewByStringId("comprador1Txt");
            comprador1Txt.setText(resp1.getTexto());
        }
        if(resp2!=null){
            TextView comprador2Txt = (TextView)findViewByStringId("comprador2Txt");
            comprador2Txt.setText(resp2.getTexto());
        }
        if(resp3!=null){
            TextView comprador3Txt = (TextView)findViewByStringId("comprador3Txt");
            comprador3Txt.setText(resp3.getTexto());
        }
    }

    public void configuraTabelaB7Q10_REGIONAL(Formulario formulario) {

        Questao q61 = questaoDAO.findQuestaoByOrdemIdFormulario(61, formulario.getId());

        Resposta resp1 = formularioBO.getResposta(q61, 1, 1);
        Resposta resp2 = formularioBO.getResposta(q61,1,2);
        Resposta resp3 = formularioBO.getResposta(q61,1,3);

        if(resp1!=null){
            TextView comprador1Txt = (TextView)findViewByStringId("comprador1Txt");
            if(comprador1Txt!=null)
                comprador1Txt.setText(resp1.getTexto() + ":");
        }
        if(resp2!=null){
            TextView comprador2Txt = (TextView)findViewByStringId("comprador2Txt");
            if(comprador2Txt!=null)
                comprador2Txt.setText(resp2.getTexto() + ":");
        }
        if(resp3!=null){
            TextView comprador3Txt = (TextView)findViewByStringId("comprador3Txt");
            if(comprador3Txt!=null)
                comprador3Txt.setText(resp3.getTexto() + ":");
        }
    }

    public void configuraTabelaB6Q6_REGIONAL(Formulario formulario){


        Questao q50 = questaoDAO.findQuestaoByOrdemIdFormulario(50, formulario.getId());
        Questao q51 = questaoDAO.findQuestaoByOrdemIdFormulario(51, formulario.getId());

        boolean verao = formularioBO.getResposta(q50, 1, 1)!=null;
        boolean inverno = formularioBO.getResposta(q50, 1, 2)!=null;

        boolean baixaMorta = formularioBO.getResposta(q51, 1, 1)!=null;
        boolean grandeAlt = formularioBO.getResposta(q51, 1, 2)!=null;


        if(!inverno){
            //Primeira tabela
            String[] strArray = new String[10];
            for(int i=0,x=1;i<strArray.length;i++,x++){
                strArray[i] = "perg"+(x)+"_cb_resp1->gone";
            }
            comandosPorComponente(strArray, null);

            TableLayout tbInverno = (TableLayout)findViewByStringId("tb_inverno");
            tbInverno.setVisibility(View.GONE);

            //Cabeçalhos da terceira tabela
            TextView cabecalhoInverno1 = (TextView)findViewByStringId("cabecalhoInverno1");
            TextView cabecalhoInverno2 = (TextView)findViewByStringId("cabecalhoInverno2");

            cabecalhoInverno1.setVisibility(View.INVISIBLE);
            cabecalhoInverno2.setVisibility(View.INVISIBLE);
        }


        if(!verao){
            String[] strArray = new String[10];
            for(int i=0,x=11;i<strArray.length;i++,x++){
                strArray[i] = "perg"+(x)+"_cb_resp1->gone";
            }
            comandosPorComponente(strArray, null);

            TableLayout tbVerao = (TableLayout)findViewByStringId("tb_verao");
            tbVerao.setVisibility(View.GONE);


            //Cabeçalhos da terceira tabela
            TextView cabecalhoVerao1 = (TextView)findViewByStringId("cabecalhoVerao1");
            TextView cabecalhoVerao2 = (TextView)findViewByStringId("cabecalhoVerao2");

            cabecalhoVerao1.setVisibility(View.INVISIBLE);
            cabecalhoVerao2.setVisibility(View.INVISIBLE);
        }


        if(!baixaMorta){
            String[] strArray = new String[5];
            for(int i=0,x=1;i<5;i++,x++){
                strArray[i] = "perg"+(x)+"_cb_resp1->false";

                TableRow tbRow = (TableRow) findViewByStringId("tbrow_"+x);
                tbRow.setVisibility(View.GONE);
            }

            comandosPorComponente(strArray, null);

            for(int i=0,x=11;i<5;i++,x++){
                strArray[i] = "perg"+(x)+"_cb_resp1->false";

                TableRow tbRow = (TableRow) findViewByStringId("tbrow_"+x);
                tbRow.setVisibility(View.GONE);
            }

            comandosPorComponente(strArray, null);

        }
//
        if(!grandeAlt){
            String[] strArray = new String[5];
            for(int i=0,x=5;i<5;i++,x++){
                strArray[i] = "perg"+(x)+"_cb_resp1->gone";
                TableRow tbRow = (TableRow) findViewByStringId("tbrow_"+x);
                tbRow.setVisibility(View.GONE);
            }

            comandosPorComponente(strArray, null);

            for(int i=0,x=15;i<5;i++,x++){
                strArray[i] = "perg"+(x)+"_cb_resp1->gone";
                TableRow tbRow = (TableRow) findViewByStringId("tbrow_"+x);
                tbRow.setVisibility(View.GONE);
            }

            comandosPorComponente(strArray, null);
        }

        //EditText's da terceira tabela
        for(int i=21;i<=28;i++){
            for(int j=1;j<=4;j++){
                if((!verao && (j==1 || j==3)) ||
                   (!inverno && (j==2 || j==4)) ){
                    EditText editText = (EditText) findViewByStringId("perg"+i+"_et_resp"+j);
                    if(editText!=null){
                        editText.setText("");
                        editText.setVisibility(View.INVISIBLE);
                    }
                }
            }

            if((!baixaMorta && (i>=21 && i<=24)) ||
                    (!grandeAlt && (i>=25 && i<=28))
                    ){
                TableRow tbRow = (TableRow) findViewByStringId("tbrow_" + i);
                if(tbRow!=null){
                    tbRow.setVisibility(View.GONE);
                }
            }
        }

        //bloquear quando não selecionou matapi, viveiro, rede e tarrafa


        Questao q49 = questaoDAO.findQuestaoByOrdemIdFormulario(49, formulario.getId());
        boolean matapi   = formularioBO.getResposta(q49, 1, 1)!=null;
        boolean viveiro  = formularioBO.getResposta(q49, 1, 2)!=null;
        boolean rede     = formularioBO.getResposta(q49, 1, 3)!=null;
        boolean tarrafa  = formularioBO.getResposta(q49, 1, 4)!=null;

        boolean[] arrResp = new boolean[]{matapi, viveiro, rede, tarrafa};
        for(int i=1,m=1;i<=20;i++,m++){
           switch (m){
               case 1: case 2:case 3: case 4:
                   if(!arrResp[m-1]){
                       CheckBox cbox = (CheckBox) findViewByStringId("perg"+i+"_cb_resp1");
                       cbox.setChecked(false);
                       cbox.setEnabled(false);
                       cbox.callOnClick();
                   }
                   break;
               default:
           }
            if(m==5) m=0;
        }

        for(int i=21,m=1;i<=28;i++,m++){

            switch (m){
                case 1: case 2:case 3: case 4:
                    if(!arrResp[m-1]){
                        for(int k=1;k<=4;k++) {
                            EditText editText = (EditText) findViewByStringId("perg"+i+"_et_resp"+k);
                            editText.setText("");
                            editText.setVisibility(View.GONE);
                        }
                    }
                    break;
                default:
            }
            if(m==4) m=0;
        }
    }



    public void configuraTabelaB6Q5_CARANGUEJO(Formulario formulario){
        Questao q58 = questaoDAO.findQuestaoByOrdemIdFormulario(58, formulario.getId());
        boolean respondeuNao = formularioBO.getResposta(q58, 1, 1)!=null;
        boolean respondeuSim = formularioBO.getResposta(q58, 1, 2)!=null;

        if(respondeuNao){
            for(int i=1;i<=6;i++){
                RadioButton rb = (RadioButton)findViewByStringId("perg1_rb_resp"+i);
                if(rb!=null){
                    rb.setChecked(false);
                    rb.setEnabled(false);
                    rb.callOnClick();
                }
            }

            RadioButton rb = (RadioButton)findViewByStringId("perg1_rb_resp6");
            rb.setChecked(true);
            rb.setEnabled(true);
        }

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
                strArray[i] = "perg"+(x)+"_cb_resp1->gone";
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
    public void configuraTabelaB7Q8_REGIONAL(final Formulario formulario){

        for(int i=1; i<=3;i++) {

            ImageView imgV = (ImageView) findViewByStringId("imgListaConsumidores" + i);

            final int indexEditText = i;


            if(imgV!=null) {
                imgV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Questao q51 = questaoDAO.findQuestaoByOrdemIdFormulario(60, formulario.getId());

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

                        builder.setTitle("Consumidores (Questão 7 - Anterior)")
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

    /**
     * Desses compradores/consumidores quais são os três principais?
     * (informar se é interno ou externo da comunidade.
     * Na lista dos compradores marcados na questão anterior,
     * deve vir descrito se é da comunidade ou fora)
     */
    public void configuraTabelaB7Q9(final Formulario formulario){

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

    public void configuraTabelaB7Q10(Formulario formulario) {

        Questao q63 = questaoDAO.findQuestaoByOrdemIdFormulario(63, formulario.getId());

        Resposta resp1 = formularioBO.getResposta(q63, 1, 1);
        Resposta resp2 = formularioBO.getResposta(q63,1,2);
        Resposta resp3 = formularioBO.getResposta(q63,1,3);

        if(resp1!=null){
            TextView comprador1Txt = (TextView)findViewByStringId("comprador1Txt");
            comprador1Txt.setText(resp1.getTexto());
        }
        if(resp2!=null){
            TextView comprador2Txt = (TextView)findViewByStringId("comprador2Txt");
            comprador2Txt.setText(resp2.getTexto());
        }
        if(resp3!=null){
            TextView comprador3Txt = (TextView)findViewByStringId("comprador3Txt");
            comprador3Txt.setText(resp3.getTexto());
        }
    }


    public void configuraB7Q11(Formulario formulario) {

        Questao q63 = questaoDAO.findQuestaoByOrdemIdFormulario(63, formulario.getId());

        Resposta resp1 = formularioBO.getResposta(q63, 1, 1);
        Resposta resp2 = formularioBO.getResposta(q63,1,2);
        Resposta resp3 = formularioBO.getResposta(q63,1,3);

        if(resp1!=null){
            TextView comprador1Txt = (TextView)findViewByStringId("comprador1Txt");
            comprador1Txt.setText("1º Comprador - " + resp1.getTexto() + ":");

        }
        if(resp2!=null){
            TextView comprador2Txt = (TextView)findViewByStringId("comprador2Txt");
            comprador2Txt.setText("2º Comprador - " + resp2.getTexto() + ":");

        }
        if(resp3!=null){
            TextView comprador3Txt = (TextView)findViewByStringId("comprador3Txt");
            comprador3Txt.setText("3º Comprador - " + resp3.getTexto() + ":");
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


    //GRAVACAO

    private String getNextFileName() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                .getAbsolutePath()
                + File.separator
                + "Record_"
                + System.currentTimeMillis()
                + ".mp4";
    }

    private void invalidateViews() {
        if(mCassetteImage!=null && mStartButton!=null && mPauseButton!=null && mPlayButton!=null) {
            switch (mAudioRecorder.getStatus()) {
                case STATUS_UNKNOWN:
                    mCassetteImage.clearAnimation();
                    mStartButton.setEnabled(false);
                    mPauseButton.setEnabled(false);
                    mPlayButton.setEnabled(false);
                    break;
                case STATUS_READY_TO_RECORD:
                    mCassetteImage.clearAnimation();
                    mStartButton.setEnabled(true);
                    mPauseButton.setEnabled(false);
                    mPlayButton.setEnabled(false);
                    break;
                case STATUS_RECORDING:
                    mCassetteImage.startAnimation(
                            AnimationUtils.loadAnimation(getActivity(), R.anim.animation_pulse));
                    mStartButton.setEnabled(false);
                    mPauseButton.setEnabled(true);
                    mPlayButton.setEnabled(false);
                    break;
                case STATUS_RECORD_PAUSED:
                    mCassetteImage.clearAnimation();
                    mStartButton.setEnabled(true);
                    mPauseButton.setEnabled(false);
                    mPlayButton.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void tryStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final int checkAudio = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
            final int checkStorage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkAudio != PackageManager.PERMISSION_GRANTED || checkStorage != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.RECORD_AUDIO)) {
                    showNeedPermissionsMessage();
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showNeedPermissionsMessage();
                } else {
                    requestPermissions(new String[]{
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_PERMISSIONS);
                }
            } else {
                start();
            }
        } else {
            start();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
                boolean userAllowed = true;
                for (final int result : grantResults) {
                    userAllowed &= result == PackageManager.PERMISSION_GRANTED;
                }
                if (userAllowed) {
                    start();
                } else {
                    /*
                     * Cannot show dialog from here
                     * https://code.google.com/p/android-developer-preview/issues/detail?id=2823
                     */
                    showNeedPermissionsMessage();
                }
                break;
            default:
                break;
        }
    }

    private void showNeedPermissionsMessage() {
        invalidateViews();
        message(getString(R.string.error_no_permissions));
    }

    private void message(String message) {
        final View root = getView();
        if (root != null) {
            final Snackbar snackbar = Snackbar.make(root, message, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(android.R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }

    private void start() {
        mAudioRecorder.start(new AudioRecorder.OnStartListener() {
            @Override
            public void onStarted() {
                invalidateViews();
            }

            @Override
            public void onException(Exception e) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                invalidateViews();
                message(getString(R.string.error_audio_recorder, e));
            }
        });
    }

    private void pause() {
        mAudioRecorder.pause(new AudioRecorder.OnPauseListener() {
            @Override
            public void onPaused(String activeRecordFileName) {
                mActiveRecordFileName = activeRecordFileName;

                getActivity().setResult(Activity.RESULT_OK,
                        //new Intent().setData(Uri.parse(mActiveRecordFileName)));
                        new Intent().setData(saveCurrentRecordToMediaDB(mActiveRecordFileName)));
                invalidateViews();
            }

            @Override
            public void onException(Exception e) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                invalidateViews();
                message(getString(R.string.error_audio_recorder, e));
            }
        });
    }

    private void play() {
        File file = new File(mActiveRecordFileName);
        if (file.exists()) {
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "audio/*");
            startActivity(intent);
        }
    }

    /**
     * Creates new item in the system's media database.
     *
     * @see <a href="https://github.com/android/platform_packages_apps_soundrecorder/blob/master/src/com/android/soundrecorder/SoundRecorder.java">Android Recorder source</a>
     */
    public Uri saveCurrentRecordToMediaDB(final String fileName) {
        if (mAudioRecordUri != null) return mAudioRecordUri;

        final Activity activity = getActivity();
        final Resources res = activity.getResources();
        final ContentValues cv = new ContentValues();
        final File file = new File(fileName);
        final long current = System.currentTimeMillis();
        final long modDate = file.lastModified();
        final Date date = new Date(current);
        final String dateTemplate = res.getString(R.string.audio_db_title_format);
        final SimpleDateFormat formatter = new SimpleDateFormat(dateTemplate, Locale.getDefault());
        final String title = formatter.format(date);
        final long sampleLengthMillis = 1;
        // Lets label the recorded audio file as NON-MUSIC so that the file
        // won't be displayed automatically, except for in the playlist.
        cv.put(MediaStore.Audio.Media.IS_MUSIC, "0");

        cv.put(MediaStore.Audio.Media.TITLE, title);
        cv.put(MediaStore.Audio.Media.DATA, file.getAbsolutePath());
        cv.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        cv.put(MediaStore.Audio.Media.DATE_MODIFIED, (int) (modDate / 1000));
        cv.put(MediaStore.Audio.Media.DURATION, sampleLengthMillis);
        cv.put(MediaStore.Audio.Media.MIME_TYPE, "audio/*");
        cv.put(MediaStore.Audio.Media.ARTIST, res.getString(R.string.audio_db_artist_name));
        cv.put(MediaStore.Audio.Media.ALBUM, res.getString(R.string.audio_db_album_name));

//        Log.d(TAG, "Inserting audio record: " + cv.toString());

        final ContentResolver resolver = activity.getContentResolver();
        final Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        Log.d(TAG, "ContentURI: " + base);

        mAudioRecordUri = resolver.insert(base, cv);
        if (mAudioRecordUri == null) {
            return null;
        }
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, mAudioRecordUri));
        return mAudioRecordUri;
    }
}
