package lib;

import hamming.lib.Hamming;
import hamming.lib.services.Intoxicator;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertEquals;

public class DecoderTest {

    @Test
    public void decode7BitsNoCorrection() {
        int hammingLevel = 7;
        Hamming hammingUtility = new Hamming(hammingLevel);

        // inputBits looks like: [1, 0, 1, 1, 0, 1, 0]
        BitSet inputBits = new BitSet(7);
        inputBits.set(0);
        inputBits.set(2);
        inputBits.set(3);
        inputBits.set(5);

        BitSet outputBits = hammingUtility.decode(inputBits, false);

        // expectedBits looks like: [1, 0, 1, 0]
        boolean[] expectedBits = {true, false, true, false};
        for (int i = 0; i < 4; i++) {
            assertEquals(expectedBits[i], outputBits.get(i));
        }
    }

    @Test
    public void decode7BitsCorrection() {
        int hammingLevel = 7;
        Hamming hammingUtility = new Hamming(hammingLevel);

        // inputBits looks like: [1, 0, 1, 1, 0, 1, 0]
        BitSet inputBits = new BitSet(7);
        inputBits.set(0);
        inputBits.set(2);
        inputBits.set(3);
        inputBits.set(5);

        Intoxicator.flipRandomBitsInChunks(inputBits, hammingLevel);

        BitSet outputBits = hammingUtility.decode(inputBits, true);

        // expectedBits looks like: [1, 0, 1, 0]
        boolean[] expectedBits = {true, false, true, false};
        for (int i = 0; i < 4; i++) {
            assertEquals(expectedBits[i], outputBits.get(i));
        }
    }
}