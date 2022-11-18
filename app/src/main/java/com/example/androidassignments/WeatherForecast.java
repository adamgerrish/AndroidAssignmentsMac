package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WeatherForecast extends AppCompatActivity {
    ProgressBar progressBar;
    String TAG ="WeatherForcast";
    ImageView wImageView;
    TextView curr, min, max;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        ForecastQuery forecast = new ForecastQuery();
        String url = getIntent().getStringExtra("url");
        forecast.execute(url);
    }

    protected class ForecastQuery extends AsyncTask<String,Integer,String> {
        String minTemp;
        String maxTemp;
        String currTemp;
        Bitmap weatherPic;
        String icon;

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            curr = findViewById(R.id.currTempTextView);
            curr.setText("Current: "+currTemp);
            min = findViewById(R.id.minTextView);
            min.setText("Min: "+minTemp);
            max = findViewById(R.id.maxTextView);
            max.setText("Max: "+maxTemp);
            wImageView = findViewById(R.id.weatherImageView);
            wImageView.setImageBitmap(weatherPic);
            progressBar.setVisibility(View.INVISIBLE);

        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL (strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                parse(connection.getInputStream());
                String fname = icon+".png";
                if (fileExistance(fname)){
                    FileInputStream fis = null;
                    try{
                        fis = openFileInput(fname);
                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                    weatherPic = BitmapFactory.decodeStream(fis);
                    Log.i(TAG, fname+": Found Locally ");
                } else {
                    String iconURL = "http://openweathermap.org/img/w/"+fname;
                    weatherPic = new HttpUtils().getImage(iconURL);
                    publishProgress(100);
                    FileOutputStream fos = openFileOutput( icon + ".png", Context.MODE_PRIVATE);
                    weatherPic.compress(Bitmap.CompressFormat.PNG,80,fos);
                    fos.flush();
                    fos.close();
                    Log.i(TAG, fname+": Downloaded ");

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }
        public List parse(InputStream in) throws XmlPullParserException, IOException{
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(in, null);
                parser.nextTag();
                String name;
                parser.require(XmlPullParser.START_TAG, null, "currTemp");
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT){
                    name = parser.getName();
                    if (parser.getEventType() == XmlPullParser.START_TAG){
                        if (name.equals("temperature")){
                            currTemp = parser.getAttributeValue(null,"value");
                            publishProgress(25);
                            minTemp = parser.getAttributeValue(null,"minTemp");
                            publishProgress(50);
                            maxTemp = parser.getAttributeValue(null, "maxTemp");
                            publishProgress(75);
                        }
                        else if (name.equals("weather")){
                            icon = parser.getAttributeValue(null,"icon");
                        }
                    }
                    parser.next();
                }
            } finally {
                in.close();
            }
            return null;
        }
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
        private class HttpUtils {
            public Bitmap getImage(URL url) {
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        return BitmapFactory.decodeStream(connection.getInputStream());
                    } else
                        return null;
                } catch (Exception e) {
                    return null;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
            public Bitmap getImage(String urlString) {
                try {
                    URL url = new URL(urlString);
                    return getImage(url);
                } catch (MalformedURLException e) {
                    return null;
                }
            }
        }
    }
}