package id.xl.pi_lah;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private SharedViewModel sharedViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedViewModel = ViewModelProviders.of(MainActivity.this).get(SharedViewModel.class);
        try {
            new FetchHeader().execute().get();
        }
        catch(Exception e){
            Log.e("ERROR", e.getMessage(), e);
        }

        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @SuppressLint("StaticFieldLeak")
    class FetchHeader extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL("https://api.thingspeak.com/channels.json?api_key=I9CEDD46LZX2HZTC");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            JSONArray obj = new JSONArray();
            JSONArray mResponse = new JSONArray();

            if(response == null) {
                Toast.makeText(MainActivity.this, "There was an error", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                obj = new JSONArray(response);
            } catch (Throwable t) {
                Log.e("My App", "Could not parse response: \"" + response + "\"");
            }

            try {
                for(int i = 0; i < obj.length(); i++){
                    String channelID = obj.getJSONObject(i).getString("id");
                    String deviceName = obj .getJSONObject(i).getString("name");
                    String latitude = obj.getJSONObject(i).getString("latitude");
                    String longitude = obj.getJSONObject(i).getString("longitude");

                    String read_api_key = obj.getJSONObject(i).getJSONArray("api_keys")
                            .getJSONObject(1).getString("api_key");

                    JSONObject dropBoxData = new JSONObject();
                    try {
                        dropBoxData.put("channelID", channelID);
                        dropBoxData.put("deviceName", deviceName);
                        dropBoxData.put("latitude", latitude);
                        dropBoxData.put("longitude", longitude);
                        dropBoxData.put("read_api_key", read_api_key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mResponse.put(dropBoxData);
                }
            }catch (Throwable t) {
                Log.e("My App", "Could not parse data: \"" + obj + "\"");
            }
            Log.d("HeaderResponse", mResponse.toString());
            sharedViewModel.setHeaderResponse(mResponse.toString());
        }
    }

//    private void setRepeatingAsyncTask() {
//
//        final Handler handler = new Handler();
//        Timer timer = new Timer();
//
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//
//                    }
//                });
//            }
//        };
//
//        timer.schedule(task, 0, 5*1000);  // interval of one minute
//
//    }
}
