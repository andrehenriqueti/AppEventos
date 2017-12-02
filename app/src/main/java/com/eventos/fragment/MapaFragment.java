package com.eventos.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.eventos.R;
import com.eventos.helper.LocalizacaoUsuario;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by ANDRE on 19/11/2017.
 */

public class MapaFragment extends Fragment implements OnMapReadyCallback{

    private View view;
    private Button buttonPesquisarEndereco;
    private GoogleMap mMap;
    private MapView mapView;

    public MapaFragment(){

    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmento_mapa, container,false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.getMapAsync(this);
        buttonPesquisarEndereco = (Button) view.findViewById(R.id.pesquisar_eventos);
        buttonPesquisarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalizacaoUsuario localizacaoUsuario = new LocalizacaoUsuario(getActivity());
                if(localizacaoUsuario.localizacaoDisponivel()){
                    double lat = localizacaoUsuario.getLatitude();
                    double lon = localizacaoUsuario.getLongitude();

                    Toast.makeText(getActivity(),"Sua localização "+lat+" "+lon,Toast.LENGTH_LONG).show();
                }
                else {
                    localizacaoUsuario.showSettingsAlert();
                }
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
