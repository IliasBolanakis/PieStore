package com.ilbolan.piesstore.models;

import com.ilbolan.piesstore.logging.AppLogger;
import com.ilbolan.piesstore.models.DBManagement.DBConnection;
import com.ilbolan.piesstore.models.beans.Pie;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Data-Access-Object for Pie bean
 */
public class PieDAO implements Serializable {

    private static final AppLogger logger = new AppLogger(PieDAO.class);

    private PieDAO(){} // No point in creating instances since class is used as a DAO

    /**
     * Get pie bean from database
     * @param resultSet the result of the query
     * @return Pie bean for given query
     */
    private static Pie preparePieBean(ResultSet resultSet) throws SQLException {
        Pie pie = new Pie();
        pie.setId(resultSet.getInt("id"));
        pie.setName(resultSet.getString("name"));
        pie.setPrice(resultSet.getDouble("price"));
        pie.setFileName(resultSet.getString("filename"));
        pie.setIngredients(resultSet.getString("ingredients"));
        return pie;
    }

    /**
     * Get pie bean based on its id
     * @param id of area to search on database
     * @return Area bean with given id
     */
    public static Pie getPieById(int id) {
        try {
            Connection connection = DBConnection.getInstance();

            String query = "SELECT * FROM pie WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();
            Pie pie = preparePieBean(resultSet);

            resultSet.close();
            statement.close();
            System.out.println(pie);
            logger.log(Level.INFO, "Successfully read pie from database");
            return pie;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "UNABLE TO READ PIE FROM DATABASE");
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all the pies in the database
     * @return a list of all the areas
     */
    public static List<Pie> getPies() {
        try {
            Connection connection = DBConnection.getInstance();

            String query = "SELECT * FROM pie";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            List<Pie> pies = new ArrayList<>();
            while (resultSet.next()) {
                Pie pie = preparePieBean(resultSet);
                pies.add(pie);
            }

            resultSet.close();
            statement.close();
            logger.log(Level.INFO, "Successfully read pies from database");
            return pies;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "UNABLE TO READ PIES FROM DATABASE");
            throw new RuntimeException(e);
        }
    }
}
