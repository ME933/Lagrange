import Data.OriData;
import Data.PreData;
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

public class PreProcess {
    OriData oriData;
    PreData preData;
    //卫星列表
    ArrayList<Integer> starList;
    //装备列表
    ArrayList<Integer> oriEquList;
    //虚拟装备列表
    ArrayList<Integer> equList;
    //弧段列表
    ArrayList<Integer> arcList;
    //弧段信息，弧段ID-弧段信息([弧段ID，卫星ID，装备ID，开始时间，结束时间, 上升下降])
    HashMap<Integer, String[]> arcInfo;
    //装备信息，装备ID-装备信息([装备ID，装备能力])
    HashMap<Integer, String[]> equInfo;
    //卫星-弧段列表
    HashMap<Integer, ArrayList<Integer>> mapStarArc;
    //装备-弧段列表
    HashMap<Integer, ArrayList<Integer>> mapEquArc;
    //卫星，装备，弧段列表
    HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> mapStarEquArc;
    //冲突列表
//    ArrayList<int[]> conArc;
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
    HashMap<Integer, ArrayList<Integer>> viaEquMap;

    String modeOrder;
    //时间线
    TimeLine[] timeLineList;
    //存储冲突弧段集合
    HashMap<Integer,ArrayList<ArrayList<Integer>>> conArcSetByEqu;
    //冲突数
//    int conNum;
    //跨卫星冲突对
//    ArrayList<Integer> crossCon;
    //维护子问题，结构y-[<[冲突对1]...,[冲突对n]>,<[跨卫星冲突index，跨卫星冲突弧段index ]...>]
//    HashMap<Integer,LinkedList[]> subQue;
    //维护子问题，结构装备Index-装备冲突对
//    HashMap<Integer,LinkedList<Integer>> equConPair;

    public PreProcess(OriData oriDataIn, String mode) {
        this.oriData = oriDataIn;
        this.modeOrder = mode;
    }

    public PreData getPreData() throws ParseException {
        this.storeList();
        this.generateMap();
//        this.generateCon();
        this.creatTimeLine();
        this.preData = new PreData(conArcSetByEqu, timeLineList, starList, equList, arcList, mapStarArc, mapEquArc, mapStarEquArc, intervalTreeByDev, eachArcConNum, starIndexMap, equIndexMap, arcIndexMap, mapArcStar, mapArcEqu, mapArcTime, mapArcRai);
        return preData;
    }

    //存储列表信息
    private void storeList() {
        this.starList = new ArrayList<>();
        this.oriEquList = new ArrayList<>();
        this.arcList = new ArrayList<>();
        this.equList = new ArrayList<>();
        this.arcInfo = new HashMap<>();
        this.equInfo = new HashMap<>();
        this.arcIndexMap = new HashMap<>();
        this.starIndexMap = new HashMap<>();
        this.equIndexMap = new HashMap<>();
        this.viaEquMap = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> tempViaEquMap;
        tempViaEquMap = new HashMap<>();
        for (String[] equ : this.oriData.getOriDataEqu()) {
            String equName = equ[0];
            this.oriEquList.add(Integer.parseInt(equ[0]));
            equInfo.put(Integer.parseInt(equ[0]), equ);
            int equCap = Integer.parseInt(equ[1]);
            //存储虚拟装备和映射
            ArrayList<Integer> viaEqu = new ArrayList<>();
            if (equCap > 1) {
                for (int i = 0; i < equCap; i++) {
                    String newName = equName + i;
                    this.equList.add(Integer.parseInt(newName));
                    viaEqu.add(Integer.parseInt(newName));
                }
            } else {
                this.equList.add(Integer.parseInt(equName));
                viaEqu.add(Integer.parseInt(equName));
            }
            tempViaEquMap.put(Integer.parseInt(equName), viaEqu);
        }
        for (String[] arc : this.oriData.getOriDataArcs()) {
            String equName = arc[2];
            String arcName = arc[0];
            int equCap = Integer.parseInt(equInfo.get(Integer.parseInt(equName))[1]);
            if (equCap > 1) {
                for (int i = 0; i < equCap; i++) {
                    String equNewName = equName + i;
                    String arcNewName = arcName + i;
                    String[] newArcInfo = arc.clone();
                    this.arcList.add(Integer.parseInt(arcNewName));
                    newArcInfo[2] = equNewName;
                    arcInfo.put(Integer.parseInt(arcNewName), newArcInfo);
                }
            } else {
                this.arcList.add(Integer.parseInt(arcName));
                arcInfo.put(Integer.parseInt(arcName), arc);
            }
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
        //添加虚拟装备
        for (int equID : tempViaEquMap.keySet()) {
            ArrayList<Integer> equIDList = tempViaEquMap.get(equID);
            ArrayList<Integer> equIndexList = new ArrayList<>();
            int equIndex = equIndexMap.get(equID);
            for (int equIDTemp : equIDList) {
                equIndexList.add(equIndexMap.get(equIDTemp));
            }
            viaEquMap.put(equIndex, equIndexList);
        }
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
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < arcList.size(); i++) {
            int arcID = arcList.get(i);
            String[] theArcInfo = arcInfo.get(arcID);
            int starIndex = starIndexMap.get(Integer.parseInt(theArcInfo[1]));
            int equIndex = equIndexMap.get(Integer.parseInt(theArcInfo[2]));
            this.mapArcStar.put(i, starIndex);
            this.mapArcEqu.put(i, equIndex);
            Date[] time = new Date[2];
            time[0] = df.parse(theArcInfo[3]);
            time[1] = df.parse(theArcInfo[4]);
            this.mapArcTime.put(i, time);
            this.mapArcRai.put(i, theArcInfo[5]);
        }
        for (int i = 0; i < arcList.size(); i++) {
/*
            int arcID = arcList.get(i);
            String[] arcArray = arcInfo.get(arcID);
            int starID = Integer.parseInt(arcArray[1]);
            int equID = Integer.parseInt(arcArray[2]);
*/
            int starIndex = mapArcStar.get(i);
            int equIndex = mapArcEqu.get(i);
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

    private void buildIntervalTreeByDev(int arcIndex) {
        int equIndex = mapArcEqu.get(arcIndex);
        if (!this.intervalTreeByDev.containsKey(equIndex)) {
                IntervalTree<Date> tree = new IntervalTree<Date>();
                this.intervalTreeByDev.put(equIndex, tree);//为每个装备构建一棵对应的区间树，加入索引
        }
        this.intervalTreeByDev.get(equIndex).add
                (new DateInterval(mapArcTime.get(arcIndex)[0], mapArcTime.get(arcIndex)[1], Interval.Bounded.CLOSED, String.valueOf(arcIndex)));//向区间树中添加区间

    }

    /*

    private ArrayList<int[]> computeCon(){
        LinkedList<int[]> tempOutConArc = new LinkedList<>();
        //遍历生成冲突对
        eachArcConNum = new HashMap<>();
        for (int i = 0; i < arcList.size(); i++) {
            int equIndex = mapArcEqu.get(i);
//            int starIndex = mapArcStar.get(i);
            Date[] time = mapArcTime.get(i);
            Date startTime = time[0];
            Date endTime = time[1];
            for (int j = i + 1; j < arcList.size(); j++) {
                int equIndexP = mapArcEqu.get(j);
//                int starIndexP = mapArcStar.get(j);
                if(equIndex == equIndexP){
                    Date[] timeP = mapArcTime.get(j);
                    Date startTimeP = timeP[0];
                    Date endTimeP = timeP[1];
                    //判断是否冲突
                    if(endTimeP.after(startTime) && startTimeP.before(endTime)) {
                        //筛选跨卫星冲突对，若属于跨卫星冲突对，则以[跨卫星冲突对序号，冲突弧段序号]存入相应卫星的第二个List，并存储lambda与冲突弧段对应信息
                        addConNum(i);
                        addConNum(j);
                        tempOutConArc.add(new int[]{i,j});
                    }
                }
            }
        }
//        this.crossCon = new ArrayList<>(tempCrossCom);
        return new ArrayList<>(tempOutConArc);
    }
    */

    /*
    private void creatEquCon(){
        this.equConPair = new HashMap<>();
        for (int i = 0; i < equList.size(); i++) {
            equConPair.put(i,new LinkedList<>());
        }
        for (int i = 0; i < conArc.size(); i++) {
            int[] conPair = conArc.get(i);
            int equIndex = mapArcEqu.get(conPair[0]);
            equConPair.get(equIndex).add(i);
        }
    }

     */

    /*
    private void creatQue(){
        crossNum = 0;
        crossCon = new ArrayList<>();
        this.subQue = new HashMap<>();
        //初始化subQue，维护子问题，结构y-[<[冲突对1]...,[冲突对n]>,<[跨卫星冲突index，跨卫星冲突弧段index ]...>]
        for (int i = 0; i < starList.size(); i++) {
            LinkedList[] comData = new LinkedList[2];
            comData[0] = new LinkedList<int[]>();
            comData[1] = new LinkedList<int[]>();
            this.subQue.put(i,comData);
        }
        for (int i = 0; i < conArc.size(); i++) {
            int[] conPair = conArc.get(i);
            int xi = conPair[0];
            int xj = conPair[1];
            int starI = mapArcStar.get(xi);
            int starJ = mapArcStar.get(xj);
            if(starI != starJ){
                subQue.get(starI)[1].add(new int[]{crossNum,xi});
                subQue.get(starJ)[1].add(new int[]{crossNum,xj});
                crossCon.add(i);
                crossNum++;
            }
            //若不属于跨卫星冲突对，将其存入相应卫星的第一个List
            else {
                subQue.get(starI)[0].add(new int[]{starI,starJ});
            }
        }
    }
    */

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

}
