package com.eventos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

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
        TextView textViewMes = (TextView) findViewById(R.id.mes_inicio_evento);
        int mes = Integer.parseInt(eventoBean.getDataHoraInicio().substring(5,7));
        String[] mesAbreviado = {"JAN","FEV","MAR","ABR","MAI","JUN","JUL","AGO","SET","OUT","NOV","DEZ"};
        textViewMes.setText(mesAbreviado[mes-1]);
        TextView textViewDia = (TextView) findViewById(R.id.dia_inicio_evento);
        textViewDia.setText(eventoBean.getDataHoraInicio().substring(8,10));
        TextView textViewNomeEvento = (TextView) findViewById(R.id.exibi_nome_evento);
        textViewNomeEvento.setText(eventoBean.getNome());
        TextView textViewHorario = (TextView) findViewById(R.id.data_evento);
        String data = "";
        if(eventoBean.getDataHoraFim().substring(0,10).equals(eventoBean.getDataHoraInicio().substring(0,10))){
            data = eventoBean.getDataHoraInicio().substring(8,10)+"/"+eventoBean.getDataHoraInicio().substring(5,7)+"/"+
                    eventoBean.getDataHoraInicio().substring(0,4)+"\n"+"Das "+eventoBean.getDataHoraInicio().substring(11,16)+" as "+
                    eventoBean.getDataHoraFim().substring(11,16);
        }
        else{
            data = eventoBean.getDataHoraInicio().substring(8,10)+"/"+eventoBean.getDataHoraInicio().substring(5,7)+"/"+
                    eventoBean.getDataHoraInicio().substring(0,4)+"-"+eventoBean.getDataHoraFim().substring(8,10)+"/"+
                    eventoBean.getDataHoraFim().substring(5,7)+"/"+ eventoBean.getDataHoraFim().substring(0,4)+"\n"+"Das "+eventoBean.getDataHoraInicio().substring(11,16)+" as "+
                    eventoBean.getDataHoraFim().substring(11,16);
        }
        textViewHorario.setText(data);
        TextView textViewEndereco = (TextView) findViewById(R.id.endereco_evento);
        textViewEndereco.setText(eventoBean.getEndereco().substring(0,eventoBean.getEndereco().indexOf("-")));
        TextView textViewDescricao = (TextView) findViewById(R.id.descricao_evento);
        String descricao = "Descrição: \n"+eventoBean.getDescricao()+"\n"+
                (eventoBean.getLotacaoMax() == 0?"Não há lotação estabelecida":("Lotação de "+eventoBean.getLotacaoMax())+" pessoas\n")+
                (eventoBean.getValor() == 0.0?"":"Há um preço estabelecido de R$"+eventoBean.getValor());
        textViewDescricao.setText(descricao);
        setupToolbar();
    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar bar = getSupportActionBar();
        if(bar != null){
            //bar.setDisplayHomeAsUpEnabled(true);
            //bar.setShowHideAnimationEnabled(true);
            bar.setTitle("Informações do Evento");
        }
    }

}
