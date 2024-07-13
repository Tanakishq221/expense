package com.example.expense;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_INCOME = 1;
    private static final int REQUEST_CODE_ADD_EXPENSE = 2;
    private TextView addExpenseButton, addIncomeButton;
    private ListView listView, listViewexpense;
    private TextView totalIncomeTextView, totalExpenseTextView, totalSavedTextView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        addExpenseButton = findViewById(R.id.addExpenseButton);
        addIncomeButton = findViewById(R.id.addIncomeButton);
        listView = findViewById(R.id.listView);
        listViewexpense = findViewById(R.id.listViewexpense);
        totalIncomeTextView = findViewById(R.id.TotalIncomeTextview);
        totalExpenseTextView = findViewById(R.id.TotalExpenseTextview);
        totalSavedTextView = findViewById(R.id.TotalSavedTextview);

        addExpenseButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_EXPENSE);
        });

        addIncomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, addincomeactivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_INCOME);
        });

        refreshData();
    }

    private void updateTotalIncome() {
        double totalIncome = db.getTotalIncome();
        @SuppressLint("DefaultLocale") String formattedTotalIncome = String.format("Total Income: $%.2f", totalIncome);
        totalIncomeTextView.setText(formattedTotalIncome);
    }

    private void updateTotalExpense() {
        double totalExpense = db.getTotalExpense();
        @SuppressLint("DefaultLocale") String formattedTotalExpense = String.format("Total Expense: $%.2f", totalExpense);
        totalExpenseTextView.setText(formattedTotalExpense);
    }

    private void updateTotalSavings() {
        double totalSavings = db.getOverallSavings();
        @SuppressLint("DefaultLocale") String formattedTotalSavings = String.format("Total Saved: %.2f", totalSavings);
        totalSavedTextView.setText(formattedTotalSavings);
    }

    private void loadIncomeData() {
        Cursor cursor = db.getAllIncome();
        String[] from = {DatabaseHelper.COLUMN_AMOUNT, DatabaseHelper.COLUMN_DETAILS, DatabaseHelper.COLUMN_TIMESTAMP};
        int[] to = {R.id.text1, R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to, 0);
        listView.setAdapter(adapter);
    }

    private void loadExpenseData() {
        Cursor cursor = db.getAllExpenses();
        String[] from = {DatabaseHelper.COLUMN_AMOUNT, DatabaseHelper.COLUMN_DETAILS, DatabaseHelper.COLUMN_TIMESTAMP};
        int[] to = {R.id.text1, R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to, 0);
        listViewexpense.setAdapter(adapter);
    }

    private void refreshData() {
        updateTotalIncome();
        updateTotalExpense();
        updateTotalSavings();
        loadIncomeData();
        loadExpenseData();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            refreshData();
        }
    }
}
