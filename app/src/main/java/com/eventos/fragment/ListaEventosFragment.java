package com.eventos.fragment;


import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eventos.R;
import com.eventos.activity.AlterarEventoActivity;
import com.eventos.activity.CadastroEventoActivity;
import com.eventos.activity.InformacoesEventoActivity;
import com.eventos.activity.Menu;
import com.eventos.app.AppConfig;
import com.eventos.app.AppController;
import com.eventos.bean.EventoBean;
import com.eventos.controller.Controller;
import com.eventos.dao.DaoEvento;
import com.eventos.helper.DatabaseHelper;
import com.eventos.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    private Snackbar mostraOpcao;
    private HashMap<String,Object> eventoSelecionado;
    private ProgressDialog progressDialog;

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        eventoSelecionado = (HashMap) listaViewEventos.getAdapter().getItem(info.position);

        //Declaração das opções do menu

        menu.setHeaderTitle("Menu de Opções");
        MenuItem excluir, alterar, maisInfo;
        excluir = menu.add("Excluir Evento");
        alterar = menu.add("Alterar Evento");
        maisInfo = menu.add("Mais Informações");

        //Declaração da ação para cada item do menu

        //Alterar
        alterar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                atualizarEvento(eventoSelecionado);
                return false;
            }
        });

        //Mais Informações
        maisInfo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                exibiInfoEvento(eventoSelecionado);
                return false;
            }
        });

        //Excluir
        excluir.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                alertExcluir();
                return false;
            }
        });
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
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
        mostraOpcao = Snackbar.make(getView(),"Clique e segure sobre o evento para mais informações",Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostraOpcao.dismiss();
            }
        });
        mostraOpcao.show();
        registerForContextMenu(listaViewEventos);
    }

    private void exibiInfoEvento(HashMap eventoSelecionado){
        EventoBean eventoBean = new EventoBean((long)eventoSelecionado.get(DatabaseHelper.Evento._ID),
                (String)eventoSelecionado.get(DatabaseHelper.Evento.NOME),
                (String)eventoSelecionado.get(DatabaseHelper.Evento.DATA_INICIAL),
                (String)eventoSelecionado.get(DatabaseHelper.Evento.DATA_FINAL),
                (String)eventoSelecionado.get(DatabaseHelper.Evento.DESCRICAO),
                (String) eventoSelecionado.get(DatabaseHelper.Evento.ENDERECO),
                (int)eventoSelecionado.get(DatabaseHelper.Evento.LOTACAO),
                (float)eventoSelecionado.get(DatabaseHelper.Evento.VALOR_EVENTO));
        Intent maisInfo = new Intent(getActivity().getBaseContext(),InformacoesEventoActivity.class);
        maisInfo.putExtra("evento",eventoBean);
        Log.i("evento",eventoBean.toString());
        startActivity(maisInfo);
    }

    private void atualizarEvento(HashMap eventoSelecionado){
        EventoBean eventoBean = new EventoBean((long)eventoSelecionado.get(DatabaseHelper.Evento._ID),
                (String)eventoSelecionado.get(DatabaseHelper.Evento.NOME),
                (String)eventoSelecionado.get(DatabaseHelper.Evento.DATA_INICIAL),
                (String)eventoSelecionado.get(DatabaseHelper.Evento.DATA_FINAL),
                (String)eventoSelecionado.get(DatabaseHelper.Evento.DESCRICAO),
                (String) eventoSelecionado.get(DatabaseHelper.Evento.ENDERECO),
                (int)eventoSelecionado.get(DatabaseHelper.Evento.LOTACAO),
                (float)eventoSelecionado.get(DatabaseHelper.Evento.VALOR_EVENTO));
        eventoBean.setCidade((String) eventoSelecionado.get(DatabaseHelper.Evento.CIDADE));
        Intent atualizar = new Intent(getActivity().getBaseContext(),AlterarEventoActivity.class);
        atualizar.putExtra("evento",eventoBean);
        Log.i("evento",eventoBean.toString());
        startActivity(atualizar);
    }

    private void excluirEvento(final long id){
        String tag_req = "req_excluir";
        progressDialog.setMessage("Excluindo Evento...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_EXCLUIR_EVENTO, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("Response:", response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        String mensagemErro = jsonObject.getString("error_msg");
                        Toast.makeText(getActivity(), mensagemErro, Toast.LENGTH_LONG).show();
                        recebeEventos();
                    } else {
                        String mensagemErro = jsonObject.getString("error_msg");
                        Toast.makeText(getActivity(), mensagemErro, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(),"Erro ao se conectar", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ao registrar: ",error.toString());
                hideDialog();
                Toast.makeText(getActivity(),"Erro ao se conectar, verifique sua conexão com a internet!",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //Pasando os parametros pelo metodo POST
                Map<String, String> parametros =  new HashMap<>();
                parametros.put("senha",sessionManager.getSenhaLogada());
                parametros.put("email",sessionManager.getEmailLogado());
                parametros.put("id",id+"");
                return parametros;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest,tag_req);

    }

    public void recebeEventos(){
        //String utilizada para cancelar a requisição
        String tag_req = "req_registro";
        progressDialog.setMessage("Listando Eventos ...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_EXIBE_EVENTOS, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("Response:", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getActivity(),"Alguns eventos encontrados", Toast.LENGTH_LONG).show();
                        JSONArray jsonArrayEventos = jsonObject.getJSONArray("eventos");
                        Controller controller = new Controller(getActivity());
                        Log.i("jsonArrayEventos",jsonArrayEventos.toString());
                        controller.salvarEventos(jsonArrayEventos);
                        hideDialog();
                    } else {
                        hideDialog();
                        String mensagemErro = jsonObject.getString("error_msg");
                        Toast.makeText(getActivity(), mensagemErro, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    hideDialog();
                    Toast.makeText(getActivity(),"Erro ao se conectar", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ao registrar: ",error.toString());
                hideDialog();
                Toast.makeText(getActivity(),"Erro ao se conectar, verifique sua conexão com a internet!",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //Pasando os parametros pelo metodo POST
                Map<String, String> parametros =  new HashMap<>();
                parametros.put("email",sessionManager.getEmailLogado());
                parametros.put("senha",sessionManager.getSenhaLogada());
                return parametros;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest,tag_req);
    }

    public void alertExcluir(){
        AlertDialog.Builder mensagem = new AlertDialog.Builder(getActivity());
        mensagem.setTitle("DESEJA REALMENTE EXCLUIR?");
        mensagem.setMessage("Digite sua senha");

        final EditText input = new EditText(getActivity().getBaseContext());
        input.setTextColor(getResources().getColor(R.color.black));
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mensagem.setView(input);
        mensagem.setPositiveButton("SIM",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String senhaLogada = sessionManager.getSenhaLogada();
                        if(senhaLogada.equals(input.getText().toString().trim())){
                            excluirEvento((long)eventoSelecionado.get(DatabaseHelper.Evento._ID));
                        }
                        else{
                            Toast.makeText(getActivity().getBaseContext(),"Senha incorreta",Toast.LENGTH_LONG).show();
                        }
                    }
                });
        mensagem.setNegativeButton("NÃO", null);
        mensagem.show();
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
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
