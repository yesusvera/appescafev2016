package br.org.unesco.appesca.control;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.org.unesco.appesca.R;
import br.org.unesco.appesca.dao.FormularioDAO;
import br.org.unesco.appesca.model.Formulario;
import br.org.unesco.appesca.model.Identity;
import br.org.unesco.appesca.util.AppescaUtil;

public class FormularioListDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private List<Formulario> mFormularioList = new ArrayList<Formulario>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            try {
                int situacaoFormulario = getArguments().getInt(ARG_ITEM_ID);

                FormularioDAO formularioDAO = new FormularioDAO(getActivity());
                String titulo = "Lista";

                TextView txtMensagemFormulariosFinalizados = (TextView) getActivity().findViewById(R.id.txtMensagemFormulariosFinalizados);
                txtMensagemFormulariosFinalizados.setText("");

                switch (situacaoFormulario){
                    case 0: //TODOS
                        mFormularioList = formularioDAO.listarTodosPorUsuario(Identity.getUsuarioLogado().getId());
                        titulo = "Todos os Formulários (Locais e Enviados)";
                        break;
                    case 1: //PARA APROVACAO
                        mFormularioList = formularioDAO.listarPorSituacaoUsuario(1, Identity.getUsuarioLogado().getId());
                        titulo = "Formulários Enviados para Aprovação (ONLINE)";
                        break;
                    case 2: //OFFLINE
                        mFormularioList = formularioDAO.listarTodosOffLine(Identity.getUsuarioLogado().getId());
                        titulo = "Formulários no Dispositivo (LOCAL)";
                        break;
                    case 3: //APROVADOS
                        mFormularioList = formularioDAO.listarPorSituacaoUsuario(2,Identity.getUsuarioLogado().getId());
                        titulo = "Formulários Finalizados (ONLINE)";


                        if(mFormularioList!=null && mFormularioList.size()>0){
                            txtMensagemFormulariosFinalizados.setText("Os formulários aprovados podem somente ser visualizados. Serão deletados do aplicativo depois de 10 dias de visualização.");
                        }

                        aplicaRegraExlusaoAposDias(10, mFormularioList);

                        mFormularioList = formularioDAO.listarPorSituacaoUsuario(2,Identity.getUsuarioLogado().getId());

                        break;
                }


                TextView txtTituloListaFormularios = (TextView) getActivity().findViewById(R.id.txtTituloListaFormularios);
                txtTituloListaFormularios.setText(titulo);

                TextView txtMensagemQuantidade = (TextView) getActivity().findViewById(R.id.txtMensagemQuantidade);
                if (mFormularioList == null || mFormularioList.size() == 0) {
                    txtMensagemQuantidade.setText("Nenhum formulário encontrado para esta lista.");
                } else {
                    txtMensagemQuantidade.setText(mFormularioList.size() + " formulário(s) encontrado(s).");
                }

            }catch (Exception e){

            }
        }
    }

    public void aplicaRegraExlusaoAposDias(int diasRegra,  List<Formulario> listaFormulario){
        if(listaFormulario==null){
            return;
        }

        for(Formulario form: listaFormulario){
            if(form.getSituacao()==2) {
                int diferencaDias = (10 - AppescaUtil.dataDiff(form.getDataAplicacao(), new Date()));

                if (diferencaDias <= 0) {
                    FormularioDAO formularioDAO = new FormularioDAO(getActivity());
                    formularioDAO.delete(form.getId());
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View rowView = inflater.inflate(R.layout.formulario_list_detail, container, false);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.formulario_list_rv);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FormularioListAdapter(mFormularioList);
        mRecyclerView.setAdapter(mAdapter);
        return rowView;
    }

}
