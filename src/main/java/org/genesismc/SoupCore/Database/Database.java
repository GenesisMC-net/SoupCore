package org.genesismc.SoupCore.Database;

import org.genesismc.SoupCore.SoupCore;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.*;

public class Database
{
    public static final List<String> stringColumns = new ArrayList<>();
    public static final List<String> integerColumns = new ArrayList<>();
    public static final List<String> booleanColumns = new ArrayList<>();

    public static Connection getConnection()
    {
        Connection connection;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(SoupCore.getConnectionURL());
        }catch(SQLException e){
            System.out.println("Problem connecting to database");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static void initialiseDatabase()
    {
        Collections.addAll(stringColumns, "uuid", "name", "kit");
        Collections.addAll(integerColumns, "kills", "killStreak", "deaths", "credits", "bounty", "activeWager", "wins", "losses", "moneyMade");
        Collections.addAll(booleanColumns, "kit1"); // Add in ur kits for permissions

        Connection connection = getConnection();
        try{
            PreparedStatement UsersStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS Users(uuid varchar(36) NOT NULL PRIMARY KEY, name varchar(40))");
            UsersStatement.execute(); // uuid, name
            PreparedStatement soupDataStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS soupData(uuid varchar(36) NOT NULL PRIMARY KEY, kit varchar(20), kills int, killStreak int, deaths int, credits int, bounty int)");
            soupDataStatement.execute(); // uuid, kit, kills, killStreak, deaths, credits, bounty
            PreparedStatement soupKitsStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS soupKitsData(uuid varchar(36) NOT NULL PRIMARY KEY)");
            soupKitsStatement.execute(); //uuid, kit1, kit2, kit3 etc >>>>> ALL ARE BOOLEANS
            PreparedStatement coinflipStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS coinflip(uuid varchar(36) NOT NULL PRIMARY KEY, activeWager int, wins int, losses int, moneyMade int)");
            coinflipStatement.execute(); //uuid, kit1, kit2, kit3 etc

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
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Integer> getAllActiveCoinFlips()
    {
        Connection connection = getConnection();
        PreparedStatement preparedStatement;
        try{

            preparedStatement = connection.prepareStatement("SELECT * FROM coinflip");
            ResultSet rows = preparedStatement.executeQuery();
            Map<String, Integer> activeWagers = new HashMap<>();
            while(rows.next())
            {
                if(Integer.parseInt(rows.getString("activeWager")) > 0)
                {
                    activeWagers.put(rows.getString("uuid"), rows.getInt("activeWager"));
                }
            }
            if (!activeWagers.isEmpty()) {
                connection.close();
                return activeWagers;
            }

            connection.close();
            return null;
        }catch(SQLException e){
            System.out.println("Error getting active wagers");
            throw new RuntimeException(e);
        }
    }

    public static UUID getUUIDFromNameInDatabase(String name)
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
                        return UUID.fromString(rows.getString("uuid"));
                    }
                }

            connection.close();
                return null;
        }catch(SQLException e){
            System.out.println("Error getting UUID from Name");
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(e);
        }
    }
    public static Boolean isPlayerInDatabaseByName(String table, String name)
    {
        String uuid = String.valueOf(getUUIDFromNameInDatabase(name));
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
            throw new RuntimeException(e);
        }
    }

    public static String getPlayerData(Player p, String table, String column)
    {
        if(!isPlayerInDatabase(p, table))
        {
            addPlayerToDataBase(p, table);
        }
        Connection connection = getConnection();
        PreparedStatement queryStatement;
        try{
            queryStatement = connection.prepareStatement("SELECT " + column + " FROM " + table + " WHERE uuid = '" + p.getUniqueId().toString() + "'");
            ResultSet rows = queryStatement.executeQuery();
            if (rows.next()) {
                return rows.getString(column);
            }
            connection.close();
        }catch(SQLException e){
            System.out.println("Error accessing data");
            throw new RuntimeException(e);
        }
        return null;
    }


    public static void SetPlayerData(Player p, String table, String column, String data)
    {
        if(!isPlayerInDatabase(p, table))
        {
            addPlayerToDataBase(p, table);
        }
        Connection connection = getConnection();
        PreparedStatement statement;
        try{
            if(stringColumns.contains(column))
            {
                statement = connection.prepareStatement("UPDATE " + table + " SET " + column + " = '" + data + "' WHERE uuid = '" + p.getUniqueId().toString() + "'");
            }
            else if(integerColumns.contains(column) || booleanColumns.contains(column))
            {
                statement = connection.prepareStatement("UPDATE " + table + " SET " + column + " = " + data + " WHERE uuid = '" + p.getUniqueId().toString() + "'");
            }
            else{
                return;
            }

            statement.execute();
            connection.close();
        }catch(SQLException e){
            System.out.println("Error modifying data");
            throw new RuntimeException(e);
        }
    }


    public static void addPlayerToDataBase(Player p, String table)
    {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try{
            switch (table) {
                case "Users":
                    statement = connection.prepareStatement("INSERT INTO Users(uuid, name) VALUES('" + p.getUniqueId().toString() + "', '" + p.getName() + "')");
                    break;
                case "soupData":
                    statement = connection.prepareStatement("INSERT INTO soupData(uuid, kit, kills, kilLStreak, deaths, credits, bounty) VALUES('" + p.getUniqueId() + "', 'Default', 0, 0, 0, 0, 0)");
                    break;
                case "soupKitsData":
                    statement = connection.prepareStatement("INSERT INTO soupKitsData(uuid) VALUES('" + p.getUniqueId().toString() + "')");
                    break;
                case "coinflip":
                    statement = connection.prepareStatement("INSERT INTO coinflip(uuid, activeWager, wins, losses, moneyMade) VALUES('" + p.getUniqueId() + "', 0, 0, 0, 0)");
                    break;
            }

            assert statement != null;
            Objects.requireNonNull(statement).execute();
            connection.close();
        }catch(SQLException ex)
        {
            System.out.println("Error adding new player to database");
            throw new RuntimeException(ex);
        }
    }
}
