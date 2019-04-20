package app.hamming;

import app.services.Util;

import java.util.BitSet;

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
        placeDataBits(hammingLevel, sourceBits, outputBits, srcOff, outOff);
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
