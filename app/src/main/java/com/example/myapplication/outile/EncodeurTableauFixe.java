package com.example.myapplication.outile;

//import org.apache.commons.codec.binary.Base64;
import android.util.Base64;

import java.util.BitSet;
import java.util.List;

public class EncodeurTableauFixe {
    public static String encodeArray(List<Integer> array) {
        BitSet bitSet = new BitSet(60);
        for (int num : array) {
            bitSet.set(num - 1);
        }
        byte[] bytes = bitSet.toByteArray();
        String base64Encoded = Base64.encodeToString(bytes, Base64.NO_WRAP);
        //String base64Encoded = org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);

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
        byte[] decodedBytes = Base64.decode(encodedString, Base64.NO_WRAP);
        //byte[] decodedBytes = Base64.decodeBase64(encodedString);
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

    public static String encodeArray1(List<Integer> array) {
        // Utiliser BitSet pour les nombres de 1 à 60
        BitSet bitSet = new BitSet(60);
        for (int num : array) {
            bitSet.set(num - 1);
        }

        // Convertir le BitSet en byte array
        byte[] bytes = bitSet.toByteArray();

        // Encoder en Base64 URL-safe sans padding
        String base64Encoded = Base64.encodeToString(bytes, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);

        // Assurez-vous que la longueur est de 11 caractères
        if (base64Encoded.length() > 11) {
            return base64Encoded.substring(0, 11);
        } else {
            return base64Encoded;
        }
    }

    public static int[] decodeString1(String encodedString) {
        // Décoder la chaîne Base64 URL-safe sans padding
        byte[] decodedBytes = Base64.decode(encodedString, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);

        // Reconstruire le BitSet à partir du byte array
        BitSet bitSet = BitSet.valueOf(decodedBytes);

        // Extraire les valeurs de 1 à 60 du BitSet
        int[] array = new int[bitSet.cardinality()];
        int index = 0;
        for (int i = 0; i < 60; i++) {
            if (bitSet.get(i)) {
                array[index++] = i + 1;
            }
        }
        return array;
    }


    public static int[] convertListToIntArray(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}
