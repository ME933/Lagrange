package ScheduleMip.PrecessData;


import IntervalTree.DateInterval;
import IntervalTree.Interval;
import IntervalTree.IntervalTree;
import ScheduleMip.Data.ConflictData;
import ScheduleMip.Data.EntityData;
import ScheduleMip.Data.HashData;
import ScheduleMip.TimeLine.TimeLine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class ProcessConflict {
    HashData hashData;
    EntityData entityData;

    //卫星-完全冲突弧段集
    ArrayList<ArrayList<Integer>> starConf;
    //装备-完全冲突弧段集
    ArrayList<ArrayList<Integer>> equConf;
    //卫星-完全冲突弧段集
    HashMap<Integer, ArrayList<ArrayList<Integer>>> eachStarConf;
    //装备-完全冲突弧段集
    HashMap<Integer, ArrayList<ArrayList<Integer>>> eachEquConf;
    //按装备构建区间树
    HashMap<Integer, IntervalTree<Date>> intervalTreeByDev;
    //弧段冲突数
    HashMap<Integer, Integer> arcConflictNum;

    public ProcessConflict(HashData hashData, EntityData entityData) {
        this.hashData = hashData;
        this.entityData = entityData;
        this.intervalTreeByDev = new HashMap<>();
        this.arcConflictNum = new HashMap<>();
    }

    public ConflictData createConflict() {
        this.createEquConf();
        this.createStarConf();
        this.createConflictNum();
        return new ConflictData(starConf, equConf, eachStarConf, eachEquConf, arcConflictNum);
    }

    //建立时间轴，求解装备冲突弧段集合
    private void createEquConf() {
        TimeLine timeLine = new TimeLine(entityData);
        equConf = new ArrayList<>();
        eachEquConf = new HashMap<>();
        for (int equIndex = 0; equIndex < entityData.getEquNum(); equIndex++) {
            ArrayList<Integer> arcList = hashData.getArcsByEqu(equIndex);
            timeLine.insertArc(arcList);
            ArrayList<ArrayList<Integer>> conList = timeLine.searchTimeline();
            eachEquConf.put(equIndex, conList);
            equConf.addAll(conList);
            timeLine.refreshTimeLine();
        }
    }

    //建立时间轴，求解卫星冲突弧段集合
    private void createStarConf() {
        TimeLine timeLine = new TimeLine(entityData);
        starConf = new ArrayList<>();
        eachStarConf = new HashMap<>();
        for (int starIndex = 0; starIndex < entityData.getStarNum(); starIndex++) {
            ArrayList<Integer> arcList = hashData.getArcsByEqu(starIndex);
            if(arcList != null){
                timeLine.insertArc(arcList);
                ArrayList<ArrayList<Integer>> conList = timeLine.searchTimeline();
                eachStarConf.put(starIndex, conList);
                starConf.addAll(conList);
                timeLine.refreshTimeLine();
            }
        }
    }

    //构建区间树，计算冲突数
    private void createConflictNum() {
        for (int arcIndex = 0; arcIndex < entityData.getArcNum(); arcIndex++) {
            buildIntervalTreeByDev(arcIndex);
        }
        this.getConflictArcNum();
    }

    //根据arcIndex构建区间树
    private void buildIntervalTreeByDev(int arcIndex) {
        int equIndex = hashData.getEquByArc(arcIndex);
        //为每个装备构建一棵对应的区间树，加入索引
        if (!this.intervalTreeByDev.containsKey(equIndex)) {
            IntervalTree<Date> tree = new IntervalTree<Date>();
            this.intervalTreeByDev.put(equIndex, tree);
        }
        //向区间树中添加区间
        Date[] arcTime = entityData.getArcTime(arcIndex);
        this.intervalTreeByDev.get(equIndex).add
                (new DateInterval(arcTime[0], arcTime[1], Interval.Bounded.CLOSED, String.valueOf(arcIndex)));

    }

    //循环查询区间树，求解各弧段冲突数
    private void getConflictArcNum() {
        for (int arcIndex = 0; arcIndex < entityData.getArcNum(); arcIndex++) {
            Date[] arcTime = entityData.getArcTime(arcIndex);
            int equIndex = hashData.getEquByArc(arcIndex);
            Set<Interval<Date>> ids = this.intervalTreeByDev.get(equIndex).
                    query(new DateInterval(arcTime[0], arcTime[1], Interval.Bounded.CLOSED, String.valueOf(arcIndex)));
            int conNum = ids.size();
            arcConflictNum.put(arcIndex, conNum);
        }
    }
}
