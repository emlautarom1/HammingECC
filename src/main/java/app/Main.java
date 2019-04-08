package app;

import app.services.Hamming;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        System.out.println("Program start");
        long startTime = System.nanoTime();

        int hammingLevel = 7;
        int chunkSize = 4;

        try {
            File file = new File(Main.class.getResource("sample.txt").toURI());
            byte[] data = Files.readAllBytes(file.toPath());

            byte[] outputBytes = Hamming.encode(data, hammingLevel, chunkSize);

            Files.write(Paths.get("encoded.bin"), outputBytes);

            long endTime = System.nanoTime();
            System.out.println("Program finished succesfully in " + (endTime - startTime) / 1000000 + " milliseconds");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
