package com.overthinker.cloud.common.handler.mybatisTypeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class StringArrayTypeHandler extends BaseTypeHandler<String[]> {

    // 分隔符
    private static final String SEPARATOR = ",";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
        // 将 String[] 转换为逗号分隔的字符串
        String joinedString = Arrays.stream(parameter)
                .collect(Collectors.joining(SEPARATOR));
        ps.setString(i, joinedString);
    }

    @Override
    public String[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 从 ResultSet 中获取字符串，并将其分割成 String[]
        String columnValue = rs.getString(columnName);
        if (columnValue != null) {
            return columnValue.split(SEPARATOR);
        }
        return null;
    }

    @Override
    public String[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // 从 ResultSet 中按列索引获取字符串，并分割
        String columnValue = rs.getString(columnIndex);
        if (columnValue != null) {
            return columnValue.split(SEPARATOR);
        }
        return null;
    }

    @Override
    public String[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 从 CallableStatement 中获取字符串，并分割
        String columnValue = cs.getString(columnIndex);
        if (columnValue != null) {
            return columnValue.split(SEPARATOR);
        }
        return null;
    }
}