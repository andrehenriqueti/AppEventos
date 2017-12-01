package com.eventos.helper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

import com.eventos.R;

/**
 * Created by ANDRE on 01/11/2017.
 */

public class DatePickerDataNascimentoAlterar extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Button buttonDataNascimento = (Button) getActivity().findViewById(R.id.data_nascimento_usuario_alterar);
        String dataNascimentoUsuario = buttonDataNascimento.getText().toString();
        int posPrimDaBarra = dataNascimentoUsuario.indexOf("/");
        int posSegDaBarra = dataNascimentoUsuario.lastIndexOf("/");
        int ano,mes,dia;
        if(posPrimDaBarra == 1){
            if(posSegDaBarra == 3){
                ano = Integer.parseInt(dataNascimentoUsuario.substring(4,8));
                mes = Character.getNumericValue(dataNascimentoUsuario.charAt(2)) - 1;
                dia = Character.getNumericValue(dataNascimentoUsuario.charAt(0));
            }
            else{
                ano = Integer.parseInt(dataNascimentoUsuario.substring(5,9));
                mes = Integer.parseInt(dataNascimentoUsuario.substring(2,4)) - 1;
                dia = Character.getNumericValue(dataNascimentoUsuario.charAt(0));
            }
        }
        else{
            if(posSegDaBarra == 4){
                ano = Integer.parseInt(dataNascimentoUsuario.substring(5,9)) ;
                mes = Character.getNumericValue(dataNascimentoUsuario.charAt(3)) - 1;
                dia = Integer.parseInt(dataNascimentoUsuario.substring(0,2));
            }
            else{
                ano = Integer.parseInt(dataNascimentoUsuario.substring(6,10));
                mes = Integer.parseInt(dataNascimentoUsuario.substring(3,5)) - 1;
                dia = Integer.parseInt(dataNascimentoUsuario.substring(0,2));
            }
        }
        return new DatePickerDialog(getActivity(),this,ano,mes,dia);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SessionManager sessionManager = new SessionManager(getActivity().getBaseContext());
        Button buttonDataNascimento = (Button) getActivity().findViewById(R.id.data_nascimento_usuario_alterar);
        String dataNoBotao = dayOfMonth+"/"+(month+1)+"/"+year;
        //só executar quando estiver tudo certo
        //sessionManager.setDataNascimento(dataNoBotao);
        buttonDataNascimento.setText(dataNoBotao);
    }
}
