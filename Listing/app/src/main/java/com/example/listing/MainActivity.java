package com.example.listing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    ArrayList<Employee> employees;
    ListAdapter listAdapter;

    ArrayList<Employee> ALL_PEOPLE;
    DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseManager = new DatabaseManager(this);

        LoadAdmin();

        GridView gridView = (GridView) findViewById(R.id.list_view_id);

        employees = new ArrayList<>();
        ALL_PEOPLE = new ArrayList<>();

        LoadAllEmployeesFromDatabase();

        listAdapter = new ListAdapter(employees);

        gridView.setAdapter(listAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Employee item = employees.get(i);
                OpenEmployeeProfile(item);

                Toast.makeText(getApplicationContext(),item.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadAllEmployeesFromDatabase();
        listAdapter.notifyDataSetChanged();
    }

    private void OpenEmployeeProfile(Employee item)
    {
        Intent EmployeeIntent = new Intent(this,EmployeeProfile.class);

        EmployeeIntent.putExtra("ID",item.ID);

        startActivity(EmployeeIntent);
    }


    @Nullable
    private ArrayList<Employee> ListAllData()
    {
        Cursor cursor = databaseManager.queryEmployee(null,null,
                null,null,null,null);

        ArrayList<Employee> Employees = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {

                int ID = cursor.getInt(cursor.getColumnIndex(databaseManager.ColEmployeeID));
                String Name = cursor.getString(cursor.getColumnIndex(databaseManager.ColEmployeeName));
                String Job = cursor.getString(cursor.getColumnIndex(databaseManager.ColEmployeeJob));

                Employee employee = new Employee(ID, Name, Job);
                Employees.add(employee);
            } while (cursor.moveToNext());
        }
            return Employees;
    }

    private void LoadAllEmployeesFromDatabase()
    {
        employees.clear();
        ALL_PEOPLE.clear();

        ArrayList<Employee> temp = ListAllData();

        for(int i = 0 ; i < temp.size() ; i++)
        {
            Employee item = temp.get(i);
            employees.add(item);
            ALL_PEOPLE.add(item);
        }
    }

    private void LoadAdmin()
    {
        AppSharedReference sharedReference = new AppSharedReference(this);
        String Name = sharedReference.loadReference("Admin_Name");
        String Email = sharedReference.loadReference("Admin_Email");

        if(Name.equals("NONE") || Email.equals("NONE"))
        {
            Intent adminSettingIntent = new Intent(this,AdminSettingActivity.class);
            startActivity(adminSettingIntent);
        }
    }

    public void newStuff(int image , String Name , String Job)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(databaseManager.ColEmployeeName,Name);
        contentValues.put(databaseManager.ColEmployeeJob,Job);

        long ID = databaseManager.insertEmployee(contentValues);
        if(ID >0) {
            Employee stuff = new Employee((int) ID,image, Name, Job);

            ALL_PEOPLE.add(stuff);
            employees.add(stuff);
            listAdapter.notifyDataSetChanged();

        }
        else
        {
            Toast.makeText(getApplicationContext(),"You cannot add this employee" , Toast.LENGTH_SHORT).show();
        }

    }

    public class ListAdapter extends BaseAdapter
    {

        ArrayList<Employee> employees;

        public ListAdapter(ArrayList<Employee> employees) {
            this.employees = employees;
        }

        @Override
        public int getCount() {
            return employees.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            LayoutInflater layoutInflater = getLayoutInflater();
            View listItemView = layoutInflater.inflate(R.layout.list_item,null);

            final Employee item = employees.get(i);

            ImageView IDTextView = listItemView.findViewById(R.id.list_item_ID);
            TextView NameTextView = listItemView.findViewById(R.id.list_item_Name);
            TextView JobTextView = listItemView.findViewById(R.id.list_item_Job);

            IDTextView.setImageResource(item.image);
            NameTextView.setText("Name : "+ item.Name);
            JobTextView.setText("Job : "+ item.Job);

            return listItemView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater  = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu,menu);


        MenuItem myMenuItem = menu.findItem(R.id.JOBS_MAIN_MENU);

        // Inflating the sub_menu menu this way, will add its menu items
        // to the empty SubMenu you created in the xml
        getMenuInflater().inflate(R.menu.jobs_menu, myMenuItem.getSubMenu());


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                employees.clear();

                ArrayList<Employee> tempList = searchEmployees(s);

                for(int i = 0 ; i < tempList.size() ; i++)
                {
                    Employee item = tempList.get(i);
                    employees.add(item);
                }

                listAdapter.notifyDataSetChanged();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                employees.clear();

                ArrayList<Employee> tempList = searchEmployees(s);

                for(int i = 0 ; i < tempList.size() ; i++)
                {
                    Employee item = tempList.get(i);
                    employees.add(item);
                }

                listAdapter.notifyDataSetChanged();

                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                LoadAllEmployeesFromDatabase();
                listAdapter.notifyDataSetChanged();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.submenu_ALL_JOBS:
                updateListView("ALL");
                Toast.makeText(getApplicationContext(),"ALL JOBS",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.submenu_ARTIST:
                updateListView("Artist");
                Toast.makeText(getApplicationContext(),"Artist",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.submenu_DOCTOR:
                updateListView("Doctor");
                Toast.makeText(getApplicationContext(),"doctor",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.submenu_ENGINEER:
                updateListView("Engineer");
                Toast.makeText(getApplicationContext(),"engineer",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.submenu_PROGRAMMER:
                updateListView("Programmer");
                Toast.makeText(getApplicationContext(),"programmer",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.submenu_STUDENT:
                updateListView("Student");
                Toast.makeText(getApplicationContext(),"student",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.submenu_TEACHER:
                updateListView("Teacher");
                Toast.makeText(getApplicationContext(),"teacher",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ADDING_EMPLOYEE_MENU_ITEM:
                AddNewEmployee();
                return true;
            case R.id.ADMIN_SETTING_MENU_ITEM:
                openAdminSettingActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAdminSettingActivity() {
        Intent adminSettingIntent = new Intent(this,AdminSettingActivity.class);
        startActivity(adminSettingIntent);
    }

    private void updateListView(@NotNull String job)
    {
        employees.clear();

        ArrayList<Employee> tempList = new ArrayList<>();

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

    private void AddNewEmployee()
    {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Are you sure you want to add a new one?")
                .setTitle("Configuration")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FragmentManager fragmentManager = getSupportFragmentManager();

                        PopUpStuffInfo popUpStuffInfo = new PopUpStuffInfo();

                        popUpStuffInfo.show(fragmentManager,"New Stuff");
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }


    @NotNull
    private ArrayList<Employee> searchEmployees(String queryName)
    {
        String[] selectionArg = {"%"+queryName+"%"};
        String selection = databaseManager.ColEmployeeName + " like ? ";

        Cursor cursor = databaseManager.queryEmployee(null,selection,
                selectionArg,null,null,databaseManager.ColEmployeeName);

        ArrayList<Employee> tempEmployees = new ArrayList<>();

        if(cursor.moveToFirst()) {
            do {
                int ID = cursor.getInt(cursor.getColumnIndex(databaseManager.ColEmployeeID));
                String Name = cursor.getString(cursor.getColumnIndex(databaseManager.ColEmployeeName));
                String Job = cursor.getString(cursor.getColumnIndex(databaseManager.ColEmployeeJob));

                Employee employee = new Employee(ID, Name, Job);
                tempEmployees.add(employee);
            } while (cursor.moveToNext());
        }
            return tempEmployees;
    }
}