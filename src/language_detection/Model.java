package language_detection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Model {
    private Perceptron[] perceptrons;
    private int epochs;

    // Classes - amount of languages, Epochs - how many times we train
    public Model(double alpha, double theta, int epochs, int classes) {
        this.epochs = epochs;
        perceptrons = new Perceptron[classes];

        for(int i = 0; i < classes; i++){
            perceptrons[i] = new Perceptron((float)theta, (float)alpha, (float)alpha);
            perceptrons[i].setAssignedClassLangCode(LanguageClasses.fromId(i).getCode());
            perceptrons[i].setAlpha(alpha);
            perceptrons[i].setTheta(theta);
        }
    }

    public void fit(List<Pair<double[], String>> trainingData) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            Collections.shuffle(trainingData);

            for (Pair<double[], String> sample : trainingData) {
                double[] inputVector = sample.getFirst();
                String trueLabel = sample.getSecond();

                for (Perceptron perceptron : perceptrons) {
                    DataPoint dp = new DataPoint(sample.getFirst(), perceptron.assignedClassLangCode.equals(trueLabel) ? 1 : 0);
                    perceptron.compute(dp, false);
                }
            }

            double trainAccuracy = Main.evaluateModelAccuracy(this, trainingData);
            System.out.printf("Epoch %2d - Training Accuracy: %.2f%%%n", epoch + 1, trainAccuracy);
        }
    }

    public String predict(double[] vector) {
        double maxNet = Double.NEGATIVE_INFINITY;
        String predictedLang = "unknown";

        for (Perceptron perceptron : perceptrons) {
            double net = 0;
            try {
                net = Misc.scalarMultiply(vector, perceptron.weightsVector) - perceptron.threshold;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (net > maxNet) {
                maxNet = net;
                predictedLang = perceptron.assignedClassLangCode;
            }
        }
        return predictedLang;
    }
}