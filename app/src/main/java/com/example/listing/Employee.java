package com.example.listing;

public class Employee
{
    public int image;
    public String Name;
    public String Job;
    public int ID;

    @Override
    public String toString() {
        return "Employee{" +
                "ID=" + ID +
                ", Name='" + Name + '\'' +
                ", Job='" + Job + '\'' +
                '}';
    }

    public Employee(int ID ,int image, String name, String job) {
        this.ID = ID;
        this.image = image;
        Name = name;
        Job = job;
    }

    public Employee(int ID ,String name, String job) {
        Name = name;
        Job = job;
        this.ID = ID;

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

    }
}
