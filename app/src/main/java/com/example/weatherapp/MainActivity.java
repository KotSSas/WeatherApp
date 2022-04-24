package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView country, city, temp;

    TextView latitude,longtitude,humidity,sunrise,sunset,pressure,wspeed;
    City cityC = new City();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        imageView = findViewById(R.id.imageView);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        temp = findViewById(R.id.temperature);
        latitude = findViewById(R.id.t_latitude);
        longtitude = findViewById(R.id.t_longtitude);
        humidity = findViewById(R.id.t_humidity);
        sunrise = findViewById(R.id.t_sunrise);
        sunset = findViewById(R.id.t_sunset);
        pressure = findViewById(R.id.t_pressure);
        wspeed = findViewById(R.id.t_wspeed);
        getCityName();
    }

    private void getCityName() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Hello!");
        alert.setMessage("Please tell me the city where are you from!");
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", (dialog, whichButton) -> {
            String s = input.getText().toString();
            cityC.setName(s);
            findWeather(cityC.name);
        });
        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
           finish();
        });
        alert.show();

    }

    private void findWeather(String cityA) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+cityA+"&appid=9f17dd3dbcc31c1edb970ec9d23ce767&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                //Calling API
                    try {
                        JSONObject object = new JSONObject(response);
                        //find country
                        JSONObject object1 = object.getJSONObject("sys");
                        String country_find = object1.getString("country");
                        country.setText(country_find);
                        //city
                        String city_find = object.getString("name");
                        city.setText(city_find);

                        //temperature
                        JSONObject object2 = object.getJSONObject("main");
                        String temp_find = object2.getString("temp");
                        temp.setText(temp_find+"°C");



                        //icon
                        JSONArray jsonArray = object.getJSONArray("weather");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String img =  jsonObject.getString("icon");
                        Picasso.get().load("https://openweathermap.org/img/wn/"+img+"@2x.png").into(imageView);

                        //bottom information

                        JSONObject object3 = object.getJSONObject("coord");
                        String lon = object3.getString("lon");
                        String lat = object3.getString("lat");
                        longtitude.setText(lon);
                        latitude.setText(lat);

                        JSONObject object4 = object.getJSONObject("main");

                        String h= object4.getString("humidity");
                        humidity.setText(h);

                        String p = object4.getString("pressure");
                        pressure.setText(p+"hPa");

                        JSONObject object5 = object.getJSONObject("sys");
                        String sr = object5.getString("sunrise");
                        String ss = object5.getString("sunset");
                        sunset.setText(ss);
                        sunrise.setText(sr);

                        JSONObject object6 = object.getJSONObject("wind");
                        String speed = object6.getString("speed");
                        wspeed.setText(speed+"km/h");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {
            Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);

    }
}