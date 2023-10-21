package me.smeo.soupcore.Database;

import me.smeo.soupcore.SoupCore;
import org.bukkit.entity.Player;

import java.sql.*;

public class Database
{

    public static Connection getConnection()
    {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(SoupCore.getConnectionURL());
        }catch(SQLException e){
            System.out.println("Problem connecting to database");
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void initialiseDatabase() // Columns: uuid, kit, kills, killStreak, deaths
    {
        Connection connection = getConnection();
        PreparedStatement preparedStatement;
        try{
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS soupData(uuid varchar(36) NOT NULL PRIMARY KEY, kit int, kills int, killStreak int, deaths int)");
            preparedStatement.execute();
            connection.close();
        }catch(SQLException e){
            System.out.println("Error creating table");
        }
    }

    public static Boolean isPlayerInDatabase(Player p)
    {
        Connection connection = getConnection();
        PreparedStatement preparedStatement;
        try{
            preparedStatement = connection.prepareStatement("SELECT * FROM soupData");
            ResultSet rows = preparedStatement.executeQuery();
            boolean isFound = false;
            while(rows.next())
            {
                if(rows.getString("uuid").equalsIgnoreCase(p.getUniqueId().toString()))
                {
                    isFound = true;
                    break;
                }
            }
            connection.close();
            return isFound;
        }catch(SQLException e){
            System.out.println("Error creating table");
            System.out.println(e);
        }
        return false;
    }

    public static Integer getPlayerData(Player p, String column)
    {
        if(isPlayerInDatabase(p) == false)
        {
            addPlayerToDataBase(p);
        }
        Connection connection = getConnection();
        PreparedStatement queryStatement;
        try{
            queryStatement = connection.prepareStatement("SELECT " + column + " FROM soupData WHERE uuid = '" + p.getUniqueId().toString() + "'");
            ResultSet rows = queryStatement.executeQuery();
            while(rows.next())
            {
                return rows.getInt(column);
            }
            connection.close();
        }catch(SQLException e){
            System.out.println("Error accessing data");
            System.out.println(e);
        }
        return null;
    }

    public static void SetPlayerData(Player p, String column, Integer data)
    {
        if(isPlayerInDatabase(p) == false)
        {
            addPlayerToDataBase(p);
        }
        Connection connection = getConnection();
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("UPDATE soupData SET " + column + " = " + data.toString() + " WHERE uuid = '" + p.getUniqueId().toString() + "'");
            statement.execute();
            connection.close();
        }catch(SQLException e){
            System.out.println("Error modifying data");
            System.out.println(e);
        }
    }

    public static void addPlayerToDataBase(Player p)
    {
        Connection connection = getConnection();
        PreparedStatement statement;
        try{
            statement = connection.prepareStatement("INSERT INTO soupData(uuid, kit, kills, killstreak) VALUES('" + p.getUniqueId().toString() + "', NULL, 0, 0, 0)");
            statement.execute();
            connection.close();
        }catch(SQLException ex)
        {
            System.out.println("Error adding new player to database");
            System.out.println(ex);
        }
    }

}
