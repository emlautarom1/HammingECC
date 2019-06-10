package lib;

import hamming.lib.Hamming;
import hamming.lib.services.Intoxicator;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.BitSet;

import static org.junit.Assert.assertEquals;

public class E2ETest {
    private byte[] dataBytes;
    private static final String fileName = "small.txt";

    @Before
    public void setUp() throws Exception {
        File file = new File(this.getClass().getResource(fileName).toURI());
        this.dataBytes = Files.readAllBytes(file.toPath());
    }

    @Test
    public void encodeDecode7() {
        encode(7);
    }

    @Test
    public void encodeDecode32() {
        encode(32);
    }

    @Test
    public void encodeDecode1024() {
        encode(1024);
    }

    @Test
    public void encodeDecode32768() {
        encode(32768);
    }

    private void encode(int hammingLevel) {
        long startTime = System.nanoTime();

        Hamming hammingUtility = new Hamming(hammingLevel);

        BitSet dataBits = BitSet.valueOf(dataBytes);
        BitSet outputBits = hammingUtility.encode(dataBits);

        Intoxicator.flipRandomBitsInChunks(outputBits, hammingLevel);

        byte[] outputBytes = outputBits.toByteArray();

        BitSet encodedBits = BitSet.valueOf(outputBytes);
        BitSet decodedBits = hammingUtility.decode(encodedBits, true);

        assertEquals(dataBits, decodedBits);

        System.out.println(
                "Hamming " + hammingLevel + " for file " + fileName + " took " + (System.nanoTime() - startTime) / 1000000 + " ms"
        );
    }
}
