package alghoritm_kmeans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Main {
    public static ArrayList<DataPoint> centroids = new ArrayList<>();
    public static ArrayList<DataPoint> vectors = new ArrayList<>();

    public static double euclides(DataPoint vector, DataPoint centroid) {
        double sum = 0.0;

        for (int i = 0; i < vector.data.size(); i++) {
            double diff = vector.data.get(i) - centroid.data.get(i);
            sum += diff * diff;
        }

        return Math.sqrt(sum);
    }

    public static double calculateTotalError(Map<DataPoint, ArrayList<DataPoint>> groups) {
        double error = 0.0;
        for (Map.Entry<DataPoint, ArrayList<DataPoint>> entry : groups.entrySet()) {
            double sum = 0.0;
            for (DataPoint dataPoint : entry.getValue()) {
                double distance = euclides(dataPoint, entry.getKey());
                error += distance * distance;
            }
        }

        return error;
    }

    // Returns array of new centroids
    public static ArrayList<DataPoint> updateCentroids(Map<DataPoint, ArrayList<DataPoint>> groups) {
        ArrayList<DataPoint> newCentroids = new ArrayList<>();

        for (var entry : groups.entrySet()) {
            ArrayList<DataPoint> vectors = entry.getValue();
            if(vectors.isEmpty()) {
                continue;
            }

            // Help variable
            int dataPointLength = vectors.getFirst().data.size();

            DataPoint newCentroid = new DataPoint(dataPointLength);
            for (int i = 0; i < dataPointLength; i++) {
                double sum = 0;
                for (var vector : vectors) {
                    sum += vector.data.get(i);
                }
                double result = sum / vectors.size();
                newCentroid.data.add(i, Double.valueOf(result));
            }

            newCentroids.add(newCentroid);
        }

        return newCentroids;
    }

    // True = oldGroups and newGroups are equal
    public static boolean doEpoch(Map<DataPoint, ArrayList<DataPoint>> groups) {
        // Deep copy so we get oldGroups that doesn't reference the groups, stupid java.
        Map<DataPoint, ArrayList<DataPoint>> oldGroups = new HashMap<>();
        for (Map.Entry<DataPoint, ArrayList<DataPoint>> entry : groups.entrySet()) {
            ArrayList<DataPoint> newList = new ArrayList<>();
            for (DataPoint dp : entry.getValue()) {
                newList.add(new DataPoint(dp));
            }
            oldGroups.put(entry.getKey(), newList);
        }
        groups.clear();

        // ---- Epoch
        // Vector -> Centroid
        Map<DataPoint, DataPoint> assignmentMap = new HashMap<>();

        for (DataPoint centroid : centroids) {
            groups.put(centroid, new ArrayList<>());
        }

        for (DataPoint vector : vectors) {
            double minDistance = Double.MAX_VALUE;
            DataPoint closestCentroid = null;

            for (DataPoint centroid : centroids) {
                double result = euclides(vector, centroid);
                if (result < minDistance) {
                    minDistance = result;
                    closestCentroid = centroid;
                }
            }

            assignmentMap.put(vector, closestCentroid);
        }

        // Add vectors to their group
        for (var entry : assignmentMap.entrySet()) {
            groups.get(entry.getValue()).add(entry.getKey());
        }

        return groups.equals(oldGroups);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj k: ");
        int k = sc.nextInt();

        String[] lines;
        try {
            BufferedReader br = new BufferedReader(new FileReader("iris.txt"));
            lines = br.lines().toArray(String[]::new);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Random rand = new Random();
        for (int i = 0; i < k; i++) {
            centroids.add(new DataPoint(lines[rand.nextInt(0, lines.length)]));
        }
        for (int i = 0; i < lines.length; i++) {
            vectors.add(new DataPoint(lines[i]));
        }

        // Initialize groups to be filled
        // Centroid -> collection of vectors
        Map<DataPoint, ArrayList<DataPoint>> groups = new HashMap<>();

        long iterationCount = 0;
        boolean result = false;
        while (!result) {
            result = doEpoch(groups);
            iterationCount++;

            if(!result) {
                // Calculate new centroids
                centroids = updateCentroids(groups);
            }
        }

        System.out.println("=== Process has finished ===");
        int groupIndex = 1;
        for (Map.Entry<DataPoint, ArrayList<DataPoint>> entry : groups.entrySet()) {
            DataPoint centroid = entry.getKey();
            ArrayList<DataPoint> clusterVectors = entry.getValue();
            System.out.println("\nGroup " + groupIndex + " (Centroid: " + centroid + "):");
            for(DataPoint vector : clusterVectors) {
                System.out.println(vector);
            }
            groupIndex++;
        }
        System.out.println("Iterations: " + iterationCount);
        System.out.printf("\nTotal squared error (E): %.4f\n", calculateTotalError(groups));

        double error = 0.0;
        double index = 0;
        for (Map.Entry<DataPoint, ArrayList<DataPoint>> entry : groups.entrySet()) {
            error = 0.0;
            for (DataPoint dataPoint : entry.getValue()) {
                double distance = euclides(dataPoint, entry.getKey());
                error += distance * distance;
            }
            System.out.println("Error group " + index + ": " + error);
            index++;
        }
    }
}