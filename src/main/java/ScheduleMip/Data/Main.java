package ScheduleMip.Data;

import ScheduleMip.Data.DataBase.ConnectMySQL;
import ScheduleMip.Data.Entity.Arc;
import ScheduleMip.ScheduleMip;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        ScheduleMip.Data.ScheduleMip.ScheduleMip.Data.DataBase.ConnectMySQL connectMySQL = new ConnectMySQL();
        connectMySQL.connectDataBase();
        ScheduleMip.Data.ScheduleMip.ScheduleMip.Data.Entity.Task task = connectMySQL.queryTask("499");
        List<ScheduleMip.Data.ScheduleMip.ScheduleMip.Data.Entity.Equip> equips = connectMySQL.queryEquip("(1311,1312,1313,1314,1318,1319)");
        List<ScheduleMip.Data.ScheduleMip.ScheduleMip.Data.Entity.Star> stars = connectMySQL.queryStar();
        List<Arc> arcs = connectMySQL.queryArc();
        connectMySQL.close();

        ScheduleMip.Data.ScheduleMip.ScheduleMip.Data.ScheduleMip.ScheduleMip scheduleMip = new ScheduleMip.Data.ScheduleMip.ScheduleMip.Data.ScheduleMip.ScheduleMip();
        List<Integer> arcIds = new ArrayList<>();
        ArrayList<Float> result =  scheduleMip.schedule(stars, equips , arcs, arcIds, task);
    }
}
