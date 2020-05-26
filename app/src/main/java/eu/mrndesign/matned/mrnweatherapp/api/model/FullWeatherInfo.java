package eu.mrndesign.matned.mrnweatherapp.api.model;

import java.io.Serializable;

public class FullWeatherInfo implements Serializable {


    private Coord coord;
    private Weather[] weather;
    private String base;
    private Main main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private long dt;
    private Sys sys;
    private int timezone;
    private long id;
    private String name;
    private int cod;

    public Coord getCoord() {
        return coord;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public int getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public long getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public int getTimezone() {
        return timezone;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }

    private String weather(){
        StringBuilder str = new StringBuilder();
        for (Weather el :
                weather) {
            str.append(el.toString());
        }
        return String.valueOf(str);
    }

    @Override
    public String toString() {
        return "FullWeatherInfo{" +
                "coord=" + coord +
                ", weather=" + weather() +
                ", base='" + base + '\'' +
                ", mainInfo=" + main +
                ", visibility=" + visibility +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", dt=" + dt +
                ", sys=" + sys +
                ", timezone=" + timezone +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", cod=" + cod +
                '}';
    }
}
