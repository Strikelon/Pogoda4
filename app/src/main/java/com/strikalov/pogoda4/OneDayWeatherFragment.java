package com.strikalov.pogoda4;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class OneDayWeatherFragment extends Fragment {

    private static final String WEATHER_TODAY = "weather_today";

    private String today;
    private String temperature;
    private String temperatureForecast;
    private String pressure;
    private String wind;
    private String windForecast;
    private String pressureForecast;
    private String humidity;
    private String humidityForecast;

    public static OneDayWeatherFragment newInstance(Weather weather){

        OneDayWeatherFragment oneDayWeatherFragment = new OneDayWeatherFragment();
        Bundle args = new Bundle();
        args.putParcelable(WEATHER_TODAY, weather);
        oneDayWeatherFragment.setArguments(args);
        return oneDayWeatherFragment;
    }

    private Weather getWeather(){
        return getArguments().getParcelable(WEATHER_TODAY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one_day_weather, container, false);

        today = getString(R.string.today);
        temperature = getString(R.string.temperature);
        wind = getString(R.string.wind);
        pressure = getString(R.string.pressure);
        humidity = getString(R.string.humidity);

        CardView cardViewOneDay = view.findViewById(R.id.card_view_one_day);
        registerForContextMenu(cardViewOneDay);

        TextView textTemperature = view.findViewById(R.id.temperature_today);
        TextView textWind = view.findViewById(R.id.wind_today);
        TextView textPressure = view.findViewById(R.id.pressure_today);
        TextView textHumidity = view.findViewById(R.id.humidity_today);

        ImageView imageTemperature = view.findViewById(R.id.picture_temperature_today);
        ImageView imageWind = view.findViewById(R.id.picture_wind_today);
        ImageView imagePressure = view.findViewById(R.id.picture_pressure_today);
        ImageView imageHumidity = view.findViewById(R.id.picture_humidity_today);

        Weather weatherToday = getWeather();

        if(weatherToday != null){

            temperatureForecast = weatherToday.getTemperature();
            windForecast = weatherToday.getWind();
            pressureForecast = weatherToday.getPressure();
            humidityForecast = weatherToday.getHumidity();

            textTemperature.setText(temperatureForecast);
            textWind.setText(windForecast);
            textPressure.setText(pressureForecast);
            textHumidity.setText(humidityForecast);

            imagePressure.setImageResource(R.drawable.barometr);
            imageHumidity.setImageResource(R.drawable.humidity);

            switch (weatherToday.getWeatherPicture()) {
                case D01:
                    imageTemperature.setImageResource(R.drawable.d01);
                    break;
                case N01:
                    imageTemperature.setImageResource(R.drawable.n01);
                    break;
                case D02:
                    imageTemperature.setImageResource(R.drawable.d02);
                    break;
                case N02:
                    imageTemperature.setImageResource(R.drawable.n02);
                    break;
                case D03:
                    imageTemperature.setImageResource(R.drawable.d03);
                    break;
                case N03:
                    imageTemperature.setImageResource(R.drawable.n03);
                    break;
                case D04:
                    imageTemperature.setImageResource(R.drawable.d04);
                    break;
                case N04:
                    imageTemperature.setImageResource(R.drawable.n04);
                    break;
                case D09:
                    imageTemperature.setImageResource(R.drawable.d09);
                    break;
                case N09:
                    imageTemperature.setImageResource(R.drawable.n09);
                    break;
                case D10:
                    imageTemperature.setImageResource(R.drawable.d10);
                    break;
                case N10:
                    imageTemperature.setImageResource(R.drawable.n10);
                    break;
                case D11:
                    imageTemperature.setImageResource(R.drawable.d11);
                    break;
                case N11:
                    imageTemperature.setImageResource(R.drawable.n11);
                    break;
                case D13:
                    imageTemperature.setImageResource(R.drawable.d13);
                    break;
                case N13:
                    imageTemperature.setImageResource(R.drawable.n13);
                    break;
                case D50:
                    imageTemperature.setImageResource(R.drawable.d50);
                    break;
                case N50:
                    imageTemperature.setImageResource(R.drawable.n50);
                    break;
                case NO_ICON:
                    imageTemperature.setImageResource(R.drawable.no_icon_temperature);
            }

            switch (weatherToday.getWindDirection()) {
                case NORTH:
                    imageWind.setImageResource(R.drawable.north);
                    break;
                case NORTH_WEST:
                    imageWind.setImageResource(R.drawable.north_west);
                    break;
                case NORTH_EAST:
                    imageWind.setImageResource(R.drawable.north_east);
                    break;
                case WEST:
                    imageWind.setImageResource(R.drawable.west);
                    break;
                case EAST:
                    imageWind.setImageResource(R.drawable.east);
                    break;
                case SOUTH:
                    imageWind.setImageResource(R.drawable.south);
                    break;
                case SOUTH_WEST:
                    imageWind.setImageResource(R.drawable.south_west);
                    break;
                case SOUTH_EAST:
                    imageWind.setImageResource(R.drawable.south_east);
                    break;
                case NO_DIRECTION:
                    imageWind.setImageResource(R.drawable.no_direction_wind);
                    break;
            }

        }

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){

        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_context_one_day, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share_one_day:

                StringBuilder message = new StringBuilder(today).append("\n");
                message.append(temperature).append(": ").append(temperatureForecast).append("\n").
                        append(wind).append(": ").append(windForecast).append("\n").
                        append(pressure).append(": ").append(pressureForecast).append("\n").
                        append(humidity).append(": ").append(humidityForecast).append("\n");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, message.toString());
                String chooserTitle = getString(R.string.chooser);
                Intent chooseIntent = Intent.createChooser(intent,chooserTitle);
                getActivity().startActivity(chooseIntent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}
