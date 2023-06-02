package ScheduleMip.Data.ScheduleMip.Data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class ConflictData {
    //卫星-完全冲突弧段集
    ArrayList<ArrayList<Integer>> starConf;
    //装备-完全冲突弧段集
    ArrayList<ArrayList<Integer>> equConf;
    //弧段冲突数
    HashMap<Integer, Integer> arcConflictNum;
    //卫星-完全冲突弧段集
    HashMap<Integer, ArrayList<ArrayList<Integer>>> eachStarConf;
    //装备-完全冲突弧段集
    HashMap<Integer, ArrayList<ArrayList<Integer>>> eachEquConf;

    public ConflictData(ArrayList<ArrayList<Integer>> starConf,
                        ArrayList<ArrayList<Integer>> equConf,
                        HashMap<Integer, ArrayList<ArrayList<Integer>>> eachStarConf,
                        HashMap<Integer, ArrayList<ArrayList<Integer>>> eachEquConf,
                        HashMap<Integer, Integer> arcConflictNum) {
        this.starConf = starConf;
        this.equConf = equConf;
        this.eachEquConf = eachEquConf;
        this.eachStarConf = eachStarConf;
        this.arcConflictNum = arcConflictNum;
    }

    public int getEquConfNum() {
        return equConf.size();
    }

    public int getStarConfNum() {
        return starConf.size();
    }

    public ArrayList<ArrayList<Integer>> getConSetByStar(int starIndex){
        return eachStarConf.get(starIndex);
    }

    public ArrayList<ArrayList<Integer>> getConSetByEqu(int equIndex){
        return eachEquConf.get(equIndex);
    }
}
