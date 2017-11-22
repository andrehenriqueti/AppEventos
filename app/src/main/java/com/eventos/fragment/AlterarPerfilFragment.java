package com.eventos.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eventos.R;
import com.eventos.activity.LoginActivity;
import com.eventos.app.AppConfig;
import com.eventos.app.AppController;
import com.eventos.bean.UsuarioBean;
import com.eventos.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AlterarPerfilFragment extends android.app.Fragment {
    private View view;
    private Button btn_desativar, btn_datNascimento, btn_alterar;
    private EditText nome, telefone;
    private Spinner spinnerSexo;
    private UsuarioBean usuarioBean;
    private SessionManager sessionManager;
    private String emailLogado, senhaLogado;

    public AlterarPerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alterar_perfil, container, false);
        sessionManager = new SessionManager(getActivity().getBaseContext());
        emailLogado = sessionManager.getEmailLogado();
        senhaLogado = sessionManager.getSenhaLogada();
        usuarioBean = new UsuarioBean(emailLogado,senhaLogado);

        nome = (EditText) view.findViewById(R.id.nome_usuario_alterar);
        telefone = (EditText) view.findViewById(R.id.telefone_usuario_alterar);
        btn_alterar = (Button) view.findViewById(R.id.btn_alterarSenha);
        btn_datNascimento = (Button) view.findViewById(R.id.data_nascimento_usuario_alterar);
        btn_desativar = (Button) view.findViewById(R.id.btn_link_desativar);
        spinnerSexo = (Spinner) view.findViewById(R.id.spinner_sexo_alterar);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.array_sexo_spinner,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerSexo.setAdapter(adapter);

        //METODO UTILIZADO PARA PREENCHER OS CAMPOS DO USUÁRIO
        preencheCampos(usuarioBean);

        btn_desativar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDesativar();
            }
        });

        return view;
    }

    //ARRUMAR ESSA PARTE DE CARREGAR OS DADOS NA TELA.
    public void preencheCampos(final UsuarioBean usuarioBean){
        //String utilizada para cancelar a requisição
        String tag_req = "req_registro";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_EXIBIR_DADOS_USUARIO, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("Response:", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonUsuario = jsonObject.getJSONObject("usuario");
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        nome.setText(jsonUsuario.getString("nome"));
                        telefone.setText(jsonUsuario.getString("telefone"));

                        //ARRUMAR ESSA PARTE, FALTA INSERIR ESSES DADOS NA TELA DO USUÁRIO.
                        char sexo = jsonUsuario.getString("sexo").charAt(0);
                        String dataNasc = jsonUsuario.getString("dataNascimento");
                    }
                    else {
                        String mensagemErro = jsonObject.getString("error_msg");
                        Toast.makeText(getActivity().getBaseContext(), mensagemErro, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity().getBaseContext(),"Erro ao se conectar", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ao registrar: ",error.toString());
                Toast.makeText(getActivity().getBaseContext(),error.getMessage(),Toast.LENGTH_LONG).show();
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

    public void alertDesativar(){
        AlertDialog.Builder mensagem = new AlertDialog.Builder(getActivity());
        mensagem.setTitle("Desativar Conta");
        mensagem.setMessage("Digite sua senha");

        final EditText input = new EditText(getActivity().getBaseContext());
        mensagem.setView(input);
        mensagem.setPositiveButton("sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SessionManager sessionManager = new SessionManager(getActivity().getBaseContext());
                        String senhaLogada = sessionManager.getSenhaLogada();
                        String emailLogado = sessionManager.getEmailLogado();

                        if(senhaLogada.equals(input.getText().toString().trim())){
                            usuarioBean = new UsuarioBean(emailLogado,senhaLogada);
                            desativarConta(usuarioBean);
                        }
                        else{
                            Toast.makeText(getActivity().getBaseContext(),"Senha incorreta",Toast.LENGTH_LONG).show();
                        }
                    }
                });
        mensagem.setNegativeButton("não", null);
        mensagem.show();
    }

    public void desativarConta(final UsuarioBean usuarioBean){
        Log.i("object:",usuarioBean.toString());
        //String utilizada para cancelar a requisição
        String tag_req = "req_registro";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_DESATIVAR_USUARIO, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("Response:", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    String mensagem = jsonObject.getString("error_msg");
                    if(!error){
                        SessionManager sessionManager = new SessionManager(getActivity().getBaseContext());
                        sessionManager.setEmailLogado("");
                        sessionManager.setSenhaLogada("");
                        startActivity(new Intent(getActivity().getBaseContext(),LoginActivity.class));
                        getActivity().finish();
                    }
                    Toast.makeText(getActivity().getBaseContext(), mensagem, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(getActivity().getBaseContext(),"Erro ao se conectar", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ao registrar: ",error.toString());
                Toast.makeText(getActivity().getBaseContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //Pasando os parametros pelo metodo POST
                Map<String, String> parametros =  new HashMap<>();
                Log.i("usuarioBean",usuarioBean.toString());
                parametros.put("senha",usuarioBean.getSenha());
                parametros.put("email",usuarioBean.getEmail());
                return parametros;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest,tag_req);
    }
}
