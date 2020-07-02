package com.example.listing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;


public class PopUpStuffInfo extends DialogFragment implements View.OnClickListener
{
    View stuffInfoView;
    EditText stuffNameEditText;
  //  EditText stuffJobEditText;
    Button AddButton;
    ArrayList<String> Jobs;
    Spinner spinnerJobs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        stuffInfoView = inflater.inflate(R.layout.pop_up_stuff_info,container,false);

        stuffNameEditText = stuffInfoView.findViewById(R.id.STUFF_NAME_ID);
     //   stuffJobEditText = stuffInfoView.findViewById(R.id.STUFF_JOB_ID);
        AddButton = stuffInfoView.findViewById(R.id.ADD_BUTTON);

        Jobs = new ArrayList<String>();
        LoadJobs();

        spinnerJobs = stuffInfoView.findViewById(R.id.JobsSpinner);

        ArrayAdapter jobsAdapter = new ArrayAdapter(stuffInfoView.getContext(),
                android.R.layout.simple_dropdown_item_1line,Jobs);

        spinnerJobs.setAdapter(jobsAdapter);


        AddButton.setOnClickListener(this);

        return stuffInfoView;
    }

    private void LoadJobs()
    {
        Jobs.add("Engineer");
        Jobs.add("Doctor");
        Jobs.add("Artist");
        Jobs.add("Programmer");
        Jobs.add("Student");
        Jobs.add("Teacher");
    }

    @Override
    public void onClick(View view)
    {
        this.dismiss();

        String Name = stuffNameEditText.getText().toString();
        //String Job = stuffJobEditText.getText().toString();
        String Job = spinnerJobs.getSelectedItem().toString();

        Job = Job.replaceAll(" " , "");

        if(Name.charAt(Name.length()-1) == ' ')
        {
            Name = Name.substring(0,Name.length()-1);
        }

        int image;
        switch (Job){
            case "Artist":
                image = R.drawable.artist;
                break;
            case "Engineer":
                image = R.drawable.engineer;
                break;
            case "Doctor":
                image = R.drawable.doctor;
                break;
            case "Programmer":
                image = R.drawable.programmer;
                break;
            case "Teacher":
                image = R.drawable.teacher;
                break;
            case "Student":
                image = R.drawable.student;
                break;
            default:
                image = R.drawable.ic_launcher_background;
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        mainActivity.newStuff(image,Name,Job);
    }
}
