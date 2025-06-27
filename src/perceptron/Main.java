package perceptron;

import java.sql.SQLOutput;
import java.util.*;

public class Main {
    public static String firstClassName = "";
    public static String secondClassName = "";
    public static int detectedVectorDimension = 1;

    public static List<DataPoint> testSet;
    public static List<DataPoint> trainSet;

    public static void main(String[] args) {
        // Loading sets
        trainSet = Misc.loadValuesSet("iris_train.txt");
        testSet = Misc.loadValuesSet("iris_test.txt");

        trainSet = Misc.shuffleList(trainSet);

        // Debug to check if we load properly
        /*
        int zeroCount = 0;
        for (DataPoint dataPoint : trainSet) {
            int dValue = dataPoint.getD_value();
            System.out.println(dValue + (dValue == 0 ? firstClassName : secondClassName));
            if(dValue == 0) {
                zeroCount++;
            }
        }
        System.out.println("FirstClass Counted: " + zeroCount);
        */

        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj opór: ");
        float threshold = Float.parseFloat(scanner.nextLine());
        System.out.println("Podaj alfe (stala uczaca): ");
        float alpha = Float.parseFloat(scanner.nextLine());

        Perceptron perceptron = new Perceptron(threshold, alpha, alpha);

        // 1. Train perceptron on train data
        for(DataPoint datapoint : trainSet) {
            perceptron.compute(datapoint, false);
        }
        // 2. Test and get accuracy
        perceptron.resetForTest();
        for(DataPoint datapoint : testSet) {
            perceptron.compute(datapoint, true);
        }
        System.out.println("Accuracy: " + perceptron.getAccuracyResult() * 100 + "%");

        while (true) {
            System.out.println("Podaj " + detectedVectorDimension + " wartosci po przecinku (Przyklad: 4.8,3.4,1.6,0.2 ... ; 7.0,3.2,4.7,1.4) lub 'exit'");
            String inputLine = scanner.nextLine();

            if(inputLine.equals("exit")) {
                break;
            }
            String[] tokens = inputLine.split(",");

            if(tokens.length != detectedVectorDimension) {
                System.err.println("[WARNING] Wymagane jest " + detectedVectorDimension + " wartosci po przecinku");
                continue;
            }

            List<Double> inputValues = new ArrayList<>(tokens.length);
            try {
                for (String token : tokens) {
                    inputValues.add(Double.parseDouble(token));
                }
            } catch (NumberFormatException ne) {
                System.err.println(ne.getMessage() + "\nWrong values input. Example: 2.5,3.2,2.1,4.4");
            }

            int y = perceptron.classify(inputValues);
            System.out.println("Podany wektor nalezy do: " + (y == 0 ? firstClassName : secondClassName));
        }
    }
}