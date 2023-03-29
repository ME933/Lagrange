package Data;

import Solve.MIPStart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ComData {
    //弧段对应卫星
    HashMap<Integer,Integer> mapArcStar;
    //弧段对应升降轨
    HashMap<Integer, String> mapArcRai;
    //弧段对应装备
    HashMap<Integer,Integer> mapArcEqu;

    //冲突列表
    ArrayList<int[]> conArc;
    //各弧段冲突数
    HashMap<Integer,Integer> eachArcConNum;

    int arcNum;
    int starNum;
    int conNum;
    //任务要求圈次数
    int taskReq;

    //卫星-弧段列表
    HashMap<Integer,ArrayList<Integer>> mapStarArc;
    //装备-弧段列表
    HashMap<Integer,ArrayList<Integer>> mapEquArc;

    //维护子问题，结构y-[<[冲突对1]...,[冲突对n]>,<[跨卫星冲突index，跨卫星冲突弧段index ]...>]
//    HashMap<Integer,LinkedList[]> subQue;
//    ArrayList<Integer> crossCon;
//    int crossNum;
    //维护子问题，结构装备Index-装备冲突对
    HashMap<Integer,LinkedList<Integer>> equConPair;

    //热启动解x
    double[] xMIPStartList;
    //热启动解y
    double[] yMIPStartList;
    //热启动模式
    public String MIPStartMode;

    public ComData(PreData preData, int taskNum,String MIPStartMode){
        this.arcNum = preData.getArcNum();
        this.starNum = preData.getStarNum();
        this.conNum= preData.getConNum();
        this.conArc = preData.getConArc();
        this.mapArcStar = preData.getMapArcStar();
        this.mapArcRai = preData.getMapArcRai();
//        this.crossNum = preData.getCrossNum();
//        this.subQue = preData.getSubQue();
//        this.crossCon = preData.getCrossCon();
        this.mapStarArc = preData.getMapStarArc();
        this.eachArcConNum = preData.getEachArcConNum();
        this.taskReq = taskNum;
        this.mapArcEqu = preData.getMapArcEqu();
        this.mapEquArc = preData.getMapEquArc();
        this.equConPair = preData.getEquConPair();
        if(MIPStartMode.equals("MIPStart")){
            MIPStart mipStart = new MIPStart();
            mipStart.readStartList();
            xMIPStartList = mipStart.getSelectedArcList(preData);
            yMIPStartList = mipStart.getCompletedStarList(preData);
            this.MIPStartMode = MIPStartMode;
        }
    }

    public ArrayList<Integer> getEquArcList(int equIndex){
        return mapEquArc.get(equIndex);
    }

    public double[] getYMIPStartList() {
        return yMIPStartList;
    }

    public double[] getXMIPStartList() {
        return xMIPStartList;
    }

    public int getArcEqu(int arcID){
        return mapArcEqu.get(arcID);
    }
    public ArrayList<Integer> getEquConList(int equIndex){
        return new ArrayList<>(equConPair.get(equIndex));
    }

    public int[] getConPair(int conIndex){
        return conArc.get(conIndex);
    }

    public HashMap<Integer, ArrayList<Integer>> getMapStarArc() {
        return mapStarArc;
    }

    public HashMap<Integer, Integer> getEachArcConNum() {
        return eachArcConNum;
    }

    public int getConNum() {
        return conNum;
    }

    public int getArcNum() {
        return arcNum;
    }

    public int getStarNum() {
        return starNum;
    }

    public int getTask() {
        return taskReq;
    }

//    public ArrayList<Integer> getCrossCon() {
//        return crossCon;
//    }
//
//    public int getCrossNum() {
//        return crossNum;
//    }

    public ArrayList<int[]> getConArc() {
        return conArc;
    }

    //获取弧段对应的卫星Index
    public Integer getArcStar(int arcIndex){
        return this.mapArcStar.get(arcIndex);
    }

    //获取卫星对应的弧段列表
    public ArrayList<Integer> getStarArc(int starID){
        return mapStarArc.get(starID);
    }

//    //获取对应子问题冲突弧段
//    public LinkedList<int[]> subProblemConPair(int starID){
//        return subQue.get(starID)[0];
//    }
//
//    //获取对应子问题松弛弧段
//    public LinkedList<int[]> subProblemConRelax(int starID){
//        return subQue.get(starID)[1];
//    }

}
