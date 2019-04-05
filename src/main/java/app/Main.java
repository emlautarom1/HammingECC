package app;

import app.services.Hamming;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        int hammingLevel = 7;
        int chunkSize = 4;

        try {
            File file = new File(Main.class.getResource("sample.txt").toURI());
            byte[] data = Files.readAllBytes(file.toPath());

            byte[] outputBytes = Hamming.encode(data, hammingLevel, chunkSize);

            Files.write(Paths.get("encoded.txt"), outputBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
