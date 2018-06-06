package com.bacdevelopers.appointmentmanagementapplication.models;

/**
 * Created by Buddhi Adhikari 16280809 on 25/04/2018.
 **/
public class Appointments {

    private String date, time, title, details;

    /**
     * Appointments constructor
     * @param date date of the appointment
     * @param time time of the appointment (initial type is datetime later converted to string)
     * @param title title for the appointment
     * @param details description of the appointment
     */
    public Appointments(String title, String date, String time, String details) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
