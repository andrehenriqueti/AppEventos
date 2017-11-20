package com.eventos.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ANDRE on 01/11/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "eventos";
    private static final int VERSAO = 1;

    public static class Usuario{
        public static final String TABELA = "usuario";
        public static final String EMAIL = "email_usuario";
        public static final String SENHA = "senha_usuario";
        public static final String NOME = "nome_usuario";
        public static final String SEXO = "sexo_usuario";
        public static final String TELEFONE = "telefone_usuario";
        public static final String STATUS = "status_usuario";
        public static final String DATANASCIMENTO = "dataNascimento_usuario";
        public static final String[] COLUNAS = new String[]{EMAIL,SENHA,NOME,SEXO,TELEFONE,STATUS,DATANASCIMENTO};
    }

    public DatabaseHelper(Context context){
        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE usuario (" +
                "email_usuario VARCHAR(60) PRIMARY KEY, " +
                "senha_usuario VARCHAR(255) NOT NULL, " +
                "nome_usuario VARCHAR(60) , " +
                "sexo_usuario CHARACTER(1), " +
                "telefone_usuario VARCHAR(20), " +
                "status_usuario CHARACTER(1),  " +
                "dataNascimento_usuario DATE) ;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Usuario.TABELA);
        onCreate(db);
    }
}
