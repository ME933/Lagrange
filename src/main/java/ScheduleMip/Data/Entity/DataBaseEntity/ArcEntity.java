package ScheduleMip.Data.Entity.DataBaseEntity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "arcs", schema = "schedule", catalog = "")
public class ArcEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private Integer arcId;
    @Basic
    @Column(name = "IdSat")
    private String starID;
    @Basic
    @Column(name = "IdDev")
    private String equipId;
    @Basic
    @Column(name = "StartTimeUtc")
    private LocalDateTime startTime;;
    @Basic
    @Column(name = "StartAzimuthDeg")
    private Double startAzimuthDeg;
    @Basic
    @Column(name = "StartElevationDeg")
    private Double startElevationDeg;
    @Basic
    @Column(name = "StartRangeKm")
    private Double startRangeKm;
    @Basic
    @Column(name = "EndPTimeUtc")
    private LocalDateTime endTime;
    @Basic
    @Column(name = "EndPAzimuthDeg")
    private Double endAzimuthDeg;
    @Basic
    @Column(name = "EndPElevationDeg")
    private Double endElevationDeg;
    @Basic
    @Column(name = "EndPRangeKm")
    private Double endRangeKm;
    @Basic
    @Column(name = "Mark")
    private String mark;
    @Basic
    @Column(name = "Rev")
    private Integer rev;

}
