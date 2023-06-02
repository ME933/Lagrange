package ScheduleMip.Data.DataBase;

import ScheduleMip.Data.Entity.Arc;
import ScheduleMip.Data.Entity.DataBaseEntity.ArcEntity;
import ScheduleMip.Data.Entity.DataBaseEntity.EquipEntity;
import ScheduleMip.Data.Entity.DataBaseEntity.StarEntity;
import ScheduleMip.Data.Entity.DataBaseEntity.TaskEntity;
import ScheduleMip.Data.Entity.Equip;
import ScheduleMip.Data.Entity.Star;
import ScheduleMip.Data.Entity.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static DataBase.DataMapper.resultSetToList;

public class ConnectMySQL {
    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/schedule";
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "M.always.99";

    Connection conn = null;
    Statement stmt = null;

    String taskId;
    String equList;

    public void connectDataBase() throws ClassNotFoundException, SQLException {
        // 注册 JDBC 驱动
        Class.forName(JDBC_DRIVER);

        // 打开链接
        conn = DriverManager.getConnection(DB_URL,USER,PASS);

        // 执行查询
        //1311、1312、1313、1314、1318、1319、1321、1322、1323、1325、1327、1328
        stmt = conn.createStatement();

        System.out.println("连接数据库成功!");
    }

    /**
     * @description:  输入taskId，查询任务信息
     * @return Entity.Task
     * @date: 2023/5/31 20:50
     */
    public Task queryTask(String taskId) throws Exception {
        this.taskId = taskId;
        //查询数据库
        String arcStar = "select task_id, cycle_count, cycle_unit, duration_time from schedule.task where task_id = " + this.taskId + ";";
        ResultSet rs  = stmt.executeQuery(arcStar);
        TaskEntity taskEntity = resultSetToList(rs, TaskEntity.class).get(0);
        //将TaskEntity转化为Task
        Task task = new Task(taskEntity);
        rs.close();
        return task;
    }

    /**
     * @description: 输入equList的字符串作为参数，查询equList中equip的信息并将其转化为Equip
     * @return java.util.List<Entity.Equip>
     * @date: 2023/5/31 15:37
     */
    public List<Equip> queryEquip(String equList) throws Exception {
        this.equList = equList;
        //查询数据库
        String sqlEqu = "select DEVICECODE, CAPABILITY FROM schedule.equip where DEVICECODE in " + this.equList + ";";
        ResultSet rs  = stmt.executeQuery(sqlEqu);
        List<EquipEntity> entityList = resultSetToList(rs, EquipEntity.class);
        //将EquipEntity转化为Equip
        List<Equip> equipList = new ArrayList<>();
        for (EquipEntity equipEntity: entityList) {
            equipList.add(new Equip(equipEntity));
        }
        rs.close();
        return equipList;
    }

    public List<Star> queryStar() throws Exception {
        //查询数据库
        String sqlStar = "select target_id from schedule.task_target where task_id = " + this.taskId +
                " and exists(select IdSat from schedule.arcs where IdDev in " + this.equList + ");";
        ResultSet rs  = stmt.executeQuery(sqlStar);
        List<StarEntity> entityList = resultSetToList(rs, StarEntity.class);
        //将StarEntity转化为Star
        List<Star> starList = new ArrayList<>();
        for (StarEntity starEntity: entityList) {
            starList.add(new Star(starEntity));
        }
        rs.close();
        return starList;
    }

    public List<Arc> queryArc() throws Exception {
        //查询数据库
        String arcStar = "select ID, IdSat ,IdDev, StartTimeUtc, StartAzimuthDeg, StartElevationDeg, StartRangeKm, EndPtimeUtc, EndPAzimuthDeg, EndPElevationDeg, EndPRangeKm, Mark, Rev " +
                "from schedule.arcs where IdSat in " +
                "(select target_id from schedule.task_target where task_id = " + this.taskId +
                " and exists(select IdSat from schedule.arcs where IdDev in " + this.equList + "));";
        ResultSet rs  = stmt.executeQuery(arcStar);
        List<ArcEntity> entityList = resultSetToList(rs, ArcEntity.class);
        //将ArcEntity转化为Arc
        List<Arc> arcList = new ArrayList<>();
        for (ArcEntity arcEntity: entityList) {
            arcList.add(new Arc(arcEntity));
        }
        rs.close();
        return arcList;
    }

    public void close() throws SQLException {
        stmt.close();
        conn.close();
    }



}
