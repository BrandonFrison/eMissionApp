package ca.cmpt276.greengoblins.PlaceModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ca.cmpt276.greengoblins.emission.R;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View window;
    private Context mContext;

    public CustomInfoWindowAdapter(Context mContext) {
        this.mContext = mContext;
        this.window = LayoutInflater.from(mContext).inflate(R.layout.custom_info_window, null);
    }

    private void getWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView textViewTitle = (TextView) view.findViewById(R.id.info_title);

        if(!title.equals("")){
            textViewTitle.setText(title);
        }
        String description = marker.getSnippet();
        TextView textViewdescription = (TextView)view.findViewById(R.id.info_desc);

        if(!description.equals("")){
            textViewdescription.setText(description);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        getWindowText(marker, window);
        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {
        getWindowText(marker, window);
        return window;
    }
}
