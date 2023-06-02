package ScheduleMip.Data.DataBase;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataMapper {
    public static <T> List<T> resultSetToList(ResultSet rs, Class<T> clazz) throws Exception {
        // 获取结果集的元数据
//        ResultSetMetaData metaData = rs.getMetaData();
//        int columnCount = metaData.getColumnCount();
        // 构造泛型集合
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            // 通过反射机制创建一个实例
            T obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            Column presentColumn;
            String columnName;
            // 遍历每个字段，获取字段名和对应的值，并通过反射赋值
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    presentColumn = field.getDeclaredAnnotation(Column.class);
                    columnName = presentColumn.name();
                    Object columnValue = rs.getObject(columnName);
                    String variableName = field.getName();
                    field = clazz.getDeclaredField(variableName);
                    field.setAccessible(true);
                    field.set(obj, columnValue);
                }
            }
            list.add(obj);
        }
        return list;
    }
}
