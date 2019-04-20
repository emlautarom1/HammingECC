package app.services;

import java.util.*;

public class Indexer {

    private static final Map<Integer, List<Integer>> indices = new HashMap<>();

    public static void buildIndices() {
        for (int pow : Util.twoPowers) {
            indices.put(pow, indexer(pow));
        }
    }

    public static List<Integer> getIndices(int powerOfTwo) {
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
}
