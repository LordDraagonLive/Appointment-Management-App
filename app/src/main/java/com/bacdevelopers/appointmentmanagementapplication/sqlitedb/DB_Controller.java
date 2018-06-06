package com.bacdevelopers.appointmentmanagementapplication.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bacdevelopers.appointmentmanagementapplication.models.Appointments;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Buddhi Adhikari 16280809 on 25/04/2018
 */
public class DB_Controller extends SQLiteOpenHelper {

    //TABLE NAME
    private static String table_Appointments = "appointments";

    /**
     * Table columns of appointment table
     */
    public static String col_ID = "_id";
    public static String col_Date = "date";
    public static String col_Time= "time";
    public static String col_Title = "title";
    public static String col_Details = "details";


    /**
     * DB_Controller constructor
     * Db info will be instantiated
     * @param context application context
     * @param name db name
     * @param factory cursor factory
     * @param version db version
     */
    public DB_Controller(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "APPDB.db", factory, 1);
    }

    /**
     * Table creation with the columns
     * @param db SQLite Database name
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+table_Appointments+
                "("+col_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                +col_Title+" TEXT ,"
                +col_Date+" TEXT ,"
                +col_Time+" DATETIME ,"
                +col_Details+" TEXT"+");");
    }

    /**
     * Will drop the table if exits and executes the onCreate() method
     * @param db SQLite db name
     * @param oldVersion current state of the db
     * @param newVersion new state of the db that is gonna be modified to
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ table_Appointments);
        onCreate(db);
    }

    /**
     * createAppointment() adds an appointment to the table_Appointments
     * which is in the db
     * @param appointment takes Appointments object
     * @return if execution success then return 0 else return -1
     */
    public int createAppointment(Appointments appointment){

        SQLiteDatabase sqLiteDatabase =getWritableDatabase();

        /**
         * Below code checks if there are any duplicates with the same title on the
         * same date. The cursor will return null or moveToFirst will return false if there are
         * no duplicates and then we can add the values to the db table
         */
        String sqlQuery = "SELECT * FROM "+ table_Appointments+ " WHERE "
                +col_Date+"=\'"+appointment.getDate()+"\'"+" AND "+ col_Title
                + "=\'"+appointment.getTitle()+"\';";

        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery,null);

        if((cursor==null)||!cursor.moveToFirst()){

            ContentValues contentValues = new ContentValues();

            //adding the data to a ContentValues variable
            contentValues.put(col_Title,appointment.getTitle());
            contentValues.put(col_Date,appointment.getDate());
            contentValues.put(col_Time,appointment.getTime());
            contentValues.put(col_Details,appointment.getDetails());

            //passing to the db table
            sqLiteDatabase.insert(table_Appointments,null,contentValues);
            //try switching the cursor and sqLiteDatabase close() methods
            cursor.close();
            sqLiteDatabase.close();
            //try changing return to 1
            return 0;

        }else{

            return -1;
        }
    }

    /**
     * Finds a existing entry from the database and modify
     * it with the new values
     * @param appointment Appointments object
     * @param title title of the appointment
     * @param time time of the appointment
     * @param details description of the appointment
     * @return if execution success then return 0 else return -1
     */
    public int updateAppointment(Appointments appointment , String title , String time, String details){

        SQLiteDatabase sqLiteDatabase =getWritableDatabase();

        /**
         * Below code checks if there are any duplicates with the same title on the
         * same date. The cursor will return null or moveToFirst will return false if there are
         * no duplicates. if is TRUE then return exit code -1 else add to the database
         */
        String sqlQuery = " SELECT *FROM "+ table_Appointments+ " WHERE "
                +col_Title+ "=\'"+ appointment.getTitle()+"\'"+ " AND "
                +col_Date+ "=\'"+appointment.getDate()+"\';";

        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery,null);


        if(cursor==null||!cursor.moveToFirst()){

            return -1;
        }else{
            ContentValues contentValues =new ContentValues();

            contentValues.put(col_Title,title);
            contentValues.put(col_Time,time);
            contentValues.put(col_Details,details);

            /**
             * updating the db table. update() method takes
             * tablename:String, contentValues:Content values, whereClause:String, whereArgs:String
             * update where col_Title==appointment.getTitle() AND col_Date == appointment.getDate();
             */
            sqLiteDatabase.update(table_Appointments,contentValues,col_Title+"='"+ appointment.getTitle()+"'"+ " AND "
                    +col_Date+"='"+appointment.getDate()+"'",null);

            cursor.close();
            sqLiteDatabase.close();
            //return 1 if any error occurs
            return 0;
        }

    }

    /**
     * Alters the date of an appointment thus
     * moving it to a different date
     * @param appointment appointments object
     * @param date date of the appointment
     * @return if execution success then return 0 else return -1
     */
    public int moveAppointment(Appointments appointment , String date ){

        SQLiteDatabase sqLiteDatabase =getWritableDatabase();

        /**
         * Below code checks if there are any duplicates with the same title on the
         * same date. The cursor will return null or moveToFirst will return false if there are
         * no duplicates. if is TRUE then return exit code -1 else add to the database
         */
        String sqlQuery = " SELECT * FROM " + table_Appointments + " WHERE "
                + col_Title + "=\'" + appointment.getTitle() + "\'" + " AND "
                + col_Date + "=\'" + appointment.getDate() + "\';";

        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery,null);

        if (cursor == null || !cursor.moveToFirst()) {

            return -1;

        } else {

            ContentValues contentValues = new ContentValues();

            //stores the values to be updated
            contentValues.put(col_Date , date);

            /**
             * updating the db table. update() method takes
             * tablename:String, contentValues:Content values, whereClause:String, whereArgs:String
             * update where col_Title==appointment.getTitle() AND col_Date == appointment.getDate();
             */
            sqLiteDatabase.update(table_Appointments, contentValues , col_Title + "='"
                    + appointment.getTitle() + "'" + " AND "
                    + col_Date + "='" + appointment.getDate() + "'" , null);

            sqLiteDatabase.close();
            cursor.close();

            //if an error occurs try changing return to 1
            return 0;

        }

    }

    /**
     * Find and delete a specified appointment on a selected date
     *
     * @param date Appointment Date
     * @param title Appointment Title
     */
    public void deleteAppointments(String title , String date){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + table_Appointments + " WHERE "
                + col_Title + "=\'" + title + "\'" + " AND "
                + col_Date + "=\'" + date + "\';");
        sqLiteDatabase.close();
    }

    /**
     * Find and delete all the appointments on the selected date
     *
     * @param date Date the appointments to be deleted
     */
    public void deleteAppointments(String date){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + table_Appointments + " WHERE "
                + col_Date + "=\'" + date + "\';");
        sqLiteDatabase.close();
    }

    /**
     * Prints the whole database as a String
     *
     * @return *database:String
     */
    public String printAll(){

        String dbPrinter = "";
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        // ' 1 ' == checks if all the conditions are met
        String sqlQuery = "SELECT * FROM " + table_Appointments + " WHERE 1 ";

        //passing the database results to a Cursor var
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        //staring from the first row of the table
        cursor.moveToFirst();

        //loop till the last record to check if all the results were added
        while (!cursor.isAfterLast()) {

            //enter condition if the title column of the table isn't null
            if (cursor.getString(cursor.getColumnIndex("title")) != null) {
                dbPrinter += cursor.getString(cursor.getColumnIndex("title"));
                dbPrinter += "~";
                dbPrinter += cursor.getString(cursor.getColumnIndex("date"));
                dbPrinter += "~";
                dbPrinter += cursor.getString(cursor.getColumnIndex("time"));
                dbPrinter += "~";
                dbPrinter += cursor.getString(cursor.getColumnIndex("details"));
                dbPrinter += "\n";
            }
            cursor.moveToNext();
        }
        sqLiteDatabase.close();
        return dbPrinter;
    }

    /**
     * Returns a list of all the appointments in a
     * selected date
     * @param date the date of the appointment
     * @return List of Appointments
     */
    public List<Appointments> listAppointments(String date){

        List<Appointments> list = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        /**
         * select all from the table where the date_col == date and
         * also order results by the time_col in ascending order
         */
        String sqlQuery = "SELECT * FROM " + table_Appointments + " WHERE " + col_Date + "=\'" + date + "\'"
                + " ORDER BY " + col_Time + " ASC";

        //passing the database results to a Cursor var
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        //staring from the first row of the table
        cursor.moveToFirst();

        //loop till the last record to check if all the results were added
        while (!cursor.isAfterLast()) {

            //enter condition if the title column of the table isn't null
            if (cursor.getString(cursor.getColumnIndex("title")) != null) {

                //pass the db table values to a new Appointments obj and add it to a list
                Appointments appointment = new Appointments(
                        cursor.getString(cursor.getColumnIndex("title")) ,
                        cursor.getString(cursor.getColumnIndex("date")) ,
                        cursor.getString(cursor.getColumnIndex("time")) ,
                        cursor.getString(cursor.getColumnIndex("details"))
                );

                list.add(appointment);
            }
            cursor.moveToNext();
        }
        //closing the cursor
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    /**
     * 'Search' and return all the entries from the table_Appointments table
     *
     * @return List of Appointments
     */
    public List<Appointments> listAppointments(){

        List<Appointments> list = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        /**
         * get all the data from the table table_Appointments
         */
        String query = "SELECT * FROM " + table_Appointments +";";

        //passing the database results to a Cursor var
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        //staring from the first row of the table
        cursor.moveToFirst();

        //loop till the last record to check if all the results were added
        while (!cursor.isAfterLast()) {

            //enter condition if the title column of the table isn't null
            if (cursor.getString(cursor.getColumnIndex("title")) != null) {

                //pass the db table values to a new Appointments obj and add it to a list
                Appointments appointment = new Appointments(
                        cursor.getString(cursor.getColumnIndex("title")) ,
                        cursor.getString(cursor.getColumnIndex("date")) ,
                        cursor.getString(cursor.getColumnIndex("time")) ,
                        cursor.getString(cursor.getColumnIndex("details"))
                );

                list.add(appointment);
            }
            cursor.moveToNext();
        }
        //closing cursor var
        cursor.close();
        sqLiteDatabase.close();
        return list;
    }

    /**
     * clearTable() NOT IMPLEMENTED
     */
}
