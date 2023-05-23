package DataTransferTest;

import TimeLine.TimeLine;
import ilog.concert.IloIntVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class Data {
    //用户星数据，id,type
    ArrayList<Integer[]> cliStar;
    //管控星数据，id
    ArrayList<Integer> conStar;
    //弧段数据，id、用户星、管控星、时长
    ArrayList<Integer[]> arc;

    //管控星-弧段列表
    HashMap<Integer,ArrayList<Integer>> mapCliStarArc;
    //用户星-弧段列表
    HashMap<Integer,ArrayList<Integer>> mapConStarArc;
    //弧段列表
    HashMap<Integer,ArrayList<Integer>> mapArc;

    //弧段-时间
    HashMap<Integer, Date[]> arcTimeRange;
    HashMap<Integer, Long> arcTime;

    //用户星-完全冲突弧段集
    ArrayList<ArrayList<Integer>> cliStarConf;
    //管控星-完全冲突弧段集
    ArrayList<ArrayList<Integer>> conStarConf;
    //完全冲突弧段集
    ArrayList<ArrayList<Integer>> conf;

    //任务所需时间
    HashMap<Integer,Integer> jobTime;
    //用户星对应带宽
    HashMap<Integer,Double> starWeight;

    int cliStarNum;
    int arcNum;
    int conStarNum;

    private void loadData(){
        this.jobTime = new HashMap<>();
        this.starWeight = new HashMap<>();
        jobTime.put(0, 34133);
        jobTime.put(1, 8533);
        jobTime.put(2, 4267);
        jobTime.put(3, 2133);
        starWeight.put(0, 0.15);
        starWeight.put(1, 0.6);
        starWeight.put(2, 1.2);
        starWeight.put(3, 2.4);
    }

    public Data(int cliStarNum, int arcNum, int timeWinSizeMax, int timeWinSizeMin) throws ParseException {
        this.loadData();

        this.cliStarNum = cliStarNum;
        this.arcNum = arcNum;
        this.conStarNum = 5;

        // 创建一个Random对象
        Random random = new Random();

        // 创建三个ArrayList对象
        cliStar = new ArrayList<>();
        conStar = new ArrayList<>();
        arc = new ArrayList<>();

        arcTimeRange = new HashMap<>();
        arcTime = new HashMap<>();
        mapCliStarArc = new HashMap<>();
        mapConStarArc = new HashMap<>();
        mapArc = new HashMap<>();
        mapArc.put(0, new ArrayList<>());

        // 定义时间窗口的格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 生成用户星列表和管控星列表
        for (int i = 0; i < cliStarNum; i++) {
            int cliType = random.nextInt(4);
            cliStar.add(new Integer[]{i, cliType});
            mapCliStarArc.put(i, new ArrayList<>());
        }

        for (int i = 0; i < conStarNum; i++) {
            conStar.add(i);
            mapConStarArc.put(i, new ArrayList<>());
        }

        // 生成时间窗口列表
        for (int i = 0; i < arcNum; i++) {
            // 随机选择一个用户星ID
            int cliStarId = cliStar.get(random.nextInt(cliStarNum))[0];

            // 随机选择一个装备ID
            int conStarId = conStar.get(random.nextInt(conStarNum));

            // 随机生成一个时间窗口开始时间，范围为2023年3月13日到2023年3月14日之间的任意时刻
            LocalDateTime startDateTime = LocalDateTime.of(2023, 3, 13, random.nextInt(24), random.nextInt(60), random.nextInt(60));

            // 时间窗口结束时间为开始时间加上timeWinSize分钟
            int timeWinSize = random.nextInt(timeWinSizeMax - timeWinSizeMin) + timeWinSizeMin;
            LocalDateTime endDateTime = startDateTime.plusMinutes(timeWinSize);

            // 创建一个数组，存储弧段信息，包括弧段ID、用户星ID、管控星ID、持续时间
            Integer[] arcInfo = new Integer[4];

            arcInfo[0] = i; // 时间窗口ID
            arcInfo[1] = cliStarId;         // 用户星ID
            arcInfo[2] = conStarId;         // 管控星ID
            arcInfo[3] = timeWinSize;  //持续时间

            Date[] dateTime = new Date[2];
            dateTime[0] = df.parse(formatter.format(startDateTime));
            dateTime[1] = df.parse(formatter.format(endDateTime));
            long timeMinute = dateTime[1].getTime() - dateTime[0].getTime();
            arcTimeRange.put(i, dateTime);
            arcTime.put(i, timeMinute);

            mapCliStarArc.get(cliStarId).add(i);
            mapConStarArc.get(conStarId).add(i);
            mapArc.get(0).add(i);

            arc.add(arcInfo);
        }

        this.getCon();
    }

    private void getCon(){
        cliStarConf = new ArrayList<>();
        conStarConf = new ArrayList<>();
        conf = new ArrayList<>();

        TimeLine cliStarTimeLine = new TimeLine();
        TimeLine conStarTimeLine = new TimeLine();
        TimeLine conTimeLine = new TimeLine();

        for (int i = 0; i < cliStar.size(); i++) {
            cliStarTimeLine.insertArc(i, mapCliStarArc, arcTimeRange);
        }
        cliStarConf = cliStarTimeLine.searchTimeline();
        
        for (int i = 0; i < conStar.size(); i++) {
            conStarTimeLine.insertArc(i, mapConStarArc, arcTimeRange);
        }
        cliStarConf = conStarTimeLine.searchTimeline();

        conTimeLine.insertArc(0, mapArc, arcTimeRange);
        conf = conTimeLine.searchTimeline();
    }

    public int getCliStarTime(int cliStarId){
        int cliStarType = cliStar.get(cliStarId)[1];
        return jobTime.get(cliStarType);
    }

    public double getArcWeight(int arcId){
        int cliStarId = arc.get(arcId)[2];
        int cliStarType = cliStar.get(cliStarId)[1];
        return starWeight.get(cliStarType);
    }

    public int getArcNum() {
        return arcNum;
    }

    public int getCliStarNum() {
        return cliStarNum;
    }

    public int getConStarNum() {
        return conStarNum;
    }
}
