package com.proofIt.bicycleInsurance.utils;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class FileReaderUtil {

    public static ArrayList<LinkedHashMap<String, Serializable>> getDataFromSourceFiles(String requestFile) {
        ArrayList<LinkedHashMap<String, Serializable>> insuranceData = new ArrayList<>();

        String fileName = "src/main/resources/static/" + requestFile;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                Gson gson = new Gson();
                LinkedHashMap map = gson.fromJson(line, LinkedHashMap.class);
                insuranceData.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return insuranceData;
    }
}
