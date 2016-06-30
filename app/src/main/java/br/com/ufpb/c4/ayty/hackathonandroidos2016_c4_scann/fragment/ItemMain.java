package br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.R;

/**
 * Created by raphaelsalviano on 29/06/16.
 */
public class ItemMain extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_container, container, false);

        Bundle bundle = getArguments();

        ((ImageView) view.findViewById(R.id.image_item)).setImageResource(bundle.getInt("img"));
        ((TextView) view.findViewById(R.id.text_item)).setText(bundle.getString("desc"));

        return view;
    }
}
