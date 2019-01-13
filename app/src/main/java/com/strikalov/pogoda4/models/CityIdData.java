package com.strikalov.pogoda4.models;

import java.util.HashMap;

public class CityIdData {

    private HashMap<Integer, Integer> idData = new HashMap<>();


    public CityIdData(){
        idData.put(0,498817);
        idData.put(1,524901);
        idData.put(2,501175);
        idData.put(3,491422);
    }

    public Integer getId(Integer key) {
        return idData.get(key);
    }
}
