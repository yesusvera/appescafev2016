package br.org.unesco.appesca.control;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.org.unesco.appesca.R;
import br.org.unesco.appesca.bo.FormularioBO;
import br.org.unesco.appesca.dao.QuestaoDAO;
import br.org.unesco.appesca.model.Formulario;
import br.org.unesco.appesca.model.Questao;
import br.org.unesco.appesca.model.Resposta;
import br.org.unesco.appesca.util.AppescaUtil;
import br.org.unesco.appesca.util.ConstantesIdsFormularios;
import br.org.unesco.appesca.util.ConstantesUNESCO;
import br.org.unesco.appesca.view.FormCamRegActivityNew;

public class FormularioListAdapter extends RecyclerView.Adapter<FormularioListAdapter.ViewHolder>{

    private List<Formulario> mListFormulario;

    private Context context;

    private QuestaoDAO questaoDAO;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNomeEntrevistado;
        public TextView txtUfMunicipio;
//        public TextView txtFuncao;
        public TextView txtTipoFormulario;
        public TextView txtDataFormulario;
        public TextView txtSituacao;
        public TextView txtIdSincronizacao;
        public ImageView imgSituacaoFormulario;
        public ImageView imgVisualizarFormulario;
        public ProgressBar progressBar;
        public TextView txtTextoResposta;

        public ViewHolder(View v) {
            super(v);

            txtNomeEntrevistado = (TextView) v.findViewById(R.id.txtNomeEntrevistado);
            txtUfMunicipio = (TextView) v.findViewById(R.id.txtUfMunicipio);
//            txtFuncao = (TextView) v.findViewById(R.id.txtFuncao);
            txtTipoFormulario = (TextView) v.findViewById(R.id.txtTipoFormulario);
            txtDataFormulario = (TextView) v.findViewById(R.id.txtDataFormulario);
            imgSituacaoFormulario = (ImageView) v.findViewById(R.id.imgSituacaoFormulario);
            txtSituacao = (TextView) v.findViewById(R.id.txtSituacao);
            txtIdSincronizacao = (TextView) v.findViewById(R.id.txtIdSincronizacao);
            imgVisualizarFormulario = (ImageView) v.findViewById(R.id.imgVisualizar);
            progressBar = (ProgressBar)v.findViewById(R.id.progressBarRespostas);
            txtTextoResposta = (TextView)v.findViewById(R.id.txtTextoResposta);

            //Questão ordem 0 - Identificação do formulário
            questaoDAO = new QuestaoDAO(v.getContext());
        }
    }

    public void add(int position, Formulario item) {
        mListFormulario.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = mListFormulario.indexOf(item);
        mListFormulario.remove(position);
        notifyItemRemoved(position);
    }

    public FormularioListAdapter(List<Formulario> listFormulario) {
        mListFormulario = listFormulario;
    }

    @Override
    public FormularioListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_formulario, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Formulario formulario = mListFormulario.get(position);
        holder.txtDataFormulario.setText(new SimpleDateFormat(ConstantesUNESCO.FORMATO_DATA).format(formulario.getDataAplicacao()));
        holder.txtTipoFormulario.setText(formulario.getNome());
//        holder.txtUfMunicipio.setText("");
//        holder.txtFuncao.setText("");

        switch(formulario.getSituacao()){
            case -1:
                holder.imgSituacaoFormulario.setImageResource(R.drawable.questao_icon_devolvido);
                holder.txtSituacao.setText("Devolvido");
                holder.txtIdSincronizacao.setText(formulario.getIdSincronizacao());
                break;
            case 0:
                holder.imgSituacaoFormulario.setImageResource(R.drawable.nao_enviado_icone);
                holder.txtSituacao.setText("Não enviado");
                break;
            case 1:
                holder.imgSituacaoFormulario.setImageResource(R.drawable.equipe_icon);
                holder.txtSituacao.setText("Em aprovação");
                holder.txtIdSincronizacao.setText(formulario.getIdSincronizacao());
                break;
            case 2:
                holder.imgSituacaoFormulario.setImageResource(R.drawable.enviado_icone);
                holder.txtSituacao.setText("Finalizado");
                holder.txtTipoFormulario.setText(holder.txtTipoFormulario.getText() + "(Em " + (10 - AppescaUtil.dataDiff(formulario.getDataAplicacao(), new Date()))+" dias será excluído do aparelho)");
                holder.txtIdSincronizacao.setText(formulario.getIdSincronizacao());
                break;
        }


        int[] arraysIdsMenuLateral = new int[]{};
        int[] arrayIdsQuestoes = new int[]{};

        switch (formulario.getIdTipoFormulario()){
            case 1:
                arraysIdsMenuLateral = ConstantesIdsFormularios.arraysIdsMenuLateralCamaraoRegional;
                arrayIdsQuestoes = ConstantesIdsFormularios.arrayIdsFormularioCamaraoRegional;
                break;
            case 2:
                arraysIdsMenuLateral = ConstantesIdsFormularios.arraysIdsMenuLateralCaranguejo;
                arrayIdsQuestoes = ConstantesIdsFormularios.arrayIdsFormularioCaranguejo;
                break;
            case 3:
                arraysIdsMenuLateral = ConstantesIdsFormularios.arraysIdsMenuLateralPiticaiaEBranco;
                arrayIdsQuestoes = ConstantesIdsFormularios.arrayIdsFormularioPiticaiaEBranco;
                break;
        }

        Questao questao = questaoDAO.findQuestaoByOrdemIdFormulario(0, formulario.getId());

        FormularioBO formularioBO = new FormularioBO();

        Resposta nomeEntrevistado = formularioBO.getResposta(questao, 1, 1);

        try {

            if(nomeEntrevistado!=null && nomeEntrevistado.getTexto()!=null) {
                holder.txtNomeEntrevistado.setText(nomeEntrevistado.getTexto());
            }

            Resposta respUF = formularioBO.getResposta(questao, 4, 1);
            Resposta respMun = formularioBO.getResposta(questao, 3, 1);


            String texto = "";
            if(respUF!=null && respUF.getTexto()!=null){
                texto += respUF.getTexto();
            }
            if(respMun!=null && respMun.getTexto()!=null){
                texto += "/" + respMun.getTexto();
            }
            holder.txtUfMunicipio.setText(texto);
        }catch(Exception e){
            Log.i("Erro", "teste");
        }

        holder.imgVisualizarFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppescaUtil.limparMemoria();
                Intent intent = new Intent(FormularioListAdapter.this.context, FormCamRegActivityNew.class);
                intent.putExtra(FormCamRegActivityNew.ID_FORMULARIO_OPEN, formulario);
                FormularioListAdapter.this.context.startActivity(intent);
            }
        });


        if(holder.progressBar!=null){
            holder.progressBar.setMax(arraysIdsMenuLateral.length);

            List<Questao> listaQuestoes = questaoDAO.getQuestoesByFormulario(formulario.getId());

            if(listaQuestoes==null || listaQuestoes.size() == 0) {
                holder.progressBar.setProgress(0);
                holder.txtTextoResposta.setText("Nenhuma questão respondida");
            }else {
                holder.progressBar.setProgress(listaQuestoes.size());

                holder.txtTextoResposta.setText((listaQuestoes == null ? 0 : listaQuestoes.size()) + " de " + arraysIdsMenuLateral.length + " respondidas.");

                if(listaQuestoes.size() == arraysIdsMenuLateral.length) {

                    holder.txtTextoResposta.setText(holder.txtTextoResposta.getText()+"\nPronto para envio.");
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mListFormulario.size();
    }
}