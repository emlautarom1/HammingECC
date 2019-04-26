package lib;

import lib.services.Indexer;
import lib.services.Intoxicator;
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
        Indexer.buildIndices();
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

        BitSet dataBits = BitSet.valueOf(dataBytes);
        BitSet outputBits = Encoder.encode(dataBits, hammingLevel);

        Intoxicator.flipRandomBit(outputBits);

        BitSet decodedBits = Decoder.decode(outputBits, hammingLevel, true);

        assertEquals(dataBits, decodedBits);

        System.out.println(
                "Hamming " + hammingLevel + " for file " + fileName + " took " + (System.nanoTime() - startTime) / 10000 + " ms"
        );
    }
}
