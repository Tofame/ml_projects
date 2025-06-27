package language_detection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BookDownloader {
    public static void main(String[] args) {
        String[][] books = {
                // Polish books
                {
                        "https://www.gutenberg.org/cache/epub/31536/pg31536.txt", // Pan Tadeusz - Adam Mickiewicz
                        "https://wolnelektury.pl/media/book/txt/skram-profesor-hieronimus.txt",
                        "https://wolnelektury.pl/media/book/txt/bulhakow-fatalne-jaja.txt",
                        "https://wolnelektury.pl/media/book/txt/doyle-dolina-trwogi.txt",
                        "https://wolnelektury.pl/media/book/txt/nachalnik-rozpruwacze.txt",
                },
                // English books
                {
                        "https://www.gutenberg.org/files/1342/1342-0.txt", // Pride and Prejudice - Jane Austen
                        "https://www.gutenberg.org/files/84/84-0.txt", // Frankenstein - Mary Shelley
                        "https://www.gutenberg.org/files/2701/2701-0.txt", // Moby-Dick - Herman Melville
                        "https://www.gutenberg.org/files/11/11-0.txt", // Alice's Adventures in Wonderland - Lewis Carroll
                        "https://www.gutenberg.org/files/2600/2600-0.txt"  // War and Peace - Leo Tolstoy
                },
                // Spanish books
                {
                        "https://www.gutenberg.org/files/2000/2000-0.txt", // Don Quijote - Miguel de Cervantes (Part 1)
                        "https://www.gutenberg.org/cache/epub/49836/pg49836.txt",
                        "https://www.gutenberg.org/cache/epub/60464/pg60464.txt",
                        "https://www.gutenberg.org/cache/epub/49756/pg49756.txt",
                        "https://www.gutenberg.org/cache/epub/44529/pg44529.txt",
                }
        };

        String[] languages = {"Polish", "English", "Spanish"};

        for (int langIdx = 0; langIdx < books.length; langIdx++) {
            for (int bookIdx = 0; bookIdx < books[langIdx].length; bookIdx++) {
                String directory = "languages/" + languages[langIdx];
                String fileName = "book_" + (bookIdx + 1) + ".txt";
                String fullPath = directory + "/" + fileName;

                try {
                    // Create directory if it doesn't exist
                    Files.createDirectories(Paths.get(directory));

                    // Get absolute path
                    File file = new File(fullPath);
                    String absolutePath = file.getAbsolutePath();

                    // Download the file
                    URL url = new URL(books[langIdx][bookIdx]);
                    ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                    try (FileOutputStream fos = new FileOutputStream(file);
                         FileChannel fileChannel = fos.getChannel()) {
                        fileChannel.transferFrom(rbc, 0, Long.MAX_VALUE);
                    }
                    rbc.close();
                    System.out.println("Downloaded: " + absolutePath);  // Print full path
                } catch (IOException e) {
                    System.err.println("Error downloading: " + books[langIdx][bookIdx] + " - " + e.getMessage());
                }
            }
        }
    }
}