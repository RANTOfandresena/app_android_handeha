package com.example.myapplication.outile;

import java.util.List;

public class Algo {
    public static int compterNumbre(List<Integer> a ,int nombreIsana){
        int nombre=0;
        for (Integer number : a) {
            if (number == nombreIsana) {
                nombre++;
            }
        }
        return nombre;
    }
    public static String printIntArray(List<Integer> array) {
        String a="[";
        for (int i = 0; i < array.size(); i++) {
            a+=String.valueOf(array.get(i));
            if (i < array.size() - 1) {
                a+=", ";
            }
        }
        a+="]";
        return a;
    }
    public static String printIntArray(int[] array) {
        String a="[";
        for (int i = 0; i < array.length; i++) {
            a+=String.valueOf(array[i]);
            if (i < array.length - 1) {
                a+=", ";
            }
        }
        a+="]";
        return a;
    }
}
