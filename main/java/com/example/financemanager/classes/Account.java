package com.example.financemanager.classes;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Formatter;

// Account class
public class Account {
    // Account id
    private int id;
    // Must be unique
    private String name;
    // Description of account
    private String description;
    // Stores money sum
    private double value;

    // Gets id
    public int GetId() {return id;}
    // Gets name
    public String GetName()
    {
        return name;
    }
    // Gets description
    public String GetDescription() {return description;}
    // Gets value
    public double GetValue()
    {
        return value;
    }
    // Gets account value sum in string form
    public String GetValueString()
    {
        DecimalFormat df= new DecimalFormat("#.##");
        return df.format(GetValue()).replace(',', '.');
    }

    // Sets id
    public void SetId(int id)
    {
        this.id = id;
    }
    // Sets name
    public void SetName(String name)
    {
        this.name = name;
    }
    // Sets description
    public void SetDescription(String description)
    {
        this.description = description;
    }
    // Sets value
    public void SetValue(double value)
    {
        if (value > Double.MAX_VALUE)
        {
            return;
        }
        if (value > 0)
        {
            this.value = value;
        }
        else
        {
            this.value = 0;
        }
    }

    // Account constructors
    public Account()
    {
        name = description = "";
        id =0;
        value = 0;
    }
    public Account(String name, String description)
    {
        SetId(0);
        SetName(name);
        SetDescription(description);
        SetValue(0);
    }
    public Account(String name, String description, double value)
    {
        SetName(name);
        SetDescription(description);
        SetValue(value);
    }
    public Account(int id, String name, String description, double value)
    {
        SetId(id);
        SetName(name);
        SetDescription(description);
        SetValue(value);
    }

    // Add some sum to our account value
    public void AddToValue(double sum)
    {
        SetValue(this.value + sum);
    }
}
