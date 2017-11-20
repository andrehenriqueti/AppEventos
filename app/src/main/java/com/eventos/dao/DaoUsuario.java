package com.eventos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.eventos.bean.UsuarioBean;
import com.eventos.helper.DatabaseHelper;

/**
 * Created by ANDRE on 05/11/2017.
 */

public class DaoUsuario {

    private static DatabaseHelper helper = null;

    public DaoUsuario(Context context){ helper = new DatabaseHelper(context); }

    public void inserir(UsuarioBean usuarioBean){

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Usuario.EMAIL, usuarioBean.getEmail());
        values.put(DatabaseHelper.Usuario.SENHA, usuarioBean.getSenha());
        values.put(DatabaseHelper.Usuario.NOME, usuarioBean.getNome());
        values.put(DatabaseHelper.Usuario.SEXO, usuarioBean.getSexo()+"");
        values.put(DatabaseHelper.Usuario.TELEFONE, usuarioBean.getTelefone());
        values.put(DatabaseHelper.Usuario.STATUS, usuarioBean.getStatus()+"");
        values.put(DatabaseHelper.Usuario.DATANASCIMENTO, usuarioBean.getDataNascimento());

        db.insert(DatabaseHelper.Usuario.TABELA, null, values);

        db.close();
    }

}
