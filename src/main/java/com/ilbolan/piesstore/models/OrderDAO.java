package com.ilbolan.piesstore.models;

import com.ilbolan.piesstore.forms.OrderForm;
import com.ilbolan.piesstore.logging.AppLogger;
import com.ilbolan.piesstore.models.DBManagement.DBConnection;
import com.ilbolan.piesstore.models.beans.Order;

import java.io.Serializable;
import java.sql.*;
import java.util.logging.Level;

/**
 * Data-Access-Object for storing Orders
 */
public class OrderDAO implements Serializable {

    private static final AppLogger logger = new AppLogger(AppLogger.class);

    /**
     * Stores the order in the database
     * @param formOrder is the form to draw data from
     */
    public static void storeOrder(OrderForm formOrder) {
        try {
            Connection connection = DBConnection.getInstance();

            String query = "INSERT INTO pitosdb.order (fullname, address, area_id, email, tel, comments, offer, payment, stamp) " +
                    "       VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, formOrder.getFullName());
            statement.setString(2, formOrder.getAddress());
            statement.setInt(3, formOrder.getAreaId());
            statement.setString(4, formOrder.getEmail());
            statement.setString(5, formOrder.getTel());
            statement.setString(6, formOrder.getComments());
            statement.setBoolean(7, formOrder.getOffer());
            statement.setString(8, formOrder.getPayment());
            statement.setTimestamp(9, java.sql.Timestamp.valueOf(formOrder.getTimestamp()));
            statement.executeUpdate();

            ResultSet genKeys = statement.getGeneratedKeys();
            genKeys.next();
            int orderId = (int) genKeys.getLong(1);
            statement.close();
            genKeys.close();
            connection.close();

            logger.log(Level.FINE, "Order successfully inserted in database");
            for (var item: formOrder.getOrderItems()) {
                item.setOrderId(orderId);
                OrderItemDAO.storeOrderItem(item);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "UNABLE TO STORE ORDER IN DATABASE");
            throw new RuntimeException(e);
        }
    }

    /**
     * Nested class that stores the pie contents
     */
    private static class OrderItemDAO {

        /**
         * Stores the order contents in database
         * @param orderItem is the order object to draw data from
         */
        public static void storeOrderItem(Order orderItem) {
            try {
                Connection connection = DBConnection.getInstance();

                String query = "INSERT INTO  order_item(order_id, pie_id, quantity) VALUES(?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, orderItem.getOrderId());
                statement.setInt(2, orderItem.getPieId());
                statement.setInt(3, orderItem.getQuantity());
                statement.executeUpdate();

                statement.close();
                connection.close();
                logger.log(Level.FINE, "Successfully inserted order item in database");
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "UNABLE TO STORE ORDER ITEM IN DATABASE");
                throw new RuntimeException(e);
            }
        }
    }
}