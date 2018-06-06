package com.bacdevelopers.appointmentmanagementapplication.modifycont;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bacdevelopers.appointmentmanagementapplication.R;
import com.bacdevelopers.appointmentmanagementapplication.models.Appointments;
import com.bacdevelopers.appointmentmanagementapplication.sqlitedb.DB_Controller;

import java.util.ArrayList;
import java.util.List;

public class ModifyActivity extends AppCompatActivity {

    private String date;
    private DB_Controller db_controller;
    private String pgType;
    private List<Appointments> appointmentsList;
    private List<Appointments> resultSearchedList;
    private ArrayList<String> arrList;
    private ArrayAdapter arrayAdapter;
    private String userInput;
    private Intent intent;
    /**
     * UI Components
     */
    Button modifyPgBtn;
    EditText modifyPgText;
    TextView modifyPgeTitle, modifyPgDesc;
    ListView modifyPgList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        //getting the selected date
        intent = getIntent();
        date = intent.getStringExtra("Date");

        //getting whether Edit, Move, Delete or Search
        pgType = intent.getStringExtra("Page Type");

        //DB_Controller database instance
        db_controller = new DB_Controller(this, null, null, 1);

        //Initilizing UI Component
        modifyPgBtn =(Button)findViewById(R.id.modifyPgBtn);

        modifyPgText =(EditText)findViewById(R.id.modifyPgTxt);

        modifyPgeTitle =(TextView) findViewById(R.id.modifyPgeTitle);
        modifyPgDesc =(TextView) findViewById(R.id.modifyPgDesc);

        modifyPgList =(ListView) findViewById(R.id.modifyPgList);

    //calling the page type function which decides the page structure
        thePgType();

        /**
         * The Button function of modifyPgBtn
         */
        modifyPgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userInput = modifyPgText.getText().toString();

                if(userInput.equals(null)||userInput.equals("")||userInput.equals("0")){
                    modifyPgText.setError("Invalid Input!");
                    modifyPgText.setText("");
                    return;
                }else{
                    modifyBtnActions();
                }

            }
        });

        /**
         * The List onClick() function
         */
        modifyPgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(pgType.equals("Search")){

                    //display the clicked item


                    String s = modifyPgList.getItemAtPosition(position).toString();

                    Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();

                    for(Appointments appointments :resultSearchedList){
                        if(s.toLowerCase().contains(appointments.getTitle().toLowerCase())){
                            //display alert msg
                            displayClickedItem(appointments.getTitle(),appointments.getDate(),
                                    appointments.getTime(),appointments.getDetails());
                            break;
                        }

                    }

                }
            }
        });
    }

    private void thePgType(){

        if(pgType.equals("Delete")){
            modifyPgeTitle.setText("DELETE APPOINTMENTS");
            modifyPgDesc.setText("You can delete an appointment by entering the number.");
            modifyPgBtn.setText("DELETE");
            modifyPgText.setInputType(InputType.TYPE_CLASS_NUMBER);
            initializeListVw();

        }else if(pgType.equals("Edit")){

            modifyPgeTitle.setText("VIEW/EDIT APPOINTMENTS");
            modifyPgDesc.setText("You can edit an appointment by entering the number.");
            modifyPgBtn.setText("EDIT");
            modifyPgText.setInputType(InputType.TYPE_CLASS_NUMBER);
            initializeListVw();

        }else if(pgType.equals("Move")){

            modifyPgeTitle.setText("MOVE APPOINTMENTS");
            modifyPgDesc.setText("You can move an appointment by entering the number.");
            modifyPgBtn.setText("MOVE");
            modifyPgText.setInputType(InputType.TYPE_CLASS_NUMBER);
            initializeListVw();

        }else if(pgType.equals("Search")){
            modifyPgeTitle.setText("SEARCH APPOINTMENTS");
            modifyPgDesc.setText("Enter a keyword and search all the appointments");
            modifyPgBtn.setText("SEARCH");
            modifyPgText.setInputType(InputType.TYPE_CLASS_TEXT);
            //initializing with a list view
            appointmentsList=db_controller.listAppointments();
        }

    }

    private void initializeListVw(){

        appointmentsList = db_controller.listAppointments(date);
        arrList = new ArrayList<>();

        for(int i=0 ; i<appointmentsList.size() ; i++){

            arrList.add(i+1 + ". " + appointmentsList.get(i).getTime() + " " + appointmentsList.get(i).getTitle());
            //testing listView
//            Toast.makeText(getBaseContext() ,arrList.get(i) , Toast.LENGTH_SHORT ).show();
        }

        arrayAdapter = new ArrayAdapter<String>(this, R.layout.layout_listview, arrList);

        modifyPgList = (ListView) findViewById(R.id.modifyPgList);
        modifyPgList.setAdapter(arrayAdapter);
    }

    private void modifyBtnActions() {

        if(pgType.equals("Search")){

            String userSearch=modifyPgText.getText().toString();
            try {
                if (userSearch.equals("") || userSearch.equals(null)) {

                    modifyPgText.setError("Cannot Leave Blank");

                } else {
                    //initializing the list of appointments
                    resultSearchedList = new ArrayList<>();

                    /**
                     *  Check if the appointmentsList objs contain any of the keywords and
                     *  add them to resultSearchedList
                     */
                    for (Appointments appointment : appointmentsList) {

                        if (appointment.getTitle().toLowerCase().contains(userSearch.toLowerCase())) {

                            resultSearchedList.add(appointment);
                        }

                    }

                    arrList = new ArrayList<>();

                    for(int i=0 ; i<resultSearchedList.size() ; i++){

                        arrList.add(appointmentsList.get(i).getTitle() + " :\n ' " + appointmentsList.get(i).getDetails()+" '");
                        //testing listView
//            Toast.makeText(getBaseContext() ,arrList.get(i) , Toast.LENGTH_SHORT ).show();
                    }

                    arrayAdapter =  new ArrayAdapter<String>(this, R.layout.layout_listview, arrList);

                    modifyPgList=(ListView)findViewById(R.id.modifyPgList);
                    modifyPgList.setAdapter(arrayAdapter);

                    if(resultSearchedList.size() == 0){
                        Toast.makeText(getBaseContext(),"No results was found!", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){

                Toast.makeText(getBaseContext(),"No results found: Unexpected Error", Toast.LENGTH_SHORT).show();

            }

        }else {
            try {
                if ((Integer.parseInt(userInput)) <= appointmentsList.size()) {


                    //if in delete page
                    if (pgType.equals("Delete")) {
                        delDialog();


                    } else if (pgType.equals("Edit")) {

                        intent = new Intent(getBaseContext(), UpdateActivity.class);
                        intent.putExtra("userInput", userInput);
                        intent.putExtra("Date", date);
                        startActivityForResult(intent, 0);

                    } else if (pgType.equals("Move")) {
                        intent = new Intent(getApplicationContext(), MoveActivity.class);
                        intent.putExtra("userInput", userInput);
                        intent.putExtra("Date", date);
                        startActivityForResult(intent, 1);
                        //                    startActivity(intent);
                    }
                } else {
                    Toast.makeText(getBaseContext(), "Cannot find appointment " + userInput +
                            ". Please enter a valid number.", Toast.LENGTH_SHORT).show();
                }

            } catch (IndexOutOfBoundsException ex) {
                modifyPgText.setText("");
                Toast.makeText(getBaseContext(), "Cannot find appointment " + userInput +
                        ". Please enter a valid number.", Toast.LENGTH_SHORT).show();

            } catch (Exception ex) {
                modifyPgText.setText("");
                Toast.makeText(getBaseContext(), "Unexpected Error. Try a valid number"
                        , Toast.LENGTH_SHORT).show();
            } finally {
                modifyPgText.setText("");
            }
        }


    }

    private void delDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog);
        builder.setMessage("Would you like to delete appointment : “ " +
                        appointmentsList.get(Integer.parseInt(userInput) - 1).getTitle() + " ”?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //success msg
                        Toast.makeText(getBaseContext(), "Deletion of " +
                                appointmentsList.get(Integer.parseInt(userInput) - 1).getTitle() +
                                " appointment success.", Toast.LENGTH_LONG).show();

                        //deleting the appointment
                        db_controller.deleteAppointments(appointmentsList.get(Integer.parseInt(userInput)-1).getTitle(),date);
                        dialog.dismiss();

                        //refreshing the ListView
                        initializeListVw();
                    }
                });
        builder.setNegativeButton(
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void displayClickedItem(String title, String date, String time, String details){

        new AlertDialog.Builder(ModifyActivity.this)
                .setTitle("Selected Appointment")
                .setMessage(Html.fromHtml("<b>Title :<br/></b>"+title+
                        "<br/><b>Date :<br/></b>"+date+
                        "<br/><b>Time :<br/></b>"+time+
                        "<br/><b>Details :<br/></b>"+details))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(requestCode==0){
            if(resultCode== Activity.RESULT_OK){
                initializeListVw();
            }else{
                //do nothing
            }
        }else if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){
                initializeListVw();
            }else{
                //do nothing
            }
        }

    }



}
