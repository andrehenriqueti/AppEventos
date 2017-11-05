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
import com.eventos.bean.UsuarioBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by ANDRE on 04/11/2017.
 */

public class ControllerUsuario {

    private Context context;

    public ControllerUsuario(Context context) {
        this.context = context;
    }
}
