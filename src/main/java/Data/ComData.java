package Data;

import IntervalTree.IntervalTree;
import IntervalTree.Interval;
import IntervalTree.DateInterval;
import Solve.MIPStart;
import TimeLine.TimeLine;

import java.text.ParseException;
import java.util.*;

public class ComData {
    //弧段对应卫星
    HashMap<Integer,Integer> mapArcStar;
    //弧段对应升降轨
    HashMap<Integer, String> mapArcRai;
    //弧段对应装备
    HashMap<Integer,Integer> mapArcEqu;
    HashMap<Integer,Date[]> mapArcTime;
    HashMap<Integer,Integer> eachArcConNum;


    int arcNum;
    int starNum;
    int conNum;
    //任务要求圈次数
    int taskReq;
    int equNum;

    //卫星-弧段列表
    HashMap<Integer,ArrayList<Integer>> mapStarArc;
    //装备-弧段列表
    HashMap<Integer,ArrayList<Integer>> mapEquArc;

    //按装备构建区间树
    HashMap<Integer, IntervalTree<Date>> intervalTreeByDev;
    //饱和冲突弧段集合构成的集合
    HashMap<Integer,ArrayList<ArrayList<Integer>>> conArcSetByEqu;

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
        this.mapArcStar = preData.getMapArcStar();
        this.mapArcRai = preData.getMapArcRai();
        this.mapStarArc = preData.getMapStarArc();
        this.taskReq = taskNum;
        this.mapArcEqu = preData.getMapArcEqu();
        this.mapEquArc = preData.getMapEquArc();
        this.mapArcTime = preData.getMapArcTime();
        this.equNum = preData.getEquNum();
        this.MIPStartMode = MIPStartMode;
        this.conArcSetByEqu = preData.getConArcSetByEqu();
        this.eachArcConNum = new HashMap<>();
        if(MIPStartMode.equals("MIPStart")){
            MIPStart mipStart = new MIPStart();
            mipStart.readStartList();
            xMIPStartList = mipStart.getSelectedArcList(preData);
            yMIPStartList = mipStart.getCompletedStarList(preData);
        }
    }

    public  HashMap<Integer,ArrayList<ArrayList<Integer>>> getConArcSet(){
        return conArcSetByEqu;
    }
    public int getEquNum() {
        return equNum;
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

    public int getArcEqu(int arcIndex){
        return mapArcEqu.get(arcIndex);
    }

    //获取弧段对应的时间
    public Date[] getArcTime(int arcIndex){
        return this.mapArcTime.get(arcIndex);
    }

//    public ArrayList<Integer> getEquConList(int equIndex){
//        return new ArrayList<>(equConPair.get(equIndex));
//    }

    public ArrayList<Integer> getConArc(int arcIndex){
        ArrayList<Integer> conArcList = new ArrayList<>();
        Set<Interval<Date>> ids = this.intervalTreeByDev
                .get(getArcEqu(arcIndex))
                .query(new DateInterval(mapArcTime.get(arcIndex)[0], mapArcTime.get(arcIndex)[1], Interval.Bounded.CLOSED, String.valueOf(arcIndex)));
        for (Interval<Date> i : ids) {
            conArcList.add(Integer.parseInt(i.getID()));
        }
        return conArcList;
    }


    public HashMap<Integer, ArrayList<ArrayList<Integer>>> getConArcSetByEqu() {
        return conArcSetByEqu;
    }

    public HashMap<Integer, ArrayList<Integer>> getMapStarArc() {
        return mapStarArc;
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

    //获取弧段对应的卫星Index
    public Integer getArcStar(int arcIndex){
        return this.mapArcStar.get(arcIndex);
    }

    //获取卫星对应的弧段列表
    public ArrayList<Integer> getStarArc(int starID){
        return mapStarArc.get(starID);
    }

    /**
     * @description: 获取与装备对应的弧段数量
     * @param equIndex 装备ID
     * @return 对应ID装备的弧段数量
     */
    public int getEquArcNum(int equIndex){
        return mapEquArc.get(equIndex).size();
    }

    /**
     * @description: 获取与id弧段冲突的弧段数量
     * @param arcIndex 弧段ID
     * @return 弧段ID的冲突数量
     */
    public int getConflictArcNum(int arcIndex) {
        if (!eachArcConNum.containsKey(arcIndex)){
            Date[] arcTime = getArcTime(arcIndex);
            Set<Interval<Date>> ids = this.intervalTreeByDev.get(getArcEqu(arcIndex)).
                    query(new DateInterval(arcTime[0], arcTime[1], Interval.Bounded.CLOSED, String.valueOf(arcIndex)));
            int num = ids.size();
            eachArcConNum.put(arcIndex, num);
            return num;
        }else {
            return eachArcConNum.get(arcIndex);
        }
    }

    /**
     * @description: 获取装备对应的饱和冲突集
     * @param equIndex 装备ID
     * @return 饱和冲突集构成的List
     */
    public ArrayList<ArrayList<Integer>> getConSetByEqu(int equIndex) {
        return conArcSetByEqu.get(equIndex);
    }

}
