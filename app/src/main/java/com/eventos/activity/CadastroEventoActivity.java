package com.eventos.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.eventos.R;
import com.eventos.helper.DatePickerDataFinal;
import com.eventos.helper.DatePickerDataInicial;
import com.eventos.helper.SessionManager;
import com.eventos.helper.TimePickerHoraFim;
import com.eventos.helper.TimePickerHoraInicial;

import java.util.Calendar;

/**
 * Created by ANDRE on 20/11/2017.
 */

public class CadastroEventoActivity extends AppCompatActivity {
    private Button btnDataInicio;
    private Button btnDataFim;
    private Button btnHorarioInicio;
    private Button btnHorarioFinal;
    private DatePickerDataInicial datePickerDataInicial;
    private DatePickerDataFinal datePickerDataFinal;
    private TimePickerHoraInicial timePickerHoraInicial;
    private TimePickerHoraFim timePickerHoraFinal;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);
        sessionManager = new SessionManager(getBaseContext());
        btnDataInicio = (Button) findViewById(R.id.btn_data_inicio_evento_cadastro);
        btnDataFim = (Button) findViewById(R.id.btn_data_fim_evento_cadastro);

        Calendar hoje = Calendar.getInstance();
        if(sessionManager.getDataInicio().isEmpty()) {
            btnDataInicio.setText(hoje.get(Calendar.DAY_OF_MONTH) + "/" + (hoje.get(Calendar.MONTH) + 1) + "/" + hoje.get(Calendar.YEAR));
        }
        else{
            btnDataInicio.setText(sessionManager.getDataInicio());
        }
        if(sessionManager.getDataFim().isEmpty()){
            btnDataFim.setText(hoje.get(Calendar.DAY_OF_MONTH)+"/"+(hoje.get(Calendar.MONTH)+1)+"/"+hoje.get(Calendar.YEAR));
        }
        else{
            btnDataFim.setText(sessionManager.getDataFim());
        }
        btnDataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDataInicial = new DatePickerDataInicial();
                datePickerDataInicial.show(getFragmentManager(),"Data Inicial do Evento");
            }
        });
        btnDataFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDataFinal = new DatePickerDataFinal();
                datePickerDataFinal.show(getFragmentManager(),"Data Final do Evento");
            }
        });

        btnHorarioInicio = (Button) findViewById(R.id.btn_horario_inicio_evento_cadastro);
        btnHorarioInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerHoraInicial = new TimePickerHoraInicial();
                timePickerHoraInicial.show(getFragmentManager(),"Hora de Inicio do Evento");
            }
        });

        if(sessionManager.getHorarioInicio().isEmpty()) {
            btnHorarioInicio.setText(hoje.get(Calendar.HOUR_OF_DAY)+":"+hoje.get(Calendar.MINUTE));
        }
        else{
            btnHorarioInicio.setText(sessionManager.getHorarioInicio());
        }

        btnHorarioFinal = (Button) findViewById(R.id.btn_horario_fim_evento_cadastro);
        btnHorarioFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerHoraFinal = new TimePickerHoraFim();
                timePickerHoraFinal.show(getFragmentManager(),"Hora de Encerramento");
            }
        });
        if(sessionManager.getHorarioFim().isEmpty()){
            btnHorarioFinal.setText(hoje.get(Calendar.HOUR_OF_DAY)+":"+hoje.get(Calendar.MINUTE));
        }
        else{
            btnHorarioFinal.setText(sessionManager.getHorarioFim());
        }
    }

    @Override
    public void onBackPressed() {
        sessionManager.setDataFim("");
        sessionManager.setDataInicio("");
        sessionManager.setHorarioInicio("");
        sessionManager.setHorarioFim("");
        super.onBackPressed();
    }
}
