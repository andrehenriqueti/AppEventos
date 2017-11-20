package com.eventos.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventos.R;

/**
 * Created by ANDRE on 19/11/2017.
 */

public class MapaFragment extends Fragment {

    private View view;

    public MapaFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmento_mapa, container,false);
        return view;
    }
}
