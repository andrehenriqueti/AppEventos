package com.eventos.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eventos.R;
import com.eventos.activity.CadastroEventoActivity;
import com.eventos.app.AppConfig;
import com.eventos.app.AppController;
import com.eventos.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

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
        view = inflater.inflate(R.layout.fragment_lista_eventos,container,false);
        floatingActionButtonAddEvento = (FloatingActionButton) view.findViewById(R.id.link_cadastro_evento);
        floatingActionButtonAddEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(getActivity().getBaseContext());
                sessionManager.setDataFim("");
                sessionManager.setDataInicio("");
                sessionManager.setHorarioInicio("");
                sessionManager.setHorarioFim("");
                sessionManager.setEndereco("");
                startActivity(new Intent(getActivity().getBaseContext(), CadastroEventoActivity.class));
            }
        });
        setupToolbar(view);
        return view;
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
