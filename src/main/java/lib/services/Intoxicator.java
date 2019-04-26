package lib.services;

import java.util.BitSet;

public class Intoxicator {
    private static final int byteLength = 8;


    public static void flipRandomBit(BitSet data) {
        int byteIndex = (int) Math.floor(Math.random() * data.toByteArray().length);
        int bitIndex = (int) Math.floor(Math.random() * byteLength);

        int indexToFlip = byteIndex * byteLength + bitIndex;
        data.flip(indexToFlip);

//        System.out.println("Intoxicator flipped bit " + (indexToFlip));
//        System.out.println("Corrupted bits:");
//        Util.printBitSet(data, 16);
    }
}
