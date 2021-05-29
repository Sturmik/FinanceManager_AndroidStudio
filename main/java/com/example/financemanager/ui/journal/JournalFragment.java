package com.example.financemanager.ui.journal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financemanager.R;
import com.example.financemanager.adapters.TransactionAdapter;

import java.util.ArrayList;

import static com.example.financemanager.MainManager.GetInstance;

public class JournalFragment extends Fragment {

    // View variables
    private CheckBox allTransactionsCheckBox;
    private Spinner transactionsSpinner;
    private RecyclerView transactionsRecyclerView;
    private TransactionAdapter transactionAdapter;

    @Override
    public void onStop() {
        super.onStop();
        InitializeData();
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateData();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_journal, container, false);
        // Initialize transactionsSpinner
        transactionsSpinner = root.findViewById(R.id.transactions_spinner);
        ArrayList<String> accounts = new ArrayList<>();
        for (int i = 0; i < GetInstance().GetAccounts().size(); i++)
        {
            accounts.add(GetInstance().GetAccounts().get(i).GetName());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                root.getContext(), R.layout.spinner_item_string , accounts);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_string);
        transactionsSpinner.setAdapter(arrayAdapter);
        // Initialize allTransactionsCheckBox
        allTransactionsCheckBox = root.findViewById(R.id.allTransactions_checkBox);
        allTransactionsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GetInstance().GetTransactions().clear();
                // If we want to see all transactions
                if(isChecked) {
                    transactionsSpinner.setVisibility(View.INVISIBLE);
                    InitializeData();
                }
                // If we want to see specific transaction
                else {
                    transactionsSpinner.setVisibility(View.VISIBLE);
                    if(transactionsSpinner.getSelectedItem() != null) {
                        GetInstance().LoadTransactions(GetInstance().GetAccountIdByAccountName(transactionsSpinner.getSelectedItem().toString()), getContext());
                    }
                }
                UpdateData();
            }
        });
        // Add on item selected listener
        transactionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected,
                                       int selectedItemPosition, long selectedId) {
                // If all transactions check box is not checked
                if (!allTransactionsCheckBox.isChecked()) {
                    // Show us this
                    GetInstance().GetTransactions().clear();
                    if (transactionsSpinner.getSelectedItem() != null) {
                        GetInstance().LoadTransactions(GetInstance().GetAccountIdByAccountName(transactionsSpinner.getSelectedItem().toString()), getContext());
                    }
                    UpdateData();
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing...
            }
        });
        // Initialize the RecyclerView
        transactionsRecyclerView = root.findViewById(R.id.transactions_recyclerView);
        // Set the Layout Manager
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(root.findViewById(R.id.journal_linearLayout).getContext()));
        // Initialize the adapter and set it to the RecyclerView.
        transactionAdapter = new TransactionAdapter(root.findViewById(R.id.journal_linearLayout).getContext(), GetInstance().GetTransactions());
        transactionsRecyclerView.setAdapter(transactionAdapter);
        // Initialize data
        InitializeData();
        return root;
    }

    private void InitializeData() {
        // Loading transactions
        GetInstance().GetTransactions().clear();
        GetInstance().LoadTransactions(getContext());
        UpdateData();
    }

    private void UpdateData()
    {
        // Notify the adapter about the change of data.
        transactionAdapter.notifyDataSetChanged();
    }
}