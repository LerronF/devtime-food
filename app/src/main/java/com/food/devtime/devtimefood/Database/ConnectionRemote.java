package com.food.devtime.devtimefood.Database;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Lerron on 11/07/2016.
 */
public class ConnectionRemote {

    // String ip = "192.168.0.63";
    // String driver = "net.sourceforge.jtds.jdbc.Driver";
    // String db = "DTPadrao";
    //  String un = "sa";
    //  String password = "Apollo28am";

    String ip = "";
    String driver = "";
    String db = "";
    String un = "";
    String password = "";

    @SuppressLint("NewApi")
    public Connection CONN(String IP,String DRIVER,String BANCODADOS, String USUARIO, String SENHA ) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;

        ip = IP;
        driver = DRIVER;
        db = BANCODADOS;
        un = USUARIO;
        password = SENHA;

        try {

            Class.forName(driver);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }
        return conn;
    }
}
