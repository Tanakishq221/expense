package com.example.expense;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActivity extends AppCompatActivity {

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        db = new DatabaseHelper(this);

        EditText editTextAmount = findViewById(R.id.edittext1);
        EditText editTextDetails = findViewById(R.id.edittext2);
        Button buttonSave = findViewById(R.id.button_save_expense);

        buttonSave.setOnClickListener(v -> {
            String amountStr = editTextAmount.getText().toString();
            String details = editTextDetails.getText().toString();
            if (!amountStr.isEmpty() && !details.isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    db.addExpense(amount, details);

                    // Set result and finish
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
    }
}
