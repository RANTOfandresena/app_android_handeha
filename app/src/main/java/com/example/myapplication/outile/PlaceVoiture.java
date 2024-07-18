package com.example.myapplication.outile;

import java.util.List;

public class PlaceVoiture {
    public static int[][] generatePlace(int longe, int larg) {
        int[][] matrix = new int[larg][longe];
        for (int i = 0; i < larg; i++) {
            for (int j = 0; j <longe; j++) {
                matrix[i][j] = 0;
            }
        }
        return matrix;
    }
}
