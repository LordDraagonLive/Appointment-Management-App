package com.bacdevelopers.appointmentmanagementapplication.modifycont;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.bacdevelopers.appointmentmanagementapplication.R;
import com.bacdevelopers.appointmentmanagementapplication.models.Appointments;
import com.bacdevelopers.appointmentmanagementapplication.sqlitedb.DB_Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MoveActivity extends AppCompatActivity {

    private String userValue;
    private Intent intent;
    private List<Appointments> appointmentsList;
    private DB_Controller db_controller;
    private String previousDate;
    private String date;

    /**
     * UI Components
     */
    Button moveBtn, cancelBtn;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);


        //getting the user value from the view page
        intent = getIntent();
        userValue = intent.getStringExtra("userInput");
        //getting the date
        previousDate=intent.getStringExtra("Date");

        //DB_Controller database instance
        db_controller = new DB_Controller(this, null, null, 1);

        //Initilizing UI Component
        moveBtn =(Button)findViewById(R.id.moveBtnMvPg);
        cancelBtn=(Button)findViewById(R.id.cancelBtnMvPg);

        calendarView =(CalendarView) findViewById(R.id.calendarViewUpdate);
        /**
         * if user clicks on a date select it and assign it to the date var
         */
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String dateSelected = simpleDateFormat.format(new GregorianCalendar(year, month, dayOfMonth).getTime());
                date = dateSelected;
            }
        });

        /**
         *
         * If the user did not select any date just assign today's date
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateSelected = simpleDateFormat.format(new Date(calendarView.getDate()));

        date=dateSelected;

        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    appointmentsList =db_controller.listAppointments(previousDate);
                    db_controller.moveAppointment(appointmentsList.get(Integer.parseInt(userValue) - 1) , date);

                    Toast.makeText(getBaseContext(), "Moved to "+date+" Successfully!" , Toast.LENGTH_SHORT).show();

                    //close the current activity
                    setResult(RESULT_OK);
                    finish();


                }catch (IndexOutOfBoundsException e){

                    Toast.makeText(getBaseContext(), "Cannot find the appointment." , Toast.LENGTH_SHORT).show();

                }catch (Exception e){

                    Toast.makeText(getBaseContext(), "Invalid input!" , Toast.LENGTH_SHORT).show();
                }

            }

        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
