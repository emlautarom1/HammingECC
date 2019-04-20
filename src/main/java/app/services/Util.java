package app.services;

import java.util.Arrays;
import java.util.BitSet;


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

    public static void printBitSet(BitSet bs, int limit) {
        for (int i = 0; i < limit; i++) {
            if (bs.get(i)) {
                System.out.print(1);
            } else {
                System.out.print(0);
            }
        }
        System.out.println();
    }

    // Returns the data bits to read (calculateChunkSize) given a hammingLevel
    public static int calculateChunkSize(int hammingLevel) {
        int parityBitCount = 0;
        for (int twoPower : twoPowers) {
            if (twoPower <= hammingLevel) parityBitCount++;
            else break;
        }
        return hammingLevel - parityBitCount;
    }

    public static boolean isPowerOfTwo(int n) {
        return Arrays.binarySearch(twoPowers, n) >= 0;
    }
}
