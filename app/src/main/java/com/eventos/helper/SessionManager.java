package com.eventos.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Victor on 05/11/2017.
 */
//Classe que será utilizada para verificar se o usuário ja está logado

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    private static final String EMAIL_LOGADO = "email";
    private static final String DATA_FIM = "dataFim";
    private static final String DATA_INICIO = "dataInicio";
    private static final String DATA_NASCIMENTO = "dataNascimento";
    private static final String HORARIO_FIM = "horarioFim";
    private static final String HORARIO_INICIO = "horarioInicio";
    private static final String SENHA_LOGADA = "senha";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        //pode dar erro aqui
        editor.apply();
    }

    public void setSenhaLogada(String senhaLogada) {
        editor.putString(SENHA_LOGADA,senhaLogada);
        editor.commit();
    }

    public String getSenhaLogada() {
        return pref.getString(SENHA_LOGADA,"");
    }

    public void setHorarioInicio(String horarioInicio) {
        editor.putString(HORARIO_INICIO,horarioInicio);
        editor.commit();
    }

    public String getHorarioInicio() {
        return pref.getString(HORARIO_INICIO,"");
    }

    public void setHorarioFim(String horarioFim) {
        editor.putString(HORARIO_FIM,horarioFim);
        editor.commit();
    }

    public String getHorarioFim() {
        return pref.getString(HORARIO_FIM,"");
    }

    public void setDataNascimento(String dataNascimento) {
        editor.putString(DATA_NASCIMENTO,dataNascimento);
        editor.commit();
    }

    public String getDataNascimento() {
        return pref.getString(DATA_NASCIMENTO,"");
    }

    public void setDataInicio(String dataInicio) {
        editor.putString(DATA_INICIO,dataInicio);
        editor.commit();
    }

    public String getDataInicio() {
        return pref.getString(DATA_INICIO,"");
    }

    public void setDataFim(String dataFim) {
        editor.putString(DATA_FIM,dataFim);
        editor.commit();
    }

    public String getDataFim() {
        return pref.getString(DATA_FIM,"");
    }

    public void setEmailLogado(String emailLogado) {
        editor.putString(EMAIL_LOGADO,emailLogado);
        editor.commit();
    }

    public String getEmailLogado() {
        return pref.getString(EMAIL_LOGADO,"");
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}