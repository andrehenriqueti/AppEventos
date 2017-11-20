package com.eventos.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eventos.R;
import com.eventos.app.AppConfig;
import com.eventos.app.AppController;
import com.eventos.bean.UsuarioBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ANDRE on 04/11/2017.
 */

public class LoginActivity extends Activity {
    private Button buttonLinkRecuperar;
    private Button buttonLinkCadastro;
    private Button buttonLogin;
    private String stringLogin;
    private String stringPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        buttonLinkCadastro = (Button) findViewById(R.id.btn_link_cadastrar);
        buttonLogin = (Button) findViewById(R.id.btn_login);
        buttonLinkRecuperar = (Button) findViewById(R.id.btn_link_recuperar);

        buttonLinkCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CadastroUsuarioActivity.class));
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringPassword = ((EditText) findViewById(R.id.senha_usuario_login)).getText().toString().trim();
                stringLogin = ((EditText) findViewById(R.id.email_usuario_login)).getText().toString().trim();
                UsuarioBean usuarioBeanLogin = new UsuarioBean(stringLogin, stringPassword);
                validaLogin(usuarioBeanLogin);
            }
        });

        buttonLinkRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RecuperarContaSenhaActivity.class));
            }
        });

    }

    public void validaLogin(final UsuarioBean usuarioBean){
        Log.i("object:",usuarioBean.toString());
        //String utilizada para cancelar a requisição
        String tag_req = "req_registro";
        progressDialog.setMessage("Carregando...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("Response:", response.toString());
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                       startActivity(new Intent(LoginActivity.this,Menu.class));
                    } else {
                        String mensagemErro = jsonObject.getString("error_msg");
                        Toast.makeText(getBaseContext(), mensagemErro, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(),"Erro ao se conectar", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ao registrar: ",error.toString());
                hideDialog();
                Toast.makeText(getBaseContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //Pasando os parametros pelo metodo POST
                Map<String, String> parametros =  new HashMap<>();
                Log.i("usuarioBean",usuarioBean.toString());
                parametros.put("email",usuarioBean.getEmail());
                parametros.put("senha",usuarioBean.getSenha());
                return parametros;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest,tag_req);
    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
