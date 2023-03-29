package File;

import com.google.gson.Gson;

import java.io.FileReader;
import java.util.ArrayList;

public class MIPJsonStart {
    final String fileNameArc = "File\\selectedArc.json";
    final String fileNameStar = "File\\completedStar.json";

    public ArrayList<Integer> ReadArcJson(){
        // Create a HashMap<Integer, ArrayList<Integer>>
        return getArrayList(fileNameArc);
    }

    public ArrayList<Integer> ReadStarJson(){
        // Create a HashMap<Integer, ArrayList<Integer>>
        return getArrayList(fileNameStar);
    }

    private ArrayList<Integer> getArrayList(String fileNameStar) {
        ArrayList<int[]> conArc = new ArrayList<>();
        Gson gson = new Gson();
        ArrayList<Integer> completedStar = new ArrayList<>();
        try {
            // Read the JSON file
            FileReader reader = new FileReader(fileNameStar);

            // Convert the JSON file to a Map object
            ArrayList<String> array = gson.fromJson(reader, ArrayList.class);
            for (String arcID:array) {
                completedStar.add(Integer.valueOf(arcID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return completedStar;
    }
}
