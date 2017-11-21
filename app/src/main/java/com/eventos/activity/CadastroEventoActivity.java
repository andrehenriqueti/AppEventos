package com.eventos.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eventos.R;
import com.eventos.helper.DatePickerDataFinal;
import com.eventos.helper.DatePickerDataInicial;
import com.eventos.helper.SessionManager;
import com.eventos.helper.TimePickerHoraFim;
import com.eventos.helper.TimePickerHoraInicial;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private Button btnEndereco;
    private Button btnCadastro;
    private int REQUEST_CODE = 200;
    private String enderecoEvento;
    private String cidade;
    private double latitude,longitude;

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

        btnEndereco = (Button) findViewById(R.id.endereco_evento_cadastro);
        btnEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesquisaEndereco();
            }
        });
        if(!sessionManager.getEndereco().isEmpty()){
            btnEndereco.setText(sessionManager.getEndereco());
        }
        btnCadastro = (Button) findViewById(R.id.cadastrar_evento);
        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });
        setupToolbar();
    }

    public void cadastrar(){
        String nomeEvento = ((EditText) findViewById(R.id.nome_evento_cadastro)).getText().toString();
        String dataInicial = btnDataInicio.getText().toString();
        String dataFinal = btnDataFim.getText().toString();
        String descricao = ((EditText) findViewById(R.id.descricao_evento_cadastro)).getText().toString();
        String endereco = btnEndereco.getText().toString();
        String lotacao = ((EditText) findViewById(R.id.lotacao_evento_cadastro)).getText().toString();
        String horaInicial = btnHorarioInicio.getText().toString();
        String horaFinal = btnHorarioFinal.getText().toString();
        String valor = ((EditText) findViewById(R.id.valor_evento_cadastro)).getText().toString();

        if(!validaNomeEvento(nomeEvento)){
            Toast.makeText(getBaseContext(),"NOME DO EVENTO INCORRETO",Toast.LENGTH_SHORT).show();
        }
        else if(!validaDatas(dataInicial,dataFinal) || !validarHorarios(horaInicial,horaFinal)){
            Toast.makeText(getBaseContext(),"HORARIO DO EVENTO INCORRETO",Toast.LENGTH_SHORT).show();
        }
        else if(descricao.isEmpty()) {
            Toast.makeText(getBaseContext(),"DESCRIÇÃO VAZIA",Toast.LENGTH_SHORT).show();
        }
        else if(endereco.equals(getString(R.string.hint_endereco))){
            Toast.makeText(getBaseContext(),"ENDEREÇO NÃO PODE FICAR VAZIO",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getBaseContext(),"CADASTRO PODE SER REALIZADO",Toast.LENGTH_SHORT).show();
        }
    }

    public void pesquisaEndereco(){
        try {
            startActivityForResult(new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this), REQUEST_CODE);
        }
        catch (GooglePlayServicesRepairableException e) {
            Toast.makeText(this,"Erro no apliactivo GooglePlayServices",Toast.LENGTH_LONG);
            Log.e("GooglePlayServiceError",e.getMessage());
        }
        catch(GooglePlayServicesNotAvailableException e){
            Toast.makeText(this,"Instale o aplicativo GooglePlayServices",Toast.LENGTH_LONG);
            Log.e("GooglePlayServiceError",e.getMessage());
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Verificando resposta da API de Endereços
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place endereco = PlaceAutocomplete.getPlace(this, data);
                enderecoEvento = endereco.getAddress().toString();
                btnEndereco.setText(enderecoEvento);
                latitude = endereco.getLatLng().latitude;
                longitude = endereco.getLatLng().longitude;
                cidade = buscaEndereco(latitude,longitude).getSubAdminArea();
                sessionManager.setEndereco(enderecoEvento);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i("Erro", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                Log.i("Operação cancelada", "Usuário cancelou a consulta");
            }
        }
    }

    public boolean validaNomeEvento(String nome){
        Pattern pattern = Pattern.compile("^[[ ]|\\p{L}*]+$");
        Matcher matcher = pattern.matcher(nome);
        return matcher.matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public Address buscaEndereco(double latitude, double longitude){
        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;
        geocoder = new Geocoder(getApplicationContext());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                address = addresses.get(0);
            }
        }
        catch (IOException e){
            Log.e("IOException",e.getMessage());
        }
        return address;
    }

    @Override
    public void onBackPressed() {
        sessionManager.setDataFim("");
        sessionManager.setDataInicio("");
        sessionManager.setHorarioInicio("");
        sessionManager.setHorarioFim("");
        sessionManager.setEndereco("");
        super.onBackPressed();
    }

    public boolean validaDatas(String dataInicio, String dataFim){

        if(!dataInicio.isEmpty() && !dataFim.isEmpty()) {
            int di[] = retornaDatas(dataInicio);
            int df[] = retornaDatas(dataFim);
            int dataInicialEmHoras = di[0] * 8760 + di[1] * 730 + di[2] * 24;
            int dataFinalEmHoras = df[0] * 8760 + df[1] * 730 + df[2] * 24;
            Calendar hoje = Calendar.getInstance();
            int dataAtualEmHoras = hoje.get(Calendar.YEAR) * 8760 + (hoje.get(Calendar.MONTH) + 1) * 730 + hoje.get(Calendar.DAY_OF_MONTH) * 24;

            if (dataInicialEmHoras >= dataAtualEmHoras && dataFinalEmHoras >= dataInicialEmHoras) {
                return true;
            } else {
                return false;
            }
        }
        else {
            return false;
        }

    }

    public boolean validarHorarios(String horaInicial, String horaFinal){
        if(!horaInicial.isEmpty() && !horaFinal.isEmpty()) {
            int hi[], hf[];
            hi = retornaHorarios(horaInicial);
            hf = retornaHorarios(horaFinal);
            int horasInicialEmMinutos = hi[0] * 60 + hi[1];
            int horasFinalEmMinutos = hf[0] * 60 + hf[1];

            if (horasFinalEmMinutos > horasInicialEmMinutos) {
                return true;
            } else if (horasInicialEmMinutos > horasFinalEmMinutos && !btnDataInicio.getText().toString().equals(btnDataFim.getText().toString())) {
                return true;
            } else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public int[] retornaHorarios(String horario){
        int horas,minutos;
        int v[] = {0,0};
        int indexDoisPontos = horario.indexOf(":");
        if(horario.substring(0,indexDoisPontos).length() == 1){
            horas = Character.getNumericValue(horario.charAt(0));
        }
        else {
            horas = Integer.parseInt(horario.substring(0,indexDoisPontos));
        }
        if(horario.substring(indexDoisPontos+1,horario.length()).length() == 1){
            minutos = Character.getNumericValue(indexDoisPontos+1);
        }
        else{
            minutos = Integer.parseInt(horario.substring(indexDoisPontos+1,horario.length()));
        }
        v[0] = horas;
        v[1] = minutos;
        return v;
    }

    public int[] retornaDatas(String data){
        int posPrimDaBarra = data.indexOf("/");
        int posSegDaBarra = data.lastIndexOf("/");
        int ano,mes,dia;
        int v[] = {0,0,0};
        if(posPrimDaBarra == 1){
            if(posSegDaBarra == 3){
                ano = Integer.parseInt(data.substring(4,8));
                mes = Character.getNumericValue(data.charAt(2));
                dia = Character.getNumericValue(data.charAt(0));
            }
            else{
                ano = Integer.parseInt(data.substring(5,9));
                mes = Integer.parseInt(data.substring(2,4));
                dia = Character.getNumericValue(data.charAt(0));
            }
        }
        else{
            if(posSegDaBarra == 4){
                ano = Integer.parseInt(data.substring(5,9)) ;
                mes = Character.getNumericValue(data.charAt(3));
                dia = Integer.parseInt(data.substring(0,2));
            }
            else{
                ano = Integer.parseInt(data.substring(6,10));
                mes = Integer.parseInt(data.substring(3,5));
                dia = Integer.parseInt(data.substring(0,2));
            }
        }
        v[0] = ano;
        v[1] = mes;
        v[2] = dia;
        return v;
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar bar = getSupportActionBar();
        if(bar != null){
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setShowHideAnimationEnabled(true);
            bar.setTitle("Cadastro de Eventos");
        }
    }
}
