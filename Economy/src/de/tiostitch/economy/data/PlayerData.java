package de.tiostitch.economy.data;

import org.bukkit.entity.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PlayerData {

    private Connection connection;

    public PlayerData(String host, String database, String username, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + host + "/" + database;
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void createPlayer(Player player) {
        // Primeiro, verifique se o jogador já existe no banco de dados
        if (playerExists(player)) {
            return; // Se o jogador já existe, não faz nada
        }

        // Caso contrário, crie um novo registro
        String query = "INSERT INTO economy_players (uuid, name, money, topRank, recToggle) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            statement.setDouble(3, 0);
            statement.setInt(4, 0);
            statement.setInt(5, 0);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para verificar se um jogador já existe no banco de dados
    private boolean playerExists(Player player) {
        String query = "SELECT uuid FROM economy_players WHERE uuid = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next(); // Se houver resultados, o jogador já existe
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Em caso de erro, considere que o jogador não existe
        }
    }

    public double getCoins(Player player) {
        String query = "SELECT money FROM economy_players WHERE uuid = ?";
        double coins = 0;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                coins = result.getDouble("money");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coins;
    }

    public double getCoins(String playerName) {
        String query = "SELECT money FROM economy_players WHERE name = ?";
        double coins = 0;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, playerName);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                coins = result.getDouble("money");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coins;
    }

    public Set<String> getTopPlayers(int limit) {
        Set<String> topPlayers = new LinkedHashSet<>();
        String query = "SELECT name FROM economy_players ORDER BY money DESC LIMIT ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, limit);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String playerName = result.getString("name");
                topPlayers.add(playerName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topPlayers;
    }

    public void setCoins(Player player, double coins) {
        String query = "UPDATE economy_players SET money = ? WHERE uuid = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDouble(1, coins);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getToggle(Player player) {
        String query = "SELECT recToggle FROM economy_players WHERE uuid = ?";
        double coins = 0;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                coins = result.getDouble("recToggle");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coins;
    }

    public void setToggle(Player player, double value) {
        String query = "UPDATE economy_players SET recToggle = ? WHERE uuid = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDouble(1, value);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfHasData(Player player) {
        String query = "SELECT COUNT(*) AS count FROM economy_players WHERE uuid = ?";
        int count = 0;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                count = result.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count > 0;
    }

    public void createTable() {
        String playerDataQuery = "CREATE TABLE IF NOT EXISTS economy_players ("
                + "id INT(11) NOT NULL AUTO_INCREMENT,"
                + "uuid VARCHAR(36) NOT NULL,"
                + "name VARCHAR(16) NOT NULL,"
                + "money DOUBLE NOT NULL,"
                + "topRank INT(11) NOT NULL,"
                + "recToggle INT(11) NOT NULL,"
                + "PRIMARY KEY (id));";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(playerDataQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
