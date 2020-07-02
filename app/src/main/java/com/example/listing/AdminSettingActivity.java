package com.example.listing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class AdminSettingActivity extends AppCompatActivity
{
    EditText EmailEditText;
    EditText NameEditText;
    Admin currentAdmin = null;
    AppSharedReference sharedReference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_setting);

        sharedReference = new AppSharedReference(this);

        EmailEditText = findViewById(R.id.ADMIN_EMAIL_ID);
        NameEditText  = findViewById(R.id.ADMIN_NAME_ID);


        LoadAdmin();
    }

    private void LoadAdmin()
    {
        String Name = sharedReference.loadReference("Admin_Name");
        String Email = sharedReference.loadReference("Admin_Email");

        if(Name.equals("NONE") || Email.equals("NONE"))
        {
            currentAdmin = null;
            //Toast.makeText(getApplicationContext(),"Add Admin info", Toast.LENGTH_LONG).show();
            EmailEditText.setText("");
            NameEditText.setText("");
         /*
            FragmentManager fragmentManager = getSupportFragmentManager();
            AdminConfigDialog adminConfigDialog = new AdminConfigDialog();

            //TODO

            adminConfigDialog.show(fragmentManager,"configAdmin");
            */

        }
        else
        {
            currentAdmin = new Admin(Name,Email);

            EmailEditText.setText(currentAdmin.Email);
            NameEditText.setText(currentAdmin.Name);
        }
    }

    public boolean setAdmin(@NotNull Admin admin)
    {
        currentAdmin = admin;

        sharedReference.saveReference("Admin_Name",admin.Name);
        sharedReference.saveReference("Admin_Email",admin.Email);

        Toast.makeText(getApplicationContext(),"Admin Saved",Toast.LENGTH_LONG).show();

        return true;
    }

    public void onClickUpdateButton(View view)
    {
        String Name = NameEditText.getText().toString();
        String Email = EmailEditText.getText().toString();

        currentAdmin = new Admin(Name,Email);

        setAdmin(currentAdmin);
        finish();
    }
}