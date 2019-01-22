package com.strikalov.pogoda4.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.strikalov.pogoda4.R;
import com.strikalov.pogoda4.models.SelectedCityData;

import java.util.List;

public class SelectedCityAdapter extends RecyclerView.Adapter<SelectedCityAdapter.ViewHolder> {

    private List<SelectedCityData> selectedCityDataList;

    public SelectedCityAdapter(List<SelectedCityData> selectedCityDataList){
        this.selectedCityDataList = selectedCityDataList;
    }

    @Override
    public int getItemCount(){
        return selectedCityDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }

    }

    @Override
    public SelectedCityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_selected_city, parent, false);
        return new SelectedCityAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(SelectedCityAdapter.ViewHolder holder, final int position) {

        final CardView cardView = holder.cardView;

        SelectedCityData selectedCityItem = selectedCityDataList.get(position);

        TextView selectedCityName = cardView.findViewById(R.id.card_selected_city_name);
        selectedCityName.setText(selectedCityItem.getCityName());

        TextView selectedCityDate = cardView.findViewById(R.id.card_selected_city_date);
        selectedCityDate.setText(selectedCityItem.getDate());

        TextView selectedCityTemperature = cardView.findViewById(R.id.card_selected_city_temperature);
        selectedCityTemperature.setText(selectedCityItem.getTemperature());

    }

}
