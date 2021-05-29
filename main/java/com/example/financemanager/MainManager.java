package com.example.financemanager;

import android.content.Context;
import android.widget.Toast;

import com.example.financemanager.classes.Account;
import com.example.financemanager.classes.Transaction;
import com.example.financemanager.classes.TransactionType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

// Main manager is our access for all important functions
public class MainManager
{
    // Singleton patter with manager class
    private static Manager instance;
    // Return manager static class
    public static Manager GetInstance()
    {
        if (instance == null)
        {
            instance = new Manager();
        }
        return instance;
    }

    // Shows toast message
    public static void ShowToast(String message, Context context)
    {
        Toast errorMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
        errorMessage.show();
    }

    // Manager holds control of data in our program
    // - accounts
    // - transaction history
    public static class Manager {
        // Is data already loaded
        private boolean isAccountsDataLoaded;
        // Save file destination for accounts
        public static final String NAME_BASIC_FILE = "basic.bin";
        // Save file destination for accounts
        public static final String NAME_ACCOUNTS_FILE = "accounts.bin";
        // Save file destination for transactions
        public static final String NAME_TRANSACTIONS_FILE = "transactions.bin";
        // Accounts
        private final ArrayList<Integer> freeAccountIdsArrayList;
        private double accountValueSum;
        private final ArrayList<Account> accountArrayList;
        // Transactions
        private final ArrayList<Transaction> transactionArrayList;

        // Constructor
        Manager() {
            freeAccountIdsArrayList = new ArrayList<>();
            accountArrayList = new ArrayList<>();
            transactionArrayList = new ArrayList<>();
            accountValueSum = 0;
            isAccountsDataLoaded = false;
        }

        // Getter for accounts free id array list
        public ArrayList<Integer> GetFreeAccountsIdsArrayList() {return freeAccountIdsArrayList;}
        // Getter for accounts list
        public ArrayList<Account> GetAccounts() {
            return accountArrayList;
        }
        // Getter for transaction list
        public ArrayList<Transaction> GetTransactions() {return transactionArrayList;}
        // Getter for account value sum
        public double GetAccountsValueSum() {
            accountValueSum = 0;
            // Calculating sum of all accounts
            for (int i = 0; i < accountArrayList.size(); i++) {
                accountValueSum += accountArrayList.get(i).GetValue();
            }
            return accountValueSum;
        }
        // Getter for account value sum - string
        public String GetAccountsValueSumString()
        {
            GetAccountsValueSum();
            DecimalFormat df= new DecimalFormat("#.##");
            return df.format(accountValueSum).replace(',', '.');
        }
        // Checks if given name is unique
        public boolean IsAccountNameUnique(String name, int indexOfRecentAccount)
        {
            if (indexOfRecentAccount > -1) {
                for (int i = 0; i < indexOfRecentAccount; i++) {
                    // If the name is already occupied
                    if (name.equalsIgnoreCase(accountArrayList.get(i).GetName())) {
                        return false;
                    }
                }
                for (int i = indexOfRecentAccount + 1; i < accountArrayList.size(); i++) {
                    // If the name is already occupied
                    if (name.equalsIgnoreCase(accountArrayList.get(i).GetName())) {
                        return false;
                    }
                }
            }
            else
            {
                for (int i = 0; i < accountArrayList.size(); i++) {
                    // If the name is already occupied
                    if (name.equalsIgnoreCase(accountArrayList.get(i).GetName())) {
                        return false;
                    }
                }
            }
            // else
            return true;
        }

        // Gets account id by accounts name
        public int GetAccountIdByAccountName(String name)
        {
            for (int i = 0; i < accountArrayList.size(); i++)
            {
                if (accountArrayList.get(i).GetName().equals(name))
                {
                    return accountArrayList.get(i).GetId();
                }
            }
            return -1;
        }

        // Method for getting new account free id
        public int GetFreeAccountId()
        {
            // If there is free id return it
            if (freeAccountIdsArrayList.size() > 0)
            {
                // Get id
                int id = freeAccountIdsArrayList.get(0);
                // Delete id from free ones
                freeAccountIdsArrayList.remove(0);
                return id;
            }
            else
            {
                return accountArrayList.size();
            }
        }

        // Method for adding new account free id
        public void AddNewFreeAccountId(int freeId)
        {
            // Checking if there is already this freeId in list
            if (freeAccountIdsArrayList.contains(freeId))
            {
                return;
            }
            // else add it
            freeAccountIdsArrayList.add(freeId);
        }

        // Methods for checking if account id is free
        public boolean IsAccountIdFree(int id)
        {
            // Checking if id is free
            if (freeAccountIdsArrayList.contains(id))
            {
                return false;
            }
            return true;
        }

        // Gets account with specified id
        public Account GetAccountWithId(int id)
        {
            Account defAcc = new Account(); defAcc.SetId(-1);
            for (int i = 0; i < accountArrayList.size(); i++)
            {
                if (accountArrayList.get(i).GetId() == id)
                {
                    return accountArrayList.get(i);
                }
            }
            return defAcc;
        }

        // Deletes all transactions with specified id
        public void DeleteAllTransactionsWithId(int id, Context context)
        {
            // Clearing list
            transactionArrayList.clear();
            // Loading all transactions
            LoadTransactions(context);
            // Going through all of them and deleting all with specified id
            for (int i = 0; i < transactionArrayList.size(); i++)
            {
                if (transactionArrayList.get(i).GetAccountId() == id)
                {
                    transactionArrayList.remove(transactionArrayList.get(i));
                    i--;
                }
            }
            // Saving all transactions
            SaveTransactions(context);
        }
        // Saves basic info
        // - Free id
        public void SaveBasicInfo(Context context) {
            // Writing in binary file
            try(DataOutputStream dos_info = new DataOutputStream(new FileOutputStream(context.getFilesDir() + "\\" + NAME_BASIC_FILE)))
            {
                // Saving number of free ids
                dos_info.writeInt(freeAccountIdsArrayList.size());
                // Saving
                for (int i = 0; i < freeAccountIdsArrayList.size(); i++) {
                    dos_info.writeInt(freeAccountIdsArrayList.get(i));
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        // Loads basic info from file
        public void LoadBasicInfo(Context context) {
            // Reading the file
            try(DataInputStream dis_info = new DataInputStream(new FileInputStream(context.getFilesDir() + "\\" + NAME_ACCOUNTS_FILE)))
            {
                // Size of array list
                int freeIdSize = dis_info.readInt();
                // Setting up variables
                for(int i = 0; i < freeIdSize; i++)
                {
                    freeAccountIdsArrayList.add(dis_info.readInt());
                }
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            }
        }
        // Saves accounts to file
        public void SaveAccounts(Context context) {
            // Writing in binary file
            try(DataOutputStream dos_accounts = new DataOutputStream(new FileOutputStream(context.getFilesDir() + "\\" + NAME_ACCOUNTS_FILE)))
            {
                // Saving all accounts
                ArrayList<Account> accounts = GetAccounts();
                Account saveAccount;
                // Saving
                for (int i = 0; i < accounts.size(); i++) {
                    saveAccount = accounts.get(i);
                    dos_accounts.writeInt(saveAccount.GetId());
                    dos_accounts.writeUTF(saveAccount.GetName());
                    dos_accounts.writeUTF(saveAccount.GetDescription());
                    dos_accounts.writeDouble(saveAccount.GetValue());
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        // Loads accounts from file
        public void LoadAccounts(Context context) {
            // Checking if we have already loaded accounts
            if (isAccountsDataLoaded)
            {
                return;
            }
            // Reading the file
            try(DataInputStream dis_accounts = new DataInputStream(new FileInputStream(context.getFilesDir() + "\\" + NAME_ACCOUNTS_FILE)))
            {
                // Loading all accounts
                ArrayList<Account> accounts = GetAccounts();
                Account loadAccount = new Account();
                // Setting up variables
                while(true)
                {
                    loadAccount = new Account();
                    loadAccount.SetId(dis_accounts.readInt());
                    loadAccount.SetName(dis_accounts.readUTF());
                    loadAccount.SetDescription(dis_accounts.readUTF());
                    loadAccount.SetValue(dis_accounts.readDouble());
                    // Adding this variable
                    accounts.add(loadAccount);
                }
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            }
            // Also loading basic info
            LoadBasicInfo(context);
            // Marking the fact, that we have already loaded all accounts
            isAccountsDataLoaded = true;
        }

        // Saves transactions to file
        public void SaveTransactions(Context context) {
            // Writing in binary file
            try(DataOutputStream dos_transactions = new DataOutputStream(new FileOutputStream(context.getFilesDir() + "\\" + NAME_TRANSACTIONS_FILE)))
            {
                // Saving all transactions
                ArrayList<Transaction> transactions = GetTransactions();
                Transaction saveTransaction;
                // Saving
                for (int i = 0; i < transactions.size(); i++) {
                    saveTransaction = transactions.get(i);
                    dos_transactions.writeInt(saveTransaction.GetTransactionType().getValue());
                    dos_transactions.writeInt(saveTransaction.GetAccountId());
                    dos_transactions.writeDouble(saveTransaction.GetSum());
                    dos_transactions.writeDouble(saveTransaction.GetReminder());
                    dos_transactions.writeUTF(saveTransaction.GetComment());
                    dos_transactions.writeUTF(saveTransaction.GetDateTimeString());
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        // Loads transactions from file
        public void LoadTransactions(Context context) {
            // Reading the file
            try(DataInputStream dis_transactions = new DataInputStream(new FileInputStream(context.getFilesDir() + "\\" + NAME_TRANSACTIONS_FILE)))
            {
                // Loading all transactions
                ArrayList<Transaction> transactions = GetTransactions();
                Transaction loadTransaction;
                SimpleDateFormat dateFor;
                // Setting up variables
                while(true)
                {
                    dateFor = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                    loadTransaction =
                            new Transaction(TransactionType.values()[dis_transactions.readInt()],
                                    dis_transactions.readInt(),
                                    dis_transactions.readDouble(),
                                    dis_transactions.readDouble(),
                                    dis_transactions.readUTF(),
                                    dateFor.parse(dis_transactions.readUTF()));
                    // Adding this variable
                    transactions.add(loadTransaction);
                }
            }
            catch(IOException | ParseException ex){
                System.out.println(ex.getMessage());
            }
        }
        // Loads specific transactions by id from file
        public void LoadTransactions(int id, Context context)
        {
            // Reading the file
            try(DataInputStream dis_transactions = new DataInputStream(new FileInputStream(context.getFilesDir() + "\\" + NAME_TRANSACTIONS_FILE)))
            {
                // Loading all transactions
                ArrayList<Transaction> transactions = GetTransactions();
                Transaction loadTransaction;
                SimpleDateFormat dateFor;
                // Setting up variables
                while(true)
                {
                    dateFor = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                    loadTransaction =
                            new Transaction(TransactionType.values()[dis_transactions.readInt()],
                                    dis_transactions.readInt(),
                                    dis_transactions.readDouble(),
                                    dis_transactions.readDouble(),
                                    dis_transactions.readUTF(),
                                    dateFor.parse(dis_transactions.readUTF()));
                    // Adding this variable, if it's id
                    if (loadTransaction.GetAccountId() == id) {
                        transactions.add(loadTransaction);
                    }
                }
            }
            catch(IOException | ParseException ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}