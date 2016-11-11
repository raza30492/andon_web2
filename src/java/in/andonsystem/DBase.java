/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.andonsystem;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * This class is used to obtain Connection Object.
 * @author Md Zahid Raza
 */
public class DBase {
 
/* 
    static    String url = "jdbc:mysql://localhost:3306/andons";
    static    String username = "root";
    static    String password = "zahid";
*/
    static    String url = "jdbc:mysql://localhost:3306/andonsys";
    static    String username = "developer";
    static    String password = "andonsys@50";

    /**
     * If all Configurations of JDBC Connection are correct it returns Connection Object
     * @return Connection Object.
     */
    /*
    public static Connection getConnection(){
        Connection conn = null;
        try{  
            //MysqlDataSource
            //Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);         
        }catch(Exception e){
            e.printStackTrace();           
        }
        
        if(conn == null){
                System.out.println("Erron in Database Connection");
        }
        return conn;
    }
    */
    public static Connection getConn(){
        Connection conn = null;
        try{
            Context ctx = new InitialContext();
            Context initCtx  = (Context) ctx.lookup("java:/comp/env");
            DataSource ds = (DataSource) initCtx.lookup("jdbc/MyLocalDB");
            conn = ds.getConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
        return conn;
    }
 
}
