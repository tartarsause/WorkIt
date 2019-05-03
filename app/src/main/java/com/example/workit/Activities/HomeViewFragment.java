package com.example.workit.Activities;

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

    public EditText mSearchField;
    ListView listView;
    //ListViewAdapter adapter;
    SearchView editsearch;
    String[] cityNameList;
    ArrayList<String> list;

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

//        ArrayList<CityNames> arraylist = new ArrayList<CityNames>();

        // Locate the Views
        editsearch = getActivity().findViewById(R.id.locationInput);
        listView = getActivity().findViewById(R.id.locInputListView);

        // Generate sample data
//        list = new ArrayList<>();
//        list.add("Singapore");
//        list.add("Kuala Lumpur");
//        list.add("Penang");
//        list.add("Jakarta");

        cityNameList = new String[] {"Lion", "Tiger", "Dog",
                "Cat", "Tortoise", "Rat", "Elephant", "Fox",
                "Cow","Donkey","Monkey"};


//        for (int i = 0; i < cityNameList.length; i++) {
//            CityNames cityNames = new CityNames(cityNameList[i]);
//            arraylist.add(cityNames);
//        }
//
//        //Pass results to ListViewAdapter Class
//        adapter = new ListViewAdapter(getContext(), arraylist);
//
//        //Bind adapter to ListView
//        listView.setAdapter(adapter);
//
//
//        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if (list.contains(query)) {
//                    adapter.getFilter().filter(query);
//                }
//                else {
//                    Toast.makeText(getActivity(), "No Match Found", Toast.LENGTH_LONG).show();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //adapter.getFilter().filter(newText);
//                String text = newText;
//                adapter.getFilter(text);
//                return false;
//            }
//        });

//        for (int i = 0; i < CityNameList.length; i++) {
//            CityNames cityNames = new CityNames(CityNameList[i]);
//            // Binds all strings into an array
//            arraylist.add(CityNames);
//        }
//
//        // Pass results to ListViewAdapter Class
//        adapter = new ListViewAdapter(this, arraylist);
//
//        // Binds the Adapter to the ListView
//        list.setAdapter(adapter);
//
//        // Locate the EditText in listview_main.xml
//        editsearch = (SearchView) getActivity().findViewById(R.id.search);
//        editsearch.setOnQueryTextListener(this);
//    }
//
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        String text = newText;
//        adapter.filter(text);
//        return false;
//    }
//        mSearchField = getActivity().findViewById(R.id.locationInput);
//        Toast.makeText(getContext(), "Input was:" + mSearchField, Toast.LENGTH_LONG).show();

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
//                android.R.layout.simple_dropdown_item_1line, CITIES);
//        AutoCompleteTextView textView = (AutoCompleteTextView)
//                getView().findViewById(R.id.locationInput);
//        textView.setAdapter(adapter);
//    }
//
//    private static final String[] CITIES = new String[] {
//            "Belgium", "France", "Italy", "Germany", "Spain", "Singapore", "Kuala Lumpur", "Penang"
    };
    //TODO: 27/4/19 0147HRS Link locationInput with coordinates in map activity, will have to add button to initiate search as well.

}
