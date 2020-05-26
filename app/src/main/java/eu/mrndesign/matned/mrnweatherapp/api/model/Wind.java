package eu.mrndesign.matned.mrnweatherapp.api.model;

public class Wind {
    private double speed;
    private double deg;

    public double getSpeed() {
        return speed;
    }

    public double getDeg() {
        return deg;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "speed=" + speed +
                ", deg=" + deg +
                '}';
    }
}
