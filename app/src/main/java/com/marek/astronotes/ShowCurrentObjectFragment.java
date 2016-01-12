package com.marek.astronotes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marek.astronotes.entity.MessierObject;
import com.marek.astronotes.service.MessierObjectManager;

import java.io.IOException;
import java.sql.SQLException;

public class ShowCurrentObjectFragment extends Fragment {
    public static final String CURRENT_OBJECT_POSITION = "com.marek.astronotes.CURRENT_OBJECT_POSITION";
    MessierObjectManager messierObjectManager = MessierObjectManager.getInstance(getActivity());
    MessierObject messierObject;
    View rootView;
    Button addMessierButton;
    ImageView messierAddedImageView;

    public ShowCurrentObjectFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.show_current_object_fragment, container, false);
        Bundle args = getArguments();
        messierObject = messierObjectManager.getCurrentObject(args.getInt(CURRENT_OBJECT_POSITION));

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
        addMessierButton =  (Button) rootView.findViewById(R.id.addMessierButton);
        messierAddedImageView = (ImageView) rootView.findViewById(R.id.messierAddedImageView);

        try {
            if (MessierObjectManager.getInstance(getActivity()).isInMyTrophies(messierObject)) {
                addMessierButton.setVisibility(View.GONE);
            } else {
                messierAddedImageView.setVisibility(View.GONE);
            }
        } catch (SQLException e) {
            Toast.makeText(getActivity(), R.string.DATABASE_PROBLEM,Toast.LENGTH_SHORT).show();
        }

        addMessierButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ObjectAnimator animator;

                animator = ObjectAnimator.ofFloat(addMessierButton, View.ALPHA, 1f, 0f);
                animator.setDuration(800); //ms
                animator.start();
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        addMessierButton.setVisibility(View.GONE);
                        messierAddedImageView.setVisibility(View.VISIBLE);
                    }
                });
                try {
                    MessierObjectManager.getInstance(getActivity()).addToMyTrophies(messierObject);
                } catch (SQLException e) {
                    Toast.makeText(getActivity(), R.string.DATABASE_PROBLEM,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        LoadImage loadImage = new LoadImage();
        loadImage.execute();

        return rootView;
    }

    public static ShowCurrentObjectFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(CURRENT_OBJECT_POSITION, position);
        ShowCurrentObjectFragment fragment = new ShowCurrentObjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    protected class LoadImage extends AsyncTask<Void, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap image = null;
            try {
                image = messierObjectManager.getMessierImage(messierObject);
            } catch (IOException e) {
                Toast.makeText(getActivity(), R.string.NETWORK_PROBLEM_IMAGE, Toast.LENGTH_SHORT).show();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap image) {
            ((ImageView) rootView.findViewById(R.id.currentMessierImageView)).
                    setImageBitmap(image);
             rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }
}

