package com.example.myapplication.outile;

import org.apache.commons.codec.binary.Base64;

import java.util.BitSet;
public class EncodeurTableauFixe {
    public static String encodeArray(int[] array) {
        BitSet bitSet = new BitSet(60);
        for (int num : array) {
            bitSet.set(num - 1);
        }
        byte[] bytes = bitSet.toByteArray();
        String base64Encoded = org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);

        if (base64Encoded.length() > 11) {
            return base64Encoded.substring(0, 11);
        } else if (base64Encoded.length() < 11) {
            return String.format("%-11s", base64Encoded).replace(' ', '0');
        } else {
            return base64Encoded;
        }
    }
    public static int[] decodeString(String encodedString) {
        encodedString = encodedString.replace("0", "").trim();
        byte[] decodedBytes = Base64.decodeBase64(encodedString);
        BitSet bitSet = BitSet.valueOf(decodedBytes);

        int[] array = new int[bitSet.cardinality()];
        int index = 0;
        for (int i = 0; i < 60; i++) {
            if (bitSet.get(i)) {
                array[index++] = i + 1;
            }
        }
        return array;
    }
}
