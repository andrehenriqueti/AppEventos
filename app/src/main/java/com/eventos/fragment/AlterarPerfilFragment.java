package com.eventos.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
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
import com.eventos.activity.CadastroUsuarioActivity;
import com.eventos.activity.LoginActivity;
import com.eventos.app.AppConfig;
import com.eventos.app.AppController;
import com.eventos.bean.UsuarioBean;
import com.eventos.helper.DatePickerDataNascimento;
import com.eventos.helper.DatePickerDataNascimentoAlterar;
import com.eventos.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AlterarPerfilFragment extends android.app.Fragment {
    private View view;
    private Button btn_desativar, btn_datNascimento, btn_alterar;
    private EditText nome, telefone;
    private Spinner spinnerSexo;
    private UsuarioBean usuarioBean;
    private SessionManager sessionManager;
    private String emailLogado, senhaLogado;
    private ProgressDialog progressDialog;
    private DatePickerDataNascimentoAlterar datePickerDialog;


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

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);


        nome = (EditText) view.findViewById(R.id.nome_usuario_alterar);
        telefone = (EditText) view.findViewById(R.id.telefone_usuario_alterar);
        btn_alterar = (Button) view.findViewById(R.id.btn_alterar);
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
        //Botao para desativar usuário

        btn_datNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDataNascimentoAlterar();
                datePickerDialog.show(getFragmentManager(),"Data de Nascimento");
            }
        });
        btn_desativar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDesativar();
            }
        });

        //Botão para alterar os dados do usuário
        btn_alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int posicaoSpinner = spinnerSexo.getSelectedItemPosition();
                char sexoUsuario = ' ';
                String dataNascimentoUsuario = btn_datNascimento.getText().toString();
                String nomeUsuario = nome.getText().toString().trim();
                String telefoneUsuario = telefone.getText().toString().trim();
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
                    toastMensagemErro = Toast.makeText(getActivity().getBaseContext(),"NOME INCORRETO",Toast.LENGTH_SHORT);
                    toastMensagemErro.show();
                }
                else if(telefoneUsuario.isEmpty() || telefoneUsuario.equals(" ")){
                    toastMensagemErro = Toast.makeText(getActivity().getBaseContext(),"CAMPO TELEFONE VAZIO",Toast.LENGTH_SHORT);
                    toastMensagemErro.show();
                }
                else if(dataNascimentoUsuario.isEmpty() || dataNascimentoUsuario.equals(" ") || !validaIdadeUsuario(dataNascimentoUsuario)){
                    toastMensagemErro = Toast.makeText(getActivity().getBaseContext(),"É NECESSÁRIO TER 18 ANOS PARA USAR O APP",Toast.LENGTH_SHORT);
                    toastMensagemErro.show();
                }
                else if(sexoUsuario == ' '){
                    toastMensagemErro = Toast.makeText(getActivity().getBaseContext(),"CAMPO SEXO VAZIO",Toast.LENGTH_SHORT);
                    toastMensagemErro.show();
                }
                else if(nomeUsuario.length() < 3){
                    toastMensagemErro = Toast.makeText(getActivity().getBaseContext(),"NOME INVÁLIDO",Toast.LENGTH_SHORT);
                    toastMensagemErro.show();
                }
                else{
                    try {
                        usuarioBean = new UsuarioBean(nomeUsuario, emailLogado, senhaLogado, dataPadraoSQL(dataNascimentoUsuario), sexoUsuario, telefoneUsuario);
                    }
                    catch(Exception e){
                        Log.e("Exception",e.getMessage());
                    }
                    alteraUsuario(usuarioBean);
                }
            }
        });
        setupToolbar(view);
        return view;
    }

    public void alteraUsuario(final UsuarioBean usuarioBean){

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
        return false;
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

    public void preencheCampos(final UsuarioBean usuarioBean){
        //String utilizada para cancelar a requisição
        String tag_req = "req_registro";
        progressDialog.setMessage("Carregando...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_EXIBIR_DADOS_USUARIO, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("Response:", response);
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonUsuario = jsonObject.getJSONObject("usuario");
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        nome.setText(jsonUsuario.getString("nome"));
                        telefone.setText(jsonUsuario.getString("telefone"));
                        char sexo = jsonUsuario.getString("sexo").charAt(0);
                        String dataNasc = jsonUsuario.getString("dataNascimento");
                        dataNasc = dataPadraoBR(dataNasc);
                        btn_datNascimento.setText(dataNasc);
                        int posicaoSpinner = 0;
                        switch (sexo){
                            case 'M':
                                posicaoSpinner = 1;
                                break;
                            case 'F':
                                posicaoSpinner = 2;
                                break;
                            case 'O':
                                posicaoSpinner = 3;
                                break;
                            case 'N':
                                posicaoSpinner = 4;
                                break;
                        }
                        spinnerSexo.setSelection(posicaoSpinner);
                    }
                    else {
                        String mensagemErro = jsonObject.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(), mensagemErro, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplication(),"Erro ao se conectar", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ao registrar: ",error.toString());
                hideDialog();
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
        input.setTextColor(getResources().getColor(R.color.black));
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        //input.setTransformationMethod(PasswordTransformationMethod.getInstance());
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
                        sessionManager.setLogin(false);
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
       // AppController.getInstance().getRequestQueue().cancelAll(tag_req);
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
            bar.setTitle("Alterar Dados");
        }
    }

    public String dataPadraoBR(String dataNascimentoUsuario){
        int tamanho = dataNascimentoUsuario.length() - 1;
        int posSegDaBarra = dataNascimentoUsuario.lastIndexOf("-");
        String data;
        if(posSegDaBarra == 7) {
            if (tamanho == 9) {
                data = dataNascimentoUsuario.substring(8) + "/" + dataNascimentoUsuario.substring(5, 7) + "/" + dataNascimentoUsuario.substring(0, 4);
            } else {
                data = dataNascimentoUsuario.substring(8) + "/" + dataNascimentoUsuario.substring(5,7) + "/" + dataNascimentoUsuario.substring(0,4);
            }
        }
        else {
            if (tamanho == 8) {
                data = dataNascimentoUsuario.substring(7) + "/" + dataNascimentoUsuario.charAt(5) + "/" + dataNascimentoUsuario.substring(0, 4);
            } else {
                data = dataNascimentoUsuario.substring(7) + "/" + dataNascimentoUsuario.charAt(5)+ "/" + dataNascimentoUsuario.substring(0,4);
            }
        }
        return data;
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
