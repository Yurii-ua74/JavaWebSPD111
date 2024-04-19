package step.learning.dal.dao;

import com.google.inject.Inject;
import step.learning.dal.dto.Product;
//import step.learning.dal.dto.User;
import step.learning.services.db.DbService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// DAL - Data Access Layer
public class ProductDao {

    private final DbService dbService;

    @Inject
    public ProductDao(DbService dbService) {
        this.dbService = dbService;
    }

    public List<Product> getList(int skip, int take) {
        //Цей метод отримує список продуктів з бази даних з пагінацією.
        //Параметри skip та take визначають, скільки записів потрібно пропустити перед початком отримання
        // та скільки записів потрібно взяти.
        //Спочатку формується SQL-запит для вибірки продуктів з бази даних з використанням функції LIMIT,
        // що дозволяє обмежити кількість записів, які повертаються.
        //Після цього виконується запит до бази даних, отримані результати перетворюються на об'єкти класу
        // Product та додаються до списку result.

        // skip,  take  - основа пагінації - поділу на сторінки
        List<Product> result = new ArrayList<>();
         // формуємо запит
        String sql = String.format("SELECT * FROM Products LIMIT %d, %d",
                skip, take);
        try(Statement statement = dbService.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                result.add(new Product(resultSet));
            }
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }
        return result;
    }

    public boolean add( Product product ) {
        //Цей метод додає новий продукт до бази даних.
        //Спочатку формується SQL-запит для вставки нового продукту у таблицю.
        //Підготовлений SQL-запит виконується з параметрами, отриманими з об'єкта product.
        //Якщо вставка успішна, метод повертає true, в іншому випадку - false.
        if( product == null ) return false ;
        if( product.getId() == null ) product.setId( UUID.randomUUID() );

        String sql = "INSERT INTO Products" +
                "(product_id,product_name,product_price,product_description,product_image ) " +
                "VALUES(?,?,?,?,?)";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql) ) {
            prep.setString( 1, product.getId().toString() );   // у JDBC відлік від 1
            prep.setString( 2, product.getName() );
            prep.setDouble( 3, product.getPrice() );
            prep.setString( 4, product.getDescription() );
            prep.setString( 5, product.getImage() );
            prep.executeUpdate();
            return true;
        }
        catch (SQLException ex) {
            System.err.println( ex.getMessage() );
            System.out.println( sql );
            return false ;
        }
    }


}
