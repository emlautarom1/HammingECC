package app.services;

import java.util.*;

public class Indexer {

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

    private static final Map<Integer, List<Integer>> indices = new HashMap<>();

    public static void buildIndices() {
        for (int pow : twoPowers) {
            indices.put(pow, indexer(pow));
        }
    }

    static List<Integer> getIndices(int powerOfTwo) {
        return indices.get(powerOfTwo);
    }

    private static List<Integer> indexer(int base) {
        int limit = (int) Math.pow(2, 15);
        List<Integer> indices = new ArrayList<>(limit);
        int current = base;
        boolean ignoreFirst = true;
        while (current < limit) {
            for (int i = 0; i < base; i++) {
                if (!ignoreFirst) {
                    indices.add(current + i);
                } else {
                    ignoreFirst = false;
                }
            }
            current += (base * 2);
        }
        return indices;
    }

    static boolean isPowerOfTwo(int n) {
        return Arrays.binarySearch(twoPowers, n) >= 0;
    }
}
