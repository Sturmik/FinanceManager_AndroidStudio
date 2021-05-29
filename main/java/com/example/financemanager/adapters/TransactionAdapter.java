package com.example.financemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financemanager.R;
import com.example.financemanager.classes.Transaction;

import java.util.ArrayList;

import static com.example.financemanager.MainManager.GetInstance;

// Extended account adapter adds description textView in it
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>
{
    // List of accounts
    private ArrayList<Transaction> transactionData;
    private Context adapterContext;

    /**
     * Initialize the dataset of the Adapter.
     */
    public TransactionAdapter(Context context, ArrayList<Transaction> transactionData)
    {
        this.adapterContext = context;
        this.transactionData = transactionData;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(adapterContext).
                inflate(R.layout.recyclerview_transaction_item, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        // Get current account
        Transaction currentTransaction = transactionData.get(position);

        // Populate the textViews with data
        holder.bindTo(currentTransaction);
    }

    // Return the size of your dataset
    @Override
    public int getItemCount() {
        return transactionData.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     */
    public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // Transaction date and time
        private final TextView transactionDateTimeTextView;
        // Account name
        private final TextView transactionAccountNameTextView;
        // Transaction sum
        private final TextView transactionSumTextView;
        // Transaction reminder
        private final TextView transactionTypeTextView;
        // Transaction type
        private final TextView transactionReminderTextView;
        // Transaction comment
        private final TextView transactionCommentTextView;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the views
            transactionDateTimeTextView = itemView.findViewById(R.id.transactionDateTime_item_textView);
            transactionAccountNameTextView = itemView.findViewById(R.id.transactionAccountName_item_textView);
            transactionSumTextView = itemView.findViewById(R.id.transactionSum_item_textView);
            transactionReminderTextView = itemView.findViewById(R.id.transactionReminder_item_textView);
            transactionTypeTextView = itemView.findViewById(R.id.transactionType_item_textView);
            transactionCommentTextView = itemView.findViewById(R.id.transactionComment_item_textView);

            // Set the OnClickListener to the entire view.
            itemView.setOnClickListener(this);
        }

        /**
         * Binds data to specific layout
         */
        void bindTo(Transaction currentTransaction) {
            // Populate the textviews with data.
            transactionDateTimeTextView.setText(currentTransaction.GetDateTimeString());
            transactionAccountNameTextView.setText(GetInstance().GetAccountWithId(currentTransaction.GetAccountId()).GetName());
            transactionSumTextView.setText(currentTransaction.GetSumString());
            switch (currentTransaction.GetTransactionType())
            {
                case INCOME:
                    transactionTypeTextView.setText(adapterContext.getString(R.string.transactionIncome_text));
                    // According to the type of the transaction, we change color of its text
                    transactionTypeTextView.setTextColor(adapterContext.getResources().getColor(R.color.green));
                    break;
                case EXPENSE:
                    transactionTypeTextView.setText(adapterContext.getString(R.string.transactionExpense_text));
                    // According to the type of the transaction, we change color of its text
                    transactionTypeTextView.setTextColor(adapterContext.getResources().getColor(R.color.red));
                    break;
            }
            transactionSumTextView.setTextColor(transactionTypeTextView.getCurrentTextColor());
            transactionReminderTextView.setText(currentTransaction.GetReminderString());
            transactionCommentTextView.setText(currentTransaction.GetComment());
        }

        @Override
        public void onClick(View v) {
            // ...
        }
    }
}
