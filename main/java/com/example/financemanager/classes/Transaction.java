package com.example.financemanager.classes;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// Transaction class
public class Transaction {
    // Transaction type
    private final TransactionType transactionType;
    // Account id
    private int accountId;
    // Transaction date
    Date transactionDateTime;
    // Transaction sum
    private final double sum;
    // Transaction reminder
    private final double reminder;
    // Transaction description
    private final String comment;

    // Transaction constructors
    public Transaction(TransactionType transactionType, int accountId, double sum, double reminder, String comment, Date transactionDateTime) {
        this.transactionType = transactionType;
        this.accountId = accountId;
        this.reminder = reminder;
        this.transactionDateTime = transactionDateTime;
        if (sum > Double.MAX_VALUE) {
            this.sum = Double.MAX_VALUE;
        }
        else
        {
            this.sum = sum;
        }
        this.comment = comment;
    }

    // Gets transaction type
    public TransactionType GetTransactionType() {return transactionType;}
    // Gets account id
    public int GetAccountId() {return accountId;}
    // Gets transaction date time
    public Date GetDateTime() {return transactionDateTime;}
    // Gets transaction date and time in string form
    public String GetDateTimeString()
    {
        SimpleDateFormat dateFor = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        return dateFor.format(GetDateTime());
    }
    // Gets reminder
    public Double GetReminder(){return reminder;}
    // Gets reminder in string form
    public String GetReminderString()
    {
        DecimalFormat df= new DecimalFormat("#.##");
        return df.format(GetReminder()).replace(',', '.');
    }
    // Gets value
    public double GetSum() { return sum; }
    // Gets transaction value sum in string form
    public String GetSumString()
    {
        DecimalFormat df= new DecimalFormat("#.##");
        return df.format(GetSum()).replace(',', '.');
    }
    // Gets description
    public String GetComment() {return comment;}

    // Sets account id
    public void SetAccountId(int accountId) {this.accountId = accountId;}
}
