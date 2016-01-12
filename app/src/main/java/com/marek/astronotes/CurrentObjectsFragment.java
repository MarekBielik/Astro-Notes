package com.marek.astronotes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.marek.astronotes.entity.MessierObject;
import com.marek.astronotes.service.MessierObjectManager;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Marek on 10/25/2015.
 */
public class CurrentObjectsFragment extends ListFragment {

    public static final String ITEM_POSITION = "com.fazula.marek.ITEM_POSITION";

    private MessierObjectManager messierObjectManager = MessierObjectManager.getInstance(getActivity());
    private CustomListAdapter listAdapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_objects_fragment, container, false);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);

        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        CreateList createList = new CreateList();
                        createList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
        );
        return view;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        CreateList createList = new CreateList();
        createList.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (listAdapter != null)
            listAdapter = new CustomListAdapter(getActivity(), R.layout.current_object_item_layout,
                    MessierObjectManager.getCurrentMessierObjects());
        setListAdapter(listAdapter);
    }

    private class CustomListAdapter extends ArrayAdapter {

        //TODO: replace with normal view
        TextView messierNumber;
        List<MessierObject> messiers;
        ImageView inMyTrophiesImageView;

        public CustomListAdapter(Context context, int resource, List<MessierObject> items) {
            super(context, resource, items);
            messiers = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.current_object_item_layout, parent, false);
            }

            inMyTrophiesImageView = (ImageView) convertView.findViewById(R.id.inMyTrophiesImageView);
            inMyTrophiesImageView.setVisibility(View.GONE);

            //decide whether display imageView or not
            try {
                if (MessierObjectManager.getInstance(getActivity()).isInMyTrophies(messiers.get(position)))
                    inMyTrophiesImageView.setVisibility(View.VISIBLE);
            } catch (SQLException e) {
                Toast.makeText(getActivity(), R.string.DATABASE_PROBLEM, Toast.LENGTH_SHORT).show();
            }
            messierNumber = (TextView) convertView.findViewById(R.id.itemTextView);
            messierNumber.setText(messiers.get(position).getMessierString());
            return convertView;
        }
    }

    public static CurrentObjectsFragment newInstance() {
        CurrentObjectsFragment fragment = new CurrentObjectsFragment();
        return fragment;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), ShowCurrentObjectActivity.class);
        intent.putExtra(ITEM_POSITION, position);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
    }

    private class CreateList extends AsyncTask<Void, Void, List<MessierObject>> {
        @Override
        protected List<MessierObject> doInBackground(Void... params) {
            MessierObjectManager messierObjectManager = MessierObjectManager.getInstance(getActivity());
            try{
                messierObjectManager.refreshCurrentMessierObjects();
            } catch (IOException | JSONException e) {
                return null;
            }
            return MessierObjectManager.getCurrentMessierObjects();
        }

        @Override
        protected void onPostExecute(List<MessierObject> messiers) {
            if(messiers != null) {
                listAdapter = new CustomListAdapter(getActivity(), R.layout.current_object_item_layout,
                        messiers);
                setListAdapter(listAdapter);
                refreshLayout.setRefreshing(false);
            }
            else
                Toast.makeText(getActivity(), R.string.CURRENT_MESSIERS_NETWORK_PROBLEM,
                        Toast.LENGTH_SHORT).show();
        }
    }
}