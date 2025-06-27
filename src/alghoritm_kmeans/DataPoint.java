package alghoritm_kmeans;

import java.util.ArrayList;
import java.util.Objects;

public class DataPoint {
    public ArrayList<Double> data;
    public String name = "#";

    public DataPoint(int dataSize) {
        data = new ArrayList<>(dataSize);
    }

    public DataPoint(DataPoint other) {
        this.data = new ArrayList<>(other.data);
        this.name = other.name;
    }

    public DataPoint(String line) {
        String[] dataString = line.split(",");
        int length = dataString.length;

        // -1, because the last element might or might not be a name (string)
        data = new ArrayList<>(length-1);

        for (int i = 0; i < length; i++) {
            try {
                double value = Double.parseDouble(dataString[i].trim());
                data.add(i, Double.valueOf(value));
            } catch (NumberFormatException e) {
                name = dataString[i].trim();
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DataPoint)) return false;
        DataPoint other = (DataPoint) obj;
        return data.equals(other.data) && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        String text = "DP-" + name + "(";
        for (int i = 0; i < data.size(); i++) {
            text += data.get(i) + ",";
        }
        text = text.substring(0, text.length() - 1);

        return text + ")";
    }
}