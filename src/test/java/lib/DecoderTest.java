package lib;

import lib.services.Indexer;
import lib.services.Intoxicator;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

public class DecoderTest {

    @Before
    public void setUp() throws Exception {
        Indexer.buildIndices();
    }

    @Test
    public void decode7BitsNoCorrection() {
        int hammingLevel = 7;
        boolean correct = false;

        // inputBits looks like: [1, 0, 1, 1, 0, 1, 0]
        BitSet inputBits = new BitSet(7);
        inputBits.set(0);
        inputBits.set(2);
        inputBits.set(3);
        inputBits.set(5);

        BitSet outputBits = Decoder.decode(inputBits, hammingLevel, correct);

        // expectedBits looks like: [1, 0, 1, 0]
        boolean[] expectedBits = {true, false, true, false};
        for (int i = 0; i < 4; i++) {
            assertEquals(expectedBits[i], outputBits.get(i));
        }
    }

    @Test
    public void decode7BitsCorrection() {
        int hammingLevel = 7;
        boolean correct = true;

        // inputBits looks like: [1, 0, 1, 1, 0, 1, 0]
        BitSet inputBits = new BitSet(7);
        inputBits.set(0);
        inputBits.set(2);
        inputBits.set(3);
        inputBits.set(5);

        Intoxicator.flipRandomBit(inputBits);

        BitSet outputBits = Decoder.decode(inputBits, hammingLevel, correct);

        // expectedBits looks like: [1, 0, 1, 0]
        boolean[] expectedBits = {true, false, true, false};
        for (int i = 0; i < 4; i++) {
            assertEquals(expectedBits[i], outputBits.get(i));
        }
    }
}