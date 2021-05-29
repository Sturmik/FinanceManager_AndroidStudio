package com.example.financemanager.ui.accountControl;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financemanager.activities.AccountEditActivity;
import com.example.financemanager.adapters.AccountExtendedAdapter;
import com.example.financemanager.R;

import static com.example.financemanager.MainManager.GetInstance;

public class AccountControlFragment extends Fragment {

    // View variables
    private RecyclerView accountsRecyclerView;
    private AccountExtendedAdapter accountAdapter;

    @Override
    public void onResume() {
        super.onResume();
        UpdateData();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account_control, container, false);
        // Initialize button and onClick for creating account
        Button addNewAccountButton = (Button) root.findViewById(R.id.addNewAccount_button);
        addNewAccountButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Creating intent
                Intent createAccountIntent = new Intent(getActivity(), AccountEditActivity.class);
                // Put extra int to intent to define type of interaction with activity
                createAccountIntent.putExtra("Type", 1);
                // Start work with account
                startActivity(createAccountIntent);
            }
        });
        // Initialize the RecyclerView
        accountsRecyclerView = root.findViewById(R.id.extendedAccounts_recyclerView);
        // Set the Layout Manager
        accountsRecyclerView.setLayoutManager(new LinearLayoutManager(root.findViewById(R.id.extendedAccounts_recyclerView).getContext()));
        // Initialize the adapter and set it to the RecyclerView.
        accountAdapter = new AccountExtendedAdapter(root.findViewById(R.id.accountControl_linearLayout).getContext(), GetInstance().GetAccounts());
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
        // Notify the adapter about the change of data.
        accountAdapter.notifyDataSetChanged();
    }
}