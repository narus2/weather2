package com.weather.narus.weather;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.weather.narus.weather.model.model;

public class InfoWindowFiller {
    private Context mContext;
    private Resources mRes;
    private View mView;
    private model mWeather;

    public InfoWindowFiller(Context context, View view, model weather) {
        this.mContext = context;
        this.mRes = mContext.getResources();
        this.mView = view;
        this.mWeather = weather;
    }

    public void getFilledView() {
        String text = "";

        text = getCity();
        TextView tvCity = (TextView) mView.findViewById(R.id.tvCity);
        tvCity.setText(text);


        ImageView ivClouds = (ImageView) mView.findViewById(R.id.ivClouds);
        setCloudsIconID(ivClouds);

        text = getTemp();
        TextView tvTemp = (TextView) mView.findViewById(R.id.tvTemp);
        tvTemp.setText(text);

        text = getClouds();
        TextView tvClouds = (TextView) mView.findViewById(R.id.tvClouds);
        tvClouds.setText(text);
        changeVisibility(tvClouds, text);

        text = getHumidity();
        TextView tvHumidity = (TextView) mView.findViewById(R.id.tvHumidity);
        tvHumidity.setText(text);
        changeVisibility(tvHumidity, text);

        text = getWind();
        TextView tvWind = (TextView) mView.findViewById(R.id.tvWind);
        tvWind.setText(text);
        changeVisibility(tvWind, text);


        text = getPressure();
        TextView tvPressure = (TextView) mView.findViewById(R.id.tvPressure);
        tvPressure.setText(text);
        changeVisibility(tvPressure, text);
    }

    private void changeVisibility(View view, String text) {
        if (text.equals("")) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkString(String str) {
        return (str != null) && (!str.equals(""));
    }

    private String getCity() {
        String delimiter = "";
        if (checkString(mWeather.getName()) && checkString(mWeather.getSys().getCountry())) {
            delimiter = ", ";
        }

        String city = (mWeather.getName() != null ? mWeather.getName() : "")
                + delimiter
                + (mWeather.getSys().getCountry() != null ? mWeather.getSys().getCountry() : "");

        return city;
    }

    private void setCloudsIconID(ImageView ivClouds) {
        String PICT_URL = "http://openweathermap.org/img/w/" + mWeather.getWeather().get(0).getIcon() + ".png";
        Context context = mContext;

        Bitmap result = null;
        Picasso.with(context).load(PICT_URL)
                .resize(150, 150)
                .into(ivClouds);
    }

    private String getTemp() {
        if (mWeather.getMain().getTemp() != null) {
            return String.format("%.1f\u2103", mWeather.getMain().getTemp());
            //Converter.convertKelvinToCelsius(mWeather.getMain().getTemp()));
        }
        return "";
    }

    private String getClouds() {
        if (mWeather.getClouds().getAll() != null) {
            return mRes.getString(R.string.weather_clouds_title) + " "
                    + mWeather.getClouds().getAll() + mRes.getString(R.string.weather_clouds_unit);
        }
        return "";
    }

    private String getHumidity() {
        if (mWeather.getMain().getHumidity() != null) {
            return mRes.getString(R.string.weather_humidity_title) + " "
                    + mWeather.getMain().getHumidity()
                    + mRes.getString(R.string.weather_humidity_unit);
        }
        return "";
    }

    private String getWind() {
        StringBuilder wind = new StringBuilder("");
        if (mWeather.getWind().getDeg() != null) {
            wind.append(presentWindDirection(mWeather.getWind().getDeg(),
                    mRes.getStringArray(R.array.cardinal_directions)));
        }
        if (mWeather.getWind().getSpeed() != null) {
            wind.append(" " + String.format("%.2f", mWeather.getWind().getSpeed())
                    + mRes.getString(R.string.weather_wind_speed_unit));
        }
        if (wind.length() > 0) {
            wind.insert(0, mRes.getString(R.string.weather_wind_title) + " ");
        }
        return wind.toString();
    }

    public static String presentWindDirection(Double deg, String[] directions) {
        return directions[(int) Math.floor(((deg + 11.25) % 360) / 22.5)];
    }

    private String getPressure() {
        if (mWeather.getMain().getPressure() != null) {
            return mRes.getString(R.string.weather_pressure_title) + " "
                    + String.format("%.2f", mWeather.getMain().getPressure())
                    + mRes.getString(R.string.weather_pressure_unit);
        }
        return "";
    }

}
