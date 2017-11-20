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

import java.util.Calendar;

/**
 * Created by ANDRE on 20/11/2017.
 */

public class CadastroEventoActivity extends AppCompatActivity {
    private Button btnDataInicio;
    private Button btnDataFim;
    private DatePickerDataInicial datePickerDataInicial;
    private DatePickerDataFinal datePickerDataFinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);
        btnDataInicio = (Button) findViewById(R.id.btn_data_inicio_evento_cadastro);
        btnDataFim = (Button) findViewById(R.id.btn_data_fim_evento_cadastro);
        Calendar hoje = Calendar.getInstance();
        btnDataInicio.setText(hoje.get(Calendar.DAY_OF_MONTH)+"/"+(hoje.get(Calendar.MONTH)+1)+"/"+hoje.get(Calendar.YEAR));
        btnDataFim.setText(hoje.get(Calendar.DAY_OF_MONTH)+"/"+(hoje.get(Calendar.MONTH)+1)+"/"+hoje.get(Calendar.YEAR));
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
    }
}
