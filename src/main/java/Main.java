import DataBase.ConnectMySQL;
import Entity.Arc;
import Entity.Equip;
import Entity.Star;
import Entity.Task;
import ScheduleMip.ScheduleMip;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        ConnectMySQL connectMySQL = new ConnectMySQL();
        connectMySQL.connectDataBase();
        Task task = connectMySQL.queryTask("499");
        List<Equip> equips = connectMySQL.queryEquip("(1311,1312,1313,1314,1318,1319)");
        List<Star> stars = connectMySQL.queryStar();
        List<Arc> arcs = connectMySQL.queryArc();
        connectMySQL.close();

        ScheduleMip scheduleMip = new ScheduleMip();
        List<Integer> arcIds = new ArrayList<>();
        ArrayList<Float> result =  scheduleMip.schedule(stars, equips , arcs, arcIds, task);
    }
}
