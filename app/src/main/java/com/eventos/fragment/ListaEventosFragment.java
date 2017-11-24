package com.eventos.fragment;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.eventos.R;
import com.eventos.activity.CadastroEventoActivity;
import com.eventos.dao.DaoEvento;
import com.eventos.helper.DatabaseHelper;
import com.eventos.helper.SessionManager;

/**
 * Created by ANDRE on 20/11/2017.
 */

public class ListaEventosFragment extends ListFragment {

    private View view;
    private FloatingActionButton floatingActionButtonAddEvento;
    private ListView listaViewEventos;
    private BottomNavigationView listas;
    private String []de = {DatabaseHelper.Evento.NOME,DatabaseHelper.Evento.ENDERECO};
    private int[] para = {R.id.nome,R.id.end};
    private DaoEvento daoEvento;
    private SessionManager sessionManager;

    public ListaEventosFragment(){

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.listar_pendentes:
                    listaViewEventos.setAdapter(new SimpleAdapter(getActivity().getBaseContext(), daoEvento.listarEventos('P'), R.layout.componente_lista, de, para));
                    sessionManager.setListaSelecionada(1);
                    return true;
                case R.id.listar_ativos:
                    listaViewEventos.setAdapter(new SimpleAdapter(getActivity().getBaseContext(), daoEvento.listarEventos('A'), R.layout.componente_lista, de, para));
                    sessionManager.setListaSelecionada(2);
                    return true;
                case R.id.lista_finalizados:
                    listaViewEventos.setAdapter(new SimpleAdapter(getActivity().getBaseContext(), daoEvento.listarEventos('F'), R.layout.componente_lista, de, para));
                    sessionManager.setListaSelecionada(3);
                    return true;
            }
            return false;
        }
    };

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lista_eventos,container,false);
        floatingActionButtonAddEvento = (FloatingActionButton) view.findViewById(R.id.link_cadastro_evento);
        sessionManager = new SessionManager(getActivity().getBaseContext());
        floatingActionButtonAddEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setDataFim("");
                sessionManager.setDataInicio("");
                sessionManager.setHorarioInicio("");
                sessionManager.setHorarioFim("");
                sessionManager.setEndereco("");
                startActivity(new Intent(getActivity().getBaseContext(), CadastroEventoActivity.class));
            }
        });
        listas = (BottomNavigationView) view.findViewById(R.id.btn_navegacao);
        listas.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        daoEvento = new DaoEvento(getActivity().getApplicationContext());
        setupToolbar(view);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listaViewEventos = getListView();
        switch (sessionManager.getListaSelecionada()){
            case 1:
                listaViewEventos.setAdapter(new SimpleAdapter(getActivity().getBaseContext(),daoEvento.listarEventos('P'),R.layout.componente_lista,de,para));
                break;
            case 2:
                listaViewEventos.setAdapter(new SimpleAdapter(getActivity().getBaseContext(),daoEvento.listarEventos('A'),R.layout.componente_lista,de,para));
                break;
            case 3:
                listaViewEventos.setAdapter(new SimpleAdapter(getActivity().getBaseContext(),daoEvento.listarEventos('F'),R.layout.componente_lista,de,para));
                break;
            default:
                break;
        }
    }

    private void setupToolbar(View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        final ActionBar bar = appCompatActivity.getSupportActionBar();
        if(bar != null){
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setShowHideAnimationEnabled(true);
            bar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            bar.setTitle("Listar Eventos");
        }
    }
}
