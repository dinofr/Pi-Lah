package id.xl.pi_lah.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import id.xl.pi_lah.R;
import id.xl.pi_lah.SharedViewModel;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View mWindow;
    private Context mContext;
    private SharedViewModel sharedViewModel;

    public CustomInfoWindowAdapter(Context context) {
        this.mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_snippet, null);
    }

    private void rendowWindowText(Marker marker, View view){
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }
}
