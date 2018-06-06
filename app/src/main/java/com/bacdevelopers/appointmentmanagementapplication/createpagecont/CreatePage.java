package com.bacdevelopers.appointmentmanagementapplication.createpagecont;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bacdevelopers.appointmentmanagementapplication.R;
import com.bacdevelopers.appointmentmanagementapplication.models.Appointments;
import com.bacdevelopers.appointmentmanagementapplication.models.Synonyms;
import com.bacdevelopers.appointmentmanagementapplication.sqlitedb.DB_Controller;
import com.bacdevelopers.appointmentmanagementapplication.thesaurus.Thesaurus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Buddhi Adhikari 16280809 on 25/04/2018
 */

public class CreatePage extends AppCompatActivity implements View.OnClickListener{

    private String date;

    /**
     * UI Components
     */
    EditText titleETxt, timeETxt, detailsETxt, thesaurusTxt;
    Button saveBtn, thesaurusBtn, thesaurusReplaceBtn;
    Toast toast;

    /**
     * DB Controller
     */
    DB_Controller db_controller;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_page);

        /**
         * getting the date stored in the MainPage
         * using Intents
         */
        Intent intent = getIntent();
        date = intent.getStringExtra("Date");
        //testing the date
//        toast = Toast.makeText(getBaseContext(),date,Toast.LENGTH_LONG);
//        toast.show();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        /**
         * Initializing UI component
         */
        titleETxt = (EditText) findViewById(R.id.appointmentTitleTxt);
        timeETxt = (EditText) findViewById(R.id.appointmentTimeTxt);
        detailsETxt = (EditText) findViewById(R.id.appointmentDetailTxt);
        thesaurusTxt = (EditText) findViewById(R.id.thesaurusTxt);

        saveBtn=(Button)findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(this);

        thesaurusBtn=(Button)findViewById(R.id.thesaurusBtn);
        thesaurusBtn.setOnClickListener(this);

        thesaurusReplaceBtn=(Button)findViewById(R.id.thesaurusReplaceBtn);
        thesaurusReplaceBtn.setOnClickListener(this);

        /**
         * Creating new Database controller instance with db version 1
         */
        db_controller=new DB_Controller(this,null,null,1);

//        Toast.makeText(getBaseContext(),db_controller.listAppointments(date),Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.saveButton :{
                saveBtnAction();
                break;
            }

            case R.id.thesaurusBtn:{
                thesaurusBtnAction(false);
                break;
            }

            case R.id.thesaurusReplaceBtn:{
                thesaurusBtnAction(true);
                break;
            }

        }

    }

    /**
     * returns a dialog message which is build using
     * 'message' parameter
     * @param message a message String which can store message for the dialog
     */
    public void msgDialog(String message, final boolean bool)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.Theme_AppCompat_Light_Dialog);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if(bool){
                            finish();
                        }
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void saveBtnAction(){
        String title, time, details;

        title = titleETxt.getText().toString();
        time = timeETxt.getText().toString();
        details = detailsETxt.getText().toString();

        if(TextUtils.isEmpty(title)){

            titleETxt.setError("Title cannot be empty");
//                    Toast.makeText(getBaseContext(),"Title cannot be empty",Toast.LENGTH_SHORT).show();
            return;

        }else if (TextUtils.isEmpty(time)) {

            timeETxt.setError("Time cannot be empty");
//                    Toast.makeText(getBaseContext(),"Time cannot be empty",Toast.LENGTH_SHORT).show();
            return;

        }else if(TextUtils.isEmpty(details)) {

            detailsETxt.setError("Details cannot be empty");
//                    Toast.makeText(getBaseContext(),"Details cannot be empty",Toast.LENGTH_SHORT).show();
            return;

        }else{

            Appointments appointment = new Appointments(title,date,time,details);
            int db_result = db_controller.createAppointment(appointment);

            if (db_result==0){
                msgDialog("Appointment " + title + " on " + date + " Creation Success!",true);
                detailsETxt.setText("");
                titleETxt.setText("");
                timeETxt.setText("");
                //testing end activity


            }else if(db_result==-1){
                msgDialog("Appointment " + title + " already exists on " + date + " please choose a different Title!",false);
            }
        }

    }

    public void thesaurusBtnAction(final Boolean isReplace){

        int startSelection=detailsETxt.getSelectionStart();
        int endSelection=detailsETxt.getSelectionEnd();
        String word="";

        final String selectedText = detailsETxt.getText().toString().substring(startSelection, endSelection);

        if(TextUtils.isEmpty(selectedText)&& isReplace){
            detailsETxt.setError("You have to select a word!");
            return;
        }else if (TextUtils.isEmpty(thesaurusTxt.getText().toString())&&!isReplace) {
            thesaurusTxt.setError("Synonym cannot be empty!");
            return;
        }

        try {

            // setup the alert builder
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Synonyms List");

            if(isReplace){
                word=selectedText;
            }else{
                word=thesaurusTxt.getText().toString();
            }

            Thesaurus thesaurus = new Thesaurus(word, CreatePage.this);

            List<Synonyms> synonymsList = thesaurus.sendRequest();
            if(synonymsList.isEmpty()){
                return;
            }
            // add a list
            final List<String> someList= new ArrayList<>();

            for(Synonyms synonym: synonymsList){
                List<String> list =Arrays.asList(synonym.getSynonym().split("\\|"));
                for(String strng:list ){
                    someList.add(strng);
                }
            }

            builder.setItems(someList.toArray(new String[someList.size()]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(isReplace){
                        String str =someList.get(which).replaceAll("\\(.*?\\)","");
                        String modifiedStr = detailsETxt.getText().toString().replace(selectedText,str);
//                        Toast.makeText(CreatePage.this,str,Toast.LENGTH_LONG).show();
                        detailsETxt.setText(modifiedStr);
                    }

                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setLayout(600,600);
        }catch (Exception ex){
            Toast.makeText(this,"Unexpected Error occurred on click ",Toast.LENGTH_LONG).show();
        }
    }
}
