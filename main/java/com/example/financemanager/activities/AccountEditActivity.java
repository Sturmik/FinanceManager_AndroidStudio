package com.example.financemanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.financemanager.MainManager;
import com.example.financemanager.R;
import com.example.financemanager.classes.Account;

import static com.example.financemanager.MainManager.GetInstance;

// One activity can be used for creating new account or editing existing one
enum AccountInteractionType
{
    Create,
    Edit
}

public class AccountEditActivity extends AppCompatActivity {

    // Index of account with which we work with
    AccountInteractionType accountInteractionType;
    int accountIndex;
    // View variables
    EditText accountName_editText;
    EditText accountDescription_editText;

    // After activity closes itself, we go back
    // to the fragment from which
    // we got access to this activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        // Getting variables
        accountName_editText = findViewById(R.id.accountName_editText);
        accountDescription_editText = findViewById(R.id.accountDescription_editText);
        // Working with intent
        Intent intent = getIntent();
        // Getting data by intent from accountAdapter
        int type = intent.getIntExtra("Type", -1);
        // Defining type of operation and setting the label of activity according to it
        switch (type)
        {
            case 1:
                accountInteractionType = AccountInteractionType.Create;
                accountIndex=-1;
                setTitle(R.string.createAccount_text);
                break;
            case 2:
                accountInteractionType = AccountInteractionType.Edit;
                setTitle(R.string.editAccount_text);
                break;
        }
        // If we work with editing, set data in empty spaces
        if (accountInteractionType == AccountInteractionType.Edit) {
            // Getting account index
            accountIndex = intent.getIntExtra("accountIndex", -1);
            // If everything is correct setting up the data
            if (accountIndex != -1) {
                // Setting up the text
                accountName_editText.setText(GetInstance().GetAccounts().get(accountIndex).GetName());
                accountDescription_editText.setText(GetInstance().GetAccounts().get(accountIndex).GetDescription());
            }
        }
    }

    public void confirmAccount_click(View view) {
        // Check the name of the transaction, it cannot be empty
        accountName_editText.setText(accountName_editText.getText().toString().trim());
        accountDescription_editText.setText(accountDescription_editText.getText().toString().trim());
        accountDescription_editText.setText(accountDescription_editText.getText().toString().replace('\n', ' '));
        // Checking if the account name is empty
        if (accountName_editText.getText().toString().isEmpty())
        {
            MainManager.ShowToast(getString(R.string.accountEditError_cannotBeEmpty_toast), this);
            return;
        }
        // Checking if the account name is unique
        if (!GetInstance().IsAccountNameUnique(accountName_editText.getText().toString(), accountIndex))
        {
            MainManager.ShowToast(getString(R.string.accountEditError_mustBeUnique_toast), this);
            return;
        }
        // Setting default value for account description, if the edit text is empty
        if (accountDescription_editText.getText().toString().isEmpty())
        {
            accountDescription_editText.setText(getString(R.string.no_description_text_label));
        }
        // Defining type of operation
        switch (accountInteractionType)
        {
            // Create new account
            case Create:
                Account newAccount = new Account(GetInstance().GetFreeAccountId(), accountName_editText.getText().toString(),
                        accountDescription_editText.getText().toString(), 0);
                GetInstance().GetAccounts().add(newAccount);
                break;
                // Edit existing account
            case Edit:
                GetInstance().GetAccounts().
                        get(accountIndex).SetName(accountName_editText.getText().toString());
                GetInstance().GetAccounts().
                        get(accountIndex).SetDescription(accountDescription_editText.getText().toString());
                break;
        }
        // Saving info about changes
        GetInstance().SaveBasicInfo(this);
        GetInstance().SaveAccounts(this);
        // Exit intent
        finish();
    }

    public void cancelAccount_click(View view) {
        // Exit intent
        finish();
    }
}