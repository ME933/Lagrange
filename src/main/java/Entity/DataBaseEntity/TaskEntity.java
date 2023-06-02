package Entity.DataBaseEntity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class TaskEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "task_id")
    private Integer taskId;
    @Basic
    @Column(name = "cycle_count")
    private Integer cycleCount;
    @Basic
    @Column(name = "cycle_unit")
    private String cycleUnit;
    @Basic
    @Column(name = "duration_time")
    private Integer durationTime;
}
