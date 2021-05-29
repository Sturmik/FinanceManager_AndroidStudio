package com.example.financemanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financemanager.classes.Account;
import com.example.financemanager.R;
import com.example.financemanager.activities.TransactionActivity;

import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder>
{
    // List of accounts
    private ArrayList<Account> accountsData;
    private Context adapterContext;

    /**
     * Initialize the dataset of the Adapter.
     */
    public AccountAdapter(Context context, ArrayList<Account> accountsData)
    {
        this.adapterContext = context;
        this.accountsData = accountsData;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AccountViewHolder(LayoutInflater.from(adapterContext).
                inflate(R.layout.recyclerview_account_item, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
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
    public class AccountViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // Account name
        private final TextView accountNameTextView;
        // Account value
        private final TextView accountValueTextView;
        // Account description
        private final TextView accountDescriptionTextView;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views
            accountNameTextView = itemView.findViewById(R.id.accountName_item_textView);
            accountValueTextView = itemView.findViewById(R.id.accountValue_item_textView);
            accountDescriptionTextView = itemView.findViewById(R.id.accountDescription_item_textView);

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
            // Sometimes, we won't depic description
            // That's why, we check if accountDescriptionTextView is not null
            if (accountDescriptionTextView != null)
            {
                accountDescriptionTextView.setText(currentAccount.GetDescription());
            }
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
