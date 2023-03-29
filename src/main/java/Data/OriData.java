package Data;

import java.util.ArrayList;

public class OriData {
    //原始卫星数据
    ArrayList<String> oriDataStar;
    //原始设备数据
    ArrayList<String[]> oriDataEqu;
    //原始弧段数据
    ArrayList<String[]> oriDataArcs;

    public OriData(){
        ConnectMySQL connect = new ConnectMySQL();
        connect.loadFile();
        this.oriDataStar = connect.getOriDataStar();
        this.oriDataEqu = connect.getOriDataEqu();
        this.oriDataArcs = connect.getOriDataArcs();
    }

    public OriData(int starNum, int equNum, int arcNum, int timeWinSizeMin, int[] equCap) {
        GenerateOriData generateOriData = new GenerateOriData(starNum, equNum, arcNum, timeWinSizeMin, equCap);
        this.oriDataStar = generateOriData.getOriDataStar();
        this.oriDataEqu = generateOriData.getOriDataEqu();
        this.oriDataArcs = generateOriData.getOriDataArcs();
    }


    public ArrayList<String> getOriDataStar() {
        return oriDataStar;
    }

    public ArrayList<String[]> getOriDataEqu() {
        return oriDataEqu;
    }

    public ArrayList<String[]> getOriDataArcs() {
        return oriDataArcs;
    }
}
