package com.bacdevelopers.appointmentmanagementapplication.modifycont;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bacdevelopers.appointmentmanagementapplication.R;
import com.bacdevelopers.appointmentmanagementapplication.modifycont.ModifyActivity;
import com.bacdevelopers.appointmentmanagementapplication.sqlitedb.DB_Controller;

public class DeleteMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private String date;
    private Intent intent;
    /**
     * UI Components
     */
    Button delAllBrn, selectDelBtn;
    TextView delAllTxt, selectDelTxt;

    DB_Controller db_controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_menu);

        /**
         * getting the date stored in the MainPage
         * using Intents
         */
        intent = getIntent();
        date = intent.getStringExtra("Date");

        /**
         * Initializing UI Components
         */
        delAllBrn=(Button)findViewById(R.id.delAllBtn);
        delAllBrn.setOnClickListener(this);
        selectDelBtn=(Button)findViewById(R.id.selectDelBtn);
        selectDelBtn.setOnClickListener(this);

        delAllTxt=(TextView)findViewById(R.id.delAllTxt);
        delAllTxt.setText("Delete all appointments for "+date);


        /**
         * Creating new Database controller instance with db version 1
         */
        db_controller=new DB_Controller(this,null,null,1);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.delAllBtn:{

                db_controller.deleteAppointments(date);
                msgDialog("All the appointments on "+date+" was deleted successfully!");
                break;
            }

            case R.id.selectDelBtn:{
                intent = new Intent(getBaseContext(), ModifyActivity.class);
                intent.putExtra("Date",date);
                intent.putExtra("Page Type","Delete");
                startActivity(intent);
                finish();
                /**
                 * open an intent by destroying current activity
                 */
                break;
            }
        }
    }

    /**
     * returns a dialog message which is build using
     * 'message' parameter
     * @param message a message String which can store message for the dialog
     */
    public void msgDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this , R.style.Theme_AppCompat_Light_Dialog);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
