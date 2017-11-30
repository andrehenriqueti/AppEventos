package com.eventos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.eventos.R;
import com.eventos.bean.EventoBean;

/**
 * Created by ANDRE on 30/11/2017.
 */

public class InformacoesEventoActivity extends AppCompatActivity {

    private EventoBean eventoBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_evento);
        Intent evento = getIntent();
        eventoBean = (EventoBean) evento.getSerializableExtra("evento");


    }
}
