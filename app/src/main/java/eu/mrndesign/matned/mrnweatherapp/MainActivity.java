package eu.mrndesign.matned.mrnweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;

import eu.mrndesign.matned.mrnweatherapp.api.model.FullWeatherInfo;

public class MainActivity extends AppCompatActivity {

    public static final String SAVED_INSTANCE = "SAVED_INSTANCE";
    private static final String COUNTRY_TEXT = "COUNTRY KEY";
    private static final String CITY_TEXT = "CITY KEY";
    private ProgressDialog pd;
    private AlertDialog ad;
    private Button btnHit;
    private EditText city;
    private EditText country;
    private TextView humidity;
    private TextView pressure;
    private TextView temp;
    private TextView weather;
    private TextView wind;
    private TextView currentTime;
    private FullWeatherInfo fullWeatherInfo;
    private String cityText;
    private String countryText;
    private boolean errFlag;


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.cityText);
        country = findViewById(R.id.countryText);
        btnHit = findViewById(R.id.acceptButton);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);
        temp = findViewById(R.id.temp);
        weather = findViewById(R.id.weather);
        wind = findViewById(R.id.wind);
        currentTime = findViewById(R.id.currentTime);

        if (savedInstanceState != null){
            fullWeatherInfo = (FullWeatherInfo) savedInstanceState.get(SAVED_INSTANCE);
            if(fullWeatherInfo != null) {
                setTextViews();
                country.setText(countryText);
                city.setText(cityText);
            }
        }
        btnHit.setOnClickListener(v -> {
            String basicWeatherUrlTemplate = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=%s";
            String placeData = city.getText().toString() + ","+ country.getText().toString();
            String fullUrl = String.format(basicWeatherUrlTemplate, placeData, KEYS.API_TOKEN);
            countryText = String.valueOf(country.getText());
            cityText = String.valueOf(city.getText());
            new JsonTask().execute(fullUrl);
        });


    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void setTextViews() {
        country.setText(fullWeatherInfo.getSys().getCountry());
        humidity.setText(fullWeatherInfo.getMain().getHumidity() + "%");
        pressure.setText(fullWeatherInfo.getMain().getPressure() + " hPa");
        temp.setText(String.format("%.2f", fullWeatherInfo.getMain().getTemp()) + " \u00B0C");
        weather.setText(fullWeatherInfo.getWeather()[0].getDescription());
        wind.setText(fullWeatherInfo.getWind().getSpeed() + "km/h");
        currentTime.setText(String.valueOf(new Time(System.currentTimeMillis())));
    }


    private void parseWeatherJson(String weatherJson){
        Gson gson = new Gson();
        fullWeatherInfo = gson.fromJson(weatherJson, FullWeatherInfo.class);
    }

    @SuppressLint("StaticFieldLeak")
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }



        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                errFlag = false;
                long start = System.currentTimeMillis();
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    if(System.currentTimeMillis() - start > 60000) {
                        errFlag = true;
                        break;
                    }
                }
                return buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                super.onPostExecute(result);
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                parseWeatherJson(result);
                setTextViews();
            }else {
                pd.dismiss();
                if (!errFlag)showAlertDialog();
                else showTimeoutAlertDialog();
            }
        }
    }

    private void showAlertDialog() {
        ad = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Error")
                .setMessage("Something went wrong!!!")
                .setNegativeButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showTimeoutAlertDialog() {
        ad = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Error")
                .setMessage("Too much time, check your connection!!!")
                .setNegativeButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(SAVED_INSTANCE, fullWeatherInfo);
        outState.putSerializable(CITY_TEXT, cityText);
        outState.putSerializable(COUNTRY_TEXT, countryText);
        super.onSaveInstanceState(outState);
    }
}
