package com.thesis2.genise_villanueva.thesis;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MapView mapView;
    private PieChart pieChart;
    private FloatingActionButton btn_center;
    private TextView tvSubjectivity;
    private ProgressBar subjectivityBar;
    private Mapbox mapBox;
    private AssetsController assetsController;
    private FirebaseController firebaseController;
    private ListView reviewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        assetsController = new AssetsController(MainActivity.this);
        firebaseController = new FirebaseController(MainActivity.this, savedInstanceState);
        firebaseController.initializeDB();
        //TEST ON ADDING REVIEWS
        int x = 0;
        if (x != 0) {
            firebaseController.writeReviews();
            x++;
        }
        firebaseController.viewData();

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
        tvSubjectivity = findViewById(R.id.tvSubjectivity);
        subjectivityBar = findViewById(R.id.subjectivityBar);
        reviewList = findViewById(R.id.reviewList);


//        // Setup the data source
//        ArrayList<Reviews> reviewsArrayList = new ArrayList<>();
//        reviewsArrayList.add(new Reviews("id","user","location","review"));
//
//        // instantiate the custom list adapter
//        CustomListAdapter adapter = new CustomListAdapter(this, reviewsArrayList);
//
//        // get the ListView and attach the adapter
//        ListView itemsListView = findViewById(R.id.reviewList);
//        itemsListView.setAdapter(adapter);

        mapView.onCreate(savedInstanceState);

        if (Mapbox.isConnected()) {
            Log.d(TAG, "onCreate: isConnected to Mapbox");
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(MapboxMap mapboxMap) {
                    mapboxMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(MainActivity.this));
                    // Customize map with markers, polylines, etc.
                    // Create an Icon object for the marker to use
                    IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                    Icon darkIcon = iconFactory.fromResource(R.mipmap.green_pin_marker_dark);
                    Icon normalIcon = iconFactory.fromResource(R.mipmap.green_pin_marker);
                    Icon lightIcon = iconFactory.fromResource(R.mipmap.green_pin_marker_light);

                    // center button
                    btn_center.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LatLng center = new LatLng(7.0910885, 125.6112563);
                            mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(center), 1000);
                        }
                    });

                    //Adding Markers
                    try {
                        JSONArray jsonArray;
                        JSONObject jsonObject;
                        JSONArray jsonArrayData;
                        JSONObject jsonObjectData;
                        jsonArray = new JSONArray(assetsController.loadCoordinatesFromAsset());
                        jsonObject = new JSONObject();
                        jsonArrayData = new JSONArray(assetsController.loadDataFromAsset());
                        jsonObjectData = new JSONObject();
                        MarkerOptions markerOptions = new MarkerOptions();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            jsonObjectData = jsonArrayData.getJSONObject(i);
                            String loc = jsonObject.getString("Location");
                            String location = jsonObjectData.getString("Name");
                            int reviewCount = jsonObjectData.getInt("ReviewCount");
                            int positive = jsonObjectData.getInt("Positive");
                            int negative = jsonObjectData.getInt("Negative");
                            double perc = Math.round((((double) positive) / reviewCount) * 100);
                            if (loc.equals(location)) {
//                                Timber.d("loc is equal to location");
                                if (positive > negative) {
//                                    Log.d(TAG, "positive > negative");
                                    markerOptions.icon(lightIcon);
                                    if (perc > 90) {
                                        markerOptions.icon(darkIcon);
                                    } else if (perc > 70 && perc < 80) {
                                        markerOptions.icon(normalIcon);
                                    } else if (perc > 50 && perc < 71) {
                                        markerOptions.icon(lightIcon);
                                    } else {
                                        Timber.d("perc below 50");
                                    }
                                } else {
                                    Timber.d("negative greater than positive");
                                }
                            } else {
                                Timber.e("loc IS NOT equal to location");
                            }
                            markerOptions.title(loc);
                            double lat = jsonObject.getDouble("Lat");
                            double lng = jsonObject.getDouble("Lng");
                            LatLng newLatLng = new LatLng(lat, lng);
                            markerOptions.position(newLatLng);
                            mapboxMap.addMarker(markerOptions);

                        }
                        LatLng center = new LatLng(7.0910885, 125.6112563);
                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(center), 1000);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            pieChart.animateXY(1000, 1000, Easing.EasingOption.EaseOutCirc, Easing.EasingOption.EaseOutCirc);
                            mapboxMap.easeCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 1000);
//                            mapboxMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), 1000);
                            List<PieEntry> entries = new ArrayList<>();

                            JSONArray jsonArrayData;
                            JSONObject jsonObjectData;
                            try {
                                jsonArrayData = new JSONArray(assetsController.loadDataFromAsset());
                                jsonObjectData = new JSONObject();
                                for (int x = 0; x < jsonArrayData.length(); x++) {
                                    jsonObjectData = jsonArrayData.getJSONObject(x);
                                    String location = jsonObjectData.getString("Name");
                                    int reviewCount = jsonObjectData.getInt("ReviewCount");
                                    double subjectivityScore = jsonObjectData.getDouble("SubjectivityScoreAverage");
                                    int positive = jsonObjectData.getInt("Positive");
                                    int positiveGTAvg = jsonObjectData.getInt("PositiveGTAvg");
                                    int positiveLTAvg = jsonObjectData.getInt("PositiveLTAvg");
                                    int negative = jsonObjectData.getInt("Negative");
                                    int negativeGTAvg = jsonObjectData.getInt("NegativeGTAvg");
                                    int negativeLTAvg = jsonObjectData.getInt("NegativeLTAvg");
                                    int neutral = jsonObjectData.getInt("Neutral");
                                    int neutralGTAvg = jsonObjectData.getInt("NeutralGTAvg");
                                    int neutralLTAvg = jsonObjectData.getInt("NeutralLTAvg");


                                    if (location.equals(marker.getTitle())) {
                                        entries.add(new PieEntry(positive, "Positive"));
                                        entries.add(new PieEntry(neutral, "Neutral"));
                                        entries.add(new PieEntry(negative, "Negative"));
                                        int subjectivityScoreBar = (int) Math.round(subjectivityScore * 100);
                                        pieChart.setCenterText(reviewCount + " Reviews");
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            subjectivityBar.setProgress(subjectivityScoreBar, true);
                                        }
                                        tvSubjectivity.setText("Subjectivity: " + subjectivityScoreBar + "%");
                                    }
                                    PieDataSet dataSet = new PieDataSet(entries, "Reviews");
                                    PieData data = new PieData(dataSet);
                                    Highlight h = new Highlight(0, 0, 0); // dataset index for piechart is always 0
                                    pieChart.highlightValues(new Highlight[]{h});
                                    pieChart.getDescription().setEnabled(false);
                                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                    dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                                    dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                                    data.setValueTextColor(Color.LTGRAY);
                                    dataSet.setValueLineColor(Color.LTGRAY);
                                    dataSet.setValueLinePart1OffsetPercentage(80.f);
                                    dataSet.setValueLinePart1Length(0.2f);
                                    dataSet.setValueLinePart2Length(0.4f);
                                    pieChart.getLegend().setTextColor(Color.LTGRAY);
                                    pieChart.setData(data);
                                    pieChart.invalidate();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    });

                }
            });
        } else {
            Log.e(TAG, "onCreate: !isConnect to Mapbox");
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


}
