package ScheduleMip.Data.ScheduleMip.Data;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class IndexData {
    //由ID查找索引
    HashMap<Integer, Integer> starIndexMap;
    HashMap<Integer, Integer> equIndexMap;
    HashMap<Integer, Integer> arcIndexMap;

    public IndexData(HashMap<Integer, Integer> starIndexMap,
                     HashMap<Integer, Integer> equIndexMap,
                     HashMap<Integer, Integer> arcIndexMap) {
        this.starIndexMap = starIndexMap;
        this.equIndexMap = equIndexMap;
        this.arcIndexMap = arcIndexMap;
    }


    //根据ID获得索引
    public int getStarIndexById(int starId) {
        return starIndexMap.get(starId);
    }

    public int getEquIndexById(int equId) {
        return equIndexMap.get(equId);
    }

    public int getArcIndexById(int arcId) {
        return arcIndexMap.get(arcId);
    }

}
