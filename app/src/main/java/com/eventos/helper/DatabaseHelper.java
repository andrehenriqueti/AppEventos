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

    public static class Evento{
        public static final String TABELA = "evento";
        public static final String _ID = "_id";
        public static final String NOME = "nome_evento";
        public static final String DESCRICAO = "descricao_evento";
        public static final String ENDERECO = "endereco_evento";
        public static final String STATUS = "status_evento";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String LOTACAO = "lotacao";
        public static final String DATA_INICIAL = "data_hora_ini";
        public static final String DATA_FINAL = "data_hora_final";
        public static final String VALOR_EVENTO = "valor_evento";
        public static final String CIDADE = "cidade";
        public static final String[] COLUNAS = new String[]{_ID,NOME,DESCRICAO,ENDERECO,STATUS,LATITUDE,LONGITUDE,LOTACAO,DATA_INICIAL,
        DATA_FINAL,VALOR_EVENTO,CIDADE};
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
        String create_evento = "CREATE TABLE "+Evento.TABELA+" (" +
                Evento._ID+" INTEGER, "+Evento.NOME+" VARCHAR(20), "+
                Evento.DESCRICAO+" VARCHAR(140), "+Evento.ENDERECO+" VARCHAR(100),"+
                Evento.STATUS+" CHAR, "+Evento.LATITUDE+" DOUBLE,"+Evento.LONGITUDE+"DOUBLE,"+
                Evento.LOTACAO+" INTEGER,"+Evento.DATA_INICIAL+"VARCHAR(19),"+Evento.DATA_FINAL+" VARCHAR(19),"+
                Evento.VALOR_EVENTO+" DOUBLE,"+Evento.CIDADE+" VARCHAR(50));";
        db.execSQL(create_evento);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Usuario.TABELA+";DROP TABLE IF EXISTS "+Evento.TABELA);
        onCreate(db);
    }
}
