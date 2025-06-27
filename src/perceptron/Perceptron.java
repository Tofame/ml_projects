package perceptron;

import java.util.ArrayList;
import java.util.List;

public class Perceptron {
    float threshold; // opór
    float alfa; // stala uczaca
    float beta; // stala kontrolujaca prog
    List<Double> weightsVector;

    int result_Valid;
    int result_Total;


    Perceptron(float threshold, float alpha, float beta) {
        this.threshold = threshold;
        this.alfa = alpha;
        this.beta = beta;
        this.weightsVector = Misc.getRandomWeightVector(Main.detectedVectorDimension, 1.0 , 10.0);
        System.out.println("DEBUG: " + weightsVector + " weight vector");
    }

    public int classify(List<Double> inputVectorValues) {
        double scalarResult = 0;
        try {
            scalarResult = Misc.scalarMultiply(inputVectorValues, weightsVector);
        } catch (Exception e) {
            System.err.println("Error in classify of Perceptron with values: " + inputVectorValues);
        }

        System.out.println("DEBUG classify: Opór(" + threshold + ") Scalar: " + scalarResult);
        return scalarResult >= threshold ? 1 : 0;
    }

    // Wyznaczanie y i przejście do learn
    public void compute(DataPoint inputVector, boolean isTest) {
        try {
            int d = inputVector.getD_value(); // d: 0 or 1, earlier set based on class name

            double scalarResult = Misc.scalarMultiply(inputVector.getVectorValues(), weightsVector);
            int y = scalarResult >= threshold ? 1 : 0;

            // Valid prediction
            if (y == d) {
                result_Valid++;
                result_Total++;
                return;
            }

            // Training not valid, update threshold and weight vector
            if(!isTest) {
                learn(inputVector, y);
            }
            result_Total++;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void resetForTest() {
        result_Valid = 0;
        result_Total = 0;
    }

    public double getAccuracyResult() {
        return (double)result_Valid / (double)result_Total;
    }

    // Update Wektor wag, opór
    private void learn(DataPoint inputVector, int y) {
        int d = inputVector.getD_value();
        List<Double> inputVectorVectorValues = inputVector.getVectorValues();

        for (int i = 0; i < weightsVector.size(); i++) {
            weightsVector.set(i, weightsVector.get(i) + (alfa * (d - y) * inputVectorVectorValues.get(i)));
        }

        threshold = threshold - (beta * (d - y));

        System.out.println("DEBUG: Updated weights -> " + weightsVector);
        System.out.println("DEBUG: Updated threshold -> " + threshold);
    }
}
