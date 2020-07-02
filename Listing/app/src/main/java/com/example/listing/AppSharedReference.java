package com.example.listing;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedReference
{
    SharedPreferences sharedPreferences;

    public AppSharedReference(Context context)
    {
        sharedPreferences = context.getSharedPreferences("myRef" , Context.MODE_PRIVATE);
    }

    public void saveReference(String key,String value)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public String loadReference(String key)
    {
        String value = sharedPreferences.getString(key,"NONE");

        return value;
    }

}
