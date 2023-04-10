package TimeLine;


import Data.ComData;
import Data.PreData;

import java.util.*;

// 完整时间轴
public class TimeLine {
    ArrayList<TimePoint> timeline;
    boolean update;

    /**
     * @description: 构建可重叠时间轴
     * @params: []
     * @return:
     */
    public TimeLine() {
        this.timeline = new ArrayList<>();
        this.update = false;
    }

    public void insertArc(int equIndex, HashMap<Integer,ArrayList<Integer>> mapEquArc, HashMap<Integer, Date[]> mapArcTime){
        ArrayList<Integer> arcList = mapEquArc.get(equIndex);
        for (int arcIndex: arcList) {
            this.binaryInsertArc(arcIndex, mapArcTime.get(arcIndex));
        }
    }

    /**
     * @description: 折半查找插入弧段
     * @params: [arcIndex, time]
     * @return: void
     */
    private void binaryInsertArc(int arcIndex, Date[] time) {
        try {
            binaryInsertTimePoint(new TimePoint(arcIndex, time[0], "start"));
            binaryInsertTimePoint(new TimePoint(arcIndex, time[1], "end"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @description: 根据序号获得时间
     * @params: [index]
     * @return: Date
     */
    public Date getTimeByIndex(int index){
        return this.timeline.get(index).getTime();
    }

    /**
     * @description: 折半查找插入时间点
     * @params: [id, time, mark, devMode]
     * @return: int
     */
    private int binaryInsertTimePoint(TimePoint timePoint) {
        try {
            Date time = timePoint.getTime();
            int left = 0;
            int right = this.timeline.size() - 1;
            while (left <= right) {
                int mid = (left + right) / 2;
                if (time.after(getTimeByIndex(mid))){
                    left = mid + 1;
                } else if (time.before(getTimeByIndex(mid))) {
                    right = mid - 1;
                } else {
                    this.timeline.add(mid, timePoint);
                    return mid;
                }
            }
            this.timeline.add(left, timePoint);
            return left;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @description: 返回饱和冲突弧段列表
     * @params: []
     * @return: ArrayList
     */
    public ArrayList<ArrayList<Integer>>  searchTimeline(){
        ArrayList<ArrayList<Integer>> conArcList = new ArrayList<>();
        HashSet<Integer> conArcSet = new HashSet<>();
        for (int i = 0; i < timeline.size()-1; i++){
            TimePoint timePoint = timeline.get(i);
            if(timePoint.getPointType().equals("start")){
                update = true;
                conArcSet.add(timePoint.getArcIndex());
            }else if(timePoint.getPointType().equals("end")){
                if(update && conArcSet.size() > 1){
                    ArrayList<Integer> tempSet = new ArrayList<>(conArcSet);
                    conArcList.add(tempSet);
                }
                update = false;
                conArcSet.remove(timePoint.getArcIndex());
            }
        }
        return conArcList;
    }

}
