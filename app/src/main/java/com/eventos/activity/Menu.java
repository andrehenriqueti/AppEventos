package com.eventos.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.eventos.R;
import com.eventos.fragment.AlteraSenhaFragment;
import com.eventos.fragment.ListaEventosFragment;
import com.eventos.helper.SessionManager;

public class Menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView nome;
    private TextView email;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
        nome.setText("Nome do usuario");
        email.setText("Email do usuário");
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
        session = new SessionManager(getBaseContext());
        session.setEmailLogado("");
        session.setSenhaLogada("");
        session.setLogin(false);
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

    public void selectDrawerItem(MenuItem menuItem){
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.nav_listareventos:
                fragment = new ListaEventosFragment();
                break;
            case R.id.nav_alterarsenha:
                fragment = new AlteraSenhaFragment();
                break;
            case R.id.nav_sair:
                SessionManager sessionManager = new SessionManager(getBaseContext());
                sessionManager.setEmailLogado("");
                sessionManager.setSenhaLogada("");
                finish();
            default:
               break;
        }
        if(fragment != null){
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout,fragment).commit();
            setTitle(menuItem.getTitle());
        }
    }
}
