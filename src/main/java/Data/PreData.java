package Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

public class PreData {
    //卫星列表
    ArrayList<Integer> starList;
    //装备列表
    ArrayList<Integer> equList;
    //弧段列表
    ArrayList<Integer> arcList;

    //由ID查找索引
    HashMap<Integer,Integer> starIndexMap;
    HashMap<Integer,Integer> equIndexMap;
    HashMap<Integer,Integer> arcIndexMap;


    //弧段信息，弧段ID-弧段信息([弧段ID，卫星ID，装备ID，开始时间，结束时间, 上升下降])
    HashMap<Integer,Integer> mapArcStar;
    HashMap<Integer,Integer> mapArcEqu;
    HashMap<Integer,Date[]> mapArcTime;
    HashMap<Integer, String> mapArcRai;

    //以下数据均通过索引存储
    //卫星-弧段列表
    HashMap<Integer,ArrayList<Integer>> mapStarArc;
    //装备-弧段列表
    HashMap<Integer,ArrayList<Integer>> mapEquArc;
    //卫星，装备，弧段列表
    HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> mapStarEquArc;
    //冲突列表
    ArrayList<int[]> conArc;
    //各弧段冲突数
    HashMap<Integer,Integer> eachArcConNum;
    //维护子问题，结构y-[<[冲突对1]...,[冲突对n]>,<[跨卫星冲突index，跨卫星冲突弧段index ]...>]
//    HashMap<Integer, LinkedList[]> subQue;
//    ArrayList<Integer> crossCon;
    //维护子问题，结构装备Index-装备冲突对
    HashMap<Integer,LinkedList<Integer>> equConPair;

    int arcNum;
    int starNum;
    int conNum;
    int crossNum;

    public PreData(ArrayList<Integer> inStarList,
                   ArrayList<Integer> inEquList,
                   ArrayList<Integer> inArcList,
                   HashMap<Integer,ArrayList<Integer>> inMapStarArc,
                   HashMap<Integer,ArrayList<Integer>> inMapEquArc,
                   HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> inMapStarEquArc,
                   ArrayList<int[]> inConArc,
                   HashMap<Integer,Integer> inEachArcConNum,
                   HashMap<Integer,Integer> inStarIndexMap,
                   HashMap<Integer,Integer> inEquIndexMap,
                   HashMap<Integer,Integer> inArcIndexMap,
                   HashMap<Integer,Integer> inMapArcStar,
                   HashMap<Integer,Integer> inMapArcEqu,
                   HashMap<Integer,Date[]> inMapArcTime,
                   HashMap<Integer, String> inMapArcRai,
                   HashMap<Integer,LinkedList<Integer>> inEquConPair){
        this.starList = inStarList;
        this.eachArcConNum = inEachArcConNum;
        this.equList = inEquList;
        this.arcList = inArcList;
        this.mapStarArc = inMapStarArc;
        this.mapEquArc = inMapEquArc;
        this.mapStarEquArc = inMapStarEquArc;
        this.conArc = inConArc;
        this.starIndexMap = inStarIndexMap;
        this.equIndexMap = inEquIndexMap;
        this.arcIndexMap = inArcIndexMap;
        this.arcNum = this.arcList.size();
        this.starNum = this.starList.size();
        this.mapArcStar = inMapArcStar;
        this.mapArcEqu = inMapArcEqu;
        this.mapArcTime = inMapArcTime;
        this.mapArcRai = inMapArcRai;
        this.equConPair = inEquConPair;
//        this.crossNum = inCrossNum;
//        this.subQue = inSubQue;
//        this.crossCon = inCrossCon;
        conNum = 0;
        conNum = conArc.size();
    }

    public HashMap<Integer, LinkedList<Integer>> getEquConPair() {
        return equConPair;
    }

    public HashMap<Integer, Integer> getEachArcConNum() {
        return eachArcConNum;
    }

    public HashMap<Integer, ArrayList<Integer>> getMapStarArc() {
        return mapStarArc;
    }

//    public ArrayList<Integer> getCrossCon() {
//        return crossCon;
//    }
//
//    public HashMap<Integer, LinkedList[]> getSubQue() {
//        return subQue;
//    }


    public HashMap<Integer, ArrayList<Integer>> getMapEquArc() {
        return mapEquArc;
    }

    public int getConNum() {
        return conNum;
    }

    public int getCrossNum() {
        return crossNum;
    }

    public int getStarNum() {
        return starNum;
    }

    public int getArcNum() {
        return arcNum;
    }

    public HashMap<Integer, Integer> getMapArcStar() {
        return mapArcStar;
    }

    public HashMap<Integer, Integer> getMapArcEqu() {
        return mapArcEqu;
    }

    public HashMap<Integer, String> getMapArcRai() {
        return mapArcRai;
    }

    //获取卫星列表
    public ArrayList<Integer> getStarList() {
        return starList;
    }

    //获取装备列表
    public ArrayList<Integer> getEquList() {
        return equList;
    }

    //获取弧段列表
    public ArrayList<Integer> getArcList() {
        return arcList;
    }

    //获取冲突列表
    public ArrayList<int[]> getConArc() {
        return conArc;
    }

    //获取弧段对应的卫星Index
    public Integer getArcStar(int arcIndex){
        return this.mapArcStar.get(arcIndex);
    }

    //获取弧段对应的装备Index
    public Integer getArcEqu(int arcIndex){
        return this.mapArcEqu.get(arcIndex);
    }

    //获取弧段对应的时间
    public Date[] getArcTime(int arcIndex){
        return this.mapArcTime.get(arcIndex);
    }

    //获取弧段对应的升降轨
    public String getArcRai(int arcIndex){
        return this.mapArcRai.get(arcIndex);
    }

    //通过Index获取弧段ID
    public Integer getArcID(int arcIndex){
        return arcList.get(arcIndex);
    }

    //通过Index获取卫星ID
    public Integer getStarID(int starIndex){
        return starList.get(starIndex);
    }

    //通过Index获取装备ID
    public Integer getEquID(int equIndex){
        return equList.get(equIndex);
    }

    public Integer getArcIndex(int arcID){
        return arcIndexMap.get(arcID);
    }

    public Integer getStarIndex(int starID){
        return starIndexMap.get(starID);
    }

//    //通过Index获取冲突列表
//    public ArrayList<Integer> getArcConList(int arcIndex){
//        return conArc.get(arcIndex);
//    }
//
//    //通过序列号获取弧段是否冲突
//    public Integer ifArcCon(int arcIndex, int arcIndexP){
//        if(conArc.get(arcIndex).contains(arcIndex) || conArc.get(arcIndexP).contains(arcIndexP)){
//            return 1;
//        }else{
//            return 0;
//        }
//    }
}
