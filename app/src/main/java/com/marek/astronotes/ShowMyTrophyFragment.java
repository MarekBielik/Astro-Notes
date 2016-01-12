package com.marek.astronotes;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.marek.astronotes.entity.MessierObject;
import com.marek.astronotes.service.MessierObjectManager;

public class ShowMyTrophyFragment extends ShowCurrentObjectFragment {

    Bundle args;

    public static ShowMyTrophyFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(CURRENT_OBJECT_POSITION, position);
        ShowMyTrophyFragment fragment = new ShowMyTrophyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_show_my_trophy, container, false);

        //get messier to show
        args = getArguments();
        messierObject = messierObjectManager.getMyTrophy(args.getInt(CURRENT_OBJECT_POSITION));

        ((TextView) rootView.findViewById(R.id.currentMessierTitleTextView)).
                setText("M " + messierObject.getMessierNumber());
        ((TextView) rootView.findViewById(R.id.ngcNumberTextView)).
                setText(messierObject.getNgcNumber());
        ((TextView) rootView.findViewById(R.id.typeOfMessierTextView)).
                setText(messierObject.getType());
        ((TextView) rootView.findViewById(R.id.constellationTextView)).
                setText(messierObject.getConstellation());
        ((TextView) rootView.findViewById(R.id.apparentMagnitudeTextView)).
                setText(String.valueOf(messierObject.getApparentMagnitude()));
        ((TextView) rootView.findViewById(R.id.noteTextView)).
                setText(String.valueOf(messierObject.getNote()));

        LoadImage loadImage = new LoadImage();
        loadImage.execute();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //refresh object and note
        messierObject = messierObjectManager.getMyTrophy(args.getInt(CURRENT_OBJECT_POSITION));
        ((TextView) rootView.findViewById(R.id.noteTextView)).
                setText(String.valueOf(messierObject.getNote()));
    }
}
