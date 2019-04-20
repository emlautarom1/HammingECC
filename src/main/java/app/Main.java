package app;

import app.hamming.Decoder;
import app.hamming.Encoder;
import app.services.Indexer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        System.out.println("Program start");
        long startTime = System.nanoTime();

        // Build indices before encoding/decoding
        Indexer.buildIndices();

        int hammingLevel = 7;

        try {
            File file = new File(Main.class.getResource("sample.txt").toURI());
            byte[] data = Files.readAllBytes(file.toPath());

            byte[] outputBytes = Encoder.encode(data, hammingLevel);

            byte[] decodedData = Decoder.decode(outputBytes, hammingLevel);

            Files.write(Paths.get("data.txt"), decodedData);
//            Files.write(Paths.get("encoded.bin"), outputBytes);

            long endTime = System.nanoTime();
            System.out.println("Program finished succesfully in " + (endTime - startTime) / 1000000 + " milliseconds");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
