package language_detection;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class TextAnalyzer {
    public static final int ALPHABET_SIZE = 26;

    public static void main(String[] args) throws IOException {
        String folderPath = "languages";
        List<Path> allFiles = new ArrayList<>();

        Files.walk(Paths.get(folderPath))
                .filter(Files::isRegularFile)
                .forEach(allFiles::add);

        // 80% treningowe | 20% testowe
        Collections.shuffle(allFiles);
        int splitIndex = (int) (allFiles.size() * 0.8);
        List<Path> trainFiles = allFiles.subList(0, splitIndex);
        List<Path> testFiles = allFiles.subList(splitIndex, allFiles.size());

        System.out.println("Rozmiar zbioru treningowego: " + trainFiles.size() + " plików");
        System.out.println("Rozmiar zbioru testowego: " + testFiles.size() + " plików");

        System.out.println("\n--> Wektory liter dla zbioru treningowego: ");
        for (Path file : trainFiles) {
            double[] vector = extractLetterFrequencies(file);
            System.out.println(file.getFileName() + " -> " + Arrays.toString(vector));
        }
    }

    private static double[] extractLetterFrequencies(Path filePath) throws IOException {
        double[] frequencies = new double[ALPHABET_SIZE];

        String content = Files.readString(filePath).toLowerCase();

        for (char c : content.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                frequencies[c - 'a']++; // c - 'a' zmienia litery a-z na indeksy 0-25 w tablicy frequencies[].
            }
        }

        return frequencies;
    }

    public static double[] extractLetterFrequenciesFromString(String text) {
        double[] frequencies = new double[ALPHABET_SIZE];

        text = text.toLowerCase();
        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                frequencies[c - 'a']++;
            }
        }
        return Misc.normalize(frequencies);
    }

    // Get training and test data from the given directory
    public static Pair<List<Pair<double[], String>>, List<Pair<double[], String>>> getLanguageVectors(String folderPath, String languageCode) {
        List<Path> allFiles = new ArrayList<>();
        try {
            Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .forEach(allFiles::add);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Shuffle files and split into training and testing sets (80%/20%)
        Collections.shuffle(allFiles);
        int splitIndex = (int) (allFiles.size() * 0.8);
        List<Path> trainFiles = allFiles.subList(0, splitIndex);
        List<Path> testFiles = allFiles.subList(splitIndex, allFiles.size());

        List<Pair<double[], String>> trainVectors = new ArrayList<>();
        List<Pair<double[], String>> testVectors = new ArrayList<>();

        for (Path file : trainFiles) {
            double[] vector = null;
            try {
                vector = extractLetterFrequencies(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            vector = Misc.normalize(vector);
            trainVectors.add(new Pair<>(vector, languageCode));
        }

        for (Path file : testFiles) {
            double[] vector = null;
            try {
                vector = extractLetterFrequencies(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            vector = Misc.normalize(vector);
            testVectors.add(new Pair<>(vector, languageCode));
        }

        return new Pair<>(trainVectors, testVectors);
    }
}