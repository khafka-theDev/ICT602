package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etUnitsUsed, etRebate;
    TextView tvTotalCharges, tvAdditionalOutput;

    DecimalFormat df = new DecimalFormat("#,###.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize EditText fields and TextViews
        etUnitsUsed = findViewById(R.id.etUnitsUsed);
        etRebate = findViewById(R.id.etRebate);
        tvTotalCharges = findViewById(R.id.tvTotalCharges);
        tvAdditionalOutput = findViewById(R.id.tvAdditionalOutput);

        // Initialize buttons and set onClickListener
        Button btnCalculate = findViewById(R.id.btnCalculate);
        Button btnGuide = findViewById(R.id.btnGuide);
        btnCalculate.setOnClickListener(this);
        btnGuide.setOnClickListener(this);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuAbout) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (selected == R.id.menuGuide) {
            Intent intent = new Intent(MainActivity.this, GuideActivity.class);
            startActivity(intent);
        } else if (selected == R.id.menuExit) {
            finishAffinity(); // This closes all the activities in the stack and exits the application
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnCalculate) {
            // Get the values entered by the user
            String unitsUsedStr = etUnitsUsed.getText().toString();
            String rebateStr = etRebate.getText().toString();

            // Check if units used field is not empty
            if (unitsUsedStr.isEmpty()) {
                Toast.makeText(this, "Please enter units used.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Parse input values to double
            double unitsUsed = Double.parseDouble(unitsUsedStr);
            double rebate = 0;
            if (!rebateStr.isEmpty()) {
                rebate = Double.parseDouble(rebateStr);
                if (rebate > 70) {
                    Toast.makeText(this, "Rebate percentage cannot be more than 70%.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Perform calculation based on provided logic
            double totalCharges = 0;
            if (unitsUsed <= 200) {
                totalCharges = unitsUsed * 0.218;
            } else if (unitsUsed <= 300) {
                totalCharges = (200 * 0.218) + ((unitsUsed - 200) * 0.334);
            } else if (unitsUsed <= 600) {
                totalCharges = (200 * 0.218) + (100 * 0.334) + ((unitsUsed - 300) * 0.516);
            } else {
                totalCharges = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((unitsUsed - 600) * 0.546);
            }

            // Calculate total before rebate
            double totalBeforeRebate = totalCharges;

            // Apply rebate if applicable
            if (rebate > 0) {
                totalCharges = totalCharges - (totalCharges * rebate / 100);
            }

            // Display the result
            tvTotalCharges.setText("Charges After Rebate: RM " + df.format(totalCharges));
            tvAdditionalOutput.setText("Charges Before Rebate: RM " + df.format(totalBeforeRebate));
        } else if (view.getId() == R.id.btnGuide) {
            // Navigate to Guide page
            Intent intent = new Intent(MainActivity.this, GuideActivity.class);
            startActivity(intent);
        }
    }
}
