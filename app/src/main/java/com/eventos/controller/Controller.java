package com.eventos.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eventos.activity.CadastroUsuarioActivity;
import com.eventos.activity.LoginActivity;
import com.eventos.app.AppConfig;
import com.eventos.app.AppController;
import com.eventos.bean.EventoBean;
import com.eventos.bean.UsuarioBean;
import com.eventos.dao.DaoEvento;
import com.eventos.dao.DaoUsuario;
import com.eventos.helper.DatabaseHelper;
import com.eventos.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by ANDRE on 04/11/2017.
 */

public class Controller {

    private Context context;
    private SessionManager sessionManager;

    public Controller(Context context) {
        this.context = context;
        sessionManager =new SessionManager(context);
    }

    public void salvarEventos(JSONArray eventos){
        Log.i("eventos",eventos.toString());
        try {
            List<EventoBean> eventoBeanList = new ArrayList<>();
            for (int i = 0; i < eventos.length(); i++) {
                JSONObject jsonObjectEvento = eventos.getJSONObject(i);
                Log.i("jsonObjectEvento",jsonObjectEvento.toString());
                EventoBean eventoBean = new EventoBean(jsonObjectEvento.getInt("id"),
                        jsonObjectEvento.getString("nome"),
                        jsonObjectEvento.getString("data_hora_ini"),
                        jsonObjectEvento.getString("data_hora_fim"),
                        jsonObjectEvento.getString("descricao"),
                        jsonObjectEvento.getString("endereco"),
                        jsonObjectEvento.getInt("lotacao"),
                        jsonObjectEvento.getDouble("longitude"),
                        jsonObjectEvento.getDouble("latitude"),
                        jsonObjectEvento.getString("status").charAt(0),
                        sessionManager.getEmailLogado(),
                        Float.parseFloat(jsonObjectEvento.getString("valor_evento")),
                        jsonObjectEvento.getString("cidade"));
                eventoBeanList.add(eventoBean);
            }
            DaoEvento daoEvento = new DaoEvento(context);
            Log.i("eventoBeanList",eventoBeanList.toString());
            daoEvento.inserirEventos(eventoBeanList);
        }
        catch (Exception e){
            Log.e("conversão",e.getMessage());
        }
    }

    public List<Map<String,Object>> criaListaEventos(JSONArray eventos){
        List<Map<String,Object>> eventoBeanList = new ArrayList<>();
        try {
            for(int i=0;i<eventos.length();i++){
                Map<String, Object> item = new HashMap<>();
                JSONObject jsonObjectEvento = eventos.getJSONObject(i);
                item.put(DatabaseHelper.Evento._ID,jsonObjectEvento.getInt("id"));
                item.put(DatabaseHelper.Evento.NOME,jsonObjectEvento.getString("nome"));
                item.put(DatabaseHelper.Evento.DESCRICAO,jsonObjectEvento.getString("descricao"));
                item.put(DatabaseHelper.Evento.ENDERECO,jsonObjectEvento.getString("endereco"));
                item.put(DatabaseHelper.Evento.STATUS,jsonObjectEvento.getString("status"));
                item.put(DatabaseHelper.Evento.LATITUDE,jsonObjectEvento.getDouble("latitude"));
                item.put(DatabaseHelper.Evento.LONGITUDE,jsonObjectEvento.getDouble("longitude"));
                item.put(DatabaseHelper.Evento.LOTACAO,jsonObjectEvento.getInt("lotacao"));
                item.put(DatabaseHelper.Evento.DATA_INICIAL,jsonObjectEvento.getString("data_hora_ini"));
                item.put(DatabaseHelper.Evento.DATA_FINAL,jsonObjectEvento.getString("data_hora_fim"));
                item.put(DatabaseHelper.Evento.VALOR_EVENTO,jsonObjectEvento.getDouble("valor_evento"));
                eventoBeanList.add(item);
            }
        }
        catch (Exception e){
            Log.e("conversão",e.getMessage());
        }
        return eventoBeanList;
    }
}
