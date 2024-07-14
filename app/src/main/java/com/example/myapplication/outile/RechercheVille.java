package com.example.myapplication.outile;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RechercheVille {
    public static void main(String[] args) {
        String filePath = "ficher.csv";
        String cityName = "Anta";
        List<String[]> results = searchCity(filePath, cityName);

        if (!results.isEmpty()) {
            for (String[] row : results) {
                System.out.println("ID: " + row[0] + ", Latitude: " + row[1] + ", Longitude: " + row[2] + ", Name: " + row[3]);
            }
        } else {
            System.out.println("City not found.");
        }
    }

    public static List<String[]> searchCity(String filePath, String cityName) {
        List<String[]> matchingRows = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[3].toLowerCase().contains(cityName.toLowerCase())) {
                    matchingRows.add(nextLine);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return matchingRows;
    }
}
