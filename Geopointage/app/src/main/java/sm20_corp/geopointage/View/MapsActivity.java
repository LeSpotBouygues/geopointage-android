package sm20_corp.geopointage.View;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sm20_corp.geopointage.Api.ApiManager;
import sm20_corp.geopointage.Api.ModelApi.ChantierList;
import sm20_corp.geopointage.Api.ModelApi.Message;
import sm20_corp.geopointage.Model.Chantier;
import sm20_corp.geopointage.Module.PermissionUtils;
import sm20_corp.geopointage.R;

import static sm20_corp.geopointage.R.id.map;

public class MapsActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        PlaceSelectionListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMarkerClickListener {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final String TAG = "MapsActivity";

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    private ImageView arrowBackButton;
    private ImageView doneButton;
    private GoogleMap mMap;
    private Marker mMarker;
    private Marker mMarkerOnClick;
    private FloatingActionButton addChantier;
    private ImageView micId;
    private GoogleApiClient mGoogleApiClient;
    private CoordinatorLayout coordinatorLayout;
    private Chantier mChantier;
    private ArrayList<Chantier> mArrayListChantier = new ArrayList<>();
    private String mIotp;
    private PlaceAutocompleteFragment mAutocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getChantier();

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        arrowBackButton = (ImageView) findViewById(R.id.activity_maps_imageview_arrow_back);
        arrowBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        doneButton = (ImageView) findViewById(R.id.activity_maps_imageview_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMarkerOnClick != null && mMarkerOnClick.isInfoWindowShown() && mMarkerOnClick.getSnippet() != null) {
                    Intent i = new Intent(MapsActivity.this, MainActivity.class);
                    mChantier = new Chantier(mMarkerOnClick.getTitle(), mMarkerOnClick.getSnippet());
                    i.putExtra("chantier_extra", mChantier);
                    startActivity(i);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.no_chantier_select), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);


        // Retrieve the PlaceAutocompleteFragment.
        mAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.activity_maps_fragment_place_autocomplete);

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        mAutocompleteFragment.setOnPlaceSelectedListener(this);

        addChantier = (FloatingActionButton) findViewById(R.id.activity_maps_fab_add);
        addChantier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mMarker != null) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.iotp));
                    startActivityForResult(intent, 1);
                    System.out.println("clicker");
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.no_chantier_select), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(MapsActivity.this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        micId = (ImageView) findViewById(R.id.activity_maps_imageview_mic_address);
        micId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.address));
                startActivityForResult(intent, 2);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("requestCode = " + requestCode + "   resultcode = " + resultCode);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            final AlertDialog dialog;
            // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getSupportActionBar().getThemedContext());
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapsActivity.this);
            //alertDialogBuilder.setMessage(message);
            LayoutInflater inflater = getLayoutInflater();
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            final View dialogView = inflater.inflate(R.layout.dialog, null);
            alertDialogBuilder.setView(dialogView);
            alertDialogBuilder.setTitle(getString(R.string.new_chantier));
            final TextView adress = (TextView) dialogView.findViewById(R.id.dialog_textview_adress);

            adress.setText(mMarker.getTitle());
            final EditText iotp = (EditText) dialogView.findViewById(R.id.dialog_edittext_iotp);
            String tmp = matches.get(0);
            tmp = tmp.replace(" ", "");
            iotp.setText(tmp);
            alertDialogBuilder.setPositiveButton(getString(R.string.valider), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    String mIotp = iotp.getText().toString();
                    if (!mIotp.isEmpty()) {
                        mChantier = new Chantier(mMarker.getTitle(), mIotp);
                        sendChantier(mChantier);
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, getString(R.string.no_iotp_select), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }

            });
            alertDialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }

            });
            dialog = alertDialogBuilder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                }
            });
            dialog.show();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            mAutocompleteFragment.setText(matches.get(0));
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                .zzlf(matches.get(0))
                                .build(this);

                startActivityForResult(intent, 3);
            } catch (GooglePlayServicesRepairableException e) {
                // TODO: Handle the error.
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }
            super.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            onPlaceSelected(place);
        } else if (requestCode == 1 && resultCode == RESULT_CANCELED) {
            final AlertDialog dialog;
            // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getSupportActionBar().getThemedContext());
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MapsActivity.this);
            //alertDialogBuilder.setMessage(message);
            LayoutInflater inflater = getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            final View dialogView = inflater.inflate(R.layout.dialog, null);
            alertDialogBuilder.setView(dialogView);
            alertDialogBuilder.setTitle(getString(R.string.new_chantier));
            final TextView adress = (TextView) dialogView.findViewById(R.id.dialog_textview_adress);


            adress.setText(mMarker.getTitle());
            final EditText iotp = (EditText) dialogView.findViewById(R.id.dialog_edittext_iotp);

            alertDialogBuilder.setPositiveButton(getString(R.string.valider), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    String mIotp = iotp.getText().toString();
                    if (!mIotp.isEmpty()) {
                        mChantier = new Chantier(mMarker.getTitle(), mIotp);
                        sendChantier(mChantier);
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(coordinatorLayout, getString(R.string.no_iotp_select), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }

            });

            alertDialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }

            });

            dialog = alertDialogBuilder.create();
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                }
            });
            dialog.show();
            super.onActivityResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
        enableMyLocation();
        LatLng paris = new LatLng(48.866667, 2.333333);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paris, 10));

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(false);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }


    }


    @Override
    public void onMapClick(LatLng point) {

    }

    @Override
    public void onMapLongClick(LatLng point) {
        String address = "";
        try {
            Geocoder geo = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());
            List<Address> addressesList = geo.getFromLocation(point.latitude, point.longitude, 1);
            if (addressesList.isEmpty()) {
                System.out.println("wait");
            } else {
                if (addressesList.size() > 0) {
                    address = addressesList.get(0).getAddressLine(0) + ", " + addressesList.get(0).getAddressLine(1);
                    for (int i = 0; i < addressesList.get(0).getMaxAddressLineIndex(); i++) {
                        System.out.println(" i = " + i + " " + addressesList.get(0).getAddressLine(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }

        if (mMarker != null)
            mMarker.remove();
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(point)
                .title(address)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMarkerOnClick = marker;

        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        System.out.println("hey start");

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        System.out.println("hey drag");

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        System.out.println("hey end");
        String address = "rien";
        try {
            Geocoder geo = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());
            List<Address> addressesList = geo.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
            if (addressesList.isEmpty()) {
                System.out.println("wait");

            } else {
                if (addressesList.size() > 0) {
                    //adress = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();

                    address = addressesList.get(0).getAddressLine(0) + ", " + addressesList.get(0).getAddressLine(1);

                    for (int i = 0; i < addressesList.get(0).getMaxAddressLineIndex(); i++) {

                        System.out.println(" i = " + i + " " + addressesList.get(0).getAddressLine(i));

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
        marker.setTitle(address);
        marker.showInfoWindow();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return true;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (location != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(18)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            if (mMarker != null)
                mMarker.remove();

            mMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title(getAddress(location.getLatitude(), location.getLongitude()))
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }

        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "Place Selected: " + place.getName());
        String tmp;
        String tmp2;

        tmp = formatPlaceDetails(getResources(), place.getName(), place.getId(),
                place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri()).toString();

        System.out.println("tmp =" + tmp);

        LatLng autoCompleteLatLng = place.getLatLng();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(autoCompleteLatLng, 15));
        if (mMarker != null)
            mMarker.remove();

        mMarker = mMap.addMarker(new MarkerOptions()
                .position(autoCompleteLatLng)
                .title(place.getAddress().toString())
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));


        CharSequence attributions = place.getAttributions();
        if (!TextUtils.isEmpty(attributions)) {

            //    mPlaceAttribution.setText(Html.fromHtml(attributions.toString()));
            tmp2 = Html.fromHtml(attributions.toString()).toString();
            System.out.println("tmp2 =" + tmp2);

        } else {
            //mPlaceAttribution.setText("");
            System.out.println("rien");
        }
    }

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {
        Log.e(TAG, "onError: Status = " + status.toString());

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        //Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private String getAddress(double lat, double lng) {
        String address = "";
        try {
            Geocoder geo = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());
            List<Address> addressesList = geo.getFromLocation(lat, lng, 1);
            if (addressesList.isEmpty()) {
                System.out.println("wait");
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "wait", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                if (addressesList.size() > 0) {
                    address = addressesList.get(0).getAddressLine(0) + ", " + addressesList.get(0).getAddressLine(1);
                    for (int i = 0; i < addressesList.get(0).getMaxAddressLineIndex(); i++) {
                        System.out.println(" i = " + i + " " + addressesList.get(0).getAddressLine(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
        return address;
    }

    private void getChantier() {
        ApiManager.get().getSite().enqueue(new Callback<ChantierList>() {
            @Override
            public void onResponse(Call<ChantierList> call, Response<ChantierList> response) {
                System.out.println("code = " + response.raw().toString());

                if (response.isSuccessful()) {
                    System.out.println("chantier = " + response.body().getChantier().get(0).getAddress());
                    mArrayListChantier = response.body().getChantier();
                    addMarkerArrayListChantier();
                } else {
                    System.out.println("not sucess getChantier = " + response.code());
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.error_get_chantier), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<ChantierList> call, Throwable t) {
                System.out.println("On failure getChantier : " + t.getMessage());
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.error_network), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void addMarkerArrayListChantier() {
        int i = 0;
        Marker marker;
        while (i < mArrayListChantier.size()) {
            marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mArrayListChantier.get(i).getLat(), mArrayListChantier.get(i).getLng()))
                    .title(getAddress(mArrayListChantier.get(i).getLat(), mArrayListChantier.get(i).getLng()))
                    .snippet(mArrayListChantier.get(i).getIotp())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            i++;
        }
    }

    private void sendChantier(Chantier chantier) {
        ApiManager.get().sendSite(chantier.getAddress(), chantier.getIotp()).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                System.out.println("code = " + response.raw().toString());

                if (response.isSuccessful()) {
                    System.out.println("sendChantier = " + response.body().toString());
                    Intent i = new Intent(MapsActivity.this, MainActivity.class);
                    i.putExtra("chantier_extra", mChantier);
                    i.putExtra("message", getString(R.string.send_user_ok));
                    startActivity(i);
                } else {
                    System.out.println("not sucess sendChantier = " + response.code());
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.error_send_Chantier), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                System.out.println("On failure sendChantier : " + t.getMessage());
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, getString(R.string.error_network), Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

}