package id.xl.pi_lah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashScreen extends AppCompatActivity {

    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedViewModel = ViewModelProviders.of(SplashScreen.this).get(SharedViewModel.class);

        try {
            new FetchHeader().execute().get();
        }
        catch(Exception e){
            Log.e("ERROR", e.getMessage(), e);
        }

        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
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
                Toast.makeText(SplashScreen.this, "There was an error", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                obj = new JSONArray(response);
            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
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
                Log.e("My App", "Could not parse malformed JSON: \"" + obj + "\"");
            }
            sharedViewModel.setHeaderResponse(mResponse.toString());
        }
    }

}
