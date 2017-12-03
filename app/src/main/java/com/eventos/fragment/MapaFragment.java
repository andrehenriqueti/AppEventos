package com.eventos.fragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eventos.R;
import com.eventos.app.AppConfig;
import com.eventos.app.AppController;
import com.eventos.bean.UsuarioBean;
import com.eventos.controller.Controller;
import com.eventos.helper.DatabaseHelper;
import com.eventos.helper.Itens;
import com.eventos.helper.LocalizacaoUsuario;
import com.eventos.helper.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ANDRE on 19/11/2017.
 */

public class MapaFragment extends Fragment implements OnMapReadyCallback,GoogleMap.InfoWindowAdapter,GoogleMap.OnMarkerClickListener {

    private View view;
    private FloatingActionButton buttonPesquisarEndereco;
    private GoogleMap mMap;
    private MapView mMapView;
    private String idMarkerUsuario;
    private ClusterManager<Itens> clusterManager;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;

    public MapaFragment(){

    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mapa, container,false);
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
                    mMap.clear();
                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location_black_24dp)));
                    idMarkerUsuario = marker.getId();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                    String cidadeUsuario = buscaCidadeUsuario(latLng);
                    UsuarioBean usuarioBean = new UsuarioBean();
                    usuarioBean.setCidade(cidadeUsuario);
                    recebeEventos(usuarioBean);
                }
                else{
                    localizacaoUsuario.showSettingsAlert();
                }
            }
        });
        setupToolbar(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity());
        sessionManager = new SessionManager(getActivity());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);
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

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    public String buscaCidadeUsuario(LatLng latLng){
        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity());
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                address = addresses.get(0);
            }
        }
        catch (IOException e){
            Log.e("IOException",e.getMessage());
        }
        return address.getSubAdminArea();
    }

    @Override
    public View getInfoContents(Marker marker) {
        if(!marker.getId().equals(idMarkerUsuario)) {
            View viewMarker = View.inflate(getActivity(), R.layout.info_windows, null);
            TextView dia = (TextView) viewMarker.findViewById(R.id.dia_inicio_evento);
            TextView mes = (TextView) viewMarker.findViewById(R.id.mes_inicio_evento);
            TextView nome = (TextView) viewMarker.findViewById(R.id.exibi_nome_evento);
            TextView horario = (TextView) viewMarker.findViewById(R.id.horario);
            TextView descricao = (TextView) viewMarker.findViewById(R.id.descricao);
            dia.setText(marker.getSnippet().substring(0, 2));
            String[] mesAbreviado = {"JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"};
            mes.setText(mesAbreviado[(Integer.parseInt(marker.getSnippet().substring(3, 5))) - 1]);
            nome.setText(marker.getTitle());
            horario.setText(marker.getSnippet().substring(0, marker.getSnippet().lastIndexOf('D')));
            descricao.setText(marker.getSnippet().substring(marker.getSnippet().lastIndexOf('D'), marker.getSnippet().length() - 1));
            return viewMarker;
        }
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    public void recebeEventos(final UsuarioBean usuarioBean){
        //String utilizada para cancelar a requisição
        String tag_req = "req_lista_eventos_cidade";
        progressDialog.setMessage("Listando Eventos em sua cidade ...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.URL_EXIBE_EVENTOS_CIDADE, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Log.d("Response:", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getActivity(),"Alguns eventos encontrados", Toast.LENGTH_SHORT).show();
                        JSONArray jsonArrayEventos = jsonObject.getJSONArray("eventos");
                        Controller controller = new Controller(getActivity());
                        Log.i("jsonArrayEventos",jsonArrayEventos.toString());
                        List<Map<String,Object>> eventos = controller.criaListaEventos(jsonArrayEventos);
                        Log.i("eventos",eventos.toString());
                        inseriEventosMapa(eventos);
                        hideDialog();
                    } else {
                        hideDialog();
                        String mensagemErro = jsonObject.getString("error_msg");
                        Toast.makeText(getActivity(), mensagemErro, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    hideDialog();
                    Toast.makeText(getActivity(),"Erro ao se conectar", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ao listar: ",error.toString());
                hideDialog();
                Toast.makeText(getActivity(),"Erro ao se conectar, verifique sua conexão com a internet!",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //Pasando os parametros pelo metodo POST
                Map<String, String> parametros =  new HashMap<>();
                parametros.put("cidade",usuarioBean.getCidade());
                parametros.put("email",sessionManager.getEmailLogado());
                parametros.put("senha",sessionManager.getSenhaLogada());
                return parametros;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest,tag_req);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void inseriEventosMapa(List<Map<String,Object>> evento){
        clusterManager = new ClusterManager<>(getActivity(),mMap);
        for (int i=0;i<evento.size();i++){
            Map<String,Object> item = evento.get(i);
            String dataInicio = (String)item.get(DatabaseHelper.Evento.DATA_INICIAL);
            String dataFim = (String)item.get(DatabaseHelper.Evento.DATA_FINAL);
            String horario = "";
            if(dataFim.substring(0,10).equals(dataInicio.substring(0,10))){
                horario = dataInicio.substring(8,10)+"/"+dataInicio.substring(5,7)+"/"+
                        dataInicio.substring(0,4)+"\n"+"Das "+dataInicio.substring(11,16)+" as "+
                        dataFim.substring(11,16)+"\n";
            }
            else{
                horario = dataInicio.substring(8,10)+"/"+dataInicio.substring(5,7)+"/"+
                        dataInicio.substring(0,4)+"-"+dataFim.substring(8,10)+"/"+
                        dataFim.substring(5,7)+"/"+ dataFim.substring(0,4)+"\n"+"Das "+dataInicio.substring(11,16)+" as "+
                        dataFim.substring(11,16)+"\n";
            }
            String descricao = "Descrição: \n"+item.get(DatabaseHelper.Evento.DESCRICAO)+"\n"+
                    ((int)item.get(DatabaseHelper.Evento.LOTACAO) == 0?"Não há lotação estabelecida":("Lotação de "+(int)item.get(DatabaseHelper.Evento.LOTACAO))+" pessoas\n")+
                    ((double)item.get(DatabaseHelper.Evento.VALOR_EVENTO) == 0.0?"":"Há um preço estabelecido de R$ "+(double)item.get(DatabaseHelper.Evento.VALOR_EVENTO));
            String snippet = horario+descricao;
            mMap.addMarker(new MarkerOptions().position(new LatLng((double)item.get(DatabaseHelper.Evento.LATITUDE),
                    (double)item.get(DatabaseHelper.Evento.LONGITUDE))).title((String) item.get(DatabaseHelper.Evento.NOME)).snippet(snippet));

        }
    }
}
