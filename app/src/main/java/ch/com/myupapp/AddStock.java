package ch.com.myupapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddStock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button addButton = findViewById(R.id.button_add);
        EditText tickerValue = findViewById(R.id.search_stock);
        addButton.setOnClickListener(e -> {
            if (tickerValue.getText() != null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("delete", false);
                intent.putExtra("ticker", tickerValue.getText().toString());
                startActivity(intent);
            }
        });
    }
}
