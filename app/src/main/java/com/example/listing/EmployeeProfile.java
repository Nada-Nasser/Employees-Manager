package com.example.listing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EmployeeProfile extends AppCompatActivity
{

    Spinner spinnerJobs;
    ArrayList<String> Jobs;
    EditText employeeNameTextView;
    TextView employeeIDTextView;
    ImageView employeeImageView;

    DatabaseManager databaseManager;
    Employee currentEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);

        databaseManager = new DatabaseManager(this);

        Bundle bundle = getIntent().getExtras();
        int ID  = bundle.getInt("ID");

        currentEmployee = getEmployee(ID);
        Toast.makeText(getApplicationContext(), currentEmployee.toString(),Toast.LENGTH_LONG).show();

        Jobs = new ArrayList<String>();
        LoadJobs();

        spinnerJobs = findViewById(R.id.JobsSpinner);
        ArrayAdapter jobsAdapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,Jobs);
        spinnerJobs.setAdapter(jobsAdapter);
        spinnerJobs.setSelection(Jobs.indexOf(currentEmployee.Job));

        employeeIDTextView = findViewById(R.id.EMPLOYEE_ID);
        employeeNameTextView = findViewById(R.id.EMPLOYEE_NAME_ID);
        employeeImageView = findViewById(R.id.EMPLOYEE_IMAGE);

        employeeIDTextView.setText(String.valueOf(ID));
        employeeNameTextView.setText(currentEmployee.Name);
        employeeImageView.setImageResource(currentEmployee.image);
    }

    private Employee getEmployee(int id)
    {
        Employee employee = null;

        Cursor cursor = databaseManager.queryEmployee(null,"ID = ?" , new String[]{String.valueOf(id)},
                null,null,null);

        if(cursor.moveToFirst())
        {
            int ID = cursor.getInt(cursor.getColumnIndex(databaseManager.ColEmployeeID));
            String Name = cursor.getString(cursor.getColumnIndex(databaseManager.ColEmployeeName));
            String Job = cursor.getString(cursor.getColumnIndex(databaseManager.ColEmployeeJob));

            employee = new Employee(ID,Name,Job);
        }

        return employee;
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

    public void onClickBackButton(View view)
    {
        backToMain();
    }

    public void onClickUpdateButton(View view)
    {
        String Name = employeeNameTextView.getText().toString();
        String Job = spinnerJobs.getSelectedItem().toString();

        Employee updated = new Employee(currentEmployee.ID, Name,Job);

        updateEmployee(currentEmployee,updated);
        currentEmployee.Name = updated.Name;
        currentEmployee.Job = updated.Job;
        currentEmployee.image = updated.image;

        employeeImageView.setImageResource(updated.image);
    }

    public void onClickDeleteButton(View view)
    {
        DeleteEmployee(currentEmployee);
    }

    public void DeleteEmployee(@NotNull Employee item)
    {
        String selection = databaseManager.ColEmployeeName + " LIKE ? AND " + databaseManager.ColEmployeeJob + " LIKE ? ";
        String[] selectionArg = {item.Name,item.Job};

        int c = databaseManager.DeleteEmployee(selection,selectionArg);
        if(c>0)
        {
            Toast.makeText(getApplicationContext(),"Employee Deleted successfully" , Toast.LENGTH_LONG).show();

            backToMain();
        }
    }

    public void updateEmployee(@NotNull Employee item , Employee updatedEmployee)
    {
        String selection = databaseManager.ColEmployeeName + " LIKE ? AND " + databaseManager.ColEmployeeJob + " LIKE ? ";
        String[] selectionArg = {item.Name,item.Job};

        ContentValues contentValues = new ContentValues();

        contentValues.put(databaseManager.ColEmployeeName,updatedEmployee.Name);
        contentValues.put(databaseManager.ColEmployeeJob,updatedEmployee.Job);

        int c = databaseManager.UpdateEmployee(contentValues,selection,selectionArg);

        if(c>0)
        {
            Toast.makeText(getApplicationContext(),"Employee Updated successfully" , Toast.LENGTH_LONG).show();
        }
    }

    public void backToMain()
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}


/*
        Jobs = new ArrayList<String>();

        LoadJobs();

        spinnerJobs = findViewById(R.id.JobsSpinner);

        ArrayAdapter jobsAdapter = new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,Jobs);

        spinnerJobs.setAdapter(jobsAdapter);


       spinnerJobs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l)
            {
                employees.clear();

                ArrayList<Employee> tempList = new ArrayList<>();
                String job = Jobs.get(pos);

                if(job.equals("ALL")){
                    LoadAllEmployeesFromDatabase();
                }
                else{
                    for(int i = 0; i < ALL_PEOPLE.size() ; i++)
                    {
                        Employee item = ALL_PEOPLE.get(i);
                        if(item.Job.equals(job))
                        {
                            tempList.add(item);
                        }
                    }

                    for(int i = 0 ; i < tempList.size() ; i++)
                    {
                        Employee item = tempList.get(i);
                        employees.add(item);
                    }
                }

                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

 */