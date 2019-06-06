package hamming.lib;

import hamming.lib.services.Indexer;
import hamming.lib.services.Util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Hamming {
    private final int chunkSize;
    private final int hammingLevel;

    public Hamming(int hammingLevel) {
        Indexer.buildIndices();
        this.hammingLevel = hammingLevel;
        this.chunkSize = Util.calculateChunkSize(hammingLevel);
    }

    public BitSet encode(BitSet sourceBits) {
        int bufferSize = (sourceBits.size() / chunkSize) * hammingLevel;
        // No need for manual padding since is automatic with BitSet.toByteArray()
        BitSet outputBits = new BitSet(bufferSize);

        int outOff = 0;
        for (int srcOff = 0; srcOff < sourceBits.size(); srcOff += chunkSize) { // Ex: 0 -> 4 -> 8 -> ...
            writeDataBits(sourceBits, outputBits, srcOff, outOff);
            writeParityBits(outputBits, outOff);
            outOff += hammingLevel; // Ex: 0 -> 7 -> 14 -> ...
        }

        return outputBits;
    }

    public BitSet decode(BitSet sourceBits, boolean correct) {
        int bufferSize = (sourceBits.size() / hammingLevel) * chunkSize;

        BitSet outputBits = new BitSet(bufferSize);

        int outOff = 0;
        for (int srcOff = 0; srcOff < sourceBits.size(); srcOff += hammingLevel) {
            if (correct) {
                correctErrors(sourceBits, srcOff);
            }
            extractDataBits(sourceBits, outputBits, srcOff, outOff);
            outOff += chunkSize;
        }

        return outputBits;
    }

    // Writtes in the out BitSet all the bits in src in indices that are NOT powers of 2
    private void writeDataBits(BitSet src, BitSet out, int srcOff, int outOff) {
        int readIndex = 0;
        for (int writeIndex = 0; writeIndex < hammingLevel; writeIndex++) {
            if (!Util.isPowerOfTwo(writeIndex + 1)) {
                if (src.get(srcOff + readIndex)) {
                    out.set(outOff + writeIndex);
                }
                readIndex++;
            }
        }
    }

    private void writeParityBits(BitSet bff, int bffOff) {
        for (int bitIndex = 0; bitIndex < hammingLevel; bitIndex++) {
            if (Util.isPowerOfTwo(bitIndex + 1)) {
                // If is power of 2, calculate the parity through XOR
                boolean parityBit = calculateParityBit(bff, bffOff, bitIndex);
                // Set the parity bit
                if (parityBit) bff.set(bffOff + bitIndex);
            }
        }
    }

    // Writtes in the out BitSet all the bits in src which indices are NOT powers of 2
    private void extractDataBits(BitSet src, BitSet out, int srcOff, int outOff) {
        int writeIndex = 0;
        for (int readIndex = 0; readIndex < hammingLevel; readIndex++) {
            if (!Util.isPowerOfTwo(readIndex + 1)) {
                if (src.get(srcOff + readIndex)) {
                    out.set(outOff + writeIndex);
                }
                writeIndex++;
            }
        }
    }

    private void correctErrors(BitSet src, int srcOff) {
        List<Boolean> syndrome = new ArrayList<>(Util.parityBitCount(hammingLevel));
        for (int bitIndex = 0; bitIndex < hammingLevel; bitIndex++) {
            if (Util.isPowerOfTwo(bitIndex + 1)) {
                boolean expectedParityBit = calculateParityBit(src, srcOff, bitIndex);
                boolean originalParityBit = src.get(srcOff + bitIndex);
                // If the expected and the original parity bit differ then add a "true" to the syndrome.
                // If the syndrome is = [false, false, ...] then there was no error
                syndrome.add(expectedParityBit != originalParityBit);
            }
        }
        int indexToFix = syndromeToIndex(syndrome);
        if (indexToFix >= 0) {
            src.flip(srcOff + indexToFix);
        }
    }

    private boolean calculateParityBit(BitSet src, int srcOff, int bitIndex) {
        boolean parityBit = false;
        for (int index : Indexer.getIndices(bitIndex + 1)) {
            if (index > hammingLevel) {
                break;
            } else {
                parityBit ^= src.get(srcOff + index - 1);
            }
        }
        return parityBit;
    }

    private int syndromeToIndex(List<Boolean> syndrome) {
        int index = 0;
        for (int i = 0; i < syndrome.size(); i++) {
            if (syndrome.get(i)) {
                index += Math.pow(2, i);
            }
        }
        return index - 1;
    }
}
