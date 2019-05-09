package com.example.workit.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.workit.R;

public class HomeViewFragment extends Fragment {

    ListView listView;
    SearchView editSearch;
    String[] cityNameList;
    ArrayList<String> list;
    CityDataPassListener mCallback;
    String cityName;

    public interface CityDataPassListener{
        void passCityData(String cityData);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.main_fragment_view, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Locate the Views
        editSearch = view.findViewById(R.id.locationInput);
        listView = view.findViewById(R.id.locInputListView);

        // Generate sample data
        cityNameList = new String[] {"Lion", "Tiger", "Dog",
                "Cat", "Tortoise", "Rat", "Elephant", "Fox",
                "Cow","Donkey","Monkey"};

        editSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cityName = editSearch.getQuery().toString().trim();
                mCallback.passCityData(cityName);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        //ensures the host activity has implemented the callback interface
        super.onAttach(context);
        try {
            mCallback = (CityDataPassListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnSearchInputEnteredListener");
        }
    }
}
