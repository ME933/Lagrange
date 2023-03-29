package File;


import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ConJson {
    final String fileName = new String("File/conArc.json");

    public ArrayList<int[]> WriteConJson(ArrayList<int[]> inConArc){
        ArrayList<int[]> conArc = inConArc;
        this.ConvertHashMapToJson(conArc);
        return conArc;
    }

    //将冲突数据写入文件
    private void ConvertHashMapToJson(ArrayList<int[]> conArc) {
        // Create a Gson object
        Gson gson = new Gson();

        // Convert the HashMap to a JSON string
        String json = gson.toJson(conArc);

        // Write the JSON string to a file
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
            writer.flush();
            System.out.println("文件写入成功。");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<int[]> ReadConJson(){
        // Create a HashMap<Integer, ArrayList<Integer>>
        ArrayList<int[]> conArc = new ArrayList<>();
        Gson gson = new Gson();
        try {
            // Read the JSON file
            FileReader reader = new FileReader(fileName);

            // Convert the JSON file to a Map object
            ArrayList<ArrayList<Double>> array = gson.fromJson(reader, ArrayList.class);

            // Loop through the keys of the Map object
            for (ArrayList<Double> list : array) {
                // Convert the Long values to Integer values
                int[] newList = new int[2];
                newList[0] = list.get(0).intValue();
                newList[1] = list.get(1).intValue();
                // Put the key and the newList into conArc
                conArc.add(newList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conArc;
    }
}
