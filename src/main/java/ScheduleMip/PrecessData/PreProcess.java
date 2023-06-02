package ScheduleMip.PrecessData;


import Entity.Arc;
import Entity.Equip;
import Entity.Star;
import Entity.Task;
import ScheduleMip.Data.EntityData;
import ScheduleMip.Data.HashData;
import ScheduleMip.Data.IndexData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PreProcess {
    List<Integer> arcIds;

    EntityData entityData;
    IndexData indexData;
    HashData hashData;

    public PreProcess(List<Star> stars, List<Equip> equips, List<Arc> arcs, Task validTask) {
        this.entityData = new EntityData(stars, equips, arcs, validTask);

    }

    public EntityData createEntity() {
        return entityData;
    }

    //创建索引数据
    public IndexData createIndex() {
        //建立由ID查找索引的Map
        HashMap<Integer, Integer> starIndexMap = new HashMap<>();
        HashMap<Integer, Integer> equIndexMap = new HashMap<>();
        HashMap<Integer, Integer> arcIndexMap = new HashMap<>();
        for (int starIndex = 0; starIndex < entityData.getStarNum(); starIndex++) {
            int starId = entityData.getStar(starIndex).getStarId();
            starIndexMap.put(starId, starIndex);
        }
        for (int equIndex = 0; equIndex < entityData.getEquNum(); equIndex++) {
            int equipID = entityData.getEqu(equIndex).getEquipId();
            equIndexMap.put(equipID, equIndex);
        }
        for (int arcIndex = 0; arcIndex < entityData.getArcNum(); arcIndex++) {
            int arcId = entityData.getArc(arcIndex).getArcId();
            arcIndexMap.put(arcId, arcIndex);
        }
        this.indexData = new IndexData(starIndexMap, equIndexMap, arcIndexMap);
        return indexData;
    }

    //创建Map数据
    public HashData createHash() {
        //装备-弧段列表，由索引查找
        HashMap<Integer, ArrayList<Integer>> arcsByEquips = new HashMap<>();
        //卫星-弧段列表，由索引查找
        HashMap<Integer, ArrayList<Integer>> arcsByStars = new HashMap<>();
        //弧段-装备索引
        HashMap<Integer, Integer> equByArc = new HashMap<>();
        //弧段-卫星索引
        HashMap<Integer, Integer> starByArc = new HashMap<>();

        for (Arc arc : entityData.getArcs()) {
            int arcId = arc.getArcId();
            int equId = arc.getEquipId();
            int starId = arc.getStarID();

            int arcIndex = indexData.getArcIndexById(arcId);
            int equIndex = indexData.getEquIndexById(equId);
            int starIndex = indexData.getStarIndexById(starId);

            //存储弧段信息
            equByArc.put(arcIndex, equIndex);
            starByArc.put(arcIndex, starIndex);

            //若map不包含该equIndex对应的Key
            if (!arcsByEquips.containsKey(equIndex)) {
                ArrayList<Integer> arcsList = new ArrayList<>();
                arcsByEquips.put(equIndex, arcsList);
            }
            //将该arcIndex加入Map中
            arcsByEquips.get(equIndex).add(arcIndex);

            //若map不包含该starIndex对应的Key
            if (!arcsByStars.containsKey(starIndex)) {
                ArrayList<Integer> arcsList = new ArrayList<>();
                arcsByStars.put(starIndex, arcsList);
            }
            //将该arcIndex加入Map中
            arcsByStars.get(starIndex).add(arcIndex);
        }
        this.hashData = new HashData(arcsByEquips, arcsByStars, equByArc, starByArc);
        return hashData;
    }
}
