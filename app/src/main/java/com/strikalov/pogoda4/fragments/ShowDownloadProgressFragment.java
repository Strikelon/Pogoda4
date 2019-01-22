package com.strikalov.pogoda4.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strikalov.pogoda4.R;

public class ShowDownloadProgressFragment extends Fragment {

    private static final String ARGUMENT_TEXT = "argument_text";

    public static ShowDownloadProgressFragment newInstance(String text){

        ShowDownloadProgressFragment showDownloadProgressFragment = new ShowDownloadProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_TEXT, text);
        showDownloadProgressFragment.setArguments(args);
        return showDownloadProgressFragment;
    }

    private String getTextForFragment(){
        if(getArguments() != null) {
            return getArguments().getString(ARGUMENT_TEXT);
        }else {
            return null;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_show_download_progress, container, false);

        TextView showFragmentText = view.findViewById(R.id.fragment_show_text);

        if(getTextForFragment() != null){

            showFragmentText.setText(getTextForFragment());

        }else {

            showFragmentText.setText("");

        }

        return view;
    }

}
