package com.eventos.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.eventos.app.AppConfig;
import com.eventos.app.AppController;
import com.eventos.bean.UsuarioBean;
import com.eventos.controller.ControllerUsuario;
import com.eventos.helper.DatePickerDataNascimento;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CadastroUsuarioActivity extends AppCompatActivity{

    private static final String TAG = CadastroUsuarioActivity.class.getSimpleName();
    private EditText editTextNomeUsuario;
    private Spinner spinnerSexo;
    private EditText editTextEmailUsuario;
    private Button buttonDataNascimento;
    private String dataNascimentoUsuario;
    private EditText editTextTelefoneUsuario;
    private Button buttonRegistrarConta;
    private Button buttonLinkLogin;
    private DatePickerDataNascimento datePickerDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        spinnerSexo = (Spinner) findViewById(R.id.spinner_sexo);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,R.array.array_sexo_spinner,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerSexo.setAdapter(adapter);
        editTextNomeUsuario = (EditText) findViewById(R.id.nome_usuario_cadastro);
        editTextEmailUsuario = (EditText) findViewById(R.id.email_usuario_cadastro);
        buttonDataNascimento = (Button) findViewById(R.id.data_nascimento_usuario_cadastro);
        editTextTelefoneUsuario = (EditText) findViewById(R.id.telefone_usuario_cadastro);
        buttonRegistrarConta = (Button) findViewById(R.id.btn_cadatrar);
        buttonLinkLogin = (Button) findViewById(R.id.btn_link_login);
        Calendar hoje = Calendar.getInstance();
        buttonLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        buttonDataNascimento.setText(hoje.get(Calendar.DAY_OF_MONTH)+"/"+(hoje.get(Calendar.MONTH)+1)+"/"+hoje.get(Calendar.YEAR));
        buttonDataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDataNascimento();
                datePickerDialog.show(getFragmentManager(),"Data de Nascimento");
            }
        });
        buttonRegistrarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posicaoSpinner = spinnerSexo.getSelectedItemPosition();
                char sexoUsuario = ' ';
                dataNascimentoUsuario = buttonDataNascimento.getText().toString();
                String nomeUsuario = editTextNomeUsuario.getText().toString().trim();
                String emailUsuario = editTextEmailUsuario.getText().toString().trim();
                String telefoneUsuario = editTextTelefoneUsuario.getText().toString().trim();
                switch (posicaoSpinner){
                    case 1: sexoUsuario = 'M';
                        break;
                    case 2: sexoUsuario = 'F';
                        break;
                    case 3: sexoUsuario = 'O';
                        break;
                    case 4: sexoUsuario = 'N';
                        break;
                }
                Toast toastMensagemErro;

                if(!validaNome(nomeUsuario)){
                    toastMensagemErro = Toast.makeText(CadastroUsuarioActivity.this,"NOME INCORRETO",Toast.LENGTH_SHORT);
                    toastMensagemErro.show();
                }
                else if(emailUsuario.isEmpty() || emailUsuario.equals(" ")){
                    toastMensagemErro = Toast.makeText(CadastroUsuarioActivity.this,"CAMPO E-MAIL VAZIO",Toast.LENGTH_SHORT);
                    toastMensagemErro.show();
                }
                else if(telefoneUsuario.isEmpty() || telefoneUsuario.equals(" ")){
                    toastMensagemErro = Toast.makeText(CadastroUsuarioActivity.this,"CAMPO TELEFONE VAZIO",Toast.LENGTH_SHORT);
                    toastMensagemErro.show();
                }
                else if(dataNascimentoUsuario.isEmpty() || dataNascimentoUsuario.equals(" ") || !validaIdadeUsuario(dataNascimentoUsuario)){
                    toastMensagemErro = Toast.makeText(CadastroUsuarioActivity.this,"É NECESSÁRIO TER 18 ANOS PARA USAR O APP",Toast.LENGTH_SHORT);
                    toastMensagemErro.show();
                }
                else if(sexoUsuario == ' '){
                    toastMensagemErro = Toast.makeText(CadastroUsuarioActivity.this,"CAMPO SEXO VAZIO",Toast.LENGTH_SHORT);
                    toastMensagemErro.show();
                }
                else if(nomeUsuario.length() < 3){
                    toastMensagemErro = Toast.makeText(CadastroUsuarioActivity.this,"NOME INVÁLIDO",Toast.LENGTH_SHORT);
                    toastMensagemErro.show();
                }
                else if(emailUsuario.length() < 7){
                    toastMensagemErro = Toast.makeText(CadastroUsuarioActivity.this,"E-MAIL INVÁLIDO",Toast.LENGTH_SHORT);
                    toastMensagemErro.show();
                }
                else{
                    UsuarioBean usuarioCadastro = null;
                    try {
                        usuarioCadastro = new UsuarioBean(nomeUsuario, new String(emailUsuario.getBytes("ISO-8859-1"),"UTF-8"), dataPadraoSQL(dataNascimentoUsuario), sexoUsuario, telefoneUsuario);
                    }
                    catch(UnsupportedEncodingException e){
                        Log.e("EncodingException",e.getMessage());
                    }
                    ControllerUsuario controllerUsuario = new ControllerUsuario(getApplicationContext());
                    registrarUsuario(usuarioCadastro);
                }
            }
        });

    }

    public boolean validaNome(String nome){
        Pattern pattern = Pattern.compile("^[[ ]|\\p{L}*]+$");
        Matcher matcher = pattern.matcher(nome);
        return matcher.matches();
    }

    public boolean validaIdadeUsuario(String dataNascimentoUsuario){
        int posPrimDaBarra = dataNascimentoUsuario.indexOf("/");
        int posSegDaBarra = dataNascimentoUsuario.lastIndexOf("/");
        int dataNascimentoUsuarioEmHoras = 0;
        int ano,mes,dia;
        if(posPrimDaBarra == 1){
            if(posSegDaBarra == 3){
                ano = Integer.parseInt(dataNascimentoUsuario.substring(4,8));
                mes = Character.getNumericValue(dataNascimentoUsuario.charAt(2));
                dia = Character.getNumericValue(dataNascimentoUsuario.charAt(0));
            }
            else{
                ano = Integer.parseInt(dataNascimentoUsuario.substring(5,9));
                mes = Integer.parseInt(dataNascimentoUsuario.substring(2,4));
                dia = Character.getNumericValue(dataNascimentoUsuario.charAt(0));
            }
        }
        else{
            if(posSegDaBarra == 4){
                ano = Integer.parseInt(dataNascimentoUsuario.substring(5,9)) ;
                mes = Character.getNumericValue(dataNascimentoUsuario.charAt(3));
                dia = Integer.parseInt(dataNascimentoUsuario.substring(0,2));
            }
            else{
                ano = Integer.parseInt(dataNascimentoUsuario.substring(6,10));
                mes = Integer.parseInt(dataNascimentoUsuario.substring(3,5));
                dia = Integer.parseInt(dataNascimentoUsuario.substring(0,2));
            }
        }
        dataNascimentoUsuarioEmHoras = ano * 8760 + mes * 730 + dia * 24;
        Calendar hoje = Calendar.getInstance();
        int dataAtualEmHoras = hoje.get(Calendar.YEAR) * 8760 + (hoje.get(Calendar.MONTH)+1) * 730 + hoje.get(Calendar.DAY_OF_MONTH) * 24;
        int diferencaEntreDatasHoras = dataAtualEmHoras - dataNascimentoUsuarioEmHoras;
        int idadeMinimaEmHoras = 18*8760;
        if(diferencaEntreDatasHoras >= idadeMinimaEmHoras){
            return true;
        }
        else {
            return false;
        }
    }

    public String dataPadraoSQL(String dataNascimentoUsuario){
        int posPrimDaBarra = dataNascimentoUsuario.indexOf("/");
        int posSegDaBarra = dataNascimentoUsuario.lastIndexOf("/");
        String data = null;
        if(posPrimDaBarra == 1){
            if(posSegDaBarra == 3){
                data = dataNascimentoUsuario.substring(4,8)+"-"+dataNascimentoUsuario.charAt(2)+"-"+dataNascimentoUsuario.charAt(0);
            }
            else{
                data = dataNascimentoUsuario.substring(5,9) +"-"+ dataNascimentoUsuario.substring(2,4) +"-"+ dataNascimentoUsuario.charAt(0);
            }
        }
        else{
            if(posSegDaBarra == 4){
                data = dataNascimentoUsuario.substring(5,9) +"-"+ dataNascimentoUsuario.charAt(3) +"-"+ dataNascimentoUsuario.substring(0,2);
            }
            else{
                data = dataNascimentoUsuario.substring(6,10) +"-"+ dataNascimentoUsuario.substring(3,5) +"-"+ dataNascimentoUsuario.substring(0,2);
            }
        }
        return data;
    }

    public void registrarUsuario(final UsuarioBean usuarioBean){
        Log.i("object:",usuarioBean.toString());
        //String utilizada para cancelar a requisição
        String tag_req = "req_registro";
        progressDialog.setMessage("Cadastrando...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_CADASTRAR_USUARIO, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("Response:", response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        enviaSenha(usuarioBean);
                    } else {
                        hideDialog();
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
                parametros.put("nome",usuarioBean.getNome());
                parametros.put("email",usuarioBean.getEmail());
                parametros.put("dataNascimento",usuarioBean.getDataNascimento());
                parametros.put("sexo",usuarioBean.getSexo()+"");
                parametros.put("telefone",usuarioBean.getTelefone());

                return parametros;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest,tag_req);
    }

    public void enviaSenha(final UsuarioBean usuarioBean){
        Log.i("object:",usuarioBean.toString());
        //String utilizada para cancelar a requisição
        String tag_req = "req_registro";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_EMAIL, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("Response:", response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        String mensagemSucesso = jsonObject.getString("error_msg");
                        Toast.makeText(getBaseContext(), mensagemSucesso, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CadastroUsuarioActivity.this,LoginActivity.class));
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
