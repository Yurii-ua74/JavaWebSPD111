package step.learning.dal.dao;

import com.google.inject.Inject;
import step.learning.dal.dto.CartItem;
import step.learning.services.db.DbService;

import java.sql.*;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CartDao {
    //public CartItem[] getCart() {
    //    return new CartItem[] {
    //            new CartItem(UUID.randomUUID(), UUID.randomUUID(), 1),
    //            new CartItem(UUID.randomUUID(), UUID.randomUUID(), 2),
    //            new CartItem(UUID.randomUUID(), UUID.randomUUID(), 3),
    //    };

    private final DbService dbService;
    @Inject
    public CartDao(DbService dbService) {
        this.dbService = dbService;
    }

    public List<CartItem> getCart() {
        //return Arrays.asList(new CartItem[] {
        //        new CartItem(UUID.randomUUID(), UUID.randomUUID(), 1),
        //        new CartItem(UUID.randomUUID(), UUID.randomUUID(), 2),
        //        new CartItem(UUID.randomUUID(), UUID.randomUUID(), 3),
        //        });
        List<CartItem> cartItems = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = dbService.getConnection();
            statement = connection.prepareStatement("SELECT * FROM carts_details");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                UUID cartId = UUID.fromString(resultSet.getString("cart_id"));
                UUID productId = UUID.fromString(resultSet.getString("product_id"));
                int count = resultSet.getInt("cart_dt_cnt");
                CartItem cartItem = new CartItem(cartId, productId, count);
                cartItems.add(cartItem);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                // if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return cartItems;
    }

    public void add(String userId, String productId, int cnt)  {
        // шукаємо чи є у користувача відкритий кошик
        String cartId = null ;
        String sql = String.format(
                "SELECT cart_id From carts WHERE cart_user='%s' AND cart_status=0 ",
                userId);
        try(Statement statement = dbService.getConnection().createStatement()) {
            ResultSet res = statement.executeQuery(sql);
            if( res.next() ) {  // є відкритий кошик
                cartId = res.getString(1);
            }
            else {  // немає відкритого кошику
                cartId = UUID.randomUUID().toString();
                sql = String.format(
                        "INSERT INTO carts(cart_id,cart_user,cart_date,cart_status) " +
                                "VALUES('%s', '%s',CURRENT_TIMESTAMP,0)",
                        cartId, userId );
                statement.executeUpdate(sql);
            }
            // cartId - id кошику чи старого, чи нового
            // Перевіряємо чи є у кошику такий товар
            sql = String.format(
                    "SELECT cart_dt_cnt FROM carts_details WHERE cart_id = '%s' AND product_id = '%s'",
                    cartId, productId);
            res = statement.executeQuery(sql);
            if( res.next() ) { // Якщо є, то збільшуємо кількість
                cnt += res.getInt(1);   // додаємо наявну та нову кількість
                sql = String.format(
                        "UPDATE carts_details SET cart_dt_cnt = %d WHERE cart_id = '%s' AND product_id = '%s'",
                        cnt, cartId, productId);
            }
            else {  // Якщо немає, то створюємо (додаємо)
                sql = String.format(
                        "INSERT INTO carts_details(cart_dt_id,cart_id,product_id,cart_dt_cnt ) " +
                                "VALUES( UUID(), '%s', '%s', %d )",
                        cartId, productId, cnt);
            }
            statement.executeUpdate(sql);
        }
        catch(SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }
        // якщо немає то створюємо
        // Перевіряємо чи є у кошику такий товар
        // якщо є то збільшуємо кількість
        // якщо немає то створюємо (додаємо)

    }
}
