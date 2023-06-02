package ScheduleMip.Data.ScheduleMip.Data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class HashData {
    //装备-弧段列表，由索引查找
    HashMap<Integer, ArrayList<Integer>> arcsByEquips;
    //卫星-弧段列表，由索引查找
    HashMap<Integer, ArrayList<Integer>> arcsByStars;
    //弧段-装备索引
    HashMap<Integer, Integer> equByArc;
    //弧段-卫星索引
    HashMap<Integer, Integer> starByArc;

    public HashData(HashMap<Integer, ArrayList<Integer>> arcsByEquips,
                    HashMap<Integer, ArrayList<Integer>> arcsByStars,
                    HashMap<Integer, Integer> equByArc,
                    HashMap<Integer, Integer> starByArc) {
        this.arcsByEquips = arcsByEquips;
        this.arcsByStars = arcsByStars;
        this.equByArc = equByArc;
        this.starByArc = starByArc;
    }

    public ArrayList<Integer> getArcsByEqu(int equIndex) {
        return arcsByEquips.get(equIndex);
    }

    public ArrayList<Integer> getArcsByStar(int starIndex) {
        return arcsByStars.get(starIndex);
    }

    public int getEquByArc(int arcIndex) {
        return equByArc.get(arcIndex);
    }

    public int getStarByArc(int arcIndex) {
        return starByArc.get(arcIndex);
    }
}
