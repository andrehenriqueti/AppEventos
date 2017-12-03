package com.eventos.helper;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by ANDRE on 03/12/2017.
 */

public class Itens implements ClusterItem {
    private LatLng posicao;
    private String titulo;
    private String descricao ;

    public Itens(double lat, double lng){
        posicao = new LatLng(lat,lng);
    }

    public Itens(double lat, double lng, String title, String snippet) {
        posicao = new LatLng(lat, lng);
        titulo = title;
        descricao = snippet;
    }

    @Override
    public LatLng getPosition() {
        return posicao;
    }

    @Override
    public String getTitle() {
        return titulo;
    }

    @Override
    public String getSnippet() {
        return descricao;
    }
}
