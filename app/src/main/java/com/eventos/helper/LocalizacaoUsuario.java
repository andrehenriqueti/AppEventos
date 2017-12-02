package com.eventos.helper;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ANDRE on 01/12/2017.
 */

public class LocalizacaoUsuario extends Service implements LocationListener {

    private final Context context;

    boolean gpsLigado = false;
    boolean redeLigada = false;
    boolean localizacaoDisponivel = false;

    Location location;
    double latitude;
    double longitude;

    protected LocationManager locationManager;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    public LocalizacaoUsuario(Context context){
        this.context = context;
        getLocation();
    }

    public Location getLocation(){
        try{
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // VERIFICANDO STATUS GPS
            gpsLigado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // VERIFICANDO STATUS DA INTERNET
            redeLigada = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!gpsLigado && !redeLigada){
                Toast.makeText(context,"Não é possível encontar sua localização! ",Toast.LENGTH_LONG);
            }
            else {
                this.localizacaoDisponivel = true;

                if(redeLigada){
                    try{
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                        if(locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if(location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    catch (SecurityException e){
                        Log.e("SecurityException",e.getMessage());
                    }
                }

                if(gpsLigado){
                    if(location == null){
                        try {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                    this
                            );
                            if(locationManager != null){
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if(location != null){
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                        catch (SecurityException e){
                            Log.e("SecurityException",e.getMessage());
                        }
                    }
                }
            }
        }
        catch (Exception e){
            Log.e("Exception",e.getMessage());
        }
        return location;
    }
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    public void pararGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(LocalizacaoUsuario.this);
        }
    }
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean localizacaoDisponivel() {
        return this.localizacaoDisponivel;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);


        alertDialog.setTitle("Configuração de GPS");


        alertDialog.setMessage("GPS desligado. Você quer ir para o menu de configurações?");


        alertDialog.setPositiveButton("Configurações", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }
}
