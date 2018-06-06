package com.bacdevelopers.appointmentmanagementapplication.modifycont;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bacdevelopers.appointmentmanagementapplication.R;
import com.bacdevelopers.appointmentmanagementapplication.models.Appointments;
import com.bacdevelopers.appointmentmanagementapplication.sqlitedb.DB_Controller;

import java.util.List;

public class UpdateActivity extends AppCompatActivity {

    private String userValue;
    private Intent intent;
    private List<Appointments> appointmentsList;
    private DB_Controller db_controller;
    private String date;
    /**
     * UI Components
     */
    Button updateBtn, cancelBtn;
    EditText titleTxt, timeTxt, detailsTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        //getting the user value from the view page
        intent = getIntent();
        userValue = intent.getStringExtra("userInput");
        //getting the date
        date=intent.getStringExtra("Date");

        //DB_Controller database instance
        db_controller = new DB_Controller(this, null, null, 1);

        //Initilizing UI Component
        updateBtn =(Button)findViewById(R.id.updateBtn);
        cancelBtn=(Button)findViewById(R.id.cancelBtn);

        titleTxt =(EditText)findViewById(R.id.appointmentTitleTxt);
        timeTxt =(EditText)findViewById(R.id.appointmentTimeTxt);
        detailsTxt =(EditText)findViewById(R.id.appointmentDetailTxt);

        /**
         * The Button function of modifyPgBtn
         */
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAppointment();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateAppointment(){
        String title, time, details;

        title = titleTxt.getText().toString();
        time = timeTxt.getText().toString();
        details = detailsTxt.getText().toString();

        if(TextUtils.isEmpty(title)){

            timeTxt.setError("Title cannot be empty");
//                    Toast.makeText(getBaseContext(),"Title cannot be empty",Toast.LENGTH_SHORT).show();
            return;

        }else if (TextUtils.isEmpty(time)) {

            timeTxt.setError("Time cannot be empty");
//                    Toast.makeText(getBaseContext(),"Time cannot be empty",Toast.LENGTH_SHORT).show();
            return;

        }else if(TextUtils.isEmpty(details)) {

            detailsTxt.setError("Details cannot be empty");
//                    Toast.makeText(getBaseContext(),"Details cannot be empty",Toast.LENGTH_SHORT).show();
            return;

        }else{
            //getting a list of the table
            appointmentsList =db_controller.listAppointments(date);

            try {
                //updating the database
                int result = db_controller.updateAppointment(appointmentsList.get(Integer.parseInt(userValue) - 1),
                        title, time, details);

                if (result == 0) {

                    Toast.makeText(getBaseContext(), "Appointment update success", Toast.LENGTH_LONG).show();

                } else if (result == -1) {

                    Toast.makeText(getBaseContext(), "Appointment " + userValue +
                            "Not Found. Please Try Again.", Toast.LENGTH_SHORT).show();
                }

                //close the current activity

                setResult(RESULT_OK);
                finish();

            }catch (IndexOutOfBoundsException e){

                Toast.makeText(getBaseContext(), "Appointment doesn't exists." , Toast.LENGTH_SHORT).show();

            }catch (Exception e){

                Toast.makeText(getBaseContext(), "Unexpected Error!! " , Toast.LENGTH_SHORT).show();
            }
        }
    }
}
