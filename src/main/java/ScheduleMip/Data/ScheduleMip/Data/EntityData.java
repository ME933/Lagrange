package ScheduleMip.Data.ScheduleMip.Data;

import ScheduleMip.Data.Entity.Arc;
import ScheduleMip.Data.Entity.Equip;
import ScheduleMip.Data.Entity.Star;
import ScheduleMip.Data.Entity.Task;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class EntityData {
    List<Star> stars;
    List<Equip> equips;
    List<Arc> arcs;
    Task validTask;

    public EntityData(List<Star> stars, List<Equip> equips, List<Arc> arcs, Task validTask) {
        this.stars = stars;
        this.equips = equips;
        this.arcs = arcs;
        this.validTask = validTask;
    }

    //根据索引获得信息
    public Arc getArc(int arcIndex) {
        return arcs.get(arcIndex);
    }

    public int getArcID(int arcIndex) {
        return arcs.get(arcIndex).getArcId();
    }

    public Equip getEqu(int equIndex) {
        return equips.get(equIndex);
    }

    public Star getStar(int starIndex) {
        return stars.get(starIndex);
    }

    //获得各数量
    public int getArcNum() {
        return arcs.size();
    }

    public int getEquNum() {
        return equips.size();
    }

    public int getStarNum() {
        return stars.size();
    }

    //获得弧段时间信息
    public Date[] getArcTime(int arcIndex) {
        Arc arc = arcs.get(arcIndex);
        Date[] arcTime = new Date[2];
        arcTime[0] = arc.getStartTime();
        arcTime[1] = arc.getEndTime();
        return arcTime;
    }

    //获得装备信息
    public int getEquCap(int equIndex) {
        return equips.get(equIndex).getCapability();
    }

    public int getCycleCount() { return validTask.getCycleCount();}
}
