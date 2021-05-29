package com.example.financemanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.financemanager.MainManager;
import com.example.financemanager.R;
import com.example.financemanager.classes.Transaction;
import com.example.financemanager.classes.TransactionType;

import java.util.Date;

import static com.example.financemanager.MainManager.GetInstance;

public class TransactionActivity extends AppCompatActivity {
    // Index of account with which we work with
    TransactionType transactionType;
    int accountIndex;
    // View variables
    EditText transactionSum_editText;
    EditText transactionComment_editText;

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
        setContentView(R.layout.activity_transaction);

        // Working with UI
        TextView accountName = findViewById(R.id.accountName_textView);
        TextView accountValue = findViewById(R.id.accountValue_textView);
        TextView accountDescription = findViewById(R.id.accountDescription_textView);
        transactionSum_editText = findViewById(R.id.transactionSum_editText);
        transactionComment_editText = findViewById(R.id.transactionComment_editText);
        // Working with switch (radio buttons)
        transactionType = TransactionType.INCOME;
        // Income radio button
        findViewById(R.id.transactionType_switch_Income).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // If it is income
                if(((RadioButton)v).isChecked())
                {
                    transactionType = TransactionType.INCOME;
                }
            }
        });
        // Expense radio button
        findViewById(R.id.transactionType_switch_Expense).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // If it is income
                if(((RadioButton)v).isChecked())
                {
                    transactionType = TransactionType.EXPENSE;
                }
            }
        });
        // Working with intent
        Intent intent = getIntent();
        // Getting data by intent from accountAdapter
        accountIndex = intent.getIntExtra("accountIndex", -1);
        // If everything is correct setting up the data
        if ( accountIndex != -1)
        {
            // Setting up the text
            accountName.setText(GetInstance().GetAccounts().get( accountIndex).GetName());
            accountValue.setText(GetInstance().GetAccounts().get(accountIndex).GetValueString());
            accountDescription.setText(GetInstance().GetAccounts().get(accountIndex).GetDescription());
        }
    }

    // Click confirm button event
    public void confirmTransaction_click(View view)
    {
        transactionComment_editText.setText(transactionComment_editText.getText().toString().trim());
        transactionComment_editText.setText(transactionComment_editText.getText().toString().replace('\n', ' '));
        // Check the sum of the transaction, it cannot be empty
        if (transactionSum_editText.getText().toString().isEmpty())
        {
            MainManager.ShowToast(getString(R.string.transactionError_cannotBeEmpty_toast), this);
            return;
        }
        // Getting entered sum
        double sumCheck = Double.parseDouble(transactionSum_editText.getText().toString());
        // Check the type of the transaction
        if (transactionType == TransactionType.EXPENSE)
        {
            // If sum of expense is bigger than the sum we have on our account - show error
            if (sumCheck > GetInstance().GetAccounts().get(accountIndex).GetValue())
            {
                MainManager.ShowToast(getString(R.string.transactionError_cannotAfford_toast), this);
                return;
            }
            // Else we go further and check other fields
        }
        // Setting default value for transaction comment, if the edit text is empty
        if (transactionComment_editText.getText().toString().isEmpty())
        {
            transactionComment_editText.setText(getString(R.string.no_comment_text_label));
        }
        // Check the type of the transaction and execute the action
        switch (transactionType)
        {
            case INCOME:
                GetInstance().GetAccounts().get(accountIndex).AddToValue(sumCheck);
                break;
            case EXPENSE:
                GetInstance().GetAccounts().get(accountIndex).AddToValue(-sumCheck);
                break;
        }
        // If transaction name is empty, fill it with default value
        if (transactionComment_editText.getText().toString().isEmpty())
        {
            transactionComment_editText.setText(getString(R.string.no_comment_text_label));
        }
        // Adding and saving this transaction
        GetInstance().GetTransactions().add(0,new Transaction(transactionType, GetInstance().GetAccounts().get(accountIndex).GetId(),
                sumCheck,GetInstance().GetAccounts().get(accountIndex).GetValue(), transactionComment_editText.getText().toString(), new Date()));
        // Saving info about changes
        GetInstance().SaveAccounts(this);
        GetInstance().LoadTransactions(this);
        GetInstance().SaveTransactions(this);
        // Exit intent
        finish();
    }

    // Click the cancel button event
    public void cancelTransaction_click(View view) {
        // Exit intent
        finish();
    }
}