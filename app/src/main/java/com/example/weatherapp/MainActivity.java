package com.example.weatherapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText et_cityName, et_cityID;
    TextView tv_result;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "e53301e27efa0b66d05045d91b2742d3";
    DecimalFormat df = new DecimalFormat("#.##");

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);
        et_cityName = findViewById(R.id.et_cityName);
        et_cityID = findViewById(R.id.et_cityID);
        tv_result = findViewById(R.id.tv_result);
    }
    public void getWeatherDetail(View view){
        String tempUrl = "";
        String city = et_cityName.getText().toString().trim();
        String cityID = et_cityID.getText().toString().trim();
        if(city.equals("")){
            tv_result.setText("City field  can not be empty");
        } else {
            if(!cityID.equals("")){
                tempUrl = url + "?q=" + city + "," + cityID + "&appid=" + appid;
            } else {
                tempUrl = url + "?q=" + city + "&appid=" + appid;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("response", response);
                    String output = "";
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelLikes = jsonObjectMain.getDouble("feels_like") -273.15;
                        double pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                        String clouds = jsonObjectCloud.getString("all");
                        JSONObject sys = jsonObject.getJSONObject("sys");
                        String countryName = sys.getString("country");
                        String cityName = jsonObject.getString("name");
                        output += "Current weather of " + cityName + " (" + countryName + ")"
                            + "\nTempeture: " + df.format(temp) + " C\nFeel like: " + df.format(feelLikes).toString() + "\nDescription: "
                            + description + "\nPressure: " + pressure + " hPa\nHumidity: " + humidity
                            + " %\nWind speed: " + wind + " m/s\nClould: " + clouds + " %";
                        Log.i("response", output);
                        tv_result.setText(output);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}
