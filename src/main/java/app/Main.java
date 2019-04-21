package app;

import app.hamming.Decoder;
import app.hamming.Encoder;
import app.hamming.Intoxicator;
import app.services.Indexer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;

public class Main {

    public static void main(String[] args) {
        System.out.println("Program start");
        long startTime = System.nanoTime();

        // Build indices before encoding/decoding
        Indexer.buildIndices();

        int hammingLevel = 7;
        String fileFullName = "big.txt";

        try {
            File file = new File(Main.class.getResource(fileFullName).toURI());
            byte[] dataBytes = Files.readAllBytes(file.toPath());
            BitSet dataBits = BitSet.valueOf(dataBytes);

            BitSet outputBits = Encoder.encode(dataBits, hammingLevel);

            Intoxicator.flipRandomBit(outputBits);

            BitSet decodedBits = Decoder.decode(outputBits, hammingLevel);

            Files.write(Paths.get("r_" + fileFullName), decodedBits.toByteArray());

            long endTime = System.nanoTime();
            System.out.println("Program finished succesfully in " + (endTime - startTime) / 1000000 + " milliseconds");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
