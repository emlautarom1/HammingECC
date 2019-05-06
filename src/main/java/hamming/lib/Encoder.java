package hamming.lib;

import hamming.lib.services.Indexer;
import hamming.lib.services.Util;

import java.util.BitSet;

public class Encoder {

    public static BitSet encode(BitSet sourceBits, int hammingLevel) {
        int chunkSize = Util.calculateChunkSize(hammingLevel);

        int bufferSize = (sourceBits.size() / chunkSize) * hammingLevel;
        // No need for manual padding since is automatic with BitSet.toByteArray()
        BitSet outputBits = new BitSet(bufferSize);

        int outOff = 0;
        for (int srcOff = 0; srcOff < sourceBits.size(); srcOff += chunkSize) { // Ex: 0 -> 4 -> 8 -> ...
            writeHamming(hammingLevel, sourceBits, outputBits, srcOff, outOff);
            outOff += hammingLevel; // Ex: 0 -> 7 -> 14 -> ...
        }

        return outputBits;
    }

    private static void writeHamming(int hammingLevel, BitSet src, BitSet out, int srcOff, int outOff) {
        placeDataBits(hammingLevel, src, out, srcOff, outOff);
        placeParityBits(hammingLevel, out, outOff);
    }

    // Writtes in the output BitSet all the bits that are NOT powers of 2.
    private static void placeDataBits(int hammingLevel, BitSet src, BitSet out, int srcOff, int outOff) {
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

    private static void placeParityBits(int hammingLevel, BitSet bff, int bffOff) {
        for (int bitIndex = 0; bitIndex < hammingLevel; bitIndex++) {
            if (Util.isPowerOfTwo(bitIndex + 1)) {
                // If is power of 2, calculate the parity through XOR
                boolean xorResult = false;
                for (int index : Indexer.getIndices(bitIndex + 1)) {
                    if (index > hammingLevel) {
                        break;
                    } else {
                        xorResult ^= bff.get(bffOff + index - 1);
                    }
                }
                // Set the parity bit
                if (xorResult) bff.set(bffOff + bitIndex);
            }
        }
    }

}
