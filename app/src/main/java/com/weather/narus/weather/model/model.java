package com.weather.narus.weather.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class model {

    private Coord coord;
    private List<Weather> weather;
    private String base;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private Rain rain;
    private Snow snow;
    private Integer dt;
    private Sys sys;
    private Integer id;
    private String name;
    private Integer cod;

    public Coord getCoord() {
        return coord;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Integer getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCod() {
        return cod;
    }

    public static class Coord {

        private Double lon;
        private Double lat;

        public Double getLon() {
            return lon;
        }

        public Double getLat() {
            return lat;
        }
    }

    public static class Weather {

        private Integer id;
        private String main;
        private String description;
        private String icon;

        public Integer getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }

    public static class Main {

        private Double temp;
        private Double pressure;
        private Integer humidity;

        @SerializedName("temp_min")
        private Double tempMin;

        @SerializedName("temp_max")
        private Double tempMax;

        public Double getTemp() {
            return temp;
        }

        public Double getPressure() {
            return pressure;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public Double getTempMin() {
            return tempMin;
        }

        public Double getTempMax() {
            return tempMax;
        }
    }

    public static class Wind {

        private Double speed;
        private Double deg;

        public Double getSpeed() {
            return speed;
        }

        public Double getDeg() {
            return deg;
        }
    }

    public static class Clouds {

        private Integer all;

        public Integer getAll() {
            return all;
        }
    }

    public static class Rain {

        @SerializedName("3h")
        private Double rain3h;

        public Double getRain3h() {
            return rain3h;
        }
    }

    public static class Snow {

        @SerializedName("3h")
        private Double snow3h;

        public Double getSnow3h() {
            return snow3h;
        }
    }

    public static class Sys {

        private Integer type;
        private Integer id;
        private Double message;
        private String country;
        private Integer sunrise;
        private Integer sunset;

        public String getCountry() {
            return country;
        }

        public Integer getSunrise() {
            return sunrise;
        }

        public Integer getSunset() {
            return sunset;
        }
    }
}
