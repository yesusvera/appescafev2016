package br.org.unesco.appesca.control;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.org.unesco.appesca.R;
import br.org.unesco.appesca.dao.QuestaoDAO;
import br.org.unesco.appesca.model.Formulario;
import br.org.unesco.appesca.model.Questao;
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
        public ImageView imgSituacaoFormulario;
        public ImageView imgVisualizarFormulario;

        public ViewHolder(View v) {
            super(v);

            txtNomeEntrevistado = (TextView) v.findViewById(R.id.txtNomeEntrevistado);
            txtUfMunicipio = (TextView) v.findViewById(R.id.txtUfMunicipio);
//            txtFuncao = (TextView) v.findViewById(R.id.txtFuncao);
            txtTipoFormulario = (TextView) v.findViewById(R.id.txtTipoFormulario);
            txtDataFormulario = (TextView) v.findViewById(R.id.txtDataFormulario);
            imgSituacaoFormulario = (ImageView) v.findViewById(R.id.imgSituacaoFormulario);
            imgVisualizarFormulario = (ImageView) v.findViewById(R.id.imgVisualizar);

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

        if(formulario.getSituacao()==0) {
            holder.imgSituacaoFormulario.setImageResource(R.drawable.nao_enviado_icone);
        }else if (formulario.getSituacao()==1){
            holder.imgSituacaoFormulario.setImageResource(R.drawable.nao_enviado_icone);
        }

        //QUESTAO ORDEM 0 (ZERO) -> IDENTIFICACAO FORMULARIO
        Questao questao = questaoDAO.findQuestaoByOrdemIdFormulario(0, formulario.getId());

        try {
            holder.txtNomeEntrevistado.setText(questao.getPerguntas().get(0).getRespostas().get(0).getTexto());
            String ufMunicipio = questao.getPerguntas().get(3).getRespostas().get(0).getTexto() +
                    "/" +
                    questao.getPerguntas().get(2).getRespostas().get(0).getTexto();
            holder.txtUfMunicipio.setText(ufMunicipio);
        }catch(Exception e){
            Log.i("Erro", "teste");
        }

        holder.imgVisualizarFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormularioListAdapter.this.context, FormCamRegActivityNew.class);
                intent.putExtra(FormCamRegActivityNew.ID_FORMULARIO_OPEN, formulario);
                FormularioListAdapter.this.context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListFormulario.size();
    }
}