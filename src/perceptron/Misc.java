package perceptron;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Misc {
    public static double scalarMultiply(List<Double> v1, List<Double> v2) throws Exception {
        if (v1.size() != v2.size()) {
            throw new RuntimeException("Vectors must be of the same size. Currently:\nv1: " + v1.size() + " v2: " + v2.size());
        }

        double result = 0;
        for (int i = 0; i < v1.size(); i++) {
            result += v1.get(i) * v2.get(i);
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

    // Wants to load only 2 classes, so skips 3rd always.
    public static void loadVectorData(List<DataPoint> dataSet, String dataLine) {
        String[] tokens = dataLine.split(",");
        boolean isLastElementString = Misc.isElementString(tokens[tokens.length - 1]);
        int lastValueIndex = isLastElementString ? tokens.length - 1 : tokens.length;

        List<Double> l_vector = new ArrayList<>(tokens.length);

        try {
            for (int i = 0; i < lastValueIndex; i++) {
                l_vector.add(Double.parseDouble(tokens[i]));
            }
        } catch (NumberFormatException ne) {
            System.err.println(ne.getMessage() + "\nDataVector Constructor - wrong format of values passed (not doubles).");
            throw new RuntimeException("DataVector Constructor - wrong format of values passed");
        }

        int D_value = 0;
        if (isLastElementString) {
            if (Main.firstClassName.isEmpty()) {
                Main.firstClassName = tokens[lastValueIndex];
            }

            boolean belongsToFirstClass = tokens[lastValueIndex].equals(Main.firstClassName);
            D_value = belongsToFirstClass ? 0 : 1;

            if (!belongsToFirstClass && Main.secondClassName.isEmpty()) {
                Main.secondClassName = tokens[lastValueIndex];
            }

            // Skips class that is not 1st and not 2nd
            if (!belongsToFirstClass && !tokens[lastValueIndex].equals(Main.secondClassName)) {
                return;
            }
        } else {
            System.err.println("Warning: Missing string name in loaded dataSet. Method Misc.loadVectorData()");
        }

        dataSet.add(new DataPoint(l_vector, D_value));
    }

    public static List<DataPoint> loadValuesSet(String fileName) {
        List<DataPoint> dataSet = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineCount = 1;

            // 1st out of loop, as I need vector dim
            line = br.readLine();
            if(line != null) {
                try {
                    // Dim size
                    String[] tokens = line.split(",");
                    boolean isLastElementString = Misc.isElementString(tokens[tokens.length - 1]);
                    Main.detectedVectorDimension = isLastElementString ? tokens.length - 1 : tokens.length;
                    // rest of the code
                    loadVectorData(dataSet, line);
                } catch (NumberFormatException ne) {
                    System.err.println("Wrongly formatted line " + lineCount + ": " + line);
                }
                lineCount++;
            }

            // Now proper loop
            while ((line = br.readLine()) != null) {
                try {
                    loadVectorData(dataSet, line);
                } catch (NumberFormatException ne) {
                    System.err.println("Wrongly formatted line " + lineCount + ": " + line);
                }
                lineCount++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }

        return dataSet;
    }

    public static List<DataPoint> shuffleList(List<DataPoint> list) {
        Collections.shuffle(list);
        return list;
    }

    public static List<Double> getRandomWeightVector(int dimension, double min, double max) {
        List<Double> weightsVector = new ArrayList<>(dimension);
        for (int i = 0; i < dimension; i++) {
            // Generate a random weight within the range [min, max]
            double weight = min + (Math.random() * (max - min));
            weightsVector.add(weight);
        }

        return weightsVector;
    }
}
