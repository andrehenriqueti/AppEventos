package com.eventos.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
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

public class RecuperarContaSenhaActivity extends AppCompatActivity {
    private String email;
    private Button btn_recuperarConta;
    private UsuarioBean usuarioBean;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_conta_senha);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        btn_recuperarConta = (Button) findViewById(R.id.btn_recuperarConta);

        btn_recuperarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = ((EditText) findViewById(R.id.campo_recuperarConta)).getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(getBaseContext(),"Campo vazio",Toast.LENGTH_LONG).show();
                }
                else{
                    usuarioBean = new UsuarioBean(email);
                    recuperarContaSenha(usuarioBean);
                }
            }
        });
    }

    public void recuperarContaSenha(final UsuarioBean usuarioBean){
        Log.i("object:",usuarioBean.toString());
        //String utilizada para cancelar a requisição
        String tag_req = "req_registro";
        progressDialog.setMessage("Carregando...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,AppConfig.URL_RECUPERAR_USUARIO, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("Response:", response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String mensagem = jsonObject.getString("error_msg");
                    if (!error) {
                        finish();
                    }
                    Toast.makeText(getBaseContext(), mensagem, Toast.LENGTH_LONG).show();
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
