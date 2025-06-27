package language_detection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Misc {
    public static double[] normalize(double[] vectorInput) {
        double[] result = new double[vectorInput.length];

        double xVal = 0;
        for (int i = 0; i < vectorInput.length; i++) {
            xVal += Math.pow(vectorInput[i], 2);
        }
        xVal = Math.sqrt(xVal);

        for (int i = 0; i < result.length; i++) {
            result[i] = vectorInput[i] / xVal;
        }

        return result;
    }

    public static double scalarMultiply(double[] v1, double[] v2) throws Exception {
        if (v1.length != v2.length) {
            throw new RuntimeException("Vectors must be of the same size. Currently:\nv1: " + v1.length + " v2: " + v2.length);
        }

        double result = 0;
        for (int i = 0; i < v1.length; i++) {
            result += v1[i] * v2[i];
        }

        return result;
    }

    // Checks if is a number or a string
    public static boolean isElementString(String element) {
        // Try to parse the last element as a number
        try {
            Double.parseDouble(element);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public static List<DataPoint> shuffleList(List<DataPoint> list) {
        Collections.shuffle(list);
        return list;
    }

    public static double[] getRandomWeightVector(int dimension, double min, double max) {
        double[] weightsVector = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            // Generate a random weight within the range [min, max]
            double weight = min + (Math.random() * (max - min));
            weightsVector[i] = weight;
        }

        return weightsVector;
    }

    public static double[] convertListToArray(List<Double> list) {
        return list.stream().mapToDouble(Double::doubleValue).toArray();
    }

    public static List<Double> convertArrayToList(double[] array) {
        return new ArrayList<>(Arrays.stream(array).boxed().toList());
    }
}
