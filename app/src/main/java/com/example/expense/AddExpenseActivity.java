package com.example.expense;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView totalExpenseTextView;
    private ListView listViewExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        db = new DatabaseHelper(this);

        EditText editTextAmount = findViewById(R.id.edittext1);
        EditText editTextDetails = findViewById(R.id.edittext2);
        Button buttonSave = findViewById(R.id.button_save_expense);
        totalExpenseTextView = findViewById(R.id.totalExpenseTextView);
        listViewExpense = findViewById(R.id.listViewExpense);

        buttonSave.setOnClickListener(v -> {
            String amountStr = editTextAmount.getText().toString();
            String details = editTextDetails.getText().toString();
            if (!amountStr.isEmpty() && !details.isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    db.addExpense(amount, details);

                    // Refresh data
                    updateTotalExpense();
                    loadExpenseData();

                    // Clear input fields
                    editTextAmount.setText("");
                    editTextDetails.setText("");

                    // Set result and finish (if needed to pass data back to the main activity)
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(AddExpenseActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AddExpenseActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            }
        });

        updateTotalExpense();
        loadExpenseData();
    }

    private void updateTotalExpense() {
        double totalExpense = db.getTotalExpense();
        String formattedTotalExpense = String.format("Total Expense: $%.2f", totalExpense);
        totalExpenseTextView.setText(formattedTotalExpense);
    }

    private void loadExpenseData() {
        Cursor cursor = db.getAllExpenses();
        String[] from = {DatabaseHelper.COLUMN_AMOUNT, DatabaseHelper.COLUMN_DETAILS, DatabaseHelper.COLUMN_TIMESTAMP};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, 0);
        listViewExpense.setAdapter(adapter);
    }
}
