package ScheduleMip.Data;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ResultData {
    List<Integer> arcIds;
    float countCover;
    float countComplete;
    float countArc;
    float timeCost;

    int starNum;
    int arcNum;

    public ResultData(List<Integer> arcIds, int starNum, int arcNum){
        this.arcIds = arcIds;
        this.starNum = starNum;
        this.arcNum = arcNum;
    }

    public void addArcResult(int arcId){
        arcIds.add(arcId);
    }

    /**
     * @description: 返回List，任务覆盖率，任务完成率，弧段覆盖率，花费时间
     * @params: []
     * @return:
     */
    public ArrayList<Float> getResultList(){
        float taskCoverRatio = ((float) Math.round(countCover/starNum * 10000)) / 100;
        float taskCompleteRatio = ((float) Math.round(countComplete/starNum * 10000)) / 100;
        float arcUsedRatio = ((float) Math.round(countArc/arcNum * 10000)) / 100;
        float timeCostSeconds = ((float) Math.round(timeCost / 10)) / 100;
        return new ArrayList<> (Arrays.asList(taskCoverRatio, taskCompleteRatio, arcUsedRatio, timeCostSeconds));
    }

}
