package com.eventos.helper;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import java.util.Calendar;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.eventos.R;

/**
 * Created by ANDRE on 20/11/2017.
 */

public class TimePickerHoraFim extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int minutos = c.get(Calendar.MINUTE);
        TimePickerDialog tpd = new TimePickerDialog(getActivity(),this,hora,minutos,DateFormat.is24HourFormat(getActivity()));
        TextView tvTitle = new TextView(getActivity());
        tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        tvTitle.setText("Hora de Encerramento do Evento");
        tpd.setCustomTitle(tvTitle);
        return tpd;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Button btnHorario = (Button) getActivity().findViewById(R.id.btn_horario_fim_evento_cadastro);
        String horaSelecionada = hourOfDay+":"+minute;
        SessionManager sessionManager = new SessionManager(getActivity().getBaseContext());
        sessionManager.setHorarioFim(horaSelecionada);
        btnHorario.setText(horaSelecionada);
    }
}
