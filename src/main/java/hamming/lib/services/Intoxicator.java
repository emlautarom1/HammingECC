package hamming.lib.services;

import java.util.BitSet;

public class Intoxicator {
    private static final int byteLength = 8;

    public static void flipRandomBitsInChunks(BitSet data, int hammingSize) {
        int totalBitCount = data.toByteArray().length * byteLength;
        for (int offset = 0; offset < (totalBitCount - hammingSize); offset += hammingSize) {
            if (Util.randomBool()) {
                int bitIndex = (int) Math.floor(Math.random() * hammingSize);
                int indexToFlip = offset + bitIndex;
                data.flip(indexToFlip);
            }
        }
    }
}
