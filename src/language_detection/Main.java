package language_detection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static int detectedVectorDimension = TextAnalyzer.ALPHABET_SIZE;

    public static void main(String[] args) {
        int epochs = 6;
        if (args.length > 0) {
            try {
                epochs = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for epochs. Defaulting to " + epochs);
            }
        }

        // 1. Load Training and Test Data
        List<Pair<double[], String>> trainingData = new ArrayList<>();
        List<Pair<double[], String>> testData = new ArrayList<>();

        // Load both training and test data from the respective folders
        Pair<List<Pair<double[], String>>, List<Pair<double[], String>>> polishData = TextAnalyzer.getLanguageVectors("languages/Polish", "pl");
        trainingData.addAll(polishData.getFirst());
        testData.addAll(polishData.getSecond());

        Pair<List<Pair<double[], String>>, List<Pair<double[], String>>> englishData = TextAnalyzer.getLanguageVectors("languages/English", "eng");
        trainingData.addAll(englishData.getFirst());
        testData.addAll(englishData.getSecond());

        Pair<List<Pair<double[], String>>, List<Pair<double[], String>>> spanishData = TextAnalyzer.getLanguageVectors("languages/Spanish", "esp");
        trainingData.addAll(spanishData.getFirst());
        testData.addAll(spanishData.getSecond());

        System.out.println("Loaded " + trainingData.size() + " training samples.");
        System.out.println("Loaded " + testData.size() + " test samples.");

        // 2. Initialize and Train Model
        Model model = new Model(0.1, 0.5, epochs, 3);
        model.fit(trainingData);

        System.out.println("Training complete. Now testing...");

        // 3. Calculate accuracy
        double accuracy = evaluateModelAccuracy(model, testData);
        System.out.println("Test accuracy: " + accuracy);

        // 4. Test Model with user's input
        Scanner scanner = new Scanner(System.in);
        boolean exitLoop = false;

        while (!exitLoop) {
            System.out.println("\nEnter a sentence to predict its language (or '-1' to exit):");

            StringBuilder inputText = new StringBuilder();

            while (true) {
                String line = scanner.nextLine();

                if (line.equalsIgnoreCase("-1")) {
                    scanner.close();
                    exitLoop = true;
                    break;
                }

                if (line.trim().isEmpty()) {
                    break;
                }

                inputText.append(line).append(" ");
            }

            String finalInputText = inputText.toString().trim();

            double[] testVector = TextAnalyzer.extractLetterFrequenciesFromString(finalInputText);
            String predictedLanguage = model.predict(testVector);

            System.out.println("Predicted Language: " + predictedLanguage);
        }

        scanner.close();
        System.out.println("Exiting program.");
    }

    public static double evaluateModelAccuracy(Model model, List<Pair<double[], String>> testData) {
        int correctPredictions = 0;
        int totalPredictions = testData.size();

        for (Pair<double[], String> testSample : testData) {
            double[] testVector = testSample.getFirst();
            String actualLanguage = testSample.getSecond();

            String predictedLanguage = model.predict(testVector);

            if (predictedLanguage.equals(actualLanguage)) {
                correctPredictions++;
            }
        }

        return (double) correctPredictions / (double)totalPredictions * 100.0;
    }
}