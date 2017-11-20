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

    private static final String EMAIL_LOGADO = "";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        //pode dar erro aqui
        editor.apply();
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
