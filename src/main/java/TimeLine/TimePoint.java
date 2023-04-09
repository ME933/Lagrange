package TimeLine;

import java.util.Date;

public class TimePoint {
    private Date time;
    private int arcIndex;
    private String pointType;

    public TimePoint(int arcIndex, Date time, String pointType) throws Exception {
        this.time = time;
        this.arcIndex = arcIndex;
        if (!pointType.equals("start") && !pointType.equals("end")) {
            throw new Exception("Invalid string value");
        }
        this.pointType = pointType;
    }

    public int getArcIndex() {
        return arcIndex;
    }

    public String getPointType() {
        return pointType;
    }

    public Date getTime() {
        return time;
    }
}
