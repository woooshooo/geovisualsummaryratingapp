package com.thesis2.genise_villanueva.thesis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.wooplr.spotlight.SpotlightConfig;
import com.wooplr.spotlight.prefs.PreferencesManager;
import com.wooplr.spotlight.utils.SpotlightSequence;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MapView mapView;
    private PieChart pieChart;
    private FloatingActionButton btn_center;
    private FloatingActionButton btn_top;
    private TextView tvSubjectivity;
    private ProgressBar subjectivityBar;
    private Mapbox mapBox;
    private AssetsController assetsController;
    private ListView reviewList;
    private boolean doubleBackToExitPressedOnce = false;
    private boolean topTenPressed = false;
    PreferencesManager mPreferencesManager;
    // Offline objects
    private OfflineManager offlineManager;
    protected OfflineRegion offlineRegion;
    private ProgressBar progressBar;
    private boolean isEndNotified;
    // JSON encoding/decoding
    public static final String JSON_CHARSET = "UTF-8";
    public static final String JSON_FIELD_REGION_NAME = "Davao";
    // Create List for Markers & Marker Options
    List<MarkerOptions> markerOptionsList = new ArrayList<>();
    MarkerOptions markerOptions = new MarkerOptions();
    List<Marker> markerList = new ArrayList<>();
    Marker newMarker = new Marker(markerOptions);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initialize AssetsController
        assetsController = new AssetsController(MainActivity.this);
        Log.d(TAG, "onCreate: AssetsController Initialized");
        //Initialize FirebaseController
        FirebaseController firebaseController = new FirebaseController(MainActivity.this, savedInstanceState);
        firebaseController.initializeDB();
        Log.d(TAG, "onCreate: FirebaseController Initialized");
        //Set the preferences manager to resetAll for the instructions to open every app open
        mPreferencesManager = new PreferencesManager(MainActivity.this);
        mPreferencesManager.resetAll();
        Log.d(TAG, "onCreate: PreferenceManager Reset Complete");
        //Call Mapbox API
        Mapbox.getInstance(this, "pk.eyJ1Ijoid2tiZ2VuaXNlIiwiYSI6ImNqampyMnF0ejBpMTAzd3BiemY0aTQ1dHUifQ.Y27Yy0ndTZSlEsDuNhpcuw");
        //Set Content View the Activity Layout
        setContentView(R.layout.activity_main);
        //Initialize pieChart
        pieChart = findViewById(R.id.pieChart);
        //Handler for Setting pieChart settings
        new Handler().post(() -> {
            pieChart.setTransparentCircleRadius(10);
            pieChart.setUsePercentValues(true);
            pieChart.setCenterTextSize(10);
            pieChart.setDrawCenterText(true);
            pieChart.setTransparentCircleRadius(50);
            pieChart.setCenterTextColor(Color.LTGRAY);
            pieChart.setEntryLabelColor(Color.LTGRAY);
            pieChart.setHoleColor(Color.TRANSPARENT);
//            pieChart.setMinimumWidth(500);
//            pieChart.setMinimumHeight(500);
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleRadius(70);
            pieChart.setRotationAngle(0);
            pieChart.setRotationEnabled(true);
            Log.d(TAG, "onCreate: pieChart Settings complete");
        });
        

        //Initialize MainActivity Objects
        mapView = findViewById(R.id.mapView);
        btn_center = (FloatingActionButton) findViewById(R.id.btn_center);
        btn_top = (FloatingActionButton) findViewById(R.id.btn_top);
        tvSubjectivity = findViewById(R.id.tvSubjectivity);
        subjectivityBar = findViewById(R.id.subjectivityBar);
        reviewList = findViewById(R.id.reviewList);
        progressBar = findViewById(R.id.progressBar);


        //Set mapView on Create with savedInstanceState as the passed value
        mapView.onCreate(savedInstanceState);

        //Setup Tutorial for Center and Recommended Places
        new Handler().post(() -> {
            //Initialize SpotlightConfig
            SpotlightConfig spotlightConfig = new SpotlightConfig();
            //Set settings for the interface design of the spotlight
            new Handler().post(() -> {
                spotlightConfig.setIntroAnimationDuration(400);
                spotlightConfig.setRevealAnimationEnabled(true);
                spotlightConfig.setPerformClick(true);
                spotlightConfig.setFadingTextDuration(400);
                spotlightConfig.setHeadingTvColor(Color.parseColor("#ada8a8"));
                spotlightConfig.setHeadingTvSize(32);
                spotlightConfig.setSubHeadingTvColor(Color.parseColor("#ffffff"));
                spotlightConfig.setSubHeadingTvSize(16);
                spotlightConfig.setMaskColor(Color.parseColor("#dc000000"));
                spotlightConfig.setLineAnimationDuration(400);
                spotlightConfig.setLineAndArcColor(Color.parseColor("#807989"));
                spotlightConfig.setDismissOnTouch(true);
                spotlightConfig.setDismissOnBackpress(true);
                spotlightConfig.setShowTargetArc(true);
                spotlightConfig.setRevealAnimationEnabled(true);
                //Set up & Start SpotlightSequence
                SpotlightSequence.getInstance(MainActivity.this, spotlightConfig)
                        .addSpotlight(btn_center, "Back to Center", "Press this button if you wish\nto go back the center", "centerbtn_id")
                        .addSpotlight(btn_top, "Recommended Places", "Press this button if you wish\nto view only the recommended places.", "topbtn_id")
                        .startSequence();
            });
        });

        //Create Mapbox Map Asynchronously
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                Timber.tag(TAG).d("onMapReader: isConnected");
                //Set InfoWindowAdapter to the CustomInfoWindowAdapter
                mapboxMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MainActivity.this));
                //Set Lat Lng Boundary for Davao and Samal with appx 6000 tiles
                // that follows the MapBox Tile estimator for the free plan
                LatLngBounds latLngBounds = new LatLngBounds.Builder()
                        .include(new LatLng(7.41170, 125.90793)) // Northeast
                        .include(new LatLng(6.77990, 125.17280)) // Southwest
                        .build();
                mapboxMap.setLatLngBoundsForCameraTarget(latLngBounds);

                // On click listener for Center Button
                btn_center.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Log.d(TAG, "center button");
                            LatLng center = new LatLng(7.0910885, 125.6112563);
                            mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(center), 1000);
                            Timber.d("Center: center button clicked!");
                        } catch (Exception e) {
                            Log.e(TAG, "center button: error", e);
                        }
                    }
                });

                // Customize map with markers, polylines, etc.
                // Create an Icon object for the marker to use
                IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                Icon darkIcon = iconFactory.fromResource(R.drawable.green_pin_marker_dark);
                Icon normalIcon = iconFactory.fromResource(R.drawable.green_pin_marker);
                Icon lightIcon = iconFactory.fromResource(R.drawable.green_pin_marker_light);
                Icon neutralIcon = iconFactory.fromResource(R.drawable.neutral_marker);

                // On click listener for Recommended Places Button
                btn_top.setOnClickListener(view -> {
                    if (!topTenPressed) {
                        Timber.tag(TAG).d("On Top Button Click: %s", topTenPressed);
                        topTenPressed = true;
                        for (int x1 = 0; markerList.size() > x1; x1++) {
                            mapboxMap.removeMarker(markerList.get(x1)); //Deletes all markers
                        }
                        //adding top markers from firebase
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Query query = reference.child("data").orderByChild("location");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        Data data = ds.getValue(Data.class);
                                        assert data != null;
                                        String loc = data.getLocation();
                                        Coordinates coordinates = data.getCoordinates();
                                        double lat = coordinates.getLatitude();
                                        double lng = coordinates.getLongtitude();
                                        LatLng newLatLng = new LatLng(lat, lng);
                                        markerOptions.position(newLatLng);
                                        SentimentInfo sentimentInfo = data.getSentimentInfo();
                                        int reviewCount = sentimentInfo.getReviewcount();
                                        int positive = sentimentInfo.getPositive();
                                        double subjectivity = sentimentInfo.getSubjectivityscoreaverage();
                                        LocationInfo locationInfo = data.getLocationInfo();
                                        markerOptions.title(loc);
                                        markerOptionsList.add(markerOptions);
                                        if (reviewCount > 100
                                                && Double.parseDouble(locationInfo.getRating()) >= 4.0
                                                && subjectivity < .50
                                                && positive > 100) {
                                            mapboxMap.addMarker(markerOptions);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Timber.e(String.valueOf(databaseError));
                            }
                        });
                    } else {
                        for (int x1 = 0; markerList.size() > x1; x1++) {
                            mapboxMap.removeMarker(markerList.get(x1));
                        }
                        markerList.clear();
                        topTenPressed = false;
                        //adding markers from firebase
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Query query = reference.child("data").orderByChild("location");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        Data data = ds.getValue(Data.class);
                                        assert data != null;
                                        String loc = data.getLocation();
                                        Coordinates coordinates = data.getCoordinates();
                                        double lat = coordinates.getLatitude();
                                        double lng = coordinates.getLongtitude();
                                        LatLng newLatLng = new LatLng(lat, lng);
                                        markerOptions.position(newLatLng);
                                        SentimentInfo sentimentInfo = data.getSentimentInfo();
                                        int reviewCount = sentimentInfo.getReviewcount();
                                        int positive = sentimentInfo.getPositive();
                                        int negative = sentimentInfo.getNegative();
                                        int neutral = sentimentInfo.getNeutral();
                                        double perc = Math.round((((double) positive) / reviewCount) * 100);
                                        if (neutral > positive || (neutral == positive && positive == negative)) {
                                            markerOptions.icon(neutralIcon);
                                        } else if (positive > negative) {
                                            markerOptions.icon(darkIcon);
                                            if (perc >= 90) {
                                                markerOptions.icon(darkIcon);
                                            } else if (perc >= 70 && perc <= 80) {
                                                markerOptions.icon(normalIcon);
                                            } else if (perc > 50 && perc < 71) {
                                                markerOptions.icon(lightIcon);
                                            } else {
                                                Timber.d("perc below 50");
                                            }
                                        } else {
                                            Timber.d("negative greater than positive");
                                        }
                                        markerOptions.title(loc);
                                        markerOptionsList.add(markerOptions);
                                        newMarker = mapboxMap.addMarker(markerOptions);
                                        pieChart.invalidate();
                                        markerList.add(newMarker);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Timber.e(String.valueOf(databaseError));
                            }
                        });
                    }
                });

                //Adding Markers from Firebase
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
                Query query2 = reference2.child("data").orderByChild("location");
                //Query data from Firebase by initializing query to reference child to 'data' and ordered by child 'location' then query.addValueEventListener
                query2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Timber.tag(TAG).d("onDataChange: inside data");
                        if (dataSnapshot.exists()) {
                            Timber.tag(TAG).d("onDataChange: datasnapshot exists");
                            //For each dataSnapshot, initialized to Data POJO
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                Data data = ds.getValue(Data.class);
                                assert data != null;
                                String loc = data.getLocation();
                                //POJOs Coordinates  and SentimentInfo is set to their Getter function in Data POJO
                                //MarkerOptions are set using the POJOs of Coordinates  and SentimentInfo
                                Coordinates coordinates = data.getCoordinates();
                                double lat = coordinates.getLatitude();
                                double lng = coordinates.getLongtitude();
                                LatLng newLatLng = new LatLng(lat, lng);
                                markerOptions.position(newLatLng);
                                SentimentInfo sentimentInfo = data.getSentimentInfo();
                                int reviewCount = sentimentInfo.getReviewcount();
                                int positive = sentimentInfo.getPositive();
                                int negative = sentimentInfo.getNegative();
                                int neutral = sentimentInfo.getNeutral();
                                double perc = Math.round((((double) positive) / reviewCount) * 100);
                                if (neutral > positive || (neutral == positive && positive == negative)) {
                                    markerOptions.icon(neutralIcon);
                                } else if (positive > negative) {
                                    markerOptions.icon(darkIcon);
                                    if (perc >= 90) {
                                        markerOptions.icon(darkIcon);
                                    } else if (perc >= 70 && perc <= 80) {
                                        markerOptions.icon(normalIcon);
                                    } else if (perc > 50 && perc < 71) {
                                        markerOptions.icon(lightIcon);
                                    } else {
                                        Timber.d("perc below 50");
                                    }
                                } else {
                                    Timber.d("negative greater than positive");
                                }
                                markerOptions.title(loc);
                                markerOptionsList.add(markerOptions);
                                //MapboxMap Add Marker function called with markeroptions as parameter
                                newMarker = mapboxMap.addMarker(markerOptions);
                                pieChart.invalidate();
                                markerList.add(newMarker);

                            }
                        }
                        //Animate camera to center
                        LatLng center = new LatLng(7.0910885, 125.6112563);
                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(center), 1000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Timber.e("adding firebase markers error: " + String.valueOf(databaseError));
                    }
                });

                ConnectivityManager cm = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = null;
//                if (cm != null) {
//                    activeNetwork = cm.getActiveNetworkInfo();
//                }
//                boolean isConnectedtoNet = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//                boolean isWiFi = false;
//                if (activeNetwork != null) {
//                    isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
//                }
//                if (isConnectedtoNet || isWiFi) {
                    Timber.tag(TAG).d("onMapReady: Connected to the Internet");
                    // Set up the offlineManager
                    offlineManager = OfflineManager.getInstance(MainActivity.this);
                    // OfflineManager
                    new Handler().post(() -> {
                        // Create offline definition using the current
                        // style and boundaries of visible map area
                        String styleUrl = mapboxMap.getStyleUrl();

                        // Create a bounding box for the offline region
                        double minZoom = mapboxMap.getCameraPosition().zoom;
                        double maxZoom = mapboxMap.getMaxZoomLevel();
                        float pixelRatio = MainActivity.this.getResources().getDisplayMetrics().density;
                        OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                                styleUrl, latLngBounds, minZoom, maxZoom, pixelRatio);

                        // Build a JSONObject using the user-defined offline region title,
                        // convert it into string, and use it to create a metadata variable.
                        // The metadata variable will later be passed to createOfflineRegion()
                        byte[] metadata;
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(JSON_FIELD_REGION_NAME, "Davao");
                            String json = jsonObject.toString();
                            metadata = json.getBytes(JSON_CHARSET);
                        } catch (Exception exception) {
                            Timber.tag(TAG).e("Failed to encode metadata: %s", exception.getMessage());
                            metadata = null;
                        }

                        // Create the offline region and launch the download
                        assert metadata != null;
                        offlineManager.createOfflineRegion(definition, metadata, new OfflineManager.CreateOfflineRegionCallback() {
                            @Override
                            public void onCreate(OfflineRegion offlineRegion) {
                                Timber.tag(TAG).d("Offline region created: Davao");
                                MainActivity.this.offlineRegion = offlineRegion;
                                offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
                                // Display the download progress bar
                                progressBar = findViewById(R.id.progressBar);
                                startProgress();

                                // Monitor the download progress using setObserver
                                offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
                                    @Override
                                    public void onStatusChanged(OfflineRegionStatus status) {
                                        // Calculate the download percentage and update the progress bar
                                        double percentage = status.getRequiredResourceCount() >= 0
                                                ? (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                                                0.0;
                                        if (status.isComplete()) {
                                            // End Progress on Download complete
                                            endProgress();
                                        } else if (status.isRequiredResourceCountPrecise()) {
                                            // Switch to determinate state
                                            setPercentage((int) Math.round(percentage));
                                        }
                                    }

                                    @Override
                                    public void onError(OfflineRegionError error) {
                                        // If an error occurs, print to logcat
                                        Timber.tag(TAG).e("onError reason: %s", error.getReason());
                                        Timber.tag(TAG).e("onError message: %s", error.getMessage());
                                    }

                                    @Override
                                    public void mapboxTileCountLimitExceeded(long limit) {
                                        // Notify if offline region exceeds maximum tile count
                                        Timber.tag(TAG).e("Mapbox tile count limit exceeded: %s", limit);
                                    }
                                });
                            }

                            @Override
                            public void onError(String error) {
                                Timber.tag(TAG).e("Error: %s", error);
                            }
                        });
                    });
//                } else {
//                    Timber.tag(TAG).e("onMapReady: Not to the Internet");
//                }


                //Setting values to piechart on Marker Click from Firebase
                mapboxMap.setOnMarkerClickListener(marker -> {
                    pieChart.animateXY(1000, 1000, Easing.EasingOption.EaseOutCirc, Easing.EasingOption.EaseOutCirc);
                    mapboxMap.easeCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 1000);
                    List<PieEntry> entries = new ArrayList<>();
                    entries.clear();
                    //get data from firebase
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                    Query query1 = reference1.child("data").orderByChild("location");
                    query1.addValueEventListener(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Data data = ds.getValue(Data.class);
                                    assert data != null;
                                    String location = data.getLocation();
                                    SentimentInfo sentimentInfo = data.getSentimentInfo();
                                    double subjectivityScore = sentimentInfo.getSubjectivityscoreaverage();
                                    int positive = sentimentInfo.getPositive();
                                    int negative = sentimentInfo.getNegative();
                                    int neutral = sentimentInfo.getNeutral();

                                    //Add PieEntry Positive, Negative, and Neutral to Entries
                                    //if Data.getLocation is Equal to Marker Location
                                    if (location.equals(marker.getTitle())) {
                                        entries.add(new PieEntry(positive, "Positive"));
                                        entries.add(new PieEntry(neutral, "Neutral"));
                                        entries.add(new PieEntry(negative, "Negative"));
                                        int subjectivityScoreBar = (int) Math.round(subjectivityScore * 100);
                                        pieChart.setCenterText("Percentage of Reviews");
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            subjectivityBar.setProgress(subjectivityScoreBar, true);
                                        }
                                        tvSubjectivity.setText("Subjectivity: " + subjectivityScoreBar + "%");
                                    }
                                    PieDataSet pieDataSet = new PieDataSet(entries, "Reviews");
                                    PieData pieData = new PieData(pieDataSet);
                                    Highlight h = new Highlight(0, 0, 0); // dataset index for piechart is always 0
                                    //Set pieDataSet, pieData, highlight and Additional Piechart Settings
                                    pieData.setValueTextColor(Color.LTGRAY);
                                    pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                    pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                                    pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                                    pieDataSet.setValueLineColor(Color.LTGRAY);
                                    pieDataSet.setValueLinePart1OffsetPercentage(80.f);
                                    pieDataSet.setValueLinePart1Length(0.2f);
                                    pieDataSet.setValueLinePart2Length(0.4f);
                                    pieChart.highlightValues(new Highlight[]{h});
                                    pieChart.getDescription().setEnabled(false);
                                    pieChart.getLegend().setTextColor(Color.LTGRAY);
                                    pieChart.setData(pieData);
                                    pieChart.invalidate();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Timber.e(String.valueOf(databaseError));
                        }
                    });
                    //To avoid infowindow not showing up
                    //return false
                    return false;
                });

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
//        if (offlineManager != null) {
//            offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
//                @Override
//                public void onList(OfflineRegion[] offlineRegions) {
//                    if (offlineRegions.length > 0) {
//                        // delete the last item in the offlineRegions list which will be davao offline map
//                        offlineRegions[(offlineRegions.length - 1)].delete(new OfflineRegion.OfflineRegionDeleteCallback() {
//                            @Override
//                            public void onDelete() {
//                                Toast.makeText(MainActivity.this, "offline map deleted", Toast.LENGTH_LONG).show();
//                            }
//
//                            @Override
//                            public void onError(String error) {
//                                Log.e(TAG, "On Delete error: " + error);
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onError(String error) {
//                    Log.e(TAG, "onListError: " + error);
//                }
//            });
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    // Progress bar methods
    private void startProgress() {
        // Start and show the progress bar
        isEndNotified = false;
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }
    private void setPercentage(final int percentage) {
        progressBar.setIndeterminate(false);
        progressBar.setProgress(percentage);
    }
    private void endProgress() {
        // Don't notify more than once
        if (isEndNotified) {
            return;
        }
        // Stop and hide the progress bar
        isEndNotified = true;
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.INVISIBLE);
        // Show a toast
        Toast.makeText(MainActivity.this, "Offline download Complete", Toast.LENGTH_LONG).show();
    }

}
