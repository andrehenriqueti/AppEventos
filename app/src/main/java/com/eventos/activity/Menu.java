package com.eventos.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eventos.R;
import com.eventos.app.AppConfig;
import com.eventos.app.AppController;
import com.eventos.controller.Controller;
import com.eventos.fragment.AlteraSenhaFragment;
import com.eventos.fragment.AlterarPerfilFragment;
import com.eventos.fragment.ListaEventosFragment;
import com.eventos.fragment.MapaFragment;
import com.eventos.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView nome;
    private TextView email;
    private SessionManager session;
    private ProgressDialog progressDialog;
    private int opcaoSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        progressDialog = new ProgressDialog(this);
        criarMenu();
        criarCabecalho();
        criaLeitorMenu(savedInstanceState);
    }

    //Incia os componentes do menu
    private void criarMenu(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if(navigationView != null){
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    // Inicia o cabeçalho
    private void criarCabecalho(){
        View header = navigationView.getHeaderView(0);
        nome = (TextView) header.findViewById(R.id.nome_usuario);
        email = (TextView) header.findViewById(R.id.email_usuario);
        exibiInfoUsuario();
    }

    private void exibiInfoUsuario(){
        nome.setText("Bem-vindo");
        session = new SessionManager(this);
        email.setText(session.getEmailLogado());
    }

    private void criaLeitorMenu(Bundle bundle){
        if(bundle == null){
            MenuItem item = navigationView.getMenu().getItem(0);
            onNavigationItemSelected(item);
        }
    }

    //Inicia o menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(isNavigationDrawerOpen()){
            closeNavigationDrawer();
        }
        else {
            super.onBackPressed();
        }
    }

    protected boolean isNavigationDrawerOpen(){
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavigationDrawer(){
        if(drawerLayout != null){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawerLayout.closeDrawers();
        selectDrawerItem(item);
        return true;
    }

    @Override
    protected void onResume() {
        if(opcaoSelecionada == R.id.nav_listareventos){
            recebeEventos();
        }
        super.onResume();
    }

    public void selectDrawerItem(MenuItem menuItem){
        Fragment fragment = null;
        ListFragment listFragment = null;
        switch (menuItem.getItemId()){
            case R.id.nav_localizar:
                opcaoSelecionada = R.id.nav_localizar;
                fragment = new MapaFragment();
                break;
            case R.id.nav_listareventos:
                recebeEventos();
                opcaoSelecionada = R.id.nav_listareventos;
                listFragment = new ListaEventosFragment();
                break;
            case R.id.nav_alterarsenha:
                opcaoSelecionada = R.id.nav_alterarsenha;
                fragment = new AlteraSenhaFragment();
                break;
            case R.id.nav_alterarperfil:
                opcaoSelecionada = R.id.nav_alterarperfil;
                fragment = new AlterarPerfilFragment();
                break;
            case R.id.nav_sair:
                SessionManager sessionManager = new SessionManager(getBaseContext());
                sessionManager.setLogin(false);
                sessionManager.setEmailLogado("");
                sessionManager.setSenhaLogada("");
                startActivity(new Intent(this,LoginActivity.class));
                finish();
            default:
               break;
        }
        // Inicia o listar eventos com a exibição dos eventos pendentes
        session.setListaSelecionada(1);
        if(fragment != null){
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout,fragment).commit();
            setTitle(menuItem.getTitle());
        }
        else if(listFragment != null){
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout,listFragment).commit();
            setTitle(menuItem.getTitle());
        }
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
                        Toast.makeText(Menu.this,"Alguns eventos encontrados", Toast.LENGTH_LONG).show();
                        JSONArray jsonArrayEventos = jsonObject.getJSONArray("eventos");
                        Controller controller = new Controller(Menu.this);
                        Log.i("jsonArrayEventos",jsonArrayEventos.toString());
                        controller.salvarEventos(jsonArrayEventos);
                        hideDialog();
                    } else {
                        hideDialog();
                        String mensagemErro = jsonObject.getString("error_msg");
                        Toast.makeText(Menu.this, mensagemErro, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    hideDialog();
                    Toast.makeText(Menu.this,"Erro ao se conectar", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ao registrar: ",error.toString());
                hideDialog();
                Toast.makeText(getBaseContext(),"Erro ao se conectar, verifique sua conexão com a internet!",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //Pasando os parametros pelo metodo POST
                Map<String, String> parametros =  new HashMap<>();
                Log.i("email",session.getEmailLogado());
                parametros.put("email",session.getEmailLogado());
                parametros.put("senha",session.getSenhaLogada());
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
