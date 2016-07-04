package softikoda.com.virtualofficelocator;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback, android.location.LocationListener {
    private GoogleMap mMap;
    TileOverlay mOverlay;
    final Context context = this;
    CheckBox cb_High_speed_internet;
    CheckBox cb_Video_Conf;
    CheckBox cb_WiFi_Internet;
    CheckBox cb_Scanner_Printer;
    CheckBox cb_Computer;
    CheckBox cb_Receptionist;
    CheckBox cb_Board_Room;
    CheckBox cb_Messenger;
    CheckBox cb_Underground_Parking;
    CheckBox cb_Hot_Shower;
    CheckBox cb_Lounge;
    CheckBox cb_Private_work_space;
    CheckBox cb_Shared_work_space;
    CheckBox cb_Cost_Hourly_Rate;
    Button btnRate;

    int High_speed_internet;
    int Video_Conf;
    int WiFi_Internet;
    int Scanner_Printer;
    int Computer;
    int Receptionist;
    int Board_Room;
    int Messenger;
    int Underground_Parking;
    int Hot_Shower;
    int Lounge;
    int Private_work_space;
    int Shared_work_space;
    int Cost_Hourly_Rate;
    int minCost, maxCost;
    Spinner list_importance_HighSpeed, list_importance_VideoConf, list_importance_Wifi, list_importance_ScannerPrinter,
            list_importance_Computer, list_importance_Receptionist, list_importance_BoardRoom, list_importance_Messenger,
            list_importance_UndergroudParking, list_importance_HotShower, list_importance_Lounge, list_importance_PrivateWorkSpace,
            list_importance_SharedWorkspace, list_importance_CostHourly;
    Spinner listPriority_HighSpeed, listPriority_VideoConf, listPriority_WIFI, listPriority_ScannerPrinter,
            listPriority_Computer, listPriority_Receptionist, listPriority_BoardRoom, listPriority_Messenger,
            listPriority_UndergroundParking, listPriority_HotShower, listPriority_Lounge, listPriority_PrivateWorkspace,
            listPriority_SharedWorkspace, listPriority_CostHourly;
    Spinner hourlyRate;

    String url;

    int LI_HighSpeed, LI_VideoConf, LI_Wifi, LI_ScannerPrinter,
            LI_Computer, LI_Receptionist, LI_BoardRoom, LI_Messenger,
            LI_UndergroudParking, LI_HotShower, LI_Lounge, LI_PrivateWorkSpace,
            LI_SharedWorkspace, LI_CostHourly;
    int LP_HighSpeed, LP_VideoConf, LP_WIFI, LP_ScannerPrinter,
            LP_Computer, LP_Receptionist, LP_BoardRoom, LP_Messenger,
            LP_UndergroundParking, LP_HotShower, LP_Lounge, LP_PrivateWorkspace,
            LP_SharedWorkspace, LP_CostHourly;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public LocationManager locationManager;
    private HashMap<Marker, MyMarker> mMarkersHashMap;
    private ArrayList<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();
    LatLng latitude_longitude, currentLocation;
    double latitude, longitude;
    JSONArray allOffices = null;
    String provider;
    private boolean isGPSEnabled;
    Location location;
    String priorityString = "";
    int maxNo = 14;
    int currentPosition = 1;
    int radius;
    ProgressDialog progressDialog;
    Polyline polyline = null;

    private static final LatLng LOWER_MANHATTAN = new LatLng(-1.2806956, 36.8141643);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
    private static final LatLng WALL_STREET = new LatLng(-1.3106914, 36.8070243);

    final String TAG = "PathGoogleMapActivity";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                loadDialog();
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        call method to fetch and display data
        // fetchData();
        // Add a marker in Sydney and move the camera
        getMyLocation();


        LatLng currentPlace = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentPlace).title("Current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPlace));
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(9));
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        // Toast.makeText(getApplicationContext()," latitude : "+latitude+" longitude : "+longitude,Toast.LENGTH_LONG).show();
        isGPSEnabled = true;
    }

    private void loadDialog() {
        currentPosition = 1;
        minCost = maxCost = 0;
//        load the dialog box here
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.all_factors);
        dialog.setTitle("Choose Features");
        dialog.setCancelable(true);
        cb_High_speed_internet = (CheckBox) dialog.findViewById(R.id.cb_High_speed_internet);
        cb_Video_Conf = (CheckBox) dialog.findViewById(R.id.cb_Video_Conf);
        cb_WiFi_Internet = (CheckBox) dialog.findViewById(R.id.cb_WiFi_Internet);
        cb_Scanner_Printer = (CheckBox) dialog.findViewById(R.id.cb_Scanner_Printer);
        cb_Computer = (CheckBox) dialog.findViewById(R.id.cb_Computer);
        cb_Receptionist = (CheckBox) dialog.findViewById(R.id.cb_Receptionist);
        cb_Board_Room = (CheckBox) dialog.findViewById(R.id.cb_Board_Room);
        cb_Messenger = (CheckBox) dialog.findViewById(R.id.cb_Messenger);
        cb_Underground_Parking = (CheckBox) dialog.findViewById(R.id.cb_Underground_Parking);
        cb_Hot_Shower = (CheckBox) dialog.findViewById(R.id.cb_Hot_Shower);
        cb_Lounge = (CheckBox) dialog.findViewById(R.id.cb_Lounge);
        cb_Private_work_space = (CheckBox) dialog.findViewById(R.id.cb_Private_work_space);
        cb_Shared_work_space = (CheckBox) dialog.findViewById(R.id.cb_Shared_work_space);
        cb_Cost_Hourly_Rate = (CheckBox) dialog.findViewById(R.id.cb_Cost_Hourly_Rate);

        list_importance_HighSpeed = (Spinner) dialog.findViewById(R.id.list_importance_HighSpeed);
        list_importance_VideoConf = (Spinner) dialog.findViewById(R.id.list_importance_VideoConf);
        list_importance_Wifi = (Spinner) dialog.findViewById(R.id.list_importance_Wifi);
        list_importance_ScannerPrinter = (Spinner) dialog.findViewById(R.id.list_importance_ScannerPrinter);
        list_importance_Computer = (Spinner) dialog.findViewById(R.id.list_importance_Computer);
        list_importance_Receptionist = (Spinner) dialog.findViewById(R.id.list_importance_Receptionist);
        list_importance_BoardRoom = (Spinner) dialog.findViewById(R.id.list_importance_BoardRoom);
        list_importance_Messenger = (Spinner) dialog.findViewById(R.id.list_importance_Messenger);
        list_importance_UndergroudParking = (Spinner) dialog.findViewById(R.id.list_importance_UndergroudParking);
        list_importance_HotShower = (Spinner) dialog.findViewById(R.id.list_importance_HotShower);
        list_importance_Lounge = (Spinner) dialog.findViewById(R.id.list_importance_Lounge);
        list_importance_PrivateWorkSpace = (Spinner) dialog.findViewById(R.id.list_importance_PrivateWorkSpace);
        list_importance_SharedWorkspace = (Spinner) dialog.findViewById(R.id.list_importance_SharedWorkspace);
        list_importance_CostHourly = (Spinner) dialog.findViewById(R.id.list_importance_CostHourly);

        listPriority_HighSpeed = (Spinner) dialog.findViewById(R.id.listPriority_HighSpeed);
        listPriority_VideoConf = (Spinner) dialog.findViewById(R.id.listPriority_VideoConf);
        listPriority_WIFI = (Spinner) dialog.findViewById(R.id.listPriority_WIFI);
        listPriority_ScannerPrinter = (Spinner) dialog.findViewById(R.id.listPriority_ScannerPrinter);
        listPriority_Computer = (Spinner) dialog.findViewById(R.id.listPriority_Computer);
        listPriority_Receptionist = (Spinner) dialog.findViewById(R.id.listPriority_Receptionist);
        listPriority_BoardRoom = (Spinner) dialog.findViewById(R.id.listPriority_BoardRoom);
        listPriority_Messenger = (Spinner) dialog.findViewById(R.id.listPriority_Messenger);
        listPriority_UndergroundParking = (Spinner) dialog.findViewById(R.id.listPriority_UndergroundParking);
        listPriority_HotShower = (Spinner) dialog.findViewById(R.id.listPriority_HotShower);
        listPriority_Lounge = (Spinner) dialog.findViewById(R.id.listPriority_Lounge);
        listPriority_PrivateWorkspace = (Spinner) dialog.findViewById(R.id.listPriority_PrivateWorkspace);
        listPriority_SharedWorkspace = (Spinner) dialog.findViewById(R.id.listPriority_SharedWorkspace);
        listPriority_CostHourly = (Spinner) dialog.findViewById(R.id.listPriority_CostHourly);

        hourlyRate = (Spinner) dialog.findViewById(R.id.hourlyRating);
//Disable all drop down when loading the dialog
        disabler();

        cb_High_speed_internet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_HighSpeed.setAdapter(adapter);
                    currentPosition++;
                    list_importance_HighSpeed.setEnabled(true);
                    list_importance_HighSpeed.setClickable(true);
                    listPriority_HighSpeed.setEnabled(true);
                    listPriority_HighSpeed.setClickable(true);
                } else {
                    list_importance_HighSpeed.setEnabled(false);
                    list_importance_HighSpeed.setClickable(false);
                    listPriority_HighSpeed.setEnabled(false);
                    listPriority_HighSpeed.setClickable(false);
                    currentPosition--;
                }
            }
        });
        cb_Video_Conf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_VideoConf.setAdapter(adapter);
                    currentPosition++;
                    list_importance_VideoConf.setEnabled(true);
                    list_importance_VideoConf.setClickable(true);
                    listPriority_VideoConf.setEnabled(true);
                    listPriority_VideoConf.setClickable(true);

                } else {
                    list_importance_VideoConf.setEnabled(false);
                    list_importance_VideoConf.setClickable(false);
                    listPriority_VideoConf.setEnabled(false);
                    listPriority_VideoConf.setClickable(false);
                    currentPosition--;
                }
            }
        });
        cb_WiFi_Internet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_WIFI.setAdapter(adapter);
                    currentPosition++;
                    list_importance_Wifi.setEnabled(true);
                    list_importance_Wifi.setClickable(true);
                    listPriority_WIFI.setEnabled(true);
                    listPriority_WIFI.setClickable(true);
                } else {
                    list_importance_Wifi.setEnabled(false);
                    list_importance_Wifi.setClickable(false);
                    listPriority_WIFI.setEnabled(false);
                    listPriority_WIFI.setClickable(false);
                    currentPosition--;
                }
            }
        });
        cb_Scanner_Printer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_ScannerPrinter.setAdapter(adapter);
                    currentPosition++;
                    list_importance_ScannerPrinter.setEnabled(true);
                    list_importance_ScannerPrinter.setClickable(true);
                    listPriority_ScannerPrinter.setEnabled(true);
                    listPriority_ScannerPrinter.setClickable(true);
                } else {
                    list_importance_ScannerPrinter.setEnabled(false);
                    list_importance_ScannerPrinter.setClickable(false);
                    listPriority_ScannerPrinter.setEnabled(false);
                    listPriority_ScannerPrinter.setClickable(false);
                    currentPosition--;
                }
            }
        });
        cb_Computer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_Computer.setAdapter(adapter);
                    currentPosition++;

                    list_importance_Computer.setEnabled(true);
                    list_importance_Computer.setClickable(true);
                    listPriority_Computer.setEnabled(true);
                    listPriority_Computer.setClickable(true);
                } else {
                    list_importance_Computer.setEnabled(false);
                    list_importance_Computer.setClickable(false);
                    listPriority_Computer.setEnabled(false);
                    listPriority_Computer.setClickable(false);
                    currentPosition--;
                }
            }
        });
        cb_Receptionist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_Receptionist.setAdapter(adapter);
                    currentPosition++;

                    list_importance_Receptionist.setEnabled(true);
                    list_importance_Receptionist.setClickable(true);
                    listPriority_Receptionist.setEnabled(true);
                    listPriority_Receptionist.setClickable(true);
                } else {
                    list_importance_Receptionist.setEnabled(false);
                    list_importance_Receptionist.setClickable(false);
                    listPriority_Receptionist.setEnabled(false);
                    listPriority_Receptionist.setClickable(false);
                    currentPosition--;
                }
            }
        });
        cb_Board_Room.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_BoardRoom.setAdapter(adapter);
                    currentPosition++;

                    list_importance_BoardRoom.setEnabled(true);
                    list_importance_BoardRoom.setClickable(true);
                    listPriority_BoardRoom.setEnabled(true);
                    listPriority_BoardRoom.setClickable(true);
                } else {
                    list_importance_BoardRoom.setEnabled(false);
                    list_importance_BoardRoom.setClickable(false);
                    listPriority_BoardRoom.setEnabled(false);
                    listPriority_BoardRoom.setClickable(false);
                    currentPosition--;
                }
            }
        });
        cb_Messenger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_Messenger.setAdapter(adapter);
                    currentPosition++;
                    list_importance_Messenger.setEnabled(true);
                    list_importance_Messenger.setClickable(true);
                    listPriority_Messenger.setEnabled(true);
                    listPriority_Messenger.setClickable(true);
                } else {
                    list_importance_Messenger.setEnabled(false);
                    list_importance_Messenger.setClickable(false);
                    listPriority_Messenger.setEnabled(false);
                    listPriority_Messenger.setClickable(false);
                    currentPosition--;
                }
            }
        });
        cb_Underground_Parking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_UndergroundParking.setAdapter(adapter);
                    currentPosition++;

                    list_importance_UndergroudParking.setEnabled(true);
                    list_importance_UndergroudParking.setClickable(true);
                    listPriority_UndergroundParking.setEnabled(true);
                    listPriority_UndergroundParking.setClickable(true);
                } else {
                    list_importance_UndergroudParking.setEnabled(false);
                    list_importance_UndergroudParking.setClickable(false);
                    listPriority_UndergroundParking.setEnabled(false);
                    listPriority_UndergroundParking.setClickable(false);
                    currentPosition--;
                }
            }
        });
        cb_Hot_Shower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_HotShower.setAdapter(adapter);
                    currentPosition++;

                    list_importance_HotShower.setEnabled(true);
                    list_importance_HotShower.setClickable(true);
                    listPriority_HotShower.setEnabled(true);
                    listPriority_HotShower.setClickable(true);
                } else {
                    list_importance_HotShower.setEnabled(false);
                    list_importance_HotShower.setClickable(false);
                    listPriority_HotShower.setEnabled(false);
                    listPriority_HotShower.setClickable(false);
                    currentPosition--;
                }
            }
        });
        cb_Lounge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_Lounge.setAdapter(adapter);
                    currentPosition++;

                    list_importance_Lounge.setEnabled(true);
                    list_importance_Lounge.setClickable(true);
                    listPriority_Lounge.setEnabled(true);
                    listPriority_Lounge.setClickable(true);
                } else {
                    list_importance_Lounge.setEnabled(false);
                    list_importance_Lounge.setClickable(false);
                    listPriority_Lounge.setEnabled(false);
                    listPriority_Lounge.setClickable(false);
                    currentPosition--;
                }
            }
        });
        cb_Private_work_space.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_PrivateWorkspace.setAdapter(adapter);
                    currentPosition++;

                    list_importance_PrivateWorkSpace.setEnabled(true);
                    list_importance_PrivateWorkSpace.setClickable(true);
                    listPriority_PrivateWorkspace.setEnabled(true);
                    listPriority_PrivateWorkspace.setClickable(true);
                } else {
                    list_importance_PrivateWorkSpace.setEnabled(false);
                    list_importance_PrivateWorkSpace.setClickable(false);
                    listPriority_PrivateWorkspace.setEnabled(false);
                    listPriority_PrivateWorkspace.setClickable(false);
                    currentPosition--;
                }
            }
        });
        cb_Shared_work_space.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_SharedWorkspace.setAdapter(adapter);
                    currentPosition++;

                    list_importance_SharedWorkspace.setEnabled(true);
                    list_importance_SharedWorkspace.setClickable(true);
                    listPriority_SharedWorkspace.setEnabled(true);
                    listPriority_SharedWorkspace.setClickable(true);
                } else {
                    list_importance_SharedWorkspace.setEnabled(false);
                    list_importance_SharedWorkspace.setClickable(false);
                    listPriority_SharedWorkspace.setEnabled(false);
                    listPriority_SharedWorkspace.setClickable(false);
                    currentPosition--;
                }
            }
        });
        cb_Cost_Hourly_Rate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<String> list = new ArrayList<String>();
                    for (int i = currentPosition; i <= currentPosition; i++) {
                        list.add("" + i);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.priority, list);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    listPriority_CostHourly.setAdapter(adapter);
                    currentPosition++;

                    list_importance_CostHourly.setEnabled(true);
                    list_importance_CostHourly.setClickable(true);
                    listPriority_CostHourly.setEnabled(true);
                    listPriority_CostHourly.setClickable(true);
                    hourlyRate.setEnabled(true);
                    hourlyRate.setClickable(true);
                } else {
                    list_importance_CostHourly.setEnabled(false);
                    list_importance_CostHourly.setClickable(false);
                    listPriority_CostHourly.setEnabled(false);
                    listPriority_CostHourly.setClickable(false);
                    hourlyRate.setEnabled(false);
                    hourlyRate.setClickable(false);
                    currentPosition--;
                }
            }
        });


        btnRate = (Button) dialog.findViewById(R.id.btnRate);
        dialog.setCanceledOnTouchOutside(false);

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LI_HighSpeed = LI_VideoConf = LI_Wifi = LI_ScannerPrinter =
                        LI_Computer = LI_Receptionist = LI_BoardRoom = LI_Messenger =
                                LI_UndergroudParking = LI_HotShower = LI_Lounge = LI_PrivateWorkSpace =
                                        LI_SharedWorkspace = LI_CostHourly = 0;
                LP_HighSpeed = LP_VideoConf = LP_WIFI = LP_ScannerPrinter =
                        LP_Computer = LP_Receptionist = LP_BoardRoom = LP_Messenger =
                                LP_UndergroundParking = LP_HotShower = LP_Lounge = LP_PrivateWorkspace =
                                        LP_SharedWorkspace = LP_CostHourly = 0;


                High_speed_internet = 0;
                Video_Conf = 0;
                WiFi_Internet = 0;
                Scanner_Printer = 0;
                Computer = 0;
                Receptionist = 0;
                Board_Room = 0;
                Messenger = 0;
                Underground_Parking = 0;
                Hot_Shower = 0;
                Lounge = 0;
                Private_work_space = 0;
                Shared_work_space = 0;
                Cost_Hourly_Rate = 0;
                url = "http://wrostdevelopers.com/virtual_offices/loadPlaces.php?user=1";
                LI_VideoConf = LI_Wifi = LI_ScannerPrinter =
                        LI_Computer = LI_Receptionist = LI_BoardRoom = LI_Messenger =
                                LI_UndergroudParking = LI_HotShower = LI_Lounge = LI_PrivateWorkSpace =
                                        LI_SharedWorkspace = LI_CostHourly = 0;

                LP_VideoConf = LP_WIFI = LP_ScannerPrinter =
                        LP_Computer = LP_Receptionist = LP_BoardRoom = LP_Messenger =
                                LP_UndergroundParking = LP_HotShower = LP_Lounge = LP_PrivateWorkspace =
                                        LP_SharedWorkspace = LP_CostHourly = 0;
                if (cb_High_speed_internet.isChecked()) {
                    High_speed_internet = 1;
                    LI_HighSpeed = ((int) list_importance_HighSpeed.getSelectedItemId() + 1);
                    LP_HighSpeed = ((int) listPriority_HighSpeed.getSelectedItemId() + 1);
                    url += "&&High_speed_internet=1&&LI_HighSpeed=" + LI_HighSpeed + "&&LP_HighSpeed=" + LP_HighSpeed;
                } else {
                    url += "&&High_speed_internet=0&&LI_HighSpeed=0&&LP_HighSpeed=0";
                }
                if (cb_Video_Conf.isChecked()) {
                    Video_Conf = 1;
                    LI_VideoConf = ((int) list_importance_VideoConf.getSelectedItemId() + 1);
                    LP_VideoConf = ((int) listPriority_VideoConf.getSelectedItemId() + 1);
                    url += "&&Video_Conf=1&&LI_VideoConf=" + LI_VideoConf + "&&LP_VideoConf=" + LP_VideoConf;
                } else {
                    url += "&&Video_Conf=0&&LI_VideoConf=0&&LP_VideoConf=0";
                }
                if (cb_WiFi_Internet.isChecked()) {
                    WiFi_Internet = 1;
                    LI_Wifi = ((int) list_importance_Wifi.getSelectedItemId() + 1);
                    LP_WIFI = ((int) listPriority_WIFI.getSelectedItemId() + 1);
                    url += "&&WiFi_Internet=1&&LI_Wifi=" + LI_Wifi + "&&LP_WIFI=" + LP_WIFI;
                } else {
                    url += "&&WiFi_Internet=0&&LI_Wifi=0&&LP_WIFI=0";
                }
                if (cb_Scanner_Printer.isChecked()) {
                    Scanner_Printer = 1;
                    LI_ScannerPrinter = ((int) list_importance_ScannerPrinter.getSelectedItemId() + 1);
                    LP_ScannerPrinter = ((int) listPriority_ScannerPrinter.getSelectedItemId() + 1);
                    url += "&&Scanner_Printer=1&&LI_ScannerPrinter=" + LI_ScannerPrinter + "&&LP_ScannerPrinter=" + LP_ScannerPrinter;
                } else {
                    url += "&&Scanner_Printer=0&&LI_ScannerPrinter=0&&LP_ScannerPrinter=0";
                }
                if (cb_Computer.isChecked()) {
                    Computer = 1;
                    LI_Computer = ((int) list_importance_Computer.getSelectedItemId() + 1);
                    LP_Computer = ((int) listPriority_Computer.getSelectedItemId() + 1);
                    url += "&&Computer=1&&LI_Computer=" + LI_Computer + "&&LP_Computer=" + LP_Computer;
                } else {
                    url += "&&Computer=0&&LI_Computer=0&&LP_Computer=0";
                }
                if (cb_Receptionist.isChecked()) {
                    Receptionist = 1;
                    LI_Receptionist = ((int) list_importance_Receptionist.getSelectedItemId() + 1);
                    LP_Receptionist = ((int) listPriority_Receptionist.getSelectedItemId() + 1);
                    url += "&&Receptionist=1&&LI_Receptionist=" + LI_Receptionist + "&&LP_Receptionist=" + LP_Receptionist;
                } else {
                    url += "&&Receptionist=0&&LI_Receptionist=0&&LP_Receptionist=0";
                }
                if (cb_Board_Room.isChecked()) {
                    Board_Room = 1;
                    LI_BoardRoom = ((int) list_importance_BoardRoom.getSelectedItemId() + 1);
                    LP_BoardRoom = ((int) listPriority_BoardRoom.getSelectedItemId() + 1);
                    url += "&&Board_Room=1&&LI_BoardRoom=" + LI_BoardRoom + "&&LP_BoardRoom=" + LP_BoardRoom;
                } else {
                    url += "&&Board_Room=0&&LI_BoardRoom=0&&LP_BoardRoom=0";
                }
                if (cb_Messenger.isChecked()) {
                    Messenger = 1;
                    LI_Messenger = ((int) list_importance_Messenger.getSelectedItemId() + 1);
                    LP_Messenger = ((int) listPriority_Messenger.getSelectedItemId() + 1);
                    url += "&&Messenger=1&&LI_Messenger=" + LI_Messenger + "&&LP_Messenger=" + LP_Messenger;
                } else {
                    url += "&&Messenger=0&&LI_Messenger=0&&LP_Messenger=0";
                }
                if (cb_Underground_Parking.isChecked()) {
                    Underground_Parking = 1;
                    LI_UndergroudParking = ((int) list_importance_UndergroudParking.getSelectedItemId() + 1);
                    LP_UndergroundParking = ((int) listPriority_UndergroundParking.getSelectedItemId() + 1);
                    url += "&&Underground_Parking=1&&LI_UndergroudParking=" + LI_UndergroudParking + "&&LP_UndergroundParking=" + LP_UndergroundParking;
                } else {
                    url += "&&Underground_Parking=0&&LI_UndergroudParking=0&&LP_UndergroundParking=0";
                }
                if (cb_Hot_Shower.isChecked()) {
                    Hot_Shower = 1;
                    LI_HotShower = ((int) list_importance_HotShower.getSelectedItemId() + 1);
                    LP_HotShower = ((int) listPriority_HotShower.getSelectedItemId() + 1);
                    url += "&&Hot_Shower=1&&LI_HotShower=" + LI_HotShower + "&&LP_HotShower=" + LP_HotShower;
                } else {
                    url += "&&Hot_Shower=0&&LI_HotShower=0&&LP_HotShower=0";
                }
                if (cb_Lounge.isChecked()) {
                    Lounge = 1;
                    LI_Lounge = ((int) list_importance_Lounge.getSelectedItemId() + 1);
                    LP_Lounge = ((int) listPriority_Lounge.getSelectedItemId() + 1);
                    url += "&&Lounge=1&&LI_Lounge=" + LI_Lounge + "&&LP_Lounge=" + LP_Lounge;
                } else {
                    url += "&&Lounge=0&&LI_Lounge=0&&LP_Lounge=0";
                }
                if (cb_Private_work_space.isChecked()) {
                    Private_work_space = 1;
                    LI_PrivateWorkSpace = ((int) list_importance_PrivateWorkSpace.getSelectedItemId() + 1);
                    LP_PrivateWorkspace = ((int) listPriority_PrivateWorkspace.getSelectedItemId() + 1);
                    url += "&&Private_work_space=1&&LI_PrivateWorkSpace=" + LI_PrivateWorkSpace + "&&LP_PrivateWorkspace=" + LP_PrivateWorkspace;
                } else {
                    url += "&&Private_work_space=0&&LI_PrivateWorkSpace=0&&LP_PrivateWorkspace=0";
                }
                if (cb_Shared_work_space.isChecked()) {
                    Shared_work_space = 1;
                    LI_SharedWorkspace = ((int) list_importance_SharedWorkspace.getSelectedItemId() + 1);
                    LP_SharedWorkspace = ((int) listPriority_SharedWorkspace.getSelectedItemId() + 1);
                    url += "&&Shared_work_space=1&&LI_SharedWorkspace=" + LI_SharedWorkspace + "&&LP_SharedWorkspace=" + LP_SharedWorkspace;
                } else {
                    url += "&&Shared_work_space=0&&LI_SharedWorkspace=0&&LP_SharedWorkspace=0";
                }
                if (cb_Cost_Hourly_Rate.isChecked()) {
                    Cost_Hourly_Rate = 1;
                    LI_CostHourly = ((int) list_importance_CostHourly.getSelectedItemId() + 1);
                    LP_CostHourly = ((int) listPriority_CostHourly.getSelectedItemId() + 1);
                    if (hourlyRate.getSelectedItemId() <= 5) {
                        String selectedHourly = hourlyRate.getSelectedItem().toString().trim();
                        String ratesData[] = selectedHourly.split("-");
                        minCost = Integer.parseInt(ratesData[0]);
                        maxCost = Integer.parseInt(ratesData[1]);
                    } else {
                        minCost = 501;
                        maxCost = 10000000;
                    }
                    url += "&&Cost_Hourly_Rate=1&&LI_CostHourly=" + LI_CostHourly + "&&LP_CostHourly=" + LP_CostHourly + "&&minCost=" + minCost + "&&maxCost=" + maxCost;
                } else {
                    url += "&&Cost_Hourly_Rate=0&&LI_CostHourly=0&&LP_CostHourly=0&&minCost=0&&maxCost=0";
                }

                dialog.cancel();
                if (internetConnectionsAvailable() == true) {
                    // fetchData();
                    displayLocation();
                    //getMyLocation();
                    CreateMap();
                    Log.d("my url : ", url);
                } else {
//                  no internet connections hence cant fetch offices from the server
                }
            }
        });
        dialog.show();
    }

    //Returns true if the connection to the server are made successfully

    private boolean internetConnectionsAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void disabler() {
        list_importance_HighSpeed.setEnabled(false);
        list_importance_HighSpeed.setClickable(false);
        listPriority_HighSpeed.setEnabled(false);
        listPriority_HighSpeed.setClickable(false);

        list_importance_VideoConf.setEnabled(false);
        list_importance_VideoConf.setClickable(false);
        listPriority_VideoConf.setEnabled(false);
        listPriority_VideoConf.setClickable(false);

        list_importance_Wifi.setEnabled(false);
        list_importance_Wifi.setClickable(false);
        listPriority_WIFI.setEnabled(false);
        listPriority_WIFI.setClickable(false);

        list_importance_ScannerPrinter.setEnabled(false);
        list_importance_ScannerPrinter.setClickable(false);
        listPriority_ScannerPrinter.setEnabled(false);
        listPriority_ScannerPrinter.setClickable(false);

        list_importance_Computer.setEnabled(false);
        list_importance_Computer.setClickable(false);
        listPriority_Computer.setEnabled(false);
        listPriority_Computer.setClickable(false);

        list_importance_Receptionist.setEnabled(false);
        list_importance_Receptionist.setClickable(false);
        listPriority_Receptionist.setEnabled(false);
        listPriority_Receptionist.setClickable(false);

        list_importance_BoardRoom.setEnabled(false);
        list_importance_BoardRoom.setClickable(false);
        listPriority_BoardRoom.setEnabled(false);
        listPriority_BoardRoom.setClickable(false);

        list_importance_Messenger.setEnabled(false);
        list_importance_Messenger.setClickable(false);
        listPriority_Messenger.setEnabled(false);
        listPriority_Messenger.setClickable(false);

        list_importance_UndergroudParking.setEnabled(false);
        list_importance_UndergroudParking.setClickable(false);
        listPriority_UndergroundParking.setEnabled(false);
        listPriority_UndergroundParking.setClickable(false);

        list_importance_HotShower.setEnabled(false);
        list_importance_HotShower.setClickable(false);
        listPriority_HotShower.setEnabled(false);
        listPriority_HotShower.setClickable(false);

        list_importance_Lounge.setEnabled(false);
        list_importance_Lounge.setClickable(false);
        listPriority_Lounge.setEnabled(false);
        listPriority_Lounge.setClickable(false);

        list_importance_PrivateWorkSpace.setEnabled(false);
        list_importance_PrivateWorkSpace.setClickable(false);
        listPriority_PrivateWorkspace.setEnabled(false);
        listPriority_PrivateWorkspace.setClickable(false);

        list_importance_SharedWorkspace.setEnabled(false);
        list_importance_SharedWorkspace.setClickable(false);
        listPriority_SharedWorkspace.setEnabled(false);
        listPriority_SharedWorkspace.setClickable(false);

        list_importance_CostHourly.setEnabled(false);
        list_importance_CostHourly.setClickable(false);
        listPriority_CostHourly.setEnabled(false);
        listPriority_CostHourly.setClickable(false);

        hourlyRate.setEnabled(false);
        hourlyRate.setClickable(false);


    }

    private void getMyLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);

        if (provider != null && !provider.equals("")) {
            Log.d("provider is : ", provider);
            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);

            //locationManager.requestSingleUpdate(provider, this, null);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                Log.d("provider is : ", location.toString());

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//                locationManager.requestLocationUpdates(provider, 20000, 1, this);

                if (location != null) {
                    onLocationChanged(location);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
            }
        } else {
//            Toast.makeText(getApplicationContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }
        if (Build.VERSION.SDK_INT == 23) {
            int hasWriteContactsPermission = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocation = this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            if (hasCoarseLocation != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        }
        enableGPS();
    }

    private void enableGPS() {
        if (!isGPSEnabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS settings");
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();

        }
    }

    public void CreateMap() {
        mMap.clear();
        progressDialog = new ProgressDialog(Map.this);
        progressDialog.setMessage("Loading Offices ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        radius = 1000000;
        url += "&&latitude=" + latitude + "&&radius=" + radius + "&&longitude=" + longitude + "&&totalElements=" + currentPosition;

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("trial : ", " entered here");
                mMap.clear();
                try {
//            response = new JSONObject(jsonString);
                    allOffices = response.getJSONArray("offices");
                    mMarkersHashMap = new HashMap<Marker, MyMarker>();
                    mMyMarkersArray.clear();
                    for (int i = 0; i < allOffices.length(); i++) {
                        JSONObject s = allOffices.getJSONObject(i);


                        double latitude = s.getDouble("latitude");
                        double longitudes = s.getDouble("longitude");
                        final String id = s.getString("id");
                        final String provider = s.getString("provider");
                        final String address1 = s.getString("address1");
                        final String address2 = s.getString("address2");
                        final String address3 = s.getString("address3");
                        String available_services = s.getString("available_services");
                        String missing_services = s.getString("missing_services");
                        int sum_weight = ((int) (s.getDouble("sum_weight") * 100));
                        int distanceMetres = s.getInt("distanceMetres");

                        latitude_longitude = new LatLng(latitude, longitudes);

                        mMyMarkersArray.add(new MyMarker(latitude, longitudes, id, provider, address1, address2, address3, available_services, missing_services, sum_weight, distanceMetres));

                    }
                    Log.d("mMyMarkersArray size : ", "" + mMyMarkersArray.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                plotMarkers(mMyMarkersArray);
                currentLocation = new LatLng(latitude, longitude);

                mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
                mMap.addCircle(new CircleOptions()
                        .center(currentLocation)
                        .radius(1000).radius(4000)
                        .strokeColor(Color.DKGRAY));
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        return false;


                    }
                });
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.getMessage());
            }
        });
        queue.add(request);

        Log.d("end of loading codes : ", "ended well");


//      new markers===========================

        //                    code to draw route here ..........................
        MarkerOptions options = new MarkerOptions();
        options.position(LOWER_MANHATTAN);
        options.position(BROOKLYN_BRIDGE);
        options.position(WALL_STREET);
        try {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        } catch (Exception e) {
            Log.d("error on click : ", e.getMessage());
        }
        mMap.addMarker(options);


    }

    private void plotMarkers(ArrayList<MyMarker> markers) {
        Log.d("end of loading codes : ", "plotted");
        if (markers.size() > 0) {
            for (MyMarker myMarker : markers) {

                Log.d("well : ", "looped");
                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getMlatitude(), myMarker.getMlongitudes()));
                try {
                    markerOption.icon(BitmapDescriptorFactory.defaultMarker(manageMarkerIcon(myMarker.getMsum_weight())));
                } catch (Exception e) {
                    Log.d("error on click : ", e.getMessage());
                }
                Marker currentMarker = mMap.addMarker(markerOption);
                mMarkersHashMap.put(currentMarker, myMarker);

                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
            progressDialog.dismiss();
        }
    }

    private float manageMarkerIcon(int sum_weight) {
        if (sum_weight >= 80) {
            return BitmapDescriptorFactory.HUE_GREEN;
        } else if (sum_weight >= 50 && sum_weight < 80) {
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else {
            return BitmapDescriptorFactory.HUE_RED;
        }
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        public MarkerInfoWindowAdapter() {
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v = getLayoutInflater().inflate(R.layout.marker_info, null);

            final MyMarker myMarker = mMarkersHashMap.get(marker);

            TextView mapnearby_officename = (TextView) v.findViewById(R.id.mapnearby_officename);
            TextView mapnearby_distance = (TextView) v.findViewById(R.id.mapnearby_distance);
            TextView mapnearby_address1 = (TextView) v.findViewById(R.id.mapnearby_address1);
            TextView mapnearby_MarksScored = (TextView) v.findViewById(R.id.mapnearby_MarksScored);
            TextView mapnearby_AvailableServices = (TextView) v.findViewById(R.id.mapnearby_AvailableServices);
            TextView mapnearby_MissingServices = (TextView) v.findViewById(R.id.mapnearby_MissingServices);
            mapnearby_MissingServices.setTextColor(Color.RED);
            mapnearby_AvailableServices.setTextColor(Color.GREEN);

            TextView available_label = (TextView) v.findViewById(R.id.mapnearby_AvailableServicesTitle);
            TextView missing_label = (TextView) v.findViewById(R.id.mapnearby_MissingServicesTitle);

            try {
//    addHeatMap();

                available_label.setText("Available services : ");
                available_label.setTextColor(Color.BLACK);
                missing_label.setText("Missing services : ");
                missing_label.setTextColor(Color.BLACK);

                mapnearby_officename.setText("Provider : " + myMarker.getMprovider());
                mapnearby_distance.setText("You are : " + myMarker.getMdistanceMetres() + " Metres away.");
                mapnearby_address1.setText("Street : " + myMarker.getMaddress1());
                mapnearby_MarksScored.setText("Weighted Marks : " + myMarker.getMsum_weight() + "%");
                mapnearby_AvailableServices.setText(myMarker.getMavailable_services());
                mapnearby_MissingServices.setText(myMarker.getMmissing_services());

//    to draw route------------------
                Log.d("Data location : ", "latitude : " + latitude + " longitude : " + longitude + " marker latitude : " + myMarker.getMlatitude() + " marker longitude : " + myMarker.getMlongitudes());
                String url = getMapsApiDirectionsUrl(latitude, longitude, myMarker.getMlatitude(), myMarker.getMlongitudes());
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url);
            } catch (Exception e) {
                Log.d("error on click 2 : ", e.getMessage());
            }

            return v;
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;
            if (polyline != null) {
                polyline.remove();
            }
            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                polyLineOptions.addAll(points);
                polyLineOptions.width(20);
//                polyLineOptions.color(Color.rgb(2, 4, 8));
                polyLineOptions.color(Color.MAGENTA);
            }

            polyline = mMap.addPolyline(polyLineOptions);
        }
    }

    private String getMapsApiDirectionsUrl(double sourceLat, double sourceLong, double destinationLat, double destinationLong) {
        String url = "http://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + sourceLat + "," + sourceLong
                + "&destination=" + destinationLat + "," + destinationLong
                + "&sensor=false&units=metric&mode=driving";
        Log.d("url", url);
        return url;
    }

    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();


        } else {
            enableGPS();
            Log.d("Location error", "(Couldn't get the location. Make sure location is enabled on the device)");
        }


    if(Build.VERSION.SDK_INT==23)

    {
        int hasWriteContactsPermission = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocation = this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }
        if (hasCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }
    }
}
}
