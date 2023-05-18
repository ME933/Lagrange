import Data.OriData;
import Data.PreData;
import DevCap.EquBeamBuild;
import DevCap.EquCap;
import DevCap.RandomTaskType;
import DevCap.TaskType;
import File.ConJson;
import IntervalTree.IntervalTree;
import IntervalTree.DateInterval;
import IntervalTree.Interval;
import TimeLine.TimeLine;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class PreProcess {
    OriData oriData;
    PreData preData;
    //卫星列表
    ArrayList<Integer> starList;
    //装备列表
    ArrayList<Integer> equList;
    //弧段列表
    ArrayList<Integer> arcList;
    //弧段信息，弧段ID-弧段信息([弧段ID，卫星ID，装备ID，开始时间，结束时间, 上升下降])
    HashMap<Integer, String[]> arcInfo;
//    //装备信息，装备ID-装备信息([装备ID，装备能力])
//    HashMap<Integer, String[]> equInfo;
    //卫星-弧段列表
    HashMap<Integer, ArrayList<Integer>> mapStarArc;
    //装备-弧段列表
    HashMap<Integer, ArrayList<Integer>> mapEquArc;
    //卫星，装备，弧段列表
    HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> mapStarEquArc;
    //按装备构建区间树
    HashMap<Integer, IntervalTree<Date>> intervalTreeByDev;
    //各弧段冲突数
    HashMap<Integer, Integer> eachArcConNum;

    //由ID查找索引
    HashMap<Integer, Integer> starIndexMap;
    HashMap<Integer, Integer> equIndexMap;
    HashMap<Integer, Integer> arcIndexMap;

    //弧段信息，弧段ID-弧段信息([弧段ID，卫星ID，装备ID，开始时间，结束时间, 上升下降])
    HashMap<Integer, Integer> mapArcStar;
    HashMap<Integer, Integer> mapArcEqu;
    HashMap<Integer, Date[]> mapArcTime;
    HashMap<Integer, String> mapArcRai;

    //装备信息，装备组列表
    HashMap<Integer, ArrayList<Integer>> virEqu;
    //存储装备对应波束
    HashMap<Integer, Integer> beamEquMap;
    //存储装备能力
    HashMap<Integer, EquCap> equCapMap;
    //存储真实卫星-虚拟卫星
    HashMap<Integer,ArrayList<Integer>> virStar;
//    //存储卫星对应任务
//    HashMap<Integer,ArrayList<TaskType>> taskTypeMap;
    //存储虚拟卫星对应任务
    HashMap<Integer, TaskType> virTaskType;

    String modeOrder;
    //时间线
    TimeLine[] timeLineList;
    //存储冲突弧段集合
    HashMap<Integer,ArrayList<ArrayList<Integer>>> conArcSetByEqu;

    DateFormat df;

    public PreProcess(OriData oriDataIn, String mode) {
        this.oriData = oriDataIn;
        this.modeOrder = mode;
    }

    public PreData getPreData(int seed) throws ParseException {
        this.df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.storeList();

        this.virtualizeEqu();
        this.generateTask(seed);
        this.virtualizeArc();

        this.generateMap();
        this.filterList();

        this.generateMap();


//        this.generateCon();
        this.creatTimeLine();
        this.preData = new PreData(conArcSetByEqu, timeLineList, starList, equList, arcList, mapStarArc, mapEquArc, mapStarEquArc, intervalTreeByDev, eachArcConNum, starIndexMap, equIndexMap, arcIndexMap, mapArcStar, mapArcEqu, mapArcTime, mapArcRai , virStar);
        return preData;
    }

    //存储列表信息
    private void storeList() {
        this.starList = new ArrayList<>();
        this.arcList = new ArrayList<>();
        this.equList = new ArrayList<>();
        this.arcInfo = new HashMap<>();
        this.arcIndexMap = new HashMap<>();
        this.starIndexMap = new HashMap<>();
        this.equIndexMap = new HashMap<>();
        for (String[] equ : this.oriData.getOriDataEqu()) {
            String equName = equ[0];
            //存储装备
            this.equList.add(Integer.parseInt(equName));
        }
        for (String[] arc : this.oriData.getOriDataArcs()) {
            String arcName = arc[0];
            this.arcList.add(Integer.parseInt(arcName));
            arcInfo.put(Integer.parseInt(arcName), arc);
        }
        for (String star : this.oriData.getOriDataStar()) {
            this.starList.add(Integer.parseInt(star));
        }
        for (int i = 0; i < arcList.size(); i++) {
            arcIndexMap.put(arcList.get(i), i);
        }
        for (int i = 0; i < equList.size(); i++) {
            equIndexMap.put(equList.get(i), i);
        }
        for (int i = 0; i < starList.size(); i++) {
            starIndexMap.put(starList.get(i), i);
        }

    }

    //生成虚拟装备
    private void virtualizeEqu(){
        ArrayList<Integer> newEquList = new ArrayList<>(equList);
//        HashMap<Integer, String[]> newEquInfo = new HashMap<>(equInfo);
        EquBeamBuild equBeamBuild = new EquBeamBuild();
        beamEquMap = new HashMap<>();
        virEqu = new HashMap<>();
        equCapMap = new HashMap<>();
        EquCap[] equCapList = equBeamBuild.getEquCapList();
        for(int equID:equList){
            virEqu.put(equID,new ArrayList<>());
            virEqu.get(equID).add(equID);
            beamEquMap.put(equID,equID);
        }
        //虚拟化波束
        for (int i = 0; i < equList.size(); i++) {
            EquCap equCap = equCapList[i];
            int equID = equList.get(i);
            equCapMap.put(equID, equCap);
            for (int j = 0; j < equCap.getBeamNum() - 1; j++) {
                int newEquIndex = newEquList.size();
                int newEquID = Integer.parseInt((String.valueOf(equID) + j));
                equIndexMap.put(newEquID, newEquIndex);
                newEquList.add(newEquID);
                virEqu.get(equID).add(newEquID);
                beamEquMap.put(newEquID, equID);
//                String[] newEquInfoString = equInfo.get(i);
//                newEquInfoString[0] = String.valueOf(newEquIndex);
//                newEquInfo.put(newEquIndex,newEquInfoString);
            }
        }
//        equInfo = newEquInfo;
        equList = newEquList;
    }

    //生成任务，根据任务生成虚拟卫星
    private void generateTask(int seed){
        RandomTaskType randomTaskType = new RandomTaskType();
        HashMap<Integer,ArrayList<TaskType>>taskTypeMap = randomTaskType.generateRandomTask(starList.size(), seed);
        ArrayList<Integer> newStarList = new ArrayList<>(starList);
        this.virStar = new HashMap<>();
        this.virTaskType = new HashMap<>();
        for (int starID : starList) {
            virStar.put(starID, new ArrayList<>());
            virStar.get(starID).add(starID);
        }
        for (int i = 0; i < starList.size(); i++) {
            int starID = starList.get(i);
            ArrayList<TaskType> taskTypeList = taskTypeMap.get(i);
            virTaskType.put(starID, taskTypeList.get(0));
            //若该卫星对应任务数大于1，虚拟化该卫星
            if(taskTypeList.size() > 1){
                for (int j = 1; j < taskTypeList.size(); j++) {
                    int newStarIndex = newStarList.size();
                    int newStarID = Integer.parseInt((String.valueOf(starID) + j));
                    starIndexMap.put(newStarID, newStarIndex);
                    newStarList.add(newStarID);
                    virStar.get(starID).add(newStarID);
                    virTaskType.put(newStarID, taskTypeList.get(j));
                }
            }
        }
        starList = newStarList;
    }

    //根据虚拟
    private void virtualizeArc() throws ParseException {
        ArrayList<Integer> newArcList = new ArrayList<>();
        //弧段信息，弧段ID-弧段信息([弧段ID，卫星ID，装备ID，开始时间，结束时间, 上升下降])
        HashMap<Integer, String[]> newArcInfo = new HashMap<>();
        HashMap<Integer, Integer> newArcIndexMap = new HashMap<>();
        //检索卫星对应虚拟卫星，若该虚拟卫星符合波束能力，生成对应波束数量的虚拟弧段
        for (int arcID: arcList) {
            String[] thisArcInfo = arcInfo.get(arcID); //得到原始弧段信息
            int starID = Integer.parseInt(thisArcInfo[1]);
            int equID = Integer.parseInt(thisArcInfo[2]);
            //检索虚拟卫星
            for (int virStarID: virStar.get(starID)) {
                TaskType taskType = virTaskType.get(virStarID);
                Date startTime = df.parse(thisArcInfo[3]);
                Date endTime = df.parse(thisArcInfo[4]);
                //筛选时间
                if(countTimeSecondsGap(startTime, endTime) > taskType.getSeconds()){
                    EquCap equCap = equCapMap.get(equID);
                    //筛选能力
                    if(equCap.verifyCap(taskType)){
                        int i = 0;
                        for (int virEquID: virEqu.get(equID)) {
                            int newArcID = newArcList.size();
                            int newArcIndex = newArcList.size();
                            i++;
                            //修改INFO信息
                            String[] thisNewArcInfo = thisArcInfo.clone();
                            thisNewArcInfo[0] = String.valueOf(newArcID);
                            thisNewArcInfo[1]  = String.valueOf(virStarID);
                            thisNewArcInfo[2]  = String.valueOf(virEquID);
                            thisNewArcInfo[3]  = thisArcInfo[3];
                            thisNewArcInfo[4]  = thisArcInfo[4];
                            thisNewArcInfo[5]  = thisArcInfo[5];
                            newArcInfo.put(newArcID, thisNewArcInfo);
                            newArcList.add(newArcID);
                            newArcIndexMap.put(newArcID, newArcIndex);
                        }
                    }
                }
            }
        }
        arcList = newArcList;
        arcInfo = newArcInfo;
        arcIndexMap = newArcIndexMap;
    }

    //建立映射信息
    private void generateMap() throws ParseException {
        this.mapStarArc = new HashMap<>();
        this.mapEquArc = new HashMap<>();
        this.mapStarEquArc = new HashMap<>();
        this.mapArcStar = new HashMap<>();
        this.mapArcEqu = new HashMap<>();
        this.mapArcTime = new HashMap<>();
        this.mapArcRai = new HashMap<>();
        for (int i = 0; i < arcList.size(); i++) {
            int arcID = arcList.get(i);
            String[] theArcInfo = arcInfo.get(arcID);
            int starID = Integer.parseInt(theArcInfo[1]);
            int equID = Integer.parseInt(theArcInfo[2]);
            int starIndex = starIndexMap.get(starID);
            int equIndex = equIndexMap.get(equID);
            int arcIndex = arcIndexMap.get(arcID);
            this.mapArcStar.put(arcIndex, starIndex);
            this.mapArcEqu.put(arcIndex, equIndex);
            Date[] time = new Date[2];
            time[0] = df.parse(theArcInfo[3]);
            time[1] = df.parse(theArcInfo[4]);
            this.mapArcTime.put(arcIndex, time);
            this.mapArcRai.put(arcIndex, theArcInfo[5]);

            //索引键值不包含该卫星，则建立新数组并存入
            if (!mapStarArc.containsKey(starIndex)) {
                mapStarArc.put(starIndex, new ArrayList<>());
            }//存入弧段Index
            mapStarArc.get(starIndex).add(i);
            //索引键值不包含该装备，则建立新数组并存入
            if (!mapEquArc.containsKey(equIndex)) {
                mapEquArc.put(equIndex, new ArrayList<>());
            }
            //存入弧段ID
            mapEquArc.get(equIndex).add(i);
            //建立双key索引
            if (!mapStarEquArc.containsKey(starIndex)) {
                mapStarEquArc.put(starIndex, new HashMap<>());
            }
            if (!mapStarEquArc.get(starIndex).containsKey(equIndex)) {
                mapStarEquArc.get(starIndex).put(equIndex, new ArrayList<>());
            }
            mapStarEquArc.get(starIndex).get(equIndex).add(i);
        }
    }

    public void filterList(){
        ArrayList<Integer> newStarList = new ArrayList<>();
        ArrayList<Integer> newEquList = new ArrayList<>();
        HashMap<Integer, Integer> newStarIndexMap = new HashMap<>();
        HashMap<Integer, Integer> newEquIndexMap = new HashMap<>();
        HashMap<Integer, Integer> newFromOld = new HashMap<>();
        for (int starID: starList) {
            int starIndex = getStarIndexByID(starID);
            if(mapStarArc.get(starIndex) != null){
                int newStarIndex = newStarList.size();
                newStarList.add(newStarIndex);
                newStarIndexMap.put(starID, newStarIndex);
                newFromOld.put(starID,newStarIndex);
            }
        }
        for (int equID: equList) {
            int equIndex = getEquIndexByID(equID);
            if(mapEquArc.get(equIndex) != null){
                int newEquIndex = newEquList.size();
                newEquList.add(equIndex);
                newEquIndexMap.put(equID, newEquIndex);
            }
        }
        this.ID2Index(newFromOld);
        starList = newStarList;
        equList = newEquList;
        starIndexMap = newStarIndexMap;
        equIndexMap = newEquIndexMap;
    }

    private void ID2Index(HashMap<Integer, Integer> newFromOld){
        HashMap<Integer,ArrayList<Integer>> newVirStar = new HashMap<>();
        for (int starID: virStar.keySet()) {
            if (newFromOld.containsKey(starID)){
                int starIndex = newFromOld.get(starID);
                    newVirStar.put(starIndex, new ArrayList<>());
                    for (int virStarID: virStar.get(starID)) {
                        if (newFromOld.containsKey(virStarID)) {
                            int virStarIndex = newFromOld.get(virStarID);
                            newVirStar.get(starIndex).add(virStarIndex);
                        }
                    }
                }
        }
        virStar = newVirStar;
    }


    private void buildIntervalTreeByDev(int arcIndex) {
        int equIndex = mapArcEqu.get(arcIndex);
        if (!this.intervalTreeByDev.containsKey(equIndex)) {
                IntervalTree<Date> tree = new IntervalTree<Date>();
                this.intervalTreeByDev.put(equIndex, tree);//为每个装备构建一棵对应的区间树，加入索引
        }
        this.intervalTreeByDev.get(equIndex).add
                (new DateInterval(mapArcTime.get(arcIndex)[0], mapArcTime.get(arcIndex)[1], Interval.Bounded.CLOSED, String.valueOf(arcIndex)));//向区间树中添加区间

    }


    private void addConNum(int i){
        if (eachArcConNum.containsKey(i)) {
            int conNum = eachArcConNum.get(i);
            eachArcConNum.put(i, conNum + 1);
        } else {
            eachArcConNum.put(i, 1);
        }
    }


    //建立时间轴，求解冲突弧段集合
    private void creatTimeLine(){
        conArcSetByEqu = new HashMap<>();
        for (int i = 0; i < equList.size(); i++) {
            TimeLine timeLine = new TimeLine();
            timeLine.insertArc(i,mapEquArc,mapArcTime);
            conArcSetByEqu.put(i,timeLine.searchTimeline());
        }
    }

    public long countTimeSecondsGap(Date time1, Date time2){
        long diffInMillie = Math.abs(time1.getTime() - time2.getTime());
        return TimeUnit.SECONDS.convert(diffInMillie, TimeUnit.MILLISECONDS);
    }

    public int getEquIndexByID(int equID){
        return equIndexMap.get(equID);
    }

    public int getStarIndexByID(int starID){
        return starIndexMap.get(starID);
    }

    public int getArcIndexByID(int arcID){
        return arcIndexMap.get(arcID);
    }


    //读取时间信息，建立冲突信息
    //若不包含冲突文件，则建立冲突信息并写入文件
    //若包含冲突文件，则读取冲突文件

    /*
    private void generateCon() {
        ConJson conJson = new ConJson();
        File file = new File("File/conArc.json"); // 创建一个File对象，指定同目录下的文件名
        if (file.exists()) {
            if (modeOrder.equals("load")) {
                System.out.println("直接读入conArc.json文件。");
//                conArc = conJson.ReadConJson();
            } else if (modeOrder.equals("compute")) {
                System.out.println("重新创建conArc.json文件，写入conArc。");
//                conArc = conJson.WriteConJson(this.computeCon());
            } else {
                System.out.println("未知指令。");
            }
        } else {
            if (modeOrder.equals("load")) {
                System.out.println("conArc.json文件不存在，退出程序。");
                System.exit(0);
            } else if (modeOrder.equals("compute")) {
                System.out.println("创建conArc.json文件，写入conArc。");
//                conArc = conJson.WriteConJson(this.computeCon());
            } else {
                System.out.println("未知指令。");
            }
        }
        this.intervalTreeByDev = new HashMap<>();
        for (int i = 0; i < arcList.size(); i++) {
            buildIntervalTreeByDev(i);
        }
//        this.creatQue();
    }
    */
}
