/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gionee.top.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqlResourceHelper {
    private SqlResourceHelper() {
    }

    static public void closeConnection(Connection conn) {
        if(conn!=null) {
            try {
                conn.close();
            } catch(Exception e) {
            }
        }
    }


    static public void closeStatement(Statement stat) {
        if(stat!=null) {
            try {
                stat.close();
            } catch(Exception e) {
            }
        }
    }


    static public void closeResultSet(ResultSet rs) {
        if(rs!=null) {
            try {
                rs.close();
            } catch(Exception e) {
            }
        }
    }


    static public void rollbackConnection(Connection conn) {
        if(conn!=null) {
            try {
                conn.rollback();
            } catch(Exception e) {
            }
        }
    }


    static public void setConnectionAutoCommit(Connection conn ,boolean autoCommit) {
        if(conn!=null) {
            try {
                conn.setAutoCommit(autoCommit);
            } catch(Exception e) {
            }
        }
    }
}
