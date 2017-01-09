package com.gionee.top.db;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.logicalcobwebs.proxool.ProxoolDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ConnectionManager
{
    private static final Log log = LogFactory.getLog(ConnectionManager.class);

    @Autowired
    private ProxoolDataSource proxoolDataSource = null;
    
    /**
     * 取连接
     * @return
     */
    public Connection getConnection()
    {
        if (proxoolDataSource == null)
        {
            return null;
        }
        try
        {
            return proxoolDataSource.getConnection();
        } catch (Exception e)
        {
            log.error("getConnection", e);
        }
        return null;
    }

}
