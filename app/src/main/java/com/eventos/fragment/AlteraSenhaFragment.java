package com.eventos.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.eventos.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AlteraSenhaFragment extends android.app.Fragment {
    private View view;
    private String senhaAtual, senhaNova, senhaConfirm, emailLogado;
    private Button btn_alterar;
    private SessionManager session;
    private Context context;
    private UsuarioBean usuarioBean;


    public AlteraSenhaFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_altera_senha, container,false);

        btn_alterar = (Button) view.findViewById(R.id.btn_alterarSenha);

        btn_alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session = new SessionManager(context);
                emailLogado = session.getEmailLogado();
                senhaAtual = ((EditText) view.findViewById(R.id.campo_senhaAtual)).getText().toString().trim();
                senhaNova = ((EditText) view.findViewById(R.id.campo_senhaNova)).getText().toString().trim();
                senhaConfirm = ((EditText) view.findViewById(R.id.campo_senhaConfirmar)).getText().toString().trim();

                if(senhaAtual.isEmpty() || senhaNova.isEmpty() || senhaConfirm.isEmpty()){
                    Toast.makeText(context, "Campos vazios", Toast.LENGTH_LONG).show();
                }
                else if (!senhaNova.equals(senhaConfirm)){
                    Toast.makeText(context, "Campos de senha e confirmação não correspondem", Toast.LENGTH_LONG).show();
                }

                else{
                    usuarioBean = new UsuarioBean(emailLogado,senhaAtual);
                    alterarSenha(usuarioBean, senhaNova);
                }

            }
        });

        return view;
    }
    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public void alterarSenha(final UsuarioBean usuarioBean, final String senhaNova){
        Log.i("object:",usuarioBean.toString());
        //String utilizada para cancelar a requisição
        String tag_req = "req_registro";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ALTERAR_SENHA, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("Response:", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String mensagem = jsonObject.getString("error_msg");

                    Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(context,"Erro ao se conectar", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ao registrar: ",error.toString());
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //Pasando os parametros pelo metodo POST
                Map<String, String> parametros =  new HashMap<>();
                Log.i("usuarioBean",usuarioBean.toString());
                parametros.put("email",usuarioBean.getEmail());
                parametros.put("senha",usuarioBean.getSenha());
                parametros.put("novaSenha",senhaNova);
                return parametros;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest,tag_req);
    }
}