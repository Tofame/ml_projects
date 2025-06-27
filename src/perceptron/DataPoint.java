package perceptron;

import java.util.List;

public class DataPoint {
    private List<Double> values;
    private int D_value;

    public DataPoint(List<Double> values, int D_value) {
        this.values = values;
        this.D_value = D_value;
    }

    public List<Double> getVectorValues() {
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
        return values.size();
    }

    public Double elementAt(int i) {
        return values.get(i);
    }
}