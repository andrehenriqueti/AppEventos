package com.eventos.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventos.R;
import com.eventos.activity.CadastroEventoActivity;
import com.eventos.activity.CadastroUsuarioActivity;
import com.eventos.helper.SessionManager;

/**
 * Created by ANDRE on 20/11/2017.
 */

public class ListaEventosFragment extends Fragment {

    private View view;
    private FloatingActionButton floatingActionButtonAddEvento;

    public ListaEventosFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmento_lista_eventos,container,false);
        floatingActionButtonAddEvento = (FloatingActionButton) view.findViewById(R.id.link_cadastro_evento);
        floatingActionButtonAddEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(getActivity().getBaseContext());
                sessionManager.setDataFim("");
                sessionManager.setDataInicio("");
                sessionManager.setHorarioInicio("");
                sessionManager.setHorarioFim("");
                startActivity(new Intent(getActivity().getBaseContext(), CadastroEventoActivity.class));
            }
        });
        return view;
    }
}
