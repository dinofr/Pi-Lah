package id.xl.pi_lah.ui.home;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import id.xl.pi_lah.R;
import id.xl.pi_lah.SharedViewModel;
import id.xl.pi_lah.SplashScreen;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;
    private SharedViewModel sharedViewModel;
    String kapasitas = "";
    String hitungan = "";
    String batt = "";

    GoogleMap mGoogleMap;
    MapView mMapView;
    View root;

    Toast toast;

    @SuppressLint("ShowToast")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        toast = Toast.makeText(getContext(),"Getting data....", Toast.LENGTH_LONG);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = root.findViewById(R.id.map);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

//        sharedViewModel.getChannelUrl().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                try {
//                    new FetchBody().execute();
//                }
//                catch(Exception e){
//                    Log.e("ERROR", e.getMessage(), e);
//                }
//            }
//        });
//
//        sharedViewModel.getBodyResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    kapasitas =  jsonObject.getString("capacity");
//                    hitungan = jsonObject.getString("count");
//                    batt = jsonObject.getString("battery");
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        sharedViewModel.getBodyResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    kapasitas =  jsonObject.getString("capacity");
//                    sharedViewModel.setCount(jsonObject.getString("count"));
//                    sharedViewModel.setBattery(jsonObject.getString("battery"));
//                }catch (JSONException e){
//                    Log.e("My App", "Could not parse malformed JSON: \"" + s + "\"");
//                }
//            }
//        });
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraPosition Jakarta = CameraPosition.builder()
                .target(new LatLng(-6.21462, 106.81513))
                .zoom(12)
                .build();

        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Jakarta));

        sharedViewModel.getHeaderResponse().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("Ready", s);
                JSONArray obj = new JSONArray();

                try{
                    obj = new JSONArray(s);
                }catch (Throwable t){
                    Log.e("My App", "Could not parse header: \"" + s + "\"");
                }

                try{
                    for (int i=0; i<obj.length();i++){
                        String channelID = obj.getJSONObject(i).getString("channelID");
                        String API = obj.getJSONObject(i).getString("read_api_key");
                        String title = obj.getJSONObject(i).getString("deviceName");
                        double latitude = obj.getJSONObject(i).getDouble("latitude");
                        double longitude = obj.getJSONObject(i).getDouble("longitude");

                        Marker marker =  mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title(title));

                        marker.setTag(channelID+";"+API);
                    }
                }catch (JSONException t) {
                    Log.e("My", "Could not parse data: \"" + s + "\"");
                }
            }
        });
//        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(final Marker marker) {
//                String[] channel = marker.getTag().toString().split(";");
//                String channelID = channel[0];
//                String read_api_key = channel[1];
//
//                String channelUrl = "https://api.thingspeak.com/channels/";
//                channelUrl += channelID;
//                channelUrl += "/feeds.json?api_key=";
//                channelUrl += read_api_key;
//                channelUrl += "&results=1";
//
//                sharedViewModel.setChannelUrl(channelUrl);
//                mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//                    @Override
//                    public View getInfoWindow(Marker marker) {
//                        return null;
//                    }
//
//                    @Override
//                    public View getInfoContents(Marker marker) {
//                        View mView = getLayoutInflater().inflate(R.layout.custom_snippet, null);
//                        TextView tvTitle = mView.findViewById(R.id.location);
//                        tvTitle.setText(marker.getTitle());
//
//                        toast.show();
//                        return mView;
//                    }
//                });
//
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        marker.hideInfoWindow();
//                        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//                            @Override
//                            public View getInfoWindow(Marker marker) {
//                                return null;
//                            }
//
//                            @Override
//                            public View getInfoContents(Marker marker) {
//                                View mView = getLayoutInflater().inflate(R.layout.custom_snippet, null);
//
//                                TextView tvKap = mView.findViewById(R.id.capacity);
//                                TextView tvHit = mView.findViewById(R.id.count);
//                                TextView tvBatt = mView.findViewById(R.id.battery);
//                                TextView tvTitle = mView.findViewById(R.id.location);
//
//                                tvTitle.setText(marker.getTitle());
//                                tvKap.setText(kapasitas);
//                                tvHit.setText(hitungan);
//                                tvBatt.setText(batt);
//                                toast.cancel();
//
//                                return mView;
//                            }
//                        });
//                        marker.showInfoWindow();
//                    }
//                }, 1000);
//
//                return false;
//            }
//        });
    }

    class FetchBody extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(sharedViewModel.getChannelUrl().getValue());
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
            JSONObject obj = new JSONObject();
            JSONObject dropBoxData = new JSONObject();

            if(response == null) {
                Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                obj = new JSONObject(response);
            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
            }

            try {
                String capacity = obj.getJSONArray("feeds").getJSONObject(0)
                        .getString("field1");
                String count = obj.getJSONArray("feeds").getJSONObject(0)
                        .getString("field2");
                String battery = obj.getJSONArray("feeds").getJSONObject(0)
                        .getString("field3");
                try {
                    dropBoxData.put("capacity", capacity);
                    dropBoxData.put("count", count);
                    dropBoxData.put("battery", battery);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + obj + "\"");
            }
            sharedViewModel.setBodyResponse(dropBoxData.toString());
        }
    }
}