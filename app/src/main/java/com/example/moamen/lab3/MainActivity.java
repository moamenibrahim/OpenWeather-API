package com.example.moamen.lab3;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDataAPI();
        notificationRoutine();

    }

    // Get data from the weather API
    public void getDataAPI() {
        // body
        final TextView mTextView = findViewById(R.id.temperature);
        final TextView winds_text = findViewById(R.id.winds);
        final TextView clouds = findViewById(R.id.clouds);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.openweathermap.org/data/2.5/forecast?id=643492&APPID=3dbee2b9500147c2fb53685ac1e272a3";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println(response);
                        float temp;
                        float wind;
                        String weather_out;

            try {
                JSONObject root = new JSONObject(response);
                JSONArray tempArray = root.getJSONArray("list");
                JSONObject temperature_list = tempArray.getJSONObject(1);
                JSONObject main_list = temperature_list.getJSONObject("main");
                JSONArray weather_list = temperature_list.getJSONArray("weather");
                JSONObject wind_list = temperature_list.getJSONObject("wind");
                System.out.println((weather_list));
                JSONObject weather = weather_list.getJSONObject(0);

                temp = main_list.getLong("temp");
                weather_out = weather.getString("description");
                wind = wind_list.getLong("speed");

                float temp_celsius=temp-273;
                mTextView.setText("Temperature: " + temp_celsius+" celsius");
                clouds.setText("Status: "+ weather_out);
                winds_text.setText("Wind speed: "+ wind);

                SharedPreferences sp = getSharedPreferences("your_prefs",
                        Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putFloat("temperature", temp_celsius);
                editor.putFloat("wind speed", wind);
                editor.putString("weather status", weather_out);
                editor.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }

        });
        queue.add(stringRequest);


    }


    // Notification
    public void notificationRoutine() {

        /// Get preferences
        SharedPreferences sp = getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        int myIntValue = sp.getInt("your_int_key", -1);
        System.out.println("VALUES" + myIntValue);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Weather notification")
                        .setContentText("Temperature alert: "+myIntValue);

        Intent resultIntent = new Intent(this, MainActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this,"Thanks for using application!!",Toast.LENGTH_LONG).show();

        Intent mServiceIntent = new Intent(this, RSSPullService.class);
        this.startService(mServiceIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"Thanks for using application!!",Toast.LENGTH_LONG).show();

        Intent mServiceIntent = new Intent(this, RSSPullService.class);
        this.startService(mServiceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Thanks for using application!!",Toast.LENGTH_LONG).show();

        Intent mServiceIntent = new Intent(this, RSSPullService.class);
        this.startService(mServiceIntent);    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Thanks for using application!!",Toast.LENGTH_LONG).show();

        Intent mServiceIntent = new Intent(this, RSSPullService.class);
        this.startService(mServiceIntent);
    }

}
