package lib;

import lib.services.Indexer;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.assertEquals;

public class EncoderTest {

    @Before
    public void setUp() throws Exception {
        Indexer.buildIndices();
    }

    @Test
    public void encode_4_bits() {
        int hammingLevel = 7;

        // inputBits looks like: [1, 0, 1, 0]
        BitSet inputBits = new BitSet(4);
        inputBits.set(0);
        inputBits.set(2);

        BitSet outputBits = Encoder.encode(inputBits, hammingLevel);

        // expectedBits looks like: [1, 0, 1, 1, 0, 1, 0]
        boolean[] expectedBits = {true, false, true, true, false, true, false};
        for (int i = 0; i < 7; i++) {
            assertEquals(expectedBits[i], outputBits.get(i));
        }
    }
}