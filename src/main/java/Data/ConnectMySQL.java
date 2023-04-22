package Data;

import java.sql.*;
import java.util.ArrayList;

public class ConnectMySQL {
    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/or";
    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "M.always.99";
    //存放原始数据
    ArrayList<String> oriDataStar;
    ArrayList<String[]> oriDataEqu;
    ArrayList<String[]> oriDataArcs;

    public void loadFile() {
        Connection conn = null;
        Statement stmt = null;
        this.oriDataStar = new ArrayList<>();
        this.oriDataEqu = new ArrayList<>();
        this.oriDataArcs = new ArrayList<>();

        try{
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接

//            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
//            System.out.println(" 实例化Statement对象...");1311
            //1311、1312、1313、1314、1318、1319、1321、1322、1323、1325、1327、1328
            stmt = conn.createStatement();
            stmt = conn.createStatement();
            //分别查找卫星、装备、弧段
            String sqlStar = "select SAT_CODE from or.xqcl_sub_require where REQUIRE_ID = 499 and exists(select IdSat from or.zbdd_data_arcs where IdSat in (SELECT SAT_CODE FROM or.xqcl_sub_require where REQUIRE_ID=499) and IdDev in (1311,1312,1313,1314,1318,1319) and ID not in (select ARCID from or.zbdd_scheduling_plan_provisional) and or.xqcl_sub_require.SAT_CODE = or.zbdd_data_arcs.IdSat);";
            String sqlEqu = "select DEVICECODE, CAPBILITY FROM or.zbdd_device_status_constraint where DEVICECODE in (1311,1312,1313,1314,1318,1319);";
            String sqlArcs = "select IdSat, IdDev, StartTimeUtc, EndPtimeUtc, Mark, Rev, ID from or.zbdd_data_arcs where IdSat in (SELECT SAT_CODE FROM or.xqcl_sub_require where REQUIRE_ID=499) and IdDev in (1311,1312,1313,1314,1318,1319) and ID not in (select ARCID from or.zbdd_scheduling_plan_provisional);";

            ResultSet rsStar  = stmt.executeQuery(sqlStar);
            // 展开结果集数据库
            while(rsStar.next()){
                String SAT_CODE = rsStar.getString("SAT_CODE");
                oriDataStar.add(SAT_CODE);
            }
            // 完成后关闭
            rsStar.close();

            ResultSet rsEqu  = stmt.executeQuery(sqlEqu);
            // 展开结果集数据库
            while(rsEqu.next()){
                String DeviceCode = rsEqu.getString("DEVICECODE");
                String Capability = rsEqu.getString("CAPBILITY");
                oriDataEqu.add(new String[]{DeviceCode, Capability});
            }
            // 完成后关闭
            rsEqu.close();

            ResultSet rsArcs  = stmt.executeQuery(sqlArcs);
            while(rsArcs.next()){
                // 通过字段检索
                String IdStar = rsArcs.getString("IdSat");
                String IdEqu = rsArcs.getString("IdDev");
                String StartTime = rsArcs.getString("StartTimeUtc");
                String EndTime = rsArcs.getString("EndPTimeUtc");
                String Mark = rsArcs.getString("Mark");
                String Rev = rsArcs.getString("Rev");
                String ID = rsArcs.getString("ID");
                String[] item = new String[]{ID, IdStar, IdEqu, StartTime, EndTime, Mark, Rev};
                oriDataArcs.add(item);
            }
            // 完成后关闭
            rsArcs.close();

            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("连接数据库成功!");
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
