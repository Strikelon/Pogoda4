package com.strikalov.pogoda4.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.strikalov.pogoda4.R;
import com.strikalov.pogoda4.models.WeatherData;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private List<WeatherData> weatherDataList;

    public WeatherAdapter(List<WeatherData> weatherDataList){
        this.weatherDataList = weatherDataList;
    }

    @Override
    public int getItemCount(){
        return weatherDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }

    }

    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_weather, parent, false);
        return new ViewHolder(cv);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){

        final CardView cardView = holder.cardView;

        WeatherData weatherItem = weatherDataList.get(position);

        ImageView imageView = cardView.findViewById(R.id.picture_info);
        imageView.setImageResource(weatherItem.getPicture());

        ImageView imageWindDirection = cardView.findViewById(R.id.picture_wind_dir);
        imageWindDirection.setImageResource(weatherItem.getWindDirectionPicture());

        TextView dateInfo = cardView.findViewById(R.id.date_info);
        dateInfo.setText(weatherItem.getDate());

        TextView temperatureInfo = cardView.findViewById(R.id.temperature_info);
        temperatureInfo.setText(weatherItem.getTemperature());

        TextView windInfo = cardView.findViewById(R.id.wind_info);
        windInfo.setText(weatherItem.getWind());

        TextView pressureInfo = cardView.findViewById(R.id.pressure_info);
        pressureInfo.setText(weatherItem.getPressure());

        TextView humidityInfo = cardView.findViewById(R.id.humidity_info);
        humidityInfo.setText(weatherItem.getHumidity());
    }

}
