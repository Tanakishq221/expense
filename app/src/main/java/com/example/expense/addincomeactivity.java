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

public class addincomeactivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView totalIncomeTextView;
    private ListView listViewIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        db = new DatabaseHelper(this);

        EditText editTextAmount = findViewById(R.id.edittext1);
        EditText editTextDetails = findViewById(R.id.edittext2);
        Button buttonSave = findViewById(R.id.button_save_income);
        totalIncomeTextView = findViewById(R.id.totalIncomeTextView);
        listViewIncome = findViewById(R.id.listViewIncome);

        buttonSave.setOnClickListener(v -> {
            String amountStr = editTextAmount.getText().toString();
            String details = editTextDetails.getText().toString();
            if (!amountStr.isEmpty() && !details.isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    db.addIncome(amount, details);

                    // Refresh data
                    updateTotalIncome();
                    loadIncomeData();

                    // Clear input fields
                    editTextAmount.setText("");
                    editTextDetails.setText("");
                } catch (NumberFormatException e) {
                    Toast.makeText(addincomeactivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(addincomeactivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            }
        });

        updateTotalIncome();
        loadIncomeData();
    }

    private void updateTotalIncome() {
        double totalIncome = db.getTotalIncome();
        String formattedTotalIncome = String.format("Total Income: $%.2f", totalIncome);
        totalIncomeTextView.setText(formattedTotalIncome);
    }

    private void loadIncomeData() {
        Cursor cursor = db.getAllIncome();
        String[] from = {DatabaseHelper.COLUMN_AMOUNT, DatabaseHelper.COLUMN_DETAILS, DatabaseHelper.COLUMN_TIMESTAMP};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, 0);
        listViewIncome.setAdapter(adapter);
    }

}
