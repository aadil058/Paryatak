package com.mobilecomputing.paryatak;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacesData {
    private static PlacesData data = null;
    private static HashMap<String, List<String>> db;

    private PlacesData() {
        db = new HashMap<String, List<String>>();
    }

    public static PlacesData getInstance() {
        if(data == null)
            data = new PlacesData();
        return data;
    }

    public static void addCity(String City, List<String> Places) {
        List<String> Places_Copy = new ArrayList<>();
        Places_Copy.addAll(Places);
        db.put(City, Places_Copy);
    }

    public static List<String> getCityPlaces(String City) {
        return db.get(City);
    }

    public static List<String> getAllPlaces() {
        List<String> AllPlaces = new ArrayList<>();

        for(String City : db.keySet()) {
            List<String> places = db.get(City);
            for(String place : places)
                AllPlaces.add(place + ", " + City);
        }

        return AllPlaces;
    }

    public Task<QuerySnapshot> getPlacesFromFirestore() {
        FirestoreService firestoreService = FirestoreService.getInstance();
        return firestoreService.getPlaces();
    }
}