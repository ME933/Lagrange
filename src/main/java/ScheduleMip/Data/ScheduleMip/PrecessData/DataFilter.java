package ScheduleMip.Data.ScheduleMip.PrecessData;


import ScheduleMip.Data.Entity.Arc;
import ScheduleMip.Data.Entity.Equip;
import ScheduleMip.Data.Entity.Star;
import ScheduleMip.Data.Entity.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataFilter {
    List<Star> stars;
    List<Equip> equips;
    List<Arc> arcs;
    Task validTask;

    //装备-弧段数量
    HashMap<Integer, Integer> arcNumByEquips;
    //卫星-弧段数量
    HashMap<Integer, Integer> arcNumByStars;

    public DataFilter(List<Star> stars, List<Equip> equips, List<Arc> arcs, Task validTask){
        this.arcs = arcs;
        this.stars = stars;
        this.equips = equips;
    }

    public List<Arc> arcFilter(){
        List<Arc> newArcs = new ArrayList<>();
        for(Arc arc: arcs){
            long startTime = arc.getStartTime().getTime();
            long endTime = arc.getEndTime().getTime();
            long seconds = (endTime - startTime) / 1000;
            if (seconds >= validTask.getDurationTime() * 60){
                newArcs.add(arc);
            }
        }
        this.arcs = newArcs;
        return newArcs;
    }

    public List<Star> starFilter(){
        List<Star> newStars = new ArrayList<>();
        arcNumByStars = new HashMap<>();
        for(Star star: stars){
            arcNumByStars.put(star.getStarId(), 0);
        }
        for(Arc arc: arcs){
            int starId = arc.getStarID();
            int num = arcNumByStars.get(starId) + 1;
            arcNumByStars.put(starId, num);
        }
        for(Star star: stars){
            if(arcNumByStars.get(star.getStarId()) > 0){
                newStars.add(star);
            }
        }
        return newStars;
    }

    public List<Equip> equipFilter(){
        List<Equip> newEquips = new ArrayList<>();
        arcNumByEquips = new HashMap<>();
        for(Equip equip: equips){
            arcNumByEquips.put(equip.getEquipId(), 0);
        }
        for(Arc arc: arcs){
            int equipId = arc.getEquipId();
            int num = arcNumByEquips.get(equipId) + 1;
            arcNumByEquips.put(equipId, num);
        }
        for(Equip equip: equips){
            int equipId = equip.getEquipId();
            if(arcNumByEquips.get(equipId) > 0){
                newEquips.add(equip);
            }
        }
        return newEquips;
    }
}
