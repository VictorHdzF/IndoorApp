package mx.itesm.csf.indoorapp;

/**
 * Created by magnus on 11/8/17.
 */

public class Beacon implements Comparable<Beacon> {
    private String id;
    private String minor;
    private String major;
    private String x;
    private String y;

    public Beacon(String id, String minor, String major, String x, String y) {
        this.id = id;
        this.minor = minor;
        this.major = major;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public String getMinor() {
        return minor;
    }

    public String getMajor() {
        return major;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public int compareTo(Beacon o) {
        return Integer.compare(Integer.parseInt(id), Integer.parseInt(o.getId()));
    }
}
