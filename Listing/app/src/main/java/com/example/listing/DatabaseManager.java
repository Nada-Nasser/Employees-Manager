package com.example.listing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.widget.Toast;


public class DatabaseManager
{
    private SQLiteDatabase sqLiteDatabase;

    private final String DatabaseName = "Company";
    private final String EmployeeTableName = "Employees";
    String ColEmployeeName = "Name";
    String ColEmployeeJob = "Job";
    String ColEmployeeID = "ID";


    int DBVersion = 1;
    private final String CreateEmployeeTableSQLQuery="CREATE TABLE IF NOT EXISTS " + EmployeeTableName + "(\n " +
                "ID Integer Primary key Autoincrement ,\n" +
                ColEmployeeName +" varchar(255),\n" +
                ColEmployeeJob  +" varchar(255)\n" +
                " ); " ;

    private final String DropEmployeeTable = "DROP TABLE IF EXISTS " + EmployeeTableName;


    class DBHelper extends SQLiteOpenHelper
    {
        Context context;

        public DBHelper(Context context)
        {
            super(context, DatabaseName, null, DBVersion);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase)
        {
            sqLiteDatabase.execSQL(CreateEmployeeTableSQLQuery);
            Toast.makeText(context,"Table is created " , Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
        {
            sqLiteDatabase.execSQL(DropEmployeeTable);
            onCreate(sqLiteDatabase);
        }
    }

    public DatabaseManager(Context context)
    {
        DBHelper dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public long insertEmployee(ContentValues contentValues)
    {
        return sqLiteDatabase.insert(EmployeeTableName,null,contentValues);
    }

    public Cursor queryEmployee(String[] projection , String Selection , String[] SelectionArguments ,
                                String GroupBy,String having,String sortOrder)
    {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(EmployeeTableName);

        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase,projection,Selection,SelectionArguments
                ,GroupBy,having,sortOrder);

        return cursor;
    }

    public int DeleteEmployee(String Selection , String[] SelectionArguments)
    {
        int count = sqLiteDatabase.delete(EmployeeTableName,Selection,SelectionArguments);

        return count;
    }

    public int UpdateEmployee(ContentValues values , String Selection , String[] SelectionArg)
    {
        int count = sqLiteDatabase.update(EmployeeTableName,values,Selection,SelectionArg);
        return count;
    }

}
