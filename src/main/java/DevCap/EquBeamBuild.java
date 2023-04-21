package DevCap;

import Data.PreData;

public class EquBeamBuild {
    EquCap[] equCapList;

    //频段：L、S、C
    //模式：RC（遥控）、TE（遥测）、SU（测量）、SS（频谱感知）、DT（数传）、NC（非合作）
    //带宽：MHz

    public EquBeamBuild(){
        this.equCapList = new EquCap[5];

        equCapList[0] = new EquCap(4);
        equCapList[0].addBeamCap(new String[]{"L","RC","20"});
        equCapList[0].addBeamCap(new String[]{"S","TE","20"});
        equCapList[0].addBeamCap(new String[]{"S","SS","20"});
        equCapList[0].addBeamCap(new String[]{"C","SU","20"});
        equCapList[0].addBeamCap(new String[]{"L","RC","20"});

        equCapList[1] = new EquCap(2);
        equCapList[1].addBeamCap(new String[]{"L","RC","20"});
        equCapList[1].addBeamCap(new String[]{"S","TE","20"});
        equCapList[1].addBeamCap(new String[]{"S","SS","20"});
        equCapList[1].addBeamCap(new String[]{"C","SU","20"});
        equCapList[1].addBeamCap(new String[]{"L","RC","20"});

        equCapList[2] = new EquCap(2);
        equCapList[2].addBeamCap(new String[]{"S","RC","20"});
        equCapList[2].addBeamCap(new String[]{"S","TE","20"});
        equCapList[2].addBeamCap(new String[]{"S","SU","20"});
        equCapList[2].addBeamCap(new String[]{"S","SS","30"});
        equCapList[2].addBeamCap(new String[]{"S","RC","20"});

        equCapList[3] = new EquCap(2);
        equCapList[3].addBeamCap(new String[]{"S","DT","20"});
        equCapList[3].addBeamCap(new String[]{"S","NC","25"});

        equCapList[4] = new EquCap(2);
        equCapList[4].addBeamCap(new String[]{"S","DT","20"});
        equCapList[4].addBeamCap(new String[]{"S","NC","25"});

        equCapList[5] = new EquCap(20);
        equCapList[5].addBeamCap(new String[]{"L","RC","20"});
        equCapList[5].addBeamCap(new String[]{"L","SS","20"});
        equCapList[5].addBeamCap(new String[]{"S","RC","20"});
        equCapList[5].addBeamCap(new String[]{"S","TE","20"});
        equCapList[5].addBeamCap(new String[]{"S","SU","20"});
        equCapList[5].addBeamCap(new String[]{"S","SS","20"});
        equCapList[5].addBeamCap(new String[]{"C","RC","20"});
        equCapList[5].addBeamCap(new String[]{"C","TE","20"});
        equCapList[5].addBeamCap(new String[]{"C","SU","20"});
        equCapList[5].addBeamCap(new String[]{"C","SS","20"});
    }

    public EquCap[] getEquCapList() {
        return equCapList;
    }

}
