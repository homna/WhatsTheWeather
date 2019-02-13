package com.example.whatstheweather;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadJson extends AsyncTask<String, Void, String> {
    public TextView resultTextView;

    public DownloadJson(TextView resultTextView) {
        this.resultTextView = resultTextView;
    }

    @Override
    protected String doInBackground(String... urls) {
        StringBuilder result = new StringBuilder();
        result.append("");
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            int read = inputStreamReader.read();
            while (read != -1) {
                char character = (char) read;
                result.append(character);
                read = inputStreamReader.read();
            }
            return result.toString();
        } catch (Exception e) {
            Log.i("GetJson_doInBackground", e.getMessage());
            e.printStackTrace();
            return "Failed!";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        StringBuilder output = new StringBuilder();
        output.append("");

        Log.i("GetJson_result", result);
        try {
            //Code for multiple City Search
         /* JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                JSONObject objectMain = object.getJSONObject("main");
                Log.i("GetJson_result_temp", object.getString("name")+": "+objectMain.getString("temp")+"C");
            }*/

            //Code for Single City Search
            //get temp from main(object)
            JSONObject jsonObject = new JSONObject(result);
            JSONObject object = jsonObject.getJSONObject("main");
            Log.i("GetJson_result_temp", object.getString("temp"));
            output.append("Temperature: " + object.getString("temp") + '\n');

            //get description from weather(array)
            JSONArray jsonArray = jsonObject.getJSONArray("weather");
            output.append("Description: ");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object1 = (JSONObject) jsonArray.get(i);
                Log.i("GetJson_result_weather", object1.getString("description"));
                output.append(object1.getString("description") + " "); //weather is an array so it can include more that one result: example:san francisco

            }
            resultTextView.setText(output.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("GetJson_onPostExecute", e.getMessage());
        }
    }
}
