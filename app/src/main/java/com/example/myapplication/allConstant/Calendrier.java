package com.example.myapplication.allConstant;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class Calendrier {
    public static Calendar afficheCalendrier(Activity activity, TextInputEditText daty, boolean withTime) {
        final Calendar currentDate = Calendar.getInstance();
        Calendar calendrier = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendrier.set(year, month, dayOfMonth);

                if (withTime) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            calendrier.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendrier.set(Calendar.MINUTE, minute);
                            daty.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", calendrier));
                        }
                    }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                } else {
                    daty.setText(android.text.format.DateFormat.format("yyyy-MM-dd", calendrier));
                }
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.show();
        return calendrier;
    }

}
