package DevCap;

import java.util.ArrayList;
import java.util.Arrays;

public class EquCap {
    //波束数量
    int beamNum;
    //波束能力
    ArrayList<String[]> beamCapList;

    public EquCap(int beamNum){
        this.beamNum = beamNum;
        this.beamCapList = new ArrayList<>();
    }

    public void addBeamCap(String[] beamCap){
        beamCapList.add(beamCap);
    }

    public boolean verifyCap(TaskType taskType){
        return beamCapList.stream().anyMatch(array -> Arrays.equals(array, taskType.getTtarTypeString()));
//        return beamCapList.contains(taskType.getTtarTypeString());
    }

    public int getBeamNum() {
        return beamNum;
    }
}
