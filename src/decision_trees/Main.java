package decision_trees;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        /*
            Project is WIP in progress, might not be working correctly.

            Example input:
            1
            10.0
            0.4
            0.7
         */

        ArrayList<Double> x1 = new ArrayList<>(Arrays.asList(5.0,13.5,21.0,3.7,4.8,6.8,23.3));
        ArrayList<Double> x2 = new ArrayList<>(Arrays.asList(3.3,1.9,2.5,9.6,1.6,10.2,5.1));
        ArrayList<Double> y = new ArrayList<>(Arrays.asList(0.13,0.71,0.3,0.24,0.91,0.48,0.6));

        Scanner in = new Scanner(System.in);
        System.out.print("Choose feature number for splitting (1 = x1, 2 = x2): ");
        Double condition_Feature = in.nextDouble(); // numerically, index 1 for x1 etc.

        System.out.print("Enter the split condition value: ");
        Double condition_Value = in.nextDouble();

        System.out.print("Enter y1 value (for left branch): ");
        Double yy1 = in.nextDouble();

        System.out.print("Enter y2 value (for right branch): ");
        Double yy2 = in.nextDouble();

        List<DataPoint> data = new ArrayList<>();
        for (int i = 0; i < x1.size(); i++) {
            data.add(new DataPoint(x1.get(i), x2.get(i), y.get(i)));
        }

        if(condition_Feature == 1) { // Sort by x1
            data.sort(Comparator.comparingDouble(dp -> dp.x1));
        } else if(condition_Feature == 2) { // Sort by x2
            data.sort(Comparator.comparingDouble(dp -> dp.x2));
        }

        // Split into 2
        List<DataPoint> belowOrEqual = new ArrayList<>();
        List<DataPoint> above = new ArrayList<>();

        for (DataPoint dp : data) {
            double value = (condition_Feature == 1) ? dp.x1 : dp.x2;
            if (value <= condition_Value) {
                belowOrEqual.add(dp);
            } else {
                above.add(dp);
            }
        }

        // y1
        List<Double> values = new ArrayList<>();
        for (DataPoint dp : belowOrEqual) {
            values.add(dp.y);
        }
        double y1 = (values.stream().mapToDouble(Double::doubleValue).sum())/values.size();

        values.clear();
        // y2
        for (DataPoint dp : above) {
            values.add(dp.y);
        }
        double y2 = (values.stream().mapToDouble(Double::doubleValue).sum())/values.size();

        y1 = yy1; // Overwriting y1 with user input
        y2 = yy2; // Overwriting y2 with user input

        // RSS (Residual Sum of Squares)
        List<Double> results = new ArrayList<>();

        Double resultFinal = 0.0;
        for (DataPoint dp : belowOrEqual) {
            resultFinal += (Math.pow(dp.y - y1, 2));
        }
        results.add(resultFinal);

        resultFinal = 0.0;
        for (DataPoint dp : above) {
            resultFinal += (Math.pow(dp.y - y2, 2));
        }
        results.add(resultFinal);

        for (var result : results) {
            System.out.println(result);
        }
    }
}