package Entity;

import Entity.DataBaseEntity.ArcEntity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class Arc{
    private Integer arcId;

    private Integer starID;

    private Integer equipId;

    private Date startTime;;

    private Double startAzimuthDeg;

    private Double startElevationDeg;

    private Double startRangeKm;

    private Date endTime;

    private Double endAzimuthDeg;

    private Double endElevationDeg;

    private Double endRangeKm;

    private String mark;

    private Integer rev;

    public Arc(ArcEntity arcEntity){
        this.arcId = arcEntity.getArcId();
        this.starID = Integer.valueOf(arcEntity.getStarID());
        this.equipId = Integer.valueOf(arcEntity.getEquipId());
        Timestamp startTimestamp = Timestamp.valueOf(arcEntity.getStartTime());
        this.startTime = new Date(startTimestamp.getTime());
        this.startAzimuthDeg = arcEntity.getStartAzimuthDeg();
        this.startElevationDeg = arcEntity.getStartElevationDeg();
        this.startRangeKm = arcEntity.getStartRangeKm();
        Timestamp endTimestamp = Timestamp.valueOf(arcEntity.getEndTime());
        this.endTime = new Date(endTimestamp.getTime());
        this.endAzimuthDeg = arcEntity.getEndAzimuthDeg();
        this.endElevationDeg = arcEntity.getEndElevationDeg();
        this.endRangeKm = arcEntity.getEndRangeKm();
        this.mark = arcEntity.getMark();
        this.rev = arcEntity.getRev();
    }

}
