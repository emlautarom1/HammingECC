package app.lib;

import app.lib.services.Indexer;
import app.lib.services.Util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Decoder {

    public static BitSet decode(BitSet sourceBits, int hammingLevel, boolean correct) {
        int chunkSize = Util.calculateChunkSize(hammingLevel);

        int bufferSize = (sourceBits.size() / hammingLevel) * chunkSize;

        BitSet outputBits = new BitSet(bufferSize);

        int outOff = 0;
        for (int srcOff = 0; srcOff < sourceBits.size(); srcOff += hammingLevel) {
            if (correct) {
                correctErrors(hammingLevel, sourceBits, srcOff);
            }
            placeDataBits(hammingLevel, sourceBits, outputBits, srcOff, outOff);
            outOff += chunkSize;
        }

//        Debug prints:
//        System.out.println("Encoded and corrected bits:");
//        Util.printBitSet(sourceBits, 16);
//        System.out.println("Decoded bits:");
//        Util.printBitSet(outputBits, 8);

        return outputBits;
    }

    private static void correctErrors(int hammingLevel, BitSet src, int srcOff) {
        List<Boolean> syndrome = new ArrayList<>(Util.parityBitCount(hammingLevel));
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
        if (indexToFix >= 0) {
//            System.out.println("Error found in bit " + (srcOff + indexToFix));
            src.flip(srcOff + indexToFix);
        }
    }

    private static int syndromeToIndex(List<Boolean> syndrome) {
        int index = 0;
        for (int i = 0; i < syndrome.size(); i++) {
            if (syndrome.get(i)) {
                index += Math.pow(2, i);
            }
        }
        return index - 1;
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
