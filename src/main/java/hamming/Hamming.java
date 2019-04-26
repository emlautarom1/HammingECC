package hamming;

import hamming.lib.Decoder;
import hamming.lib.Encoder;
import hamming.lib.Intoxicator;
import hamming.lib.services.Indexer;
import hamming.lib.services.Util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;

public class Hamming {

    public static long encode(String inputPath, String outputPath, int hammingLevel, boolean intoxicate) throws Exception {
        if (Util.isValidHammingLevel(hammingLevel)) {
            long startTime = System.nanoTime();

            Indexer.buildIndices();

            File file = new File(inputPath);
            byte[] dataBytes = Files.readAllBytes(file.toPath());
            BitSet dataBits = BitSet.valueOf(dataBytes);

            BitSet outputBits = Encoder.encode(dataBits, hammingLevel);

            if (intoxicate) {
                Intoxicator.flipRandomBit(outputBits);
            }

            Files.write(Paths.get(outputPath), outputBits.toByteArray());

            return (System.nanoTime() - startTime);
        } else {
            throw new Exception("Invalid Hamming Level");
        }
    }

    public static long decode(String inputPath, String outputPath, int hammingLevel, boolean correct) throws Exception {
        if (Util.isValidHammingLevel(hammingLevel)) {
            long startTime = System.nanoTime();

            Indexer.buildIndices();

            File file = new File(inputPath);
            byte[] dataBytes = Files.readAllBytes(file.toPath());
            BitSet dataBits = BitSet.valueOf(dataBytes);

            BitSet outputBits = Decoder.decode(dataBits, hammingLevel, correct);

            Files.write(Paths.get(outputPath), outputBits.toByteArray());

            return (System.nanoTime() - startTime);
        } else {
            throw new Exception("Invalid Hamming Level");
        }
    }
}

