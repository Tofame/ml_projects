package alghoritm_knn;

import java.io.*;
import java.util.*;

public class Main {
    public static ArrayList<DataVector<Double>> testSet;
    public static ArrayList<DataVector<Double>> trainSet;

    /**
     * Calculates the Euclidean distance between two DataVectors.
     */
    public static <T extends Number> double calculateDistance(DataVector<T> v1, DataVector<T> v2) throws VectorSizeException {
        if (v1.size() != v2.size()) {
            throw new VectorSizeException("Vectors must be of the same size. Currently:\nv1 size: " + v1.size() + ", v2 size: " + v2.size());
        }

        double sumOfSquares = 0;

        for(int i = 0; i < v1.size(); i++) {
            sumOfSquares += Math.pow(v1.elementAt(i).doubleValue() - v2.elementAt(i).doubleValue(), 2);
        }

        return Math.sqrt(sumOfSquares);
    }

    /**
     * Main method to run the KNN classifier.
     * It loads datasets, performs an accuracy test.
     */
    public static void main(String[] args) {
        testSet = loadValuesSet("iris_test.txt");
        trainSet = loadValuesSet("iris_train.txt");

        Scanner inputScanner = new Scanner(System.in);

        // Init accuracy test
        System.out.print("Enter k (number of neighbors for accuracy test): ");
        String kForAccuracyString = inputScanner.nextLine();
        int kForAccuracy;
        try {
            kForAccuracy = Integer.parseInt(kForAccuracyString);
        } catch (NumberFormatException e) {
            System.err.println("Invalid k value for accuracy test. Please enter an integer.");
            return;
        }

        double accuracy = runAccuracyTest(kForAccuracy);
        System.out.printf("Overall Accuracy (with k=%d): %.2f%%\n", kForAccuracy, accuracy);

        while(true) {
            System.out.println("\n--- New Prediction ---");
            System.out.println("Type 'exit' to quit, or enter vector coordinates separated by commas.");
            System.out.println("Example: 5.7,2.8,4.1,1.3 (for 'versi') or 5.0,3.5,1.3,0.3 (for 'setosa')");
            String userInputLine = inputScanner.nextLine();

            if(userInputLine.equalsIgnoreCase("exit")) {
                break;
            }

            DataVector<Double> userVector;
            try {
                userVector = DataVector.fromUserInput(userInputLine);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input format for vector: " + e.getMessage());
                continue;
            }

            System.out.print("Enter k (number of neighbors for this prediction): ");
            int kForPrediction;
            try {
                kForPrediction = Integer.parseInt(inputScanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid k value. Please enter an integer.");
                continue;
            }

            // Calculate distances between the user's vector and all training vectors
            for (DataVector<Double> trainVector : trainSet) {
                try {
                    trainVector.setDistance(calculateDistance(userVector, trainVector));
                } catch (VectorSizeException e) {
                    System.err.println("Error calculating distance for user input: " + e.getMessage());
                    continue;
                }
            }
            Collections.sort(trainSet);

            // Find the predicted class based on the k nearest neighbors
            String predictedClass = findKNNname(userVector, kForPrediction);
            System.out.println("Predicted Class for your vector: " + predictedClass);
        }

        inputScanner.close();
        System.out.println("Exiting KNN application. Goodbye!");
    }

    /**
     * Loads a dataset from a specified file into an ArrayList of DataVectors.
     */
    public static ArrayList<DataVector<Double>> loadValuesSet(String fileName) {
        ArrayList<DataVector<Double>> tempSet = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineCount = 1;
            while((line = reader.readLine()) != null) {
                try {
                    DataVector<Double> dv = new DataVector<>(line, true);
                    tempSet.add(dv);
                } catch (NumberFormatException ne) {
                    System.err.println("Error: Malformed number in line " + lineCount + ": '" + line + "' - " + ne.getMessage());
                    continue;
                } finally {
                    lineCount++;
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Dataset file not found: " + fileName);
            throw new RuntimeException("Failed to load dataset: " + fileName + " not found.", e);
        } catch (IOException e) {
            System.err.println("Error: Could not read from file " + fileName + ": " + e.getMessage());
            throw new RuntimeException("Error reading dataset file: " + fileName, e);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while loading file " + fileName + ": " + e.getMessage());
            throw new RuntimeException("Unexpected error during file loading: " + fileName, e);
        }

        return tempSet;
    }

    /**
     * Determines the predicted class for a given test vector using the KNN algorithm.
     * It considers the 'k' nearest neighbors
     * @return The predicted class name based on majority vote, or "Unknown (No neighbors found)"
     * if no neighbors are available.
     */
    public static String findKNNname(DataVector<Double> testVector, int k) {
        Map<String, Integer> classCountMap = new HashMap<>();

        // Iterate through the first 'k' elements of the sorted trainSet (these are the nearest neighbors)
        for (int i = 0; i < k && i < trainSet.size(); i++) {
            String className = trainSet.get(i).getClassName();
            classCountMap.put(className, classCountMap.getOrDefault(className, 0) + 1);
        }

        if (classCountMap.isEmpty()) {
            return "Unknown (No neighbors found)";
        }

        String predictedClass = null;
        int maxCount = -1;

        for (Map.Entry<String, Integer> entry : classCountMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                predictedClass = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        return predictedClass;
    }

    /**
     * Runs an accuracy test on the KNN model using the test set.
     * For each vector in the test set, it calculates its predicted class
     * and compares it to the actual class.
     *
     * @param k The number of neighbors to consider for classification.
     * @return The accuracy as a percentage
     */
    public static double runAccuracyTest(int k) {
        double correctPredictionsCount = 0;

        for (DataVector<Double> testVector : testSet) {
            // For each test vector, calculate its distance to every training vector
            for (DataVector<Double> trainVector : trainSet) {
                try {
                    trainVector.setDistance(calculateDistance(testVector, trainVector));
                } catch (VectorSizeException e) {
                    System.err.println("Error calculating distance during accuracy test: " + e.getMessage());
                    continue;
                }
            }
            Collections.sort(trainSet);

            String predictedClassName = findKNNname(testVector, k);

            // Compare the predicted class with the actual class of the test vector
            if (predictedClassName != null && predictedClassName.equals(testVector.getClassName())) {
                correctPredictionsCount++;
            }
        }

        // Avoid 0.0 division
        if (testSet.isEmpty()) {
            return 0.0;
        }

        return (correctPredictionsCount * 100.0) / testSet.size();
    }
}