package app.services;

import java.util.BitSet;

import static app.services.Indexer.isPowerOfTwo;
import static app.services.Indexer.twoPowers;

public class Hamming {

    public static byte[] encode(byte[] bytes, int hammingLevel) {
        int chunkSize = chunkSize(hammingLevel);

        BitSet sourceBits = BitSet.valueOf(bytes);

        int bufferSize = (sourceBits.size() / chunkSize) * hammingLevel;
        // No need for manual padding since is automatic with BitSet.toByteArray()
        BitSet outputBits = new BitSet(bufferSize);

        int outOff = 0;
        for (int srcOff = 0; srcOff < sourceBits.size(); srcOff += chunkSize) { // Ex: 0 -> 4 -> 8 -> ...
            writeHamming(hammingLevel, sourceBits, outputBits, srcOff, outOff);
            outOff += hammingLevel; // Ex: 0 -> 7 -> 14 -> ...
        }

        // Debug print
        // printBitSet(sourceBits, 8);
        // printBitSet(outputBits, 16);
        return outputBits.toByteArray();
    }

    private static void writeHamming(int hammingLevel, BitSet src, BitSet out, int srcOff, int outOff) {
        placeDataBits(hammingLevel, src, out, srcOff, outOff);
        placeParityBits(hammingLevel, out, outOff);
    }

    // Writtes in the output BitSet all the bits that are NOT powers of 2.
    private static void placeDataBits(int hammingLevel, BitSet src, BitSet out, int srcOff, int outBff) {
        int readIndex = 0;
        for (int writeIndex = 0; writeIndex < hammingLevel; writeIndex++) {
            if (!isPowerOfTwo(writeIndex + 1)) {
                if (src.get(srcOff + readIndex)) {
                    out.set(outBff + writeIndex);
                }
                readIndex++;
            }
        }
    }

    private static void placeParityBits(int hammingLevel, BitSet bff, int bffOff) {
        for (int bitIndex = 0; bitIndex < hammingLevel; bitIndex++) {
            if (isPowerOfTwo(bitIndex + 1)) {
                // If is power of 2, calculate the parity through XOR
                boolean xorResult = false;
                for (int index : Indexer.getIndices(bitIndex + 1)) {
                    if (index > hammingLevel) {
                        break;
                    } else {
                        xorResult = xorResult ^ bff.get(bffOff + index - 1);
                    }
                }
                // Set the parity bit
                if (xorResult) bff.set(bffOff + bitIndex);
            }
        }
    }

    // Returns the data bits to read (chunkSize) given a hammingLevel
    private static int chunkSize(int hammingLevel) {
        int parityBitCount = 0;
        for (int twoPower : twoPowers) {
            if (twoPower <= hammingLevel) parityBitCount++;
            else break;
        }
        return hammingLevel - parityBitCount;
    }

    private static void printBitSet(BitSet bs, int limit) {
        for (int i = 0; i < limit; i++) {
            if (bs.get(i)) {
                System.out.print(1);
            } else {
                System.out.print(0);
            }
        }
        System.out.println();
    }

}
