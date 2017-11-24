package com.eventos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import com.eventos.bean.EventoBean;
import com.eventos.helper.DatabaseHelper;
import com.eventos.helper.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ANDRE on 22/11/2017.
 */

public class DaoEvento {

    private static DatabaseHelper helper = null;
    private static SQLiteDatabase db = null;
    private List<Map<String,Object>> eventos;
    private SessionManager sessionManager;

    public DaoEvento(Context context){
        helper = new DatabaseHelper(context);
        sessionManager = new SessionManager(context);
    }

    private EventoBean criaEvento(Cursor cursor){
        EventoBean eventoBean = new EventoBean(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Evento._ID)),
        cursor.getString(cursor.getColumnIndex(DatabaseHelper.Evento.NOME)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Evento.DATA_INICIAL)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Evento.DATA_FINAL)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Evento.DESCRICAO)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Evento.ENDERECO)),
                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Evento.LOTACAO)),
                cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Evento.LONGITUDE)),
                cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.Evento.LATITUDE)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Evento.STATUS)).charAt(0),
                sessionManager.getEmailLogado(),
                cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.Evento.STATUS)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.Evento.CIDADE)));
            return eventoBean;
    }

    public long inserirEvento(EventoBean eventoBean, SQLiteDatabase db){
        SQLiteDatabase dbInserir = db;
        long resultado;
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Evento._ID,eventoBean.getId());
        values.put(DatabaseHelper.Evento.NOME,eventoBean.getNome());
        values.put(DatabaseHelper.Evento.DESCRICAO,eventoBean.getDescricao());
        values.put(DatabaseHelper.Evento.ENDERECO,eventoBean.getEndereco());
        values.put(DatabaseHelper.Evento.STATUS,eventoBean.getStatus().toString());
        values.put(DatabaseHelper.Evento.LATITUDE,eventoBean.getLatitude());
        values.put(DatabaseHelper.Evento.LONGITUDE,eventoBean.getLongitude());
        values.put(DatabaseHelper.Evento.LOTACAO,eventoBean.getLotacaoMax());
        values.put(DatabaseHelper.Evento.DATA_INICIAL,eventoBean.getDataHoraInicio());
        values.put(DatabaseHelper.Evento.DATA_FINAL,eventoBean.getDataHoraFim());
        values.put(DatabaseHelper.Evento.VALOR_EVENTO,eventoBean.getValor());
        values.put(DatabaseHelper.Evento.CIDADE,eventoBean.getCidade());
        values.put(DatabaseHelper.Evento.EMAIL,sessionManager.getEmailLogado());
        resultado = dbInserir.insert(DatabaseHelper.Evento.TABELA, null,values);
        return resultado;
    }

    public void inserirEventos(List<EventoBean> eventoBeanList){
        deletaEventos(sessionManager.getEmailLogado());
        db = helper.getWritableDatabase();
        for(int i=0;i<eventoBeanList.size();i++){
            inserirEvento(eventoBeanList.get(i),db);
        }
        db.close();
    }

    public List<Map<String,Object>> listarEventos(Character status){
        db = helper.getReadableDatabase();
        String pesquisa = "SELECT * FROM "+DatabaseHelper.Evento.TABELA+" WHERE "+DatabaseHelper.Evento.STATUS+" = '"+status+"' AND "+
                DatabaseHelper.Evento.EMAIL+" = '"+sessionManager.getEmailLogado()+"'";
        Cursor cursor = db.rawQuery(pesquisa,null);
        //Log.i("sql",pesquisa.toString());
        cursor.moveToFirst();
        eventos = new ArrayList<>();
        for(int i=0;i<cursor.getCount();i++){
            Map<String, Object> item = new HashMap<>();

            EventoBean eventoBean = criaEvento(cursor);
            item.put(DatabaseHelper.Evento._ID,eventoBean.getId());
            item.put(DatabaseHelper.Evento.NOME,eventoBean.getNome());
            item.put(DatabaseHelper.Evento.DESCRICAO,eventoBean.getDescricao());
            item.put(DatabaseHelper.Evento.ENDERECO,eventoBean.getEndereco());
            item.put(DatabaseHelper.Evento.STATUS,eventoBean.getStatus().toString());
            item.put(DatabaseHelper.Evento.LATITUDE,eventoBean.getLatitude());
            item.put(DatabaseHelper.Evento.LONGITUDE,eventoBean.getLongitude());
            item.put(DatabaseHelper.Evento.LOTACAO,eventoBean.getLotacaoMax());
            item.put(DatabaseHelper.Evento.DATA_INICIAL,eventoBean.getDataHoraInicio());
            item.put(DatabaseHelper.Evento.DATA_FINAL,eventoBean.getDataHoraFim());
            item.put(DatabaseHelper.Evento.VALOR_EVENTO,eventoBean.getValor());
            item.put(DatabaseHelper.Evento.CIDADE,eventoBean.getCidade());

            eventos.add(item);
            cursor.moveToNext();
        }
        return eventos;
    }

    public void deletaEventos(String email){
        db = helper.getWritableDatabase();
        String[] args = {email};
        db.delete(DatabaseHelper.Evento.TABELA,DatabaseHelper.Evento.EMAIL+" = ?",args);
    }
}
