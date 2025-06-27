package precision_recall_f1;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        int[] test = {2, 0, 2, 2, 0, 1, 1, 2, 2, 0, 1, 2, 1, 0};
        int[] classified = {0, 0, 2, 2, 0, 1, 1, 0, 2, 0, 2, 2, 0, 2};
//        int[] test = {1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 0};
//        int[] classified = {0, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 1};

        int[][] matrix = confusionMatrix(classified, test);
        for(int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
        if(matrix.length == 2) {
            double precision = precision(matrix);
            double recall = recall(matrix);
            double fScore = fScore(matrix);

            System.out.println("Precision: " + Math.round(precision * 100.0)/100.0);
            System.out.println("Recall: " + Math.round(recall * 100.0)/100.0);
            System.out.println("F-score: " + Math.round(fScore * 100.0)/100.0);
        }
    }

    public static int[][] confusionMatrix(int[] classified, int[] test) {
        int maxClass = 0;
        for (int i = 0; i < test.length; i++) {
            maxClass = Math.max(maxClass, Math.max(test[i], classified[i]));
        }

        int[][] matrix = new int[maxClass + 1][maxClass + 1];

        for (int i = 0; i < test.length; i++) {
            int actual = test[i];
            int predicted = classified[i];
            matrix[actual][predicted]++;
        }

        return matrix;
    }

    public static double precision(int[][] matrix) {
        int truePositives = matrix[1][1];
        int falsePositives = matrix[0][1];

        if (truePositives + falsePositives == 0) {
            return 0.0;
        }
        return (double) truePositives / (truePositives + falsePositives);
    }

    public static double recall(int[][] matrix) {
        int truePositives = matrix[1][1];
        int falseNegatives = matrix[1][0];

        if (truePositives + falseNegatives == 0) {
            return 0.0;
        }
        return (double) truePositives / (truePositives + falseNegatives);
    }

    public static double fScore(int[][] matrix) {
        double precision = precision(matrix);
        double recall = recall(matrix);

        if (precision + recall == 0) {
            return 0.0;
        }

        return 2 * (precision * recall) / (precision + recall);
    }
}