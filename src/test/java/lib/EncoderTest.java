package lib;

import hamming.lib.Hamming;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertEquals;

public class EncoderTest {

    @Test
    public void encode_4_bits() {
        int hammingLevel = 7;
        Hamming hammingUtility = new Hamming(hammingLevel);

        // inputBits looks like: [1, 0, 1, 0]
        BitSet inputBits = new BitSet(4);
        inputBits.set(0);
        inputBits.set(2);

        BitSet outputBits = hammingUtility.encode(inputBits);

        // expectedBits looks like: [1, 0, 1, 1, 0, 1, 0]
        boolean[] expectedBits = {true, false, true, true, false, true, false};
        for (int i = 0; i < 7; i++) {
            assertEquals(expectedBits[i], outputBits.get(i));
        }
    }
}