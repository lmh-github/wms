package com.gionee.top.db;

import java.io.BufferedReader;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gionee.top.config.SystemConfig;
import com.gionee.top.entity.Page;
import com.gionee.top.util.BeanUtil;

@Repository
public class DBHelper
{
    private  final Log log = LogFactory.getLog(DBHelper.class);

    private  final int JDBC_QUERYTIMEOUT = SystemConfig.JDBC_QUERYTIMEOUT;
    
    @Autowired
    private ConnectionManager connectionManager;
    
    /**
     * MySQL 分页查询
     * 
     * @param sql
     * @param params 查询参数
     * @param curpage 当前页
     * @param pageSize 页面大小
     * @return
     */
	public Page pageQuery(String sql, Object[] params, int curpage, int pageSize) {
    	return pageQuery(sql, params, curpage, pageSize, connectionManager.getConnection(), true);
    }
    
    /**
     * MySQL 分页查询
     * 
     * @param sql
     * @param params 查询参数
     * @param beanClass 要返回对象Class类型
     * @param curpage 当前页
     * @param pageSize 页面大小
     * @return
     */
    public <T> Page<T> pageQuery(String sql, Object[] params, Class<T> beanClass, int curpage, int pageSize) {
    	return pageQuery(sql, params, beanClass, curpage, pageSize, connectionManager.getConnection(), true);
    }
    
    /**
     * MySQL 分页查询
     * 
     * @param sql
     * @param params 查询参数
     * @param beanClass 要返回对象Class类型
     * @param curpage 当前页
     * @param pageSize 页面大小
     * @param conn 数据库连接
     * @param closeConn 是否关掉数据库连接
     * @return
     */
    public <T> Page<T> pageQuery(String sql, Object[] params, Class<T> beanClass, int curpage, int pageSize, Connection conn, boolean closeConn) {
    	Page<T> page = new Page<T>();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try {
            List<T> retList = new ArrayList<T>();
            int offset = pageSize * (curpage - 1);
            String pageQuerySql = sql + " LIMIT " + offset + "," + pageSize;
            pstat = conn.prepareStatement(pageQuerySql);
            pstat.setQueryTimeout(JDBC_QUERYTIMEOUT);
            if(null!=params) {
            	Object obj = null;
                for (int i = 0; i < params.length; i++) {
                    obj = params[i];
                    int index = i + 1;
                    pstat.setObject(index, obj);
                }
            }
            rs = pstat.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int cols = metaData.getColumnCount();
            while (rs.next()) {
            	
            	T object = beanClass.newInstance();
                for (int i = 0; i < cols; i++)
                {
                    int index = i + 1;
                    String colName = metaData.getColumnLabel(index);
                    if (StringUtils.isEmpty(colName)) {
                    	colName = metaData.getColumnName(index);
                    }
                    Object colValue = rs.getObject(index);
                    if (colValue instanceof java.sql.Date) {
                    	colValue = rs.getTimestamp(index);
                    }
                    if (colValue instanceof oracle.sql.CLOB) {
                    	colValue = convertToString((oracle.sql.CLOB)colValue);
                    }
                    BeanUtil.beanRegister(object, colName, colValue);
                }
                retList.add(object);
            }
            
            int totalCount = queryTotalCount(sql, params, conn);
            int totalpage = 0;
            if (totalCount%pageSize == 0) {
            	totalpage = totalCount/pageSize;
            } else {
            	totalpage = totalCount/pageSize + 1;
            }
			page.setCount(totalCount);
			page.setCurpage(curpage);
			page.setTotalpage(totalpage);
            page.setItems(retList);
            return page;
        } catch (Exception e) {
            log.error("pageQuery", e);
        } finally {
            SqlResourceHelper.closeResultSet(rs);
            SqlResourceHelper.closeStatement(pstat);
            if (closeConn)
            	SqlResourceHelper.closeConnection(conn);
        }
        return page;
    }
    
    /**
     * MySQL 分页查询
     * 
     * @param sql
     * @param params 参数
     * @param curpage 当前页
     * @param pageSize 页面大小
     * @param conn 数据库连接
     * @param closeConn 是否关掉数据库连接
     * @return
     */
    public Page pageQuery(String sql, Object[] params, int curpage, int pageSize, Connection conn, boolean closeConn) {
    	Page page = new Page();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try {
            List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
            int offset = pageSize * (curpage - 1);
            String pageQuerySql = sql + " LIMIT " + offset + "," + pageSize;
            pstat = conn.prepareStatement(pageQuerySql);
            pstat.setQueryTimeout(JDBC_QUERYTIMEOUT);
            if(null!=params) {
            	Object obj = null;
                for (int i = 0; i < params.length; i++) {
                    obj = params[i];
                    int index = i + 1;
                    pstat.setObject(index, obj);
                }
            }
            rs = pstat.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int cols = metaData.getColumnCount();
            while (rs.next()) {
                Map<String,Object> map = new HashMap<String,Object>();
                for (int i = 0; i < cols; i++) {
                    int index = i + 1;
                    String colName = metaData.getColumnLabel(index);
	                    Object colValue = rs.getObject(index);
	                    if (colValue instanceof java.sql.Date) {
	                    	colValue = rs.getTimestamp(index);
	                    }
	                    if (colValue instanceof oracle.sql.CLOB) {
	                    	colValue = convertToString((oracle.sql.CLOB)colValue);
	                    }
	                    map.put(colName, colValue);
                }
                retList.add(map);
            }
            
            int totalCount = queryTotalCount(sql, params, conn);
            int totalpage = 0;
            if (totalCount%pageSize == 0) {
            	totalpage = totalCount/pageSize;
            } else {
            	totalpage = totalCount/pageSize + 1;
            }
			page.setCount(totalCount);
			page.setCurpage(curpage);
			page.setTotalpage(totalpage);
            page.setItems(retList);
            return page;
        } catch (Exception e) {
            log.error("mySqlPageQuery", e);
        } finally {
            SqlResourceHelper.closeResultSet(rs);
            SqlResourceHelper.closeStatement(pstat);
            if (closeConn)
            	SqlResourceHelper.closeConnection(conn);
        }
        return page;
    }

    /**
     * 查询数据 返回Object列表
     * @param sql
     * @param param
     * @param retClass
     * @return
     */
	public <T> List<T> queryObjectList(String sql, Object[] params, Class<T> retClass) {
    	return queryObjectList(sql, params, retClass, connectionManager.getConnection(), true);
    }
    /**
     * 查询数据 返回Object列表
     * @param sql
     * @param param
     * @param retClass
     * @param conn 数据库连接
     * @param closeConn 是否关掉数据库连接
     * @return
     */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryObjectList(String sql, Object[] params,
            Class<T> retClass, Connection conn, boolean closeConn)
    {
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try
        {
            List<T> retList = new ArrayList<T>();
            pstat = conn.prepareStatement(sql);
            pstat.setQueryTimeout(JDBC_QUERYTIMEOUT);
            if(null!=params)
            {
                for (int i = 0; i < params.length; i++)
                {
                    Object obj = params[i];
                    int index = i + 1;
                    pstat.setObject(index, obj);
                }
            }
            rs = pstat.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int cols = metaData.getColumnCount();
            while (rs.next())
            {
                String retClassName = retClass.getName();
                if (-1 != retClassName.indexOf("java.lang"))
                {
                    T colValue = (T)rs.getObject(1);
                    retList.add(colValue);
                } else
                {
                    T object = retClass.newInstance();
                    for (int i = 0; i < cols; i++)
                    {
                        int index = i + 1;
                        String colName = metaData.getColumnLabel(index);
                        if (StringUtils.isEmpty(colName)) {
                        	colName = metaData.getColumnName(index);
                        }
                        Object colValue = rs.getObject(index);
                        if (colValue instanceof java.sql.Date) {
                        	colValue = rs.getTimestamp(index);
                        }
                        if (colValue instanceof oracle.sql.CLOB) {
                        	colValue = convertToString((oracle.sql.CLOB)colValue);
                        }
                        BeanUtil.beanRegister(object, colName, colValue);
                    }
                    retList.add(object);
                }
            }
            return retList;
        } catch (Exception e)
        {
            log.error("queryObjectList", e);
        } finally
        {
            SqlResourceHelper.closeResultSet(rs);
            SqlResourceHelper.closeStatement(pstat);
            if (closeConn)
            	SqlResourceHelper.closeConnection(conn);
        }
        return null;
    }
    
    /**
     * 查询数据 返回Object
     * @param sql
     * @param param
     * @param retClass
     * @return
     */
	public  <T> T queryObject(String sql, Object[] params,
            Class<T> retClass) {
    	return queryObject(sql, params, retClass, connectionManager.getConnection(), true);
    }
    
    /**
     * 查询数据 返回Object
     * @param sql
     * @param param
     * @param retClass
     * @param conn 数据库连接
     * @param closeConn 是否关掉数据库连接
     * @return
     */
	@SuppressWarnings("unchecked")
	public  <T> T queryObject(String sql, Object[] params,
            Class<T> retClass, Connection conn, boolean closeConn)
    {
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try
        {
            pstat = conn.prepareStatement(sql);
            pstat.setQueryTimeout(JDBC_QUERYTIMEOUT);
            if(null!=params)
            {
                for (int i = 0; i < params.length; i++)
                {
                    Object obj = params[i];
                    int index = i + 1;
                    pstat.setObject(index, obj);
                }
            }
            rs = pstat.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int cols = metaData.getColumnCount();
            if (rs.next())
            {
                String retClassName = retClass.getName();
                if (-1 != retClassName.indexOf("java.lang"))
                {
                    T colValue = (T)rs.getObject(1);
                    return colValue;
                } else
                {
                    T object = retClass.newInstance();
                    for (int i = 0; i < cols; i++)
                    {
                        int index = i + 1;
                        String colName = metaData.getColumnLabel(index);
                        if (StringUtils.isEmpty(colName)) {
                        	colName = metaData.getColumnName(index);
                        }
                        Object colValue = rs.getObject(index);
                        if (colValue instanceof java.sql.Date) {
                        	colValue = rs.getTimestamp(index);
                        }
                        if (colValue instanceof oracle.sql.CLOB) {
                        	colValue = convertToString((oracle.sql.CLOB)colValue);
                        }
                        BeanUtil.beanRegister(object, colName, colValue);
                    }
                    return object;
                }
            }
        } catch (Exception e)
        {
            log.error("queryObject", e);
        } finally
        {
            SqlResourceHelper.closeResultSet(rs);
            SqlResourceHelper.closeStatement(pstat);
            if (closeConn) 
            	SqlResourceHelper.closeConnection(conn);
        }
        return null;
    }
    
    /**
     * 查询数据 返回Map列表
     * 
     * @param sql
     * @param params
     * @return
     */
    public  List<Map<String,Object>> queryMapList(String sql, Object[] params) {
    	return queryMapList(sql, params, connectionManager.getConnection(), true);
    }
    /**
     * 查询数据 返回Map列表
     * @param sql
     * @param param
     * @param retClass
     * @param conn 数据库连接
     * @param closeConn 是否关掉数据库连接
     * @return
     */
    public  List<Map<String,Object>> queryMapList(String sql, Object[] params, Connection conn, boolean closeConn)
    {
    	List<Map<String,Object>> retList = new ArrayList<Map<String,Object>>();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try
        {
            pstat = conn.prepareStatement(sql);
            pstat.setQueryTimeout(JDBC_QUERYTIMEOUT);
            if(null!=params)
            {
                for (int i = 0; i < params.length; i++)
                {
                    Object obj = params[i];
                    int index = i + 1;
                    pstat.setObject(index, obj);
                }
            }
            rs = pstat.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int cols = metaData.getColumnCount();
            while (rs.next())
            {
                Map<String,Object> map = new HashMap<String,Object>();
                for (int i = 0; i < cols; i++)
                {
                    int index = i + 1;
                    String colName = metaData.getColumnLabel(index);
                    Object colValue = rs.getObject(index);
                    if (colValue instanceof java.sql.Date) {
                    	colValue = rs.getTimestamp(index);
                    }
                    if (colValue instanceof oracle.sql.CLOB) {
                    	colValue = convertToString((oracle.sql.CLOB)colValue);
                    }
                    map.put(colName, colValue);
                }
                retList.add(map);
            }
            return retList;
        } catch (Exception e)
        {
            log.error("queryMapList", e);
        } finally
        {
            SqlResourceHelper.closeResultSet(rs);
            SqlResourceHelper.closeStatement(pstat);
            if (closeConn)
            	SqlResourceHelper.closeConnection(conn);
        }
        return retList;
    }
    
    public  Map<String,Object> queryMap(String sql, Object[] params) {
    	return queryMap(sql, params, connectionManager.getConnection(), true);
    }
    /**
     * 查询数据 返回Map列表
     * @param sql
     * @param param
     * @param retClass
     *  @param conn 数据库连接
     * @param closeConn 是否关掉数据库连接
     * @return
     */
    public  Map<String,Object> queryMap(String sql, Object[] params, Connection conn, boolean closeConn)
    {
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try
        {
            pstat = conn.prepareStatement(sql);
            pstat.setQueryTimeout(JDBC_QUERYTIMEOUT);
            if(null!=params)
            {
                for (int i = 0; i < params.length; i++)
                {
                    Object obj = params[i];
                    int index = i + 1;
                    pstat.setObject(index, obj);
                }
            }
            rs = pstat.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int cols = metaData.getColumnCount();
            if (rs.next())
            {
                Map<String,Object> map = new HashMap<String,Object>();
                for (int i = 0; i < cols; i++)
                {
                    int index = i + 1;
                    String colName = metaData.getColumnLabel(index);
                    Object colValue = rs.getObject(index);
                    if (colValue instanceof java.sql.Date) {
                    	colValue = rs.getTimestamp(index);
                    }
                    if (colValue instanceof oracle.sql.CLOB) {
                    	colValue = convertToString((oracle.sql.CLOB)colValue);
                    }
                    map.put(colName, colValue);
                }
                return map;
            }
        } catch (Exception e)
        {
            log.error("queryMap", e);
        } finally
        {
            SqlResourceHelper.closeResultSet(rs);
            SqlResourceHelper.closeStatement(pstat);
            if (closeConn)
            	SqlResourceHelper.closeConnection(conn);
        }
        return null;
    }
    
    public int update(String sql, Object[] params)
    {
    	return update(sql, params, connectionManager.getConnection(), true);
    }

    /**
     * 更新
     * @param sql
     * @param paramList
     * @param conn 数据库连接
     * @param closeConn 是否关掉数据库连接
     * @return
     */
    public int update(String sql, Object[] params, Connection conn, boolean closeConn)
    {
        PreparedStatement pstat = null;
        try
        {
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(sql);
            if(null!=params)
            {
                for (int i = 0; i < params.length; i++)
                {
                    Object obj = params[i];
                    int index = i + 1;
                    pstat.setObject(index, obj);
                }
            }
            int result =  pstat.executeUpdate();
            conn.commit();
            return result;
        } catch (Exception e)
        {
            SqlResourceHelper.rollbackConnection(conn);
            log.error("Update", e);
        } finally
        {
            SqlResourceHelper.closeStatement(pstat);
            SqlResourceHelper.setConnectionAutoCommit(conn, true);
            if (closeConn)
            	SqlResourceHelper.closeConnection(conn);
        }
        return 0;
    }
    
    /**
     * 新增
     * @param sql
     * @param param
     * @return
     */
    public int insert(String sql, Object[] params)
    {
        return update(sql,params);
    }
    
    /**
     * 插入
     * @param sql 插入语句
     * @param params 参数
     * @param conn 数据库连接
     * @param closeConn 是否关掉数据库连接
     * @return
     */
    public int insert(String sql, Object[] params, Connection conn, boolean closeConn)
    {
    	return update(sql, params, conn, closeConn);
    }
    
    /**
     * 调用存储过程，存储过程的参数依次为in,out
     * @param sql
     * @param ino
     * @param outtyps
     * @return
     */
    public  List<Object> callPorcedure(String sql, Object[] ino,int[] outtypes )
    {
        Connection conn = null;
        PreparedStatement pstat = null;
        try
        {
            conn = connectionManager.getConnection();
            conn.setAutoCommit(false);
            CallableStatement proc = conn.prepareCall(sql);
            int idx = 1;
            if(null!=ino)
            {
                for(int i=0;i<ino.length;i++)
                {
                    proc.setObject(idx, ino[i]);
                    idx++;
                }
            }
            int outlocation = idx;
            if(null!=outtypes)
            {
                for(int i=0;i<outtypes.length;i++)
                {
                    proc.registerOutParameter(idx, outtypes[i]);
                    idx++;
                }
            }
            proc.execute();
            List<Object> result = new ArrayList<Object>();
            while(outlocation<idx)
            {
                Object obj = proc.getObject(outlocation);
                result.add(obj);
                outlocation++;
            }
            conn.commit();
            return result;
        } catch (Exception e)
        {
            SqlResourceHelper.rollbackConnection(conn);
            log.error("CallPorcedure", e);
        } finally
        {
            SqlResourceHelper.closeStatement(pstat);
            SqlResourceHelper.setConnectionAutoCommit(conn, true);
            SqlResourceHelper.closeConnection(conn);
        }
        return null;
    }

    public String convertToString(oracle.sql.CLOB clob)
    {
        String content = "";
        BufferedReader in = null;
        try
        {
            in = new BufferedReader(clob.getCharacterStream());
            StringWriter out = new StringWriter();
            int c;
            while ((c = in.read()) != -1)
            {
                out.write(c);
            }
            content = out.toString();
            out.close();
            in.close();
        } catch (Exception e)
        {
            log.error("convertToString", e);
        }
        return content;
    }
    
    /**
     * 获取总记录条数
     * 
     * @param sql
     * @param params
     * @param conn
     * @return
     */
    private int queryTotalCount(String sql, Object[] params, Connection conn) {
    	String s1 = sql.toLowerCase();
    	int fromKwIndex = s1.indexOf(" from ");
		int orderKwIndex = s1.indexOf(" order ", fromKwIndex);
    	if (orderKwIndex > -1) {//去掉“order by ”
    		sql = sql.substring(0, orderKwIndex);
    	}
    	String totalSql = "select count(*) " + sql.substring(fromKwIndex);
    	PreparedStatement pstat = null;
        ResultSet rs = null;
        try {
	    	pstat = conn.prepareStatement(totalSql);
	        pstat.setQueryTimeout(JDBC_QUERYTIMEOUT);
	        if(null!=params) {
	            for (int i = 0; i < params.length; i++) {
	                Object obj = params[i];
	                int index = i + 1;
	                pstat.setObject(index, obj);
	            }
	        }
	        rs = pstat.executeQuery();
	        if (rs.next()) {
	        	return rs.getInt(1);
	        }
        } catch (Exception e) {
			log.error("queryTotalCount", e);
		}  finally {
            SqlResourceHelper.closeResultSet(rs);
            SqlResourceHelper.closeStatement(pstat);
        }
    	return 0;
    }
}
