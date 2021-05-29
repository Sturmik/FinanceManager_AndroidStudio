package com.example.financemanager.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financemanager.classes.Account;
import com.example.financemanager.activities.AccountEditActivity;
import com.example.financemanager.R;
import com.example.financemanager.activities.TransactionActivity;

import java.util.ArrayList;

import static com.example.financemanager.MainManager.*;

// Extended account adapter adds description textView in it
public class AccountExtendedAdapter extends RecyclerView.Adapter<AccountExtendedAdapter.AccountExtendedViewHolder>
{
    // List of accounts
    private ArrayList<Account> accountsData;
    private Context adapterContext;

    /**
     * Initialize the dataset of the Adapter.
     */
    public AccountExtendedAdapter(Context context, ArrayList<Account> accountsData)
    {
        this.adapterContext = context;
        this.accountsData = accountsData;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AccountExtendedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountExtendedViewHolder(LayoutInflater.from(adapterContext).
                inflate(R.layout.recyclerview_account_item_extended, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull AccountExtendedViewHolder holder, int position) {
        // Get current account
        Account currentAccount = accountsData.get(position);

        // Populate the textViews with data
        holder.bindTo(currentAccount);
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return accountsData.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     */
    public class AccountExtendedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // Account name
        private final TextView accountNameTextView;
        // Account value
        private final TextView accountValueTextView;
        // Account description
        private final TextView accountDescriptionTextView;
        // Edit account button
        private final ImageButton editAccountButton;
        // Delete account button
        private final ImageButton deleteAccountButton;

        public AccountExtendedViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views
            accountNameTextView = itemView.findViewById(R.id.accountName_item_textView);
            accountValueTextView = itemView.findViewById(R.id.accountValue_item_textView);
            accountDescriptionTextView = itemView.findViewById(R.id.accountDescription_item_textView);
            editAccountButton = itemView.findViewById(R.id.editAccount_item_button);
            deleteAccountButton = itemView.findViewById(R.id.deleteAccount_item_button);
            // Working with onClick events
            // Edit button
            editAccountButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // Creating intent
                    Intent createAccountIntent = new Intent(adapterContext, AccountEditActivity.class);
                    // Put extra int to intent to define type of interaction with activity
                    createAccountIntent.putExtra("Type", 2);
                    // We also place account index, which we want to edit
                    createAccountIntent.putExtra("accountIndex", getAdapterPosition());
                    // Start work with account
                    adapterContext.startActivity(createAccountIntent);
                }
            });
            // Delete button
            deleteAccountButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {
                        AlertDialog.Builder alert = new AlertDialog.Builder(adapterContext);
                        alert.setTitle(R.string.attention_text_label);
                        alert.setMessage(R.string.confirmDeletionOfTheAccount_text);
                        alert.setPositiveButton(R.string.confirmation_text_label, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Adding new id to free ones
                                GetInstance().AddNewFreeAccountId(GetInstance().GetAccounts().get(getAdapterPosition()).GetId());
                                GetInstance().DeleteAllTransactionsWithId(GetInstance().GetAccounts().get(getAdapterPosition()).GetId(),
                                        adapterContext);
                                GetInstance().GetAccounts().remove(getAdapterPosition());
                                GetInstance().SaveAccounts(adapterContext);
                                notifyDataSetChanged();
                            }
                        });
                        alert.setNegativeButton(R.string.cancellation_text_label, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        });
                        alert.show();
                    }
                    catch (Exception e)
                    {}
                    finally {
                        GetInstance().SaveAccounts(adapterContext);
                    }
                }
            });
            // Set the OnClickListener to the entire view.
            itemView.setOnClickListener(this);
        }

        /**
         * Binds data to specific layout
         */
        void bindTo(Account currentAccount)
        {
            // Populate the textviews with data.
            accountNameTextView.setText(currentAccount.GetName());
            accountValueTextView.setText(currentAccount.GetValueString());
            accountDescriptionTextView.setText(currentAccount.GetDescription());
        }

        /**
         * Opens activity for transaction operation
         * (User needs to shortly tap on account he is interested in)
         */
        @Override
        public void onClick(View v) {
            // Creating intent
            Intent transactionIntent = new Intent(adapterContext, TransactionActivity.class);
            // We send index of account with which we are going to work with.
            // Activity will find account by its index, using singleton manager
            transactionIntent.putExtra("accountIndex", getAdapterPosition());
            adapterContext.startActivity(transactionIntent);
        }
    }
}
