package language_detection;

import java.util.List;

public class Perceptron {
    String assignedClassLangCode = "none";

    double threshold; // opór
    double alfa; // stala uczaca
    double beta; // stala kontrolujaca prog
    double[] weightsVector;

    int result_Valid;
    int result_Total;


    Perceptron(float threshold, float alpha, float beta) {
        this.threshold = threshold;
        this.alfa = alpha;
        this.beta = beta;

        this.weightsVector = Misc.getRandomWeightVector(Main.detectedVectorDimension, -0.5, 0.5);
        this.weightsVector = Misc.normalize(weightsVector);

        //System.out.println("DEBUG: " + weightsVector + " weight vector");
    }

    public void setAssignedClassLangCode(String assignedClassLangCode) {
        this.assignedClassLangCode = assignedClassLangCode;
    }

    public void setAlpha(double alpha) {
        this.alfa = alpha;
    }

    public void setTheta(double threshold) {
        this.threshold = threshold;
    }

    public int classify(double[] inputVectorValues) {
        double scalarResult = 0;
        try {
            scalarResult = Misc.scalarMultiply(inputVectorValues, weightsVector);
        } catch (Exception e) {
            System.err.println("Error in classify of Perceptron with values: " + inputVectorValues);
        }

        //System.out.println("DEBUG classify: Opór(" + threshold + ") Scalar: " + scalarResult);
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
        double[] inputVectorVectorValues = inputVector.getVectorValues();

        for (int i = 0; i < weightsVector.length; i++) {
            weightsVector[i] = weightsVector[i] + (alfa * (d - y) * inputVectorVectorValues[i]);
        }

        threshold = threshold - (beta * (d - y));

        //System.out.println("DEBUG: Updated weights -> " + weightsVector);
        //System.out.println("DEBUG: Updated threshold -> " + threshold);
    }
}
