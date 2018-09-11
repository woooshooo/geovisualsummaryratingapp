package com.thesis2.genise_villanueva.thesis;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
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
import com.wooplr.spotlight.SpotlightView;
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
    private SpotlightView spotlightViewCenter;
    private SpotlightView spotlightViewTop;
    PreferencesManager mPreferencesManager;
    // Offline objects
    private OfflineManager offlineManager;
    private OfflineRegion offlineRegion;
    private ProgressBar progressBar;
    private boolean isEndNotified;
    // JSON encoding/decoding
    public static final String JSON_CHARSET = "UTF-8";
    public static final String JSON_FIELD_REGION_NAME = "Davao";
    //MARKERS
    List<MarkerOptions> markerOptionsList = new ArrayList<>();
    MarkerOptions markerOptions = new MarkerOptions();
    List<Marker> markerList = new ArrayList<>();
    List<Marker> toptenList = new ArrayList<>();
    Marker newMarker = new Marker(markerOptions);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetsController = new AssetsController(MainActivity.this);
        FirebaseController firebaseController = new FirebaseController(MainActivity.this, savedInstanceState);
        firebaseController.initializeDB();
        //This is the preferences for the instructions.
        mPreferencesManager = new PreferencesManager(MainActivity.this);
        mPreferencesManager.resetAll();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(7.41170, 125.90793)) // Northeast
                .include(new LatLng(6.77990, 125.17280)) // Southwest
                .build();

        //Display Screen width and height
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;

        //TEST ON ADDING REVIEWS
        int x = 0;
        if (x == 0) {
//           firebaseController.writeDatastoFirebase();
//            firebaseController.writeReviewstoFirebase();
//            firebaseController.removeDATAfromFirebase();
            x++;
        }
//        Mapbox.getInstance(this, String.valueOf(R.string.access_token));

        Mapbox.getInstance(this, "pk.eyJ1Ijoid2tiZ2VuaXNlIiwiYSI6ImNqampyMnF0ejBpMTAzd3BiemY0aTQ1dHUifQ.Y27Yy0ndTZSlEsDuNhpcuw");
        setContentView(R.layout.activity_main);
        pieChart = findViewById(R.id.pieChart);
        pieChart.setTransparentCircleRadius(10);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterTextSize(14);
        pieChart.setDrawCenterText(true);
        pieChart.setTransparentCircleRadius(50);
        pieChart.setCenterTextColor(Color.LTGRAY);
        pieChart.setEntryLabelColor(Color.LTGRAY);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setMinimumWidth(500);
        pieChart.setMinimumHeight(500);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(70);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        mapView = findViewById(R.id.mapView);
        btn_center = findViewById(R.id.btn_center);
        btn_top = findViewById(R.id.btn_top);
        tvSubjectivity = findViewById(R.id.tvSubjectivity);
        subjectivityBar = findViewById(R.id.subjectivityBar);
        reviewList = findViewById(R.id.reviewList);
        progressBar = findViewById(R.id.progressBar);

        mapView.onCreate(savedInstanceState);

        Mapbox.setConnected(true);
        if (Mapbox.isConnected()) {
            Log.d(TAG, "onCreate: isConnected to Mapbox");
            mapView.getMapAsync(new OnMapReadyCallback() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onMapReady(MapboxMap mapboxMap) {
                    mapboxMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MainActivity.this));
                    mapboxMap.setLatLngBoundsForCameraTarget(latLngBounds);
                    // Set up the offlineManager
                    offlineManager = OfflineManager.getInstance(MainActivity.this);
                    // OfflineManager
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            // Create offline definition using the current
                            // style and boundaries of visible map area
                            String styleUrl = mapboxMap.getStyleUrl();
                            // Create a bounding box for the offline region

                            double minZoom = mapboxMap.getCameraPosition().zoom;
                            double maxZoom = mapboxMap.getMaxZoomLevel();
                            float pixelRatio = MainActivity.this.getResources().getDisplayMetrics().density;
                            OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                                    styleUrl, latLngBounds, minZoom, maxZoom, pixelRatio);

                            // Customize the download notification's appearance
//                            NotificationOptions notificationOptions = NotificationOptions.builder(MainActivity.this)
//                                    .smallIconRes(R.drawable.mapbox_logo_icon)
//                                    .returnActivity(MainActivity.class.getName())
//                                    .build();

                            // Start downloading the map tiles for offline use
//                            OfflinePlugin.getInstance(MainActivity.this).startDownload(
//                                    OfflineDownloadOptions.builder()
//                                            .definition(definition)
//                                            .metadata(OfflineUtils.convertRegionName("Davao"))
//                                            .notificationOptions(notificationOptions)
//                                            .build()
//                            );

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
                                Log.e(TAG, "Failed to encode metadata: " + exception.getMessage());
                                metadata = null;
                            }

                            // Create the offline region and launch the download
                            offlineManager.createOfflineRegion(definition, metadata, new OfflineManager.CreateOfflineRegionCallback() {
                                @Override
                                public void onCreate(OfflineRegion offlineRegion) {
                                    Log.d(TAG, "Offline region created: " + "Davao");
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
                                                // Download complete
                                                endProgress("Offline download Complete");
                                            } else if (status.isRequiredResourceCountPrecise()) {
                                                // Switch to determinate state
                                                setPercentage((int) Math.round(percentage));
                                            }
                                        }

                                        @Override
                                        public void onError(OfflineRegionError error) {
                                            // If an error occurs, print to logcat
                                            Log.e(TAG, "onError reason: " + error.getReason());
                                            Log.e(TAG, "onError message: " + error.getMessage());
                                        }

                                        @Override
                                        public void mapboxTileCountLimitExceeded(long limit) {
                                            // Notify if offline region exceeds maximum tile count
                                            Log.e(TAG, "Mapbox tile count limit exceeded: " + limit);
                                        }
                                    });
                                }

                                @Override
                                public void onError(String error) {
                                    Log.e(TAG, "Error: " + error);
                                }
                            });
                        }
                    });
                    offlineManager.listOfflineRegions(new OfflineManager.ListOfflineRegionsCallback() {
                        @Override
                        public void onList(OfflineRegion[] offlineRegions) {
                            // Check result. If no regions have been
                            // downloaded yet, notify user and return
                            if (offlineRegions == null || offlineRegions.length == 0) {

                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });

                    //Tutorial for Center and Recommended Places
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            SpotlightConfig spotlightConfig = new SpotlightConfig();
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
                            SpotlightSequence.getInstance(MainActivity.this, spotlightConfig)
                                    .addSpotlight(btn_center, "Back to Center", "Press this button if you wish\nto go back the center", "centerbtn_id")
                                    .addSpotlight(btn_top, "Recommended Places", "Press this button if you wish\nto view only the recommended places.", "topbtn_id")
                                    .startSequence();
                        }
                    });


                    // Customize map with markers, polylines, etc.
                    // Create an Icon object for the marker to use
                    IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                    Icon darkIcon = iconFactory.fromResource(R.mipmap.green_pin_marker_dark);
                    Icon normalIcon = iconFactory.fromResource(R.mipmap.green_pin_marker);
                    Icon lightIcon = iconFactory.fromResource(R.mipmap.green_pin_marker_light);
                    Icon neutralIcon = iconFactory.fromResource(R.mipmap.neutral_marker);

                    // center button
                    btn_center.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LatLng center = new LatLng(7.0910885, 125.6112563);
                            mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(center), 1000);

                        }
                    });

                    // top ten button
                    btn_top.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (!topTenPressed) {
//                                Toast.makeText(MainActivity.this, "Show Top Ten: "+ topTenPressed, Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "On Top Button Click: " + topTenPressed);
                                topTenPressed = true;
                                for (int x = 0; markerList.size() > x; x++) {
                                    mapboxMap.removeMarker(markerList.get(x));
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
                                                int negative = sentimentInfo.getNegative();
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
                                for (int x = 0; markerList.size() > x; x++) {
                                    mapboxMap.removeMarker(markerList.get(x));
                                }
                                markerList.clear();
//                                Toast.makeText(MainActivity.this, "Hide Top Ten: "+ topTenPressed, Toast.LENGTH_SHORT).show();
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
                                                if (neutral > positive) {
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
                        }
                    });

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
                                    if (neutral > positive) {
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
                                    markerList.add(newMarker);

                                }
                            }
                            LatLng center = new LatLng(7.0910885, 125.6112563);
                            mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(center), 1000);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Timber.e(String.valueOf(databaseError));
                        }
                    });

                    mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            pieChart.animateXY(1000, 1000, Easing.EasingOption.EaseOutCirc, Easing.EasingOption.EaseOutCirc);
                            mapboxMap.easeCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 1000);
                            List<PieEntry> entries = new ArrayList<>();
                            entries.clear();
                            //get data from firebase
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            Query query = reference.child("data").orderByChild("location");
                            query.addValueEventListener(new ValueEventListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            Data data = ds.getValue(Data.class);
                                            assert data != null;
                                            String location = data.getLocation();
                                            SentimentInfo sentimentInfo = data.getSentimentInfo();
                                            int reviewCount = sentimentInfo.getReviewcount();
                                            double subjectivityScore = sentimentInfo.getSubjectivityscoreaverage();
                                            int positive = sentimentInfo.getPositive();
                                            int negative = sentimentInfo.getNegative();
                                            int neutral = sentimentInfo.getNeutral();

                                            if (location.equals(marker.getTitle())) {
                                                entries.add(new PieEntry(positive, "Positive"));
                                                entries.add(new PieEntry(neutral, "Neutral"));
                                                entries.add(new PieEntry(negative, "Negative"));
                                                int subjectivityScoreBar = (int) Math.round(subjectivityScore * 100);
//                                                pieChart.setCenterText(reviewCount + " Reviews");
                                                pieChart.setCenterText("Percentage of Reviews");
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                    subjectivityBar.setProgress(subjectivityScoreBar, true);
                                                }
                                                tvSubjectivity.setText("Subjectivity: " + subjectivityScoreBar + "%");
                                            }
                                            PieDataSet pieDataSet = new PieDataSet(entries, "Reviews");
                                            PieData pieData = new PieData(pieDataSet);
                                            Highlight h = new Highlight(0, 0, 0); // dataset index for piechart is always 0
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
                        }
                    });

                }
            });
        } else {
            Timber.e("onCreate: !isConnect to Mapbox");
        }


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
        new Handler().postDelayed(() -> {
            doubleBackToExitPressedOnce = false;
        }, 2000);
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
    private void endProgress(final String message) {
        // Don't notify more than once
        if (isEndNotified) {
            return;
        }
        // Stop and hide the progress bar
        isEndNotified = true;
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.INVISIBLE);
        // Show a toast
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

}
