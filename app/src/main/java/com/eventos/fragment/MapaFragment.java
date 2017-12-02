package com.eventos.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.eventos.R;
import com.eventos.helper.LocalizacaoUsuario;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.eventos.R.id.nome;

/**
 * Created by ANDRE on 19/11/2017.
 */

public class MapaFragment extends Fragment implements OnMapReadyCallback{

    private View view;
    private FloatingActionButton buttonPesquisarEndereco;
    private GoogleMap mMap;
    private MapView mMapView;

    public MapaFragment(){

    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmento_mapa, container,false);
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
        buttonPesquisarEndereco = (FloatingActionButton) view.findViewById(R.id.pesquisar_eventos);
        buttonPesquisarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalizacaoUsuario localizacaoUsuario = new LocalizacaoUsuario(getActivity());
                if(localizacaoUsuario.localizacaoDisponivel()) {
                    double lat = localizacaoUsuario.getLatitude();
                    double lon = localizacaoUsuario.getLongitude();
                    LatLng latLng = new LatLng(lat, lon);
                    Toast.makeText(getActivity(), "Sua localização " + lat + " " + lon, Toast.LENGTH_LONG).show();
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Sua Localização"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(19));
                }
            }
        });
        setupToolbar(view);
        return view;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void setupToolbar(View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        final ActionBar bar = appCompatActivity.getSupportActionBar();
        if(bar != null){
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setShowHideAnimationEnabled(true);
            bar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            bar.setTitle("Eventos Próximos");
        }
    }
}
