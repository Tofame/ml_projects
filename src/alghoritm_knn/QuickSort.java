package alghoritm_knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuickSort {
    public static void sort(ArrayList<DataVector<Double>> list, int low, int high) // Changed trainSet to list
    {
        if (low < high)
        {
            int pi = partition(list, low, high);
            // recursively sort left and right side to the pivot
            sort(list, low, pi-1);
            sort(list, pi+1, high);
        }
    }

    private static void randomPivot(ArrayList<DataVector<Double>> list, int low, int high) // Changed trainSet to list
    {
        Random rand= new Random();
        int pivotIndex = low + rand.nextInt(high - low + 1);
        Collections.swap(list, pivotIndex, high);
    }

    private static int partition(ArrayList<DataVector<Double>> list, int low, int high) // Changed trainSet to list
    {
        // Randomly choose pivot
        randomPivot(list, low, high);
        DataVector<Double> pivot = list.get(high);
        int i = (low - 1);

        for (int j = low; j < high; j++)
        {
            if (list.get(j).getDistance() <= pivot.getDistance()) {
                i++;
                Collections.swap(list, i, j);
            }
        }

        // Correct placement of pivot, into its final pos
        Collections.swap(list, i + 1, high);

        return i+1;
    }
}