package eu.mrndesign.matned.mrnweatherapp.api.model;

public class Sys {

    private int type;
    private int id;
    private String country;
    private long sunrise;
    private long sunset;

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    @Override
    public String toString() {
        return "Sys{" +
                "type=" + type +
                ", id=" + id +
                ", country='" + country + '\'' +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
    }
}
