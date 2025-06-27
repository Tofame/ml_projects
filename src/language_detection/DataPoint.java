package language_detection;

import java.util.List;

public class DataPoint {
    private double[] values;
    private int D_value;

    public DataPoint(double[] values, int D_value) {
        this.values = values;
        this.D_value = D_value;
    }

    public double[] getVectorValues() {
        return values;
    }

    public int getD_value() {
        return D_value;
    }

    @Override
    public String toString() {
        return "DataPoint{" +
                "values=" + values +
                ", D_value=" + D_value +
                '}';
    }

    public int size() {
        return values.length;
    }

    public Double elementAt(int i) {
        return values[i];
    }
}