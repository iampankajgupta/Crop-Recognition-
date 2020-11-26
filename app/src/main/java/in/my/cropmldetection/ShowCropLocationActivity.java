package in.my.cropmldetection;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ShowCropLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String cropName;

    ArrayList<Pair> WheatLocations = new ArrayList<>();
    ArrayList<Pair> riceLocations = new ArrayList<>();
    ArrayList<Pair> jowarLocations = new ArrayList<>();
    ArrayList<Pair> bajraLocations = new ArrayList<>();

    private static final LatLng UttarPradesh = new LatLng(26.8467, 80.9462);
    private static final LatLng Punjab = new LatLng(31.1471, 75.3412);
    private static final LatLng Haryana = new LatLng(29.0588, 76.0856 );
    private static final LatLng MadhyaPradesh = new LatLng(22.9734, 78.6569);
    private static final LatLng Rajasthan = new LatLng(27.0238, 74.2179);
    private static final LatLng Bihar = new LatLng(25.0961, 85.3131);
    private static final LatLng Gujrat = new LatLng(22.2587, 71.1924);
    private static final LatLng TamilNadu = new LatLng(11.1271, 78.6569);
    private static final LatLng Chhattisgarh = new LatLng(21.2787, 81.8661);
    private static final LatLng Odisha = new LatLng(20.9517, 85.0985);
    private static final LatLng Assam = new LatLng(26.2006, 92.9376);
    private static final LatLng Karnataka = new LatLng(15.3173, 75.7139);
    private static final LatLng Maharashtra = new LatLng(19.7515, 75.7139);
    private static final LatLng ArunachalPradesh = new LatLng(28.2180, 94.7278);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_crop_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        cropName = getIntent().getStringExtra("cropName");

//        IF THE CROP NAME IS WHEAT THEN CALL WHEAT LOCATION METHOD TO ALL LOCATIONS OF WHEAT IN WHEATlOCATIOND ARRAY LIST

            if (cropName.equals("WHEAT")){
                wheatLocations();
            }
            if (cropName.equals("RICE")){
                riceLocations();
            }
            if (cropName.equals("JOWAR")){
                jowarLocations();
            }
            if (cropName.equals("BAJRA")){
                bajraLocations();
        }
    }
    private void jowarLocations() {
        jowarLocations.add(new Pair("Maharashtra",Maharashtra));
        jowarLocations.add(new Pair("ArunachalPradesh",ArunachalPradesh));
        jowarLocations.add(new Pair("UttarPradesh",UttarPradesh));
        jowarLocations.add(new Pair("Rajasthan",Rajasthan));
        jowarLocations.add(new Pair("Gujrat",Gujrat));
        jowarLocations.add(new Pair("MadhyaPradesh",MadhyaPradesh));
        jowarLocations.add(new Pair("Karnataka",Karnataka));
    }

    private void bajraLocations(){
        bajraLocations.add(new Pair("Maharashtra",Maharashtra));
        bajraLocations.add(new Pair("Haryana",Haryana));
        bajraLocations.add(new Pair("Gujrat",Gujrat));
        bajraLocations.add(new Pair("UttarPradesh",UttarPradesh));

    }
    private void wheatLocations(){
        WheatLocations.add(new Pair("UttarPradesh",UttarPradesh));
        WheatLocations.add(new Pair("Punjab",Punjab));
        WheatLocations.add(new Pair("Haryana",Haryana));
        WheatLocations.add(new Pair("MadhyaPradesh",MadhyaPradesh));
        WheatLocations.add(new Pair("Rajasthan",Rajasthan));
        WheatLocations.add(new Pair("Bihar",Bihar));
        WheatLocations.add(new Pair("Gujrat",Gujrat));


    }
    private void riceLocations(){
        riceLocations.add(new Pair("Bihar",Bihar));
        riceLocations.add(new Pair("Punjab",Punjab));
        riceLocations.add(new Pair("Odisha",Odisha));
        riceLocations.add(new Pair("Karnataka",Karnataka));
        riceLocations.add(new Pair("Assam",Assam));
        riceLocations.add(new Pair("TamilNadu",TamilNadu));
        riceLocations.add(new Pair("Chhattisgarh",Chhattisgarh));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (cropName.equals("WHEAT")){
            for(int i = 0;i<WheatLocations.size();i++){
                mMap.addMarker(new MarkerOptions().position(WheatLocations.get(i).latLng).title(WheatLocations.get(i).name));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(WheatLocations.get(i).latLng));
            }
        }
        if (cropName.equals("RICE")){
            for(int i = 0;i<riceLocations.size();i++){
                mMap.addMarker(new MarkerOptions().position(riceLocations.get(i).latLng).title(riceLocations.get(i).name));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(riceLocations.get(i).latLng));
            }
        }
        if (cropName.equals("JOWAR")){
            for(int i = 0;i<jowarLocations.size();i++){
                mMap.addMarker(new MarkerOptions().position(jowarLocations.get(i).latLng).title(jowarLocations.get(i).name));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(jowarLocations.get(i).latLng));
            }
        }
        if (cropName.equals("BAJRA")){
            for(int i = 0;i<bajraLocations.size();i++){
                mMap.addMarker(new MarkerOptions().position(bajraLocations.get(i).latLng).title(bajraLocations.get(i).name));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(bajraLocations.get(i).latLng));


            }
        }

    }
}
