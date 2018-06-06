package com.bacdevelopers.appointmentmanagementapplication.mainpagecont;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.bacdevelopers.appointmentmanagementapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainPageActivityFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private  static  final String TAG = "CalendarActivity";
    private String userSelectedDate;
    private CalendarView mCalendarView;


    public MainPageActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_main_page, container, false);

        mCalendarView= (CalendarView)mainView.findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                userSelectedDate= simpleDateFormat.format(new GregorianCalendar(year, month, dayOfMonth).getTime());
////                userSelectedDate=dateSelect;
////                userSelectedDate = dayOfMonth+"/"+(month+1)+"/"+"/"+year;
////                Log.d(TAG, "onSelectedDayChange: dd/mm/yyyy: "+userSelectedDate);
                onButtonPressed(userSelectedDate);
            }
        });

        /**
         * initializing the dateSelected from the calendar if the user didn't select the date
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        userSelectedDate= simpleDateFormat.format(new Date(mCalendarView.getDate()));

        /**
         * send the today's date when the view is attached to a window.
         */
        mainView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                onButtonPressed(userSelectedDate);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                onButtonPressed(userSelectedDate);
            }
        });


        return mainView;

    }

    public void onButtonPressed(String selectedDate) {
        if (mListener != null) {
            mListener.onFragmentInteraction(selectedDate);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;// This makes sure that the container activity has implemented
            // the callback interface. If not, it throws an exception
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet MyListFragment.OnItemSelectedListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(String selectedDate);

    }
    }
