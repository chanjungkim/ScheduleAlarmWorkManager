package com.rogergcc.schedulealarmworkmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.WorkManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


     Calendar calendar  = Calendar.getInstance();
     Calendar calendarActual = Calendar.getInstance();
     private int minutos,hora,dia,mes,anio;
    private String dateFormated;
    private TextView tvNotificactionDateTime;
    Button clear_notification,save_notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final SimpleDateFormat sdfToShow = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");

        final SimpleDateFormat sdfDateOnly = new SimpleDateFormat("dd MMMM yyyy");

        this.tvNotificactionDateTime = findViewById(R.id.notifications_date_time);
        this.clear_notification = findViewById(R.id.clear_notification);
        this.save_notification = findViewById(R.id.save_notification);

        findViewById(R.id.change_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anio = calendarActual.get(Calendar.YEAR);
                mes = calendarActual.get(Calendar.MONTH);
                dia = calendarActual.get(Calendar.DAY_OF_MONTH);

                hora = calendarActual.get(Calendar.HOUR_OF_DAY);
                minutos = calendarActual.get(Calendar.MINUTE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);



                        dateFormated = sdfDateOnly.format(calendar.getTime());


                        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker timePicker, int h, int m) {



                                calendar.set(Calendar.HOUR_OF_DAY, h);
                                calendar.set(Calendar.MINUTE, m);
                                calendar.set(Calendar.SECOND, 0);

//                                stringDateSelected = sdfDateOnly.format(calendarActual.getTime());

                                dateFormated = dateFormated+" "+ String.format("%02d:%02d",h,m);

                                tvNotificactionDateTime.setText(dateFormated);

                            }
                        }, hora, minutos, true);
                        timePickerDialog.setTitle(MainActivity.this.getString(R.string.select_time));
                        timePickerDialog.show();

                    }
                }, anio,mes,dia);

                datePickerDialog.getDatePicker().setMinDate(calendarActual.getTimeInMillis());
                datePickerDialog.show();

            }

        });

        save_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = generateKey();
                Long alertTime = calendar.getTimeInMillis() - System.currentTimeMillis();
                int random = (int) (Math.random()*50+1);
                Data data = saveData("Nueva Alam","alarma Fijada",random);

                 WorkManagerNotification.saveNofification(alertTime,data,tag);


                Toast.makeText(MainActivity.this, "Alarma Guarda\n "+dateFormated, Toast.LENGTH_SHORT).show();

            }
        });clear_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotification("tag1");
            }
        });
    }

    private void deleteNotification(String tag){
        WorkManager.getInstance(this).cancelAllWorkByTag(tag);
        Toast.makeText(MainActivity.this, "Alarm Clear", Toast.LENGTH_SHORT).show();

    }

    private String generateKey(){
        return UUID.randomUUID().toString();
    }

    private Data saveData(String title, String message, int id_notification){
        return new Data.Builder()
                .putString("title",title)
                .putString("message",message)
                .putInt("idNotification",id_notification).build();

    }
}
