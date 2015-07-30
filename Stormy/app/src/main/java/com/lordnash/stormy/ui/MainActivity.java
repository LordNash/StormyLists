package com.lordnash.stormy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lordnash.stormy.R;
import com.lordnash.stormy.weather.Current;
import com.lordnash.stormy.weather.Day;
import com.lordnash.stormy.weather.Forcast;
import com.lordnash.stormy.weather.Hour;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST ="DAILY_FORECAST";

    private Forcast mForcast;
    final double latittude =36.843071;
    final double longitude =11.098507;


    @Bind(R.id.timeLabel) TextView mTimeLabel;
    @Bind(R.id.temperatureLabel) TextView mTemperatureLabel;
    @Bind(R.id.humidityValue) TextView mHumidityValue;
    @Bind(R.id.precipValue) TextView mPrecipValue;
    @Bind(R.id.summaryLabel) TextView mSummaryValue;
    @Bind(R.id.iconImageView) ImageView mIconImageView;
    @Bind(R.id.refleshImageView) ImageView mRefleshImageView;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        mRefleshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getForcast(latittude ,longitude);
            }
        });

        getForcast(latittude ,longitude);
        Log.d(TAG, "Main UI CODE is running !");

    }

    private void getForcast(double latittude , double longitude ) {
        String apiKey ="37f6422e0f981cfa05fbe4edc3738048";


        String forecastUrl ="https://api.forecast.io/forecast/"+apiKey+"/"+latittude+","+longitude;

        if(isNetworkAvailable()){

            tiggleRefresh();

        OkHttpClient client= new OkHttpClient();
        Request request = new Request.Builder()
                .url(forecastUrl)
                .build();

        Call call =client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tiggleRefresh();
                    }
                });

            }

            @Override
            public void onResponse(Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tiggleRefresh();
                    }
                });

                try {
                    String jasonData = response.body().string();
                    Log.v(TAG, jasonData);
                    if (response.isSuccessful()) {
                        mForcast = parseForrecastDetails(jasonData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                upadeDislay();
                            }
                        });


                    } else {
                        alertUserAboutError();
                    }


                } catch (IOException e) {
                    Log.e(TAG, "Exception caught :", e);

                } catch (JSONException e) {
                    Log.e(TAG, "Exception caught :", e);
                }

            }
        });

        }
        else{
            Toast.makeText(this, getString(R.string.toast_network_message), Toast.LENGTH_LONG).show();
        }
    }

    private void tiggleRefresh() {
        if(mProgressBar.getVisibility() ==View.INVISIBLE){
        mProgressBar.setVisibility(View.VISIBLE);
        mRefleshImageView.setVisibility(View.INVISIBLE);}
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefleshImageView.setVisibility(View.VISIBLE);
        }

    }

    private void upadeDislay() {
        Current current = mForcast.getCurrent();
        mTemperatureLabel.setText(current.getTemperature() +"");
        mTimeLabel.setText("At "+ current.getFormattedTime() +" it will be");
        mHumidityValue.setText(current.getHumidity() + " ");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryValue.setText(current.getSummary() + "");
        Drawable drawable =getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);

    }


    private Forcast parseForrecastDetails(String jsonData)  throws JSONException{
        Forcast forcast =new Forcast();

        forcast.setCurrent(getCurrentDetails(jsonData)) ;
        forcast.setHourlyForcast(getHourlyForecast(jsonData));
      //  forcast.setDailyForecast(getDailyForecast(jsonData));

        return  forcast;

    }

    private Day[] getDailyForecast(String jsonData) throws JSONException {

        JSONObject forcast = new JSONObject(jsonData);
        String timezone = forcast.getString("timezone");
        JSONObject daily = forcast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for(int i=0;i < data.length() ; i++){
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();

            day.setSummary(jsonDay.getString("summary"));
            day.setTemperatureMax(jsonDay.getDouble("temperature"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimeZone(timezone);

            days[i] = day ;

        }
        return days;

    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {

        JSONObject forcast = new JSONObject(jsonData);
        String timezone = forcast.getString("timezone");
        JSONObject hourly = forcast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours =  new Hour[data.length()];
        for( int i =0;i<data.length();i++){
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimeZone(timezone);

            hours[i]=hour;
        }
        return hours;
    }


    private Current getCurrentDetails(String jasonData) throws JSONException

    {
        JSONObject forcast = new JSONObject(jasonData);
        String timezone = forcast.getString("timezone");
        Log.i(TAG ,"From JSON :"+ timezone);

        JSONObject currently = forcast.getJSONObject("currently");

        Current current =new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timezone);

        Log.d(TAG, current.getFormattedTime());



        return current;
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvalable = false ;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvalable = true;
        }

        return isAvalable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }



    @OnClick (R.id.dailyButton)
    public void startDailyActivity(View view){
        Intent intent =new Intent(this ,DailyForcastActivity.class);
        intent.putExtra(DAILY_FORECAST,mForcast.getDailyForecast());
        startActivity(intent);
    }











}
