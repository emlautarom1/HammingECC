package app.services;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.BitSet;

public class Hamming {

    public static byte[] encode(byte[] bytes, int level, int chunkSize) {
        if (level != 7 || chunkSize != 4) {
            throw new NotImplementedException();
            // TODO: Check case where the sourceBits size % chunkSize != 0 and add proper padding
        }
        BitSet sourceBits = BitSet.valueOf(bytes);

        int bufferSize = (sourceBits.size() / chunkSize) * level;
        // No need for manual padding since is automatic with BitSet.toByteArray()
        BitSet hammingBitsBuffer = new BitSet(bufferSize);

        int bffOff = 0;
        for (int srcOffset = 0; srcOffset < sourceBits.size(); srcOffset += chunkSize) { // Ex: 0 -> 4 -> 8 -> ...
            writeHamming7(srcOffset, sourceBits, bffOff, hammingBitsBuffer);
            bffOff += level; // Ex: 0 -> 7 -> 14 -> ...
        }

        return hammingBitsBuffer.toByteArray();
    }

    private static void writeHamming7(int srcOff, BitSet src, int bffOff, BitSet buff) {
        // TODO: Make a proper for-loop for Hamming-N
        boolean p1 = src.get(srcOff) & src.get(srcOff + 1) & src.get(srcOff + 3);
        boolean p2 = src.get(srcOff) & src.get(srcOff + 1) & src.get(srcOff + 3);
        boolean p3 = src.get(srcOff) & src.get(srcOff + 1) & src.get(srcOff + 3);

        if (p1) buff.set(bffOff);
        if (p2) buff.set(bffOff + 1);
        if (src.get(0)) buff.set(bffOff + 2);
        if (p3) buff.set(bffOff + 3);
        if (src.get(1)) buff.set(bffOff + 4);
        if (src.get(2)) buff.set(bffOff + 5);
        if (src.get(3)) buff.set(bffOff + 6);
    }

}
