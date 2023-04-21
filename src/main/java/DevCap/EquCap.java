package DevCap;

import java.util.ArrayList;

public class EquCap {
    //波束数量
    int beamNum;
    //波束能力
    ArrayList<String[]> beamCapList;

    public EquCap(int beamNum){
        this.beamNum = beamNum;
    }

    public void addBeamCap(String[] beamCap){
        beamCapList.add(beamCap);
    }

    public boolean verifyCap(TaskType taskType){
        return beamCapList.contains(taskType.getTtarTypeString());
    }

    public int getBeamNum() {
        return beamNum;
    }
}
