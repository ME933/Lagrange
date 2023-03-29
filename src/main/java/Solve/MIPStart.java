package Solve;

import Data.PreData;
import File.MIPJsonStart;

import java.util.ArrayList;

public class MIPStart {
    ArrayList<Integer> selectedArcList;
    ArrayList<Integer> completedStarList;

    public void readStartList(){
        MIPJsonStart mipJsonStart = new MIPJsonStart();
        this.selectedArcList = mipJsonStart.ReadArcJson();
        this.completedStarList = mipJsonStart.ReadStarJson();
    }

    public double[] getSelectedArcList(PreData preData){
        double[] mipStart = new double[preData.getArcNum()];
        for (int arcID:selectedArcList) {
            int arcIndex = preData.getArcIndex(arcID);
            mipStart[arcIndex] = 1.0;
        }
        return mipStart;
    }

    public double[] getCompletedStarList(PreData preData){
        double[] mipStart = new double[preData.getStarNum()];
        for (int starID:completedStarList) {
            int arcIndex = preData.getStarIndex(starID);
            mipStart[arcIndex] = 1.0;
        }
        return mipStart;
    }
}
