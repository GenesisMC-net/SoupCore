package me.smeo.soupcore.Database;

import me.smeo.soupcore.SoupCore;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database
{
    public static List<String> stringColumns = new ArrayList<String>();
    public static List<String> integerColumns = new ArrayList<String>();
    public static List<String> booleanColumns = new ArrayList<String>();

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

    public static void initialiseDatabase()
    {
        Collections.addAll(stringColumns, "uuid", "name", "kit");
        Collections.addAll(integerColumns, "kills", "killStreak", "deaths", "credits", "bounty");
        Collections.addAll(booleanColumns, "kit1"); // Add in ur kits for permissions

        Connection connection = getConnection();
        try{
            PreparedStatement UsersStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Users(uuid varchar(36) NOT NULL PRIMARY KEY, name varchar(40))");
            UsersStatement.execute(); // uuid, name
            PreparedStatement soupDataStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS soupData(uuid varchar(36) NOT NULL PRIMARY KEY, kit varchar(20), kills int, killStreak int, deaths int, credits int, bounty int)");
            soupDataStatement.execute(); // uuid, kit, kills, killStreak, deaths, credits, bounty
            PreparedStatement soupKitsStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS soupKitsData(uuid varchar(36) NOT NULL PRIMARY KEY)");
            soupKitsStatement.execute(); //uuid, kit1, kit2, kit3 etc >>>>> ALL ARE BOOLEANS


            connection.close();
        }catch(SQLException e){
            System.out.println("Error creating tables");
        }
    }

    public static Boolean isPlayerInDatabase(Player p, String table)
    {
        Connection connection = getConnection();
        PreparedStatement preparedStatement;
        try{
            preparedStatement = connection.prepareStatement("SELECT * FROM " + table);
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
            System.out.println("Error accessing data (isPlayerInDatabase)");
            System.out.println(e);
        }
        return false;
    }

    public static String getUUIDFromNameInDatabase(String name)
    {
        Connection connection = getConnection();
        PreparedStatement preparedStatement;
        try{

                preparedStatement = connection.prepareStatement("SELECT * FROM Users");
                ResultSet rows = preparedStatement.executeQuery();
                while(rows.next())
                {
                    if(rows.getString("name").equalsIgnoreCase(name))
                    {
                        connection.close();
                        return rows.getString("uuid");
                    }
                }

            connection.close();
                return null;
        }catch(SQLException e){
            System.out.println("Error getting UUID from Name");
            System.out.println(e);
        }
        return null;
    }

    public static String getNameFromUUIDInDatabase(String uuid)
    {
        Connection connection = getConnection();
        PreparedStatement preparedStatement;
        try{

            preparedStatement = connection.prepareStatement("SELECT * FROM Users");
            ResultSet rows = preparedStatement.executeQuery();
            while(rows.next())
            {
                if(rows.getString("uuid").equalsIgnoreCase(uuid))
                {
                    connection.close();
                    return rows.getString("name");
                }
            }

            connection.close();
            return null;
        }catch(SQLException e){
            System.out.println("Error getting UUID from Name");
            System.out.println(e);
        }
        return null;
    }
    public static Boolean isPlayerInDatabaseByName(String table, String name)
    {
        String uuid = getUUIDFromNameInDatabase(name);
        Connection connection = getConnection();
        PreparedStatement preparedStatement;
        boolean isFound;
        try{

            preparedStatement = connection.prepareStatement("SELECT * FROM " + table);
            ResultSet rows = preparedStatement.executeQuery();
            isFound = false;
            while(rows.next())
            {
                if(rows.getString("uuid").equalsIgnoreCase(uuid))
                {
                    isFound = true;
                    break;
                }
            }
            connection.close();
            return isFound;
        }catch(SQLException e){
            System.out.println("Error finding user (isPlayerInDatabaseByName");
            System.out.println(e);
        }
        return false;
    }

    public static Object getPlayerData(Player p, String table, String column)
    {
        if(isPlayerInDatabase(p, table) == false)
        {
            addPlayerToDataBase(p, table);
        }
        Connection connection = getConnection();
        PreparedStatement queryStatement;
        try{
            queryStatement = connection.prepareStatement("SELECT " + column + " FROM " + table + " WHERE uuid = '" + p.getUniqueId().toString() + "'");
            ResultSet rows = queryStatement.executeQuery();
            while(rows.next())
            {
                return rows.getString(column);
            }
            connection.close();
        }catch(SQLException e){
            System.out.println("Error accessing data");
            System.out.println(e);
        }
        return null;
    }


    public static void SetPlayerData(Player p, String table, String column, Integer data)
    {
        if(isPlayerInDatabase(p, table) == false)
        {
            addPlayerToDataBase(p, table);
        }
        Connection connection = getConnection();
        PreparedStatement statement;
        try{
            if(stringColumns.contains(column))
            {
                statement = connection.prepareStatement("UPDATE " + table + " SET " + column + " = '" + data.toString() + "' WHERE uuid = '" + p.getUniqueId().toString() + "'");
            }
            else if(integerColumns.contains(column) || booleanColumns.contains(column))
            {
                statement = connection.prepareStatement("UPDATE " + table + " SET " + column + " = " + data.toString() + " WHERE uuid = '" + p.getUniqueId().toString() + "'");
            }
            else{
                return;
            }

            statement.execute();
            connection.close();
        }catch(SQLException e){
            System.out.println("Error modifying data");
            System.out.println(e);
        }
    }


    public static void addPlayerToDataBase(Player p, String table)
    {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try{
            if(table.equals("Users"))
            {
                statement = connection.prepareStatement("INSERT INTO Users(uuid, name) VALUES('" + p.getUniqueId().toString() + "', '" + p.getName() + ")");
            }else if (table.equals("soupData"))
            {
                statement = connection.prepareStatement("INSERT INTO soupData(uuid, kit, kills, kilLStreak, deaths, credits, bounty) VALUES('" + p.getUniqueId() + "', 0, 0, 0, 0, 0, 0)");
            }else if(table.equals("soupKitsData"))
            {
                statement = connection.prepareStatement("INSERT INTO soupKitsData(uuid) VALUES('" + p.getUniqueId().toString() + "')");
            }

            statement.execute();
            connection.close();
        }catch(SQLException ex)
        {
            System.out.println("Error adding new player to database");
            System.out.println(ex);
        }
    }



}
