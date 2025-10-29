package com.overthinker.cloud.common.handler.mybatisTypeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Custom MyBatis TypeHandler to map a List<String> to a single VARCHAR column.
 * The list is stored as a comma-delimited string.
 * This version is robust against NULL values from the database.
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({List.class})
public class StringListTypeHandler extends BaseTypeHandler<List<String>> {

    private static final String DELIMITER = ",";

    /**
     * Converts a List<String> to a comma-delimited String for storing in the database.
     */
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        // Ensure the parameter is not null to avoid issues, though BaseTypeHandler often handles this.
        if (parameter == null) {
            ps.setString(i, "");
        } else {
            String value = StringUtils.collectionToDelimitedString(parameter, DELIMITER);
            ps.setString(i, value);
        }
    }

    /**
     * Converts a String from the database to a List<String>.
     * Handles NULL database values gracefully by returning an empty list.
     */
    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null || value.isEmpty()) {
            return Collections.emptyList(); // Return empty list for null or empty strings
        }
        return Arrays.asList(StringUtils.tokenizeToStringArray(value, DELIMITER));
    }

    /**
     * Converts a String from the database to a List<String>.
     * Handles NULL database values gracefully by returning an empty list.
     */
    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(StringUtils.tokenizeToStringArray(value, DELIMITER));
    }

    /**
     * Converts a String from the database to a List<String>.
     * Handles NULL database values gracefully by returning an empty list.
     */
    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null || value.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(StringUtils.tokenizeToStringArray(value, DELIMITER));
    }
}
