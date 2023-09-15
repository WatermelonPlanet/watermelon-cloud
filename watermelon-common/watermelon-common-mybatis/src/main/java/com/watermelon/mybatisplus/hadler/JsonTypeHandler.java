package com.watermelon.mybatisplus.hadler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * postgrepsql 数据库 json&jsonb 类型 映射处理
 * @author byh
 * @desc
 * @since 2023/08/28 09:50
 */
public class JsonTypeHandler<T> extends BaseTypeHandler<T> {

    private final Class<T> type;

    public JsonTypeHandler(Class<T> type) {
        Assert.notNull(type, "Type argument cannot be null");
        this.type = type;
    }


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        if (null != ps && null != parameter) {
            PGobject object = new PGobject();
            object.setType("jsonb");
            object.setValue(JSON.toJSONString(parameter));
            ps.setObject(i, object);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    public T parse(String json) {
        return JSON.parseObject(json, type);
    }
}
