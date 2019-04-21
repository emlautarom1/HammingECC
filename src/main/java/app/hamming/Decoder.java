package app.hamming;

import app.services.Indexer;
import app.services.Util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Decoder {

    public static byte[] decode(byte[] bytes, int hammingLevel) {
        int chunkSize = Util.calculateChunkSize(hammingLevel);

        BitSet sourceBits = BitSet.valueOf(bytes);

        int bufferSize = (sourceBits.size() / hammingLevel) * chunkSize;

        BitSet outputBits = new BitSet(bufferSize);

        int outOff = 0;
        for (int srcOff = 0; srcOff < sourceBits.size(); srcOff += hammingLevel) {
            writeHamming(hammingLevel, sourceBits, outputBits, outOff, srcOff);
            outOff += chunkSize;
        }

        // Debug print
//        Util.printBitSet(sourceBits, 16);
//        Util.printBitSet(outputBits, 8);

        return outputBits.toByteArray();
    }

    private static void writeHamming(int hammingLevel, BitSet sourceBits, BitSet outputBits, int outOff, int srcOff) {
        // TODO: Add error detection-correction before placing the bits
        correctErrors(hammingLevel, sourceBits, srcOff);
        placeDataBits(hammingLevel, sourceBits, outputBits, srcOff, outOff);
    }

    private static void correctErrors(int hammingLevel, BitSet src, int srcOff) {
        List<Boolean> syndrome = new ArrayList<>();
        for (int bitIndex = 0; bitIndex < hammingLevel; bitIndex++) {
            if (Util.isPowerOfTwo(bitIndex + 1)) {

                boolean expectedParityBit = false;
                boolean originalParityBit = src.get(srcOff + bitIndex);

                for (int index : Indexer.getIndices(bitIndex + 1)) {
                    if (index > hammingLevel) {
                        break;
                    } else {
                        expectedParityBit ^= src.get(srcOff + index - 1);
                    }
                }
                // If the expected and the original parity differ add a "true" to the syndrome.
                // If the syndrome is = [false, false, ...] then there was no error
                syndrome.add(expectedParityBit != originalParityBit);
            }
        }
        int indexToFix = syndromeToIndex(syndrome);
        if (indexToFix != 0) {
            src.flip(indexToFix);
        }
    }

    private static int syndromeToIndex(List<Boolean> syndrome) {
        int index = 0;
        int power = 0;
        for (int i = syndrome.size() - 1; i >= 0; i--) {
            if (syndrome.get(i)) {
                index += Math.pow(2, power);
            }
            power++;
        }
        return index;
    }

    private static void placeDataBits(int hammingLevel, BitSet src, BitSet out, int srcOff, int outOff) {
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
}
