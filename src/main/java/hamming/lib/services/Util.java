package hamming.lib.services;

import java.util.Arrays;

public class Util {

    static final int[] twoPowers = {
            1,
            2,
            4,
            8,
            16,
            32,
            64,
            128,
            256,
            512,
            1024,
            2048,
            4096,
            8192,
            16384,
            32768
    };

    // Returns the data bits to read (calculateChunkSize) given a hammingLevel
    public static int calculateChunkSize(int hammingLevel) {
        return hammingLevel - parityBitCount(hammingLevel);
    }

    public static int parityBitCount(int hammingLevel) {
        int parityBitCount = 0;
        for (int twoPower : twoPowers) {
            if (twoPower <= hammingLevel) parityBitCount++;
            else break;
        }
        return parityBitCount;
    }

    public static boolean isPowerOfTwo(int n) {
        return Arrays.binarySearch(twoPowers, n) >= 0;
    }
}
