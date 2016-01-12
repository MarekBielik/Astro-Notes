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
public class MyTrophiesFragment extends ListFragment {

    public static final String ITEM_POSITION = "com.fazula.marek.ITEM_POSITION";

    private CustomListAdapter listAdapter;
    MessierObjectManager messierObjectManager = MessierObjectManager.getInstance(getActivity());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_objects_fragment, container, false);

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //refresh layout is not needed in this list
                        refreshLayout.setRefreshing(false);
                    }
                }
        );
        return view;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        CreateList createList = new CreateList();
        createList.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        //refresh the list- only if the adapter already exists -> set a new one
        if(listAdapter != null)
            listAdapter = new CustomListAdapter(getActivity(),
                    R.layout.current_object_item_layout,
                    messierObjectManager.getMyTrophiesStringArray());
            setListAdapter(listAdapter);
    }

    private class CustomListAdapter extends ArrayAdapter {

        //TODO: replace with normal view
        TextView messierNumber;
        List<String> messiers;

        public CustomListAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
            messiers = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.current_object_item_layout, parent, false);
            }

            messierNumber = (TextView) convertView.findViewById(R.id.itemTextView);
            messierNumber.setText(messiers.get(position));
            return convertView;
        }
    }

    public static MyTrophiesFragment newInstance() {
        MyTrophiesFragment fragment = new MyTrophiesFragment();
        return fragment;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), ShowMyTrophyActivity.class);
        intent.putExtra(ITEM_POSITION, position);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
    }

    private class CreateList extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... params) {
            MessierObjectManager messierObjectManager = MessierObjectManager.getInstance(getActivity());
            try{
                messierObjectManager.refreshMyTrophies();
            } catch (SQLException e) {
                return null;
            }
            return messierObjectManager.getMyTrophiesStringArray();
        }

        @Override
        protected void onPostExecute(List<String> messiers) {
            if(messiers != null) {
                listAdapter = new CustomListAdapter(getActivity(),
                        R.layout.current_object_item_layout, messiers);
                setListAdapter(listAdapter);
            }
            else
                Toast.makeText(getActivity(), R.string.DATABASE_PROBLEM,
                        Toast.LENGTH_SHORT).show();
        }
    }

}