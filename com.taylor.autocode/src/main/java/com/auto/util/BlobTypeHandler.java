package com.auto.util;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.log4j.Logger;

/**
 * @notes:处理Blob类型中文乱码问题
 *
 * @author taylor
 *
 * @date 2014-4-25 下午4:00:43
 */
public class BlobTypeHandler extends BaseTypeHandler<String> implements Serializable {

    private static final Logger LOG = Logger.getLogger(BlobTypeHandler.class);

    private static final long serialVersionUID = 8262816168060958962L;

    private static final String DEFAULT_CHARSET = "utf-8";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) {
        ByteArrayInputStream bis;
        try {
            // ###把String转化成byte流
            bis = new ByteArrayInputStream(parameter.getBytes(DEFAULT_CHARSET));
            ps.setBinaryStream(i, bis, parameter.length());
        } catch (UnsupportedEncodingException e) {
            LOG.error("error", e);
        } catch (SQLException e) {
            LOG.error("error", e);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) {
        try {
            Blob blob = rs.getBlob(columnName);
            byte[] returnValue = null;
            if (null != blob) {
                returnValue = blob.getBytes(1, (int) blob.length());
                // ###把byte转化成string
                return new String(returnValue, DEFAULT_CHARSET);
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error("error", e);
        } catch (SQLException e) {
            LOG.error("error", e);
        }
        return null;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) {
        try {
            Blob blob = cs.getBlob(columnIndex);
            byte[] returnValue = null;
            if (null != blob) {
                returnValue = blob.getBytes(1, (int) blob.length());
                return new String(returnValue, DEFAULT_CHARSET);
            } else {
                LOG.error("Blob Encoding Error!");
                throw new RuntimeException("Blob Encoding Error!");
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error("error", e);
        } catch (SQLException e) {
            LOG.error("error", e);
        }
        return null;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
