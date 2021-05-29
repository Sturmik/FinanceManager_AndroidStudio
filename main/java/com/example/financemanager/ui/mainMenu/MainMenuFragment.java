package com.example.financemanager.ui.mainMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financemanager.adapters.AccountAdapter;
import com.example.financemanager.R;
import com.example.financemanager.classes.Account;
import com.example.financemanager.classes.Transaction;
import com.example.financemanager.classes.TransactionType;

import java.util.Date;

import static com.example.financemanager.MainManager.GetInstance;

public class MainMenuFragment extends Fragment {

    // View variables
    private TextView overallBalanceView;
    private RecyclerView accountsRecyclerView;
    private AccountAdapter accountAdapter;

    @Override
    public void onResume() {
        super.onResume();
        //UpdateData();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Getting root of the fragment
        View root = inflater.inflate(R.layout.fragment_main_menu, container, false);
        // Initializing UI
        overallBalanceView = root.findViewById(R.id.overallBalanceValue_textView);
        // Initialize the RecyclerView
        accountsRecyclerView = root.findViewById(R.id.accounts_recyclerView);
        // Set the Layout Manager
        accountsRecyclerView.setLayoutManager(new LinearLayoutManager(root.findViewById(R.id.mainMenu_linearLayout).getContext()));
        // Initialize the adapter and set it to the RecyclerView.
        accountAdapter = new AccountAdapter(root.findViewById(R.id.mainMenu_linearLayout).getContext(), GetInstance().GetAccounts());
        accountsRecyclerView.setAdapter(accountAdapter);
        // Initialize data
        InitializeData();
        return root;
    }

    private void InitializeData() {
        GetInstance().LoadAccounts(getContext());
        UpdateData();
    }

    private void UpdateData()
    {
        // Showing sum of all accounts values
        overallBalanceView.setText(GetInstance().GetAccountsValueSumString());
        // Notify the adapter about the change of data.
        accountAdapter.notifyDataSetChanged();
    }
}