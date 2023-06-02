package ScheduleMip.Data.ScheduleMip.TimeLine;

import java.util.Date;

public class TimePoint {
    private final Date time;
    private final int arcIndex;
    private final String pointType;

    public TimePoint(int arcIndex, Date time, String pointType) throws Exception {
        this.time = time;
        this.arcIndex = arcIndex;
        assert pointType.equals("start") || pointType.equals("end");
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
