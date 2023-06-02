package ScheduleMip.Data.Entity;

import ScheduleMip.Data.Entity.DataBaseEntity.TaskEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    private Integer taskId;
    private Integer cycleCount;
    private String cycleUnit;
    private Integer durationTime;

    public Task(TaskEntity taskEntity){
        this.taskId = taskEntity.getTaskId();
        this.cycleCount = taskEntity.getCycleCount();
        this.cycleUnit = taskEntity.getCycleUnit();
        this.durationTime = taskEntity.getDurationTime();
    }
}
