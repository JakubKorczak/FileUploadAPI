package com.korczak.fileDB;

import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jkorczak on 13/04/2017.
 */
public class Database
{


    private static Connection conn;
    private static Statement stat;

    public static Connection getConn()
    {
        return conn;
    }

    public static void connect(String drv, String db)
    {
        try
        {
            Class.forName(drv);
            SQLiteConfig conf = new SQLiteConfig();
            conf.enforceForeignKeys(true);
            conn = DriverManager.getConnection(db, conf.toProperties());
            stat = conn.createStatement();
        } catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void createTables()
    {
        String createFileInfoTable = "CREATE TABLE IF NOT EXISTS FileInfo (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                             "fileName VARCHAR(50) NOT NULL, "
                                       + "filePath VARCHAR(50) NOT NULL, numberOfChunks INTEGER NOT NULL);";


        try
        {
            stat.execute(createFileInfoTable);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public static void insertFile(String fileName, String filePath, int numberOfChunks)
    {
        String insertFile = "INSERT INTO FileInfo (fileName, filePath, numberOfChunks) VALUES (?, ?, ?);";
        PreparedStatement ps;
        try
        {
            ps = conn.prepareStatement(insertFile);
            ps.setString(1, fileName);
            ps.setString(2, filePath);
            ps.setInt(3, numberOfChunks);
            ps.execute();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void deleteFile(int id)
    {
        String deleteF = "DELETE FROM FileInfo WHERE id = ?;";
        PreparedStatement ps;

        try
        {
            ps = conn.prepareStatement(deleteF);
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static List<UploadFile> selectFiles()
    {
        List<UploadFile> lista = new ArrayList<>();
        String selectF = "SELECT * FROM FileInfo";
        try
        {
            int id, numberOfChunks;
            String fileName, filePath;
            ResultSet rs = stat.executeQuery(selectF);
            while (rs.next())
            {
                id = rs.getInt("id");
                fileName = rs.getString("fileName");
                filePath = rs.getString("filePath");
                numberOfChunks = rs.getInt("numberOfChunks");
                lista.add(new UploadFile(id, fileName, filePath, numberOfChunks));
            }
            return lista;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static UploadFile selectOneFile(Integer id1)
    {

        try
        {
            String selectF = "SELECT * FROM FileInfo WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(selectF);
            ps.setInt(1, id1);

            int id = 0, numberOfChunks = 0;
            String fileName = "", filePath = "";
            ResultSet rs = ps.executeQuery();

            id = rs.getInt("id");
            fileName = rs.getString("fileName");
            filePath = rs.getString("filePath");
            numberOfChunks = rs.getInt("numberOfChunks");

            return new UploadFile(id, fileName, filePath, numberOfChunks);

        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateChunksNumber(int id, int numberOfChunks)
    {
        String updateCh = "UPDATE FileInfo SET numberOfChunks = ? WHERE id = ?;";
        PreparedStatement ps;

        try
        {
            ps = conn.prepareStatement(updateCh);
            ps.setInt(1, numberOfChunks);
            ps.setInt(2, id);
            ps.execute();
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void closeDatabase()
    {
        try
        {
            stat.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
}
