package com.example.whatstheweather;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    public String result = "";
    TextView resultTextView;
    EditText cityEditText;

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null) && activeNetwork.isConnected();
        return isConnected;
    }

    public void searchWeather(View view) {
        //hide keyboard when user click on button
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("GetJson_hideKeyboard", e.getMessage() + " why");
        }
        if (isInternetAvailable()) {
            try {
                DownloadJson downloadJSON = new DownloadJson(resultTextView);

                //URLEncoder replace all of the spaces inside the string with %20
                String city = URLEncoder.encode(cityEditText.getText().toString(), "utf-8");

                result = downloadJSON.execute("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=8029bbde08196ff5766e51a71689c31f").get();
                if (result.equals("Failed!")) {
                    resultTextView.setText("Input is invalid. Please try again.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("GetJson_search", e.getMessage());
            }
        } else {
            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearEditText(View view) {
        cityEditText.setText("");
        cityEditText.setHint("Enter a city");
        resultTextView.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTextView = findViewById(R.id.resultTextView);
        cityEditText = findViewById(R.id.cityEditText);

        cityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resultTextView.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
