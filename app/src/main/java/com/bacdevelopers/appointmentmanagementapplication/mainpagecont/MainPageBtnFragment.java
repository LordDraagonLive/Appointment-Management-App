package com.bacdevelopers.appointmentmanagementapplication.mainpagecont;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bacdevelopers.appointmentmanagementapplication.createpagecont.CreatePage;
import com.bacdevelopers.appointmentmanagementapplication.R;
import com.bacdevelopers.appointmentmanagementapplication.modifycont.DeleteMenuActivity;
import com.bacdevelopers.appointmentmanagementapplication.modifycont.ModifyActivity;
import com.bacdevelopers.appointmentmanagementapplication.modifycont.MoveActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link MainPageBtnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageBtnFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "CalendarActivity";

    //Buttons
    private Button createBtn, editBtn, delBtn, moveBtn, searchBtn;
    private String selectedDate;

    /**
     * getter for selectedDate var
     * @return
     */
    public String getSelectedDate() {
        return selectedDate;
    }

    /**
     * setter for selectedDate var
     * @param selectedDate
     */
    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MainPageBtnFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPageBtnFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPageBtnFragment newInstance(String param1, String param2) {
        MainPageBtnFragment fragment = new MainPageBtnFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_main_page_btn, container, false);

        /**
         * Initializing the buttons and setting onClickListeners
         */
        createBtn = (Button)mainView.findViewById(R.id.createAppoint_btn);
        createBtn.setOnClickListener(this);

        delBtn =(Button)mainView.findViewById(R.id.deleteAp_Btn);
        delBtn.setOnClickListener(this);

        editBtn = (Button)mainView.findViewById(R.id.viewEditAp_btn);
        editBtn.setOnClickListener(this);

        moveBtn = (Button)mainView.findViewById(R.id.moveAp_Btn);
        moveBtn.setOnClickListener(this);

        searchBtn = (Button)mainView.findViewById(R.id.search_Btn);
        searchBtn.setOnClickListener(this);

        return mainView;
    }

    /**
     * getting the date from MainPageActivityFragment using
     * @param date
     * as a parameter
     * and setting it to the selectedDate var
     */
    public void getUserDate(String date){
//        Log.d(TAG,"onSelectedDayChange: dd/mm/yyyy: "+date);
//        Toast toast = Toast.makeText(getActivity(),date,Toast.LENGTH_LONG);
//        toast.show();
        setSelectedDate(date);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createAppoint_btn:{
//                Toast toast =Toast.makeText(getActivity(),getSelectedDate(),Toast.LENGTH_SHORT);
//                toast.show();
                Intent intent = new Intent(getContext(), CreatePage.class);
                intent.putExtra("Date",getSelectedDate());
                startActivity(intent);
                break;
            }

            case R.id.deleteAp_Btn:{
                Intent intent = new Intent(getContext(), DeleteMenuActivity.class);
                intent.putExtra("Date",getSelectedDate());
                startActivity(intent);
                break;
            }

            case R.id.viewEditAp_btn:{
                Intent intent = new Intent(getContext(), ModifyActivity.class);
                intent.putExtra("Date",getSelectedDate());
                intent.putExtra("Page Type","Edit");
                startActivity(intent);
                break;

            }

            case R.id.moveAp_Btn:{
                Intent intent = new Intent(getContext(), ModifyActivity.class);
                intent.putExtra("Date",getSelectedDate());
                intent.putExtra("Page Type","Move");
                startActivity(intent);
                break;
            }

            case R.id.search_Btn:{
                Intent intent = new Intent(getContext(), ModifyActivity.class);
                intent.putExtra("Page Type","Search");
                startActivity(intent);
                break;
            }

        }
    }
}
