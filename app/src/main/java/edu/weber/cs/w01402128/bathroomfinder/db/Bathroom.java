package edu.weber.cs.w01402128.bathroomfinder.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Bathroom {

    @PrimaryKey(autoGenerate = true)
    private int bathroom_id;

    @ColumnInfo(name = "bathroom_name")
    private String bathroom_name;
    @ColumnInfo(name = "longitude")
    private Double lon;
    @ColumnInfo(name = "latitude")
    private Double lat;
    @ColumnInfo(name = "long_wait")
    private boolean long_wait;
    @ColumnInfo(name = "key")
    private boolean key;
    @ColumnInfo(name = "handicap")
    private boolean handicap;
    @ColumnInfo(name = "unavailable")
    private boolean unavailable;
    @ColumnInfo(name = "unavailable_note")
    private String unavailable_note;


    public Bathroom(int bathroom_id, String bathroom_name, Double lon, Double lat, boolean long_wait, boolean key, boolean handicap, boolean unavailable, String unavailable_note) {
        this.bathroom_id = bathroom_id;
        this.bathroom_name = bathroom_name;
        this.lon = lon;
        this.lat = lat;
        this.long_wait = long_wait;
        this.key = key;
        this.handicap = handicap;
        this.unavailable = unavailable;
        this.unavailable_note = unavailable_note;
    }

    @Ignore
    public Bathroom(String bathroom_name, Double lon, Double lat, boolean long_wait, boolean key, boolean handicap, boolean unavailable, String unavailable_note) {
        this.bathroom_name = bathroom_name;
        this.lon = lon;
        this.lat = lat;
        this.long_wait = long_wait;
        this.key = key;
        this.handicap = handicap;
        this.unavailable = unavailable;
        this.unavailable_note = unavailable_note;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public boolean isHandicap() {
        return handicap;
    }

    public void setHandicap(boolean handicap) {
        this.handicap = handicap;
    }

    public int getBathroom_id() {
        return bathroom_id;
    }

    public void setBathroom_id(int bathroom_id) {
        this.bathroom_id = bathroom_id;
    }

    public String getBathroom_name() {
        return bathroom_name;
    }

    public void setBathroom_name(String bathroom_name) {
        this.bathroom_name = bathroom_name;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public boolean isLong_wait() {
        return long_wait;
    }

    public void setLong_wait(boolean long_wait) {
        this.long_wait = long_wait;
    }

    public boolean isUnavailable() {
        return unavailable;
    }

    public void setUnavailable(boolean unavailable) {
        this.unavailable = unavailable;
    }

    public String getUnavailable_note() {
        return unavailable_note;
    }

    public void setUnavailable_note(String unavailable_note) {
        this.unavailable_note = unavailable_note;
    }

    @Override
    public String toString() {
        return "Bathroom{" +
                "bathroom_id=" + bathroom_id +
                ", bathroom_name='" + bathroom_name + '\'' +
                ", lon=" + lon +
                ", lat=" + lat +
                ", long_wait=" + long_wait +
                ", key=" + key +
                ", handicap=" + handicap +
                ", unavailable=" + unavailable +
                ", unavailable_note='" + unavailable_note + '\'' +
                '}';
    }
}
