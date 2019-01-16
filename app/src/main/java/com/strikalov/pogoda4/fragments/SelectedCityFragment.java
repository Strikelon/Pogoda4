package com.strikalov.pogoda4.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.strikalov.pogoda4.R;
import com.strikalov.pogoda4.adapters.SelectedCityAdapter;
import com.strikalov.pogoda4.models.SelectedCityData;

import java.util.ArrayList;


public class SelectedCityFragment extends Fragment {

    private static final String SELECTED_CITY_LIST = "selected_city_list";

    public static SelectedCityFragment newInstance(ArrayList<SelectedCityData> selectedCityList){

        SelectedCityFragment selectedCityFragment = new SelectedCityFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(SELECTED_CITY_LIST, selectedCityList);
        selectedCityFragment.setArguments(args);
        return  selectedCityFragment;
    }

    private ArrayList<SelectedCityData> getSelectedCityList(){
        return getArguments().getParcelableArrayList(SELECTED_CITY_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView selectedCitiesRecycler = (RecyclerView) inflater.inflate(
                R.layout.fragment_selected_city, container, false);

        ArrayList<SelectedCityData> selectedCityDataList = getSelectedCityList();

        SelectedCityAdapter selectedCityAdapter = new SelectedCityAdapter(selectedCityDataList);
        selectedCitiesRecycler.setAdapter(selectedCityAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        selectedCitiesRecycler.setLayoutManager(linearLayoutManager);

        return selectedCitiesRecycler;
    }

}
