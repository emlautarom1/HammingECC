package hamming.lib.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndicesProvider {

    /*
     * Important: 1-Based lists
     *
     * 1 -> [1], 3, 5, 7, 9,...
     * 2 -> [2], 3, 6, 7, 10, 11,...
     * 4 -> [4], 5, 6, 7, 12, 13, 14, 15,...
     *
     * Should ingnore the first of each list (surrounded by [])!
     * The limit for each list is the hamming level!
     * The limit of indices is also the hamming level!
     *
     * */

    private final int maxIndex;
    private final Map<Integer, List<Integer>> memoizations;

    public IndicesProvider(int maxIndex) {
        this.maxIndex = maxIndex;
        this.memoizations = new HashMap<>(maxIndex);
    }

    public List<Integer> getParityIndices(int index) {
        return memoizations.computeIfAbsent(index, this::calculateParityIndices);
    }

    private List<Integer> calculateParityIndices(int index) {
        // If the index is equals to maxIndex, the "Parity Bit" should be ignored
        // Therefore, the "parity indices" lists is empty
        if (index == maxIndex) {
            return new ArrayList<>(0);
        }

        List<Integer> parityIndices = new ArrayList<>(maxIndex);
        int current = index;
        while (current <= maxIndex) {
            for (int i = 0; i < index; i++) {
                parityIndices.add(current + i);
            }
            current += (index * 2);
        }
        // The first index is ignored!
        parityIndices.remove(0);
        return parityIndices;
    }

}
