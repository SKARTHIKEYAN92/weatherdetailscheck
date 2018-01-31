package com.gsk.weatherclimatecheck;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    TextView t1;
    RelativeLayout relativeLayout;
    EditText city;
    EditText zipcode;
    ProgressDialog dialog;
    private boolean isCity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        t1=(TextView)findViewById(R.id.textView);
        t1.setVisibility(View.INVISIBLE);

        // c1=(CheckBox)findViewById (R.id.checkBox1);
        //  c2=(CheckBox)findViewById (R.id.checkBox2);
        relativeLayout=(RelativeLayout)findViewById(R.id.activity_main);
        city = (EditText) findViewById (R.id.editText1);
        zipcode = (EditText) findViewById (R.id.editText2);

        zipcode.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Toast.makeText (getApplicationContext (),"Zip Code is Entered",Toast.LENGTH_SHORT).show ();
                city.setText ("");
                isCity = false;
            }
        });
        city.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Toast.makeText (getApplicationContext (),"City Name is Entered",Toast.LENGTH_SHORT).show ();
                zipcode.setText ("");
                isCity = true;
            }
        });
    }
    class getResultTask extends AsyncTask<String , String, String>
    {
        private MainActivity mainActivity;

        public getResultTask(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        protected String doInBackground(String... Params) {
            String result = "";


            URL url;
            HttpURLConnection connection = null;
            try {

                url = new URL (Params[0]);
                connection = (HttpURLConnection) url.openConnection ();

                InputStream in = connection.getInputStream ();
                BufferedReader reader = new BufferedReader (new InputStreamReader (in));
                StringBuilder builder = new StringBuilder ();
                while ((result = reader.readLine ()) != null) {
                    builder.append (result + "\n");
                }
                Thread.sleep (4000);
                return builder.toString ().trim ();
            } catch (Exception e) {
                e.printStackTrace ();
                // Toast.makeText(getApplicationContext(), "doInBackground is Runned Successfully",Toast.LENGTH_LONG).show();

            }
            return null;
        }

        @Override
        protected void onPreExecute()

        {
            mainActivity.dialog = new ProgressDialog (mainActivity);
            mainActivity.dialog.setTitle (" Please Wait Your ");
            mainActivity.dialog.setMessage (mainActivity.city.getText ().toString () + " City Climate is Loading");
            //mainActivity.dialog.setMessage( mainActivity.e2.getText ().toString ()+ " Climate is Loading");
            mainActivity.dialog.show ();
            //Toast.makeText(getApplicationContext(), "OnPreExecut is running Successfully",Toast.LENGTH_LONG).show();
        }
        @Override
        protected void onPostExecute(String s) {
            mainActivity.dialog.dismiss ();
            String main = "";
            String description = "";
            String message = "";
            try {
                String result = "";
                JSONObject server_data = new JSONObject (s);
                String data = server_data.getString ("weather");
                //Log.v ("tag",JSONObject server_data("5"));
                // Toast.makeText("String data", server_data,Toast.LENGTH_LONG).show();
                // Log.d("TAG",server_data));
                Log.v("TAG",String.valueOf (server_data));
                JSONArray array = new JSONArray (data);
                JSONObject weather_data = array.getJSONObject (0);
                main = weather_data.getString ("main");
                description = weather_data.getString ("description");
                message = main + ":" + description;
                mainActivity.t1.setText (message);
                if (main.equals ("Drizzle")) {
                    mainActivity.relativeLayout.setBackgroundResource (R.drawable.driz);
                    Toast.makeText (mainActivity.getApplicationContext (), "LIGHT DRIZZLE  PRESENT IN  SKY", Toast.LENGTH_LONG).show ();

                } else if (main.equals ("Clouds")) {
                    mainActivity.relativeLayout.setBackgroundResource (R.drawable.cloud);
                    Toast.makeText (mainActivity.getApplicationContext (), "HEAVY CLOUDS PRESENT IN ", Toast.LENGTH_LONG).show ();
                } else if (main.equals ("Clear")) {
                    mainActivity.relativeLayout.setBackgroundResource (R.drawable.clear);
                    Toast.makeText (mainActivity.getApplicationContext (), "CLEAR SUN IN THE SKY" ,Toast.LENGTH_LONG).show ();
                } else if (main.equals ("Haze")) {
                    mainActivity.relativeLayout.setBackgroundResource (R.drawable.haz);
                    Toast.makeText (mainActivity.getApplicationContext (), "HAZE PRESENT IN SKY", Toast.LENGTH_LONG).show ();
                } else if (main.equals ("Mist")) {
                    mainActivity.relativeLayout.setBackgroundResource (R.drawable.mist);
                    Toast.makeText (mainActivity.getApplicationContext (), "HEAVY MIST PRESENT", Toast.LENGTH_LONG).show ();
                } else if (main.equals ("Smoke")) {
                    mainActivity.relativeLayout.setBackgroundResource (R.drawable.smoke);
                    Toast.makeText (mainActivity.getApplicationContext (), "HEAVY SMOKE PRESENT",Toast.LENGTH_LONG).show ();
                }
                else if (main.equals ("Rain")) {
                    mainActivity.relativeLayout.setBackgroundResource (R.drawable.raindrop);
                    Toast.makeText (mainActivity.getApplicationContext (), "HEAVY RAIN PRESENT IN  SKY", Toast.LENGTH_LONG).show ();

                } else {
                    mainActivity.relativeLayout.setBackgroundResource (R.drawable.weather);
                }

            } catch (JSONException e) {
                e.printStackTrace ();
            }
            mainActivity.t1.setVisibility (View.VISIBLE);
            //Toast.makeText(getApplicationContext(), "OnPost Method is Performed",Toast.LENGTH_LONG).show();
        }
    }
    public  void Check_Climate(View view) throws ExecutionException, InterruptedException
    {
        getResultTask task = new getResultTask (this);
//        task.execute ("http://api.openweathermap.org/data/2.5/weather?q=" + e1.getText ().toString () + "&appid=00ae1968c9fe0bc196247d280ae0eb11");
        String url = getURL ();
        task.execute(url);
    }

    /**
     * Getting Zip or City from UI
     * @return zip or City String
     */
    private String getURL(){
        String url = "";
        String str1 = "http://api.openweathermap.org/data/2.5/weather?q=";
        String str2 = "http://api.openweathermap.org/data/2.5/weather?zip=";
        String str3 =  "&appid=00ae1968c9fe0bc196247d280ae0eb11";
        if(isCity){
            url= str1 + String.valueOf (city.getText ()).trim () + str3;
        }else{
            url= str2 +String.valueOf (zipcode.getText ()).trim ()+str3;
        }
        Toast.makeText(getApplicationContext(), "url = " + url,Toast.LENGTH_LONG).show();
        return url;
    }
}

