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
}
