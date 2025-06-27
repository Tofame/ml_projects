package alghoritm_knn;

import java.util.ArrayList;

public class DataVector<E extends Number> implements Comparable<DataVector<E>> {
    private String className;
    private ArrayList<E> values = new ArrayList<>();
    private Double distance;

    public DataVector(String dataLine, boolean includesName) throws NumberFormatException {
        String[] tokens = dataLine.split(",");
        int valueCount = includesName ? tokens.length - 1 : tokens.length;

        for (int i = 0; i < valueCount; i++) {
            values.add((E) Double.valueOf(tokens[i].trim()));
        }

        if (includesName) {
            className = tokens[valueCount].trim();
        }
    }

    public static DataVector<Double> fromUserInput(String inputLine) throws NumberFormatException {
        String[] tokens = inputLine.split(",");
        ArrayList<Double> parsedValues = new ArrayList<>();
        String parsedClassName = null;

        // Try to parse all but the last token as doubles
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            try {
                parsedValues.add(Double.parseDouble(token));
            } catch (NumberFormatException e) {
                // If it's the last token and it's not a number, assume it's the class name
                if (i == tokens.length - 1) {
                    parsedClassName = token;
                } else {
                    // If a non-numeric token is found before the last, or not the last, throw
                    throw new NumberFormatException("Invalid number format in input: " + token);
                }
            }
        }

        // If the last token was actually a number and parsed, and no class name was found,
        // then the class name remains null.
        // Otherwise, if a class name was identified, create a new DataVector.
        DataVector<Double> dv = new DataVector<>(); // Use default constructor
        dv.values = parsedValues;
        dv.className = parsedClassName;
        return dv;
    }

    private DataVector() {
        this.values = new ArrayList<>();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(DataVector<E> o2) {
        return Double.compare(this.getDistance(), o2.getDistance());
    }

    public int size() {
        return values.size();
    }

    public E elementAt(int i) {
        return values.get(i);
    }

    public ArrayList<E> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "DataVector{" +
                "className='" + className + '\'' +
                ", values=" + values +
                ", distance=" + distance +
                '}';
    }
}