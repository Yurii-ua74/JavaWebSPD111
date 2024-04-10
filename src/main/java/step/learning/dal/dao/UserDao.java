package step.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.dto.User;
import step.learning.services.db.DbService;

import java.sql.*;
import java.util.UUID;


@Singleton
public class UserDao {
    private final DbService dbService;

    @Inject
    public UserDao(DbService dbService) {
        this.dbService = dbService;
    }

    public  User getUserByToken(String token){
        String sql = "SELECT t.*, u.* " +
                "FROM Tokens t JOIN Users u ON t.user_id = u.user_id " +
                "WHERE t.token_id = ? AND t.token_expires > CURRENT_TIMESTAMP " +
                "LIMIT 1";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)){
            prep.setString(1, token);
            ResultSet res = prep.executeQuery();
            if(res.next()){ //якщо є дані
                return User.fromResultSet(res);
            }
        }
        catch (SQLException ex){
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }
        return null;
    }

    public String generateToken( User user ) {
        String sql = "INSERT INTO Tokens(token_id, user_id, token_expires) VALUES(?,?,?)";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)){
            String token = UUID.randomUUID().toString();
            prep.setString(1, token);
            prep.setString(2, user.getId().toString());
            //prep.setTimestamp(3, new Timestamp(new java.util.Date().getTime() + 60 * 5)); // + 5 хв
///////////////////////////////////////////////////////////////////
            // Отримуємо поточний час в мілісекундах
            long currentTimeMillis = System.currentTimeMillis();
// Додаємо 5 хвилин до поточного часу
            long tokenExpiresMillis = currentTimeMillis + (5 * 60 * 1000);
// Створюю об'єкт Timestamp на основі поточного часу + 5 хвилин
            Timestamp tokenExpires = new Timestamp(tokenExpiresMillis);
// Встановлюю цей Timestamp у підготовлений SQL запит
            prep.setTimestamp(3, tokenExpires);
//////////////////////////////////////////////////////////////////
            prep.executeUpdate();
            return token;
        }
        catch (SQLException ex){
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }
        return null;
    }
    public User getUserByEmail(String email){
        String sql = "SELECT u.* FROM Users u WHERE u.user_email = ?";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)){
            prep.setString(1, email);
            ResultSet res = prep.executeQuery();
            if(res.next()){ //якщо є дані - користувача знайдено
                return User.fromResultSet(res);
            }
        }
        catch (SQLException ex){
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }
        return null;
    }
    public boolean registerUser(User user) {
        if (user == null) return false ;
        if (user.getId() == null) user.setId(UUID.randomUUID());

        String sql = "INSERT INTO Users(user_id, user_name, user_email, user_avatar, user_salt, user_dk)" +
                "VALUES(?,?,?,?,?,?)";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, user.getId().toString());  // У  JDBC відлік починається з 1
            prep.setString(2, user.getName());
            prep.setString(3, user.getEmail());
            prep.setString(4, user.getAvatar());
            prep.setString(5, user.getSalt());
            prep.setString(6, user.getDerivedKey());
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.print(sql);
            return false;
        }
        // return false;
    }

    ////////////////////////////////////////////////////////////////
    public String getIdByEmail(String email) {
        String sql = "SELECT u.user_id FROM Users u WHERE u.user_email = ?";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)){
            prep.setString(1, email);
            ResultSet res = prep.executeQuery();
            if(res.next()){ //якщо є дані - користувача знайдено
                return res.getString("user_id");
            }
        }
        catch (SQLException ex){
            System.err.println(ex.getMessage());
            System.out.println(sql);
        }
        return null;
    }
    ////////////////////////////////////////////////////////////////

    public String checkUserToken(String user_id) {
        System.out.println("User ID received in UserDao: " + user_id);

        String sql = "SELECT t.token_id, t.token_expires FROM Tokens t WHERE t.user_id = ? AND t.token_expires > NOW()";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            prep.setString(1, user_id);
            try (ResultSet resultSet = prep.executeQuery()) {
                if (resultSet.next()) {
                    Timestamp currentTimestamp = resultSet.getTimestamp("token_expires");
                    // Додано 150 секунд до поточного часу
                    long newExpiresMillis = currentTimestamp.getTime() + (150 * 1000);
                    Timestamp newExpires = new Timestamp(newExpiresMillis);
                    resultSet.updateTimestamp("token_expires", newExpires);
                    resultSet.updateRow(); // Оновлюємо рядок в базі даних
                    return resultSet.getString("token_id");
                }
            }
        } catch (SQLException ex) {
            System.err.println("Помилка при виконанні запиту SQL: " + ex.getMessage());
        }
        //  null, якщо токен не знайдено або відбулася помилка
        return null;
    }
    /////////////////////////////////////////////////////////////////
    public boolean installTable() {
        String sql = "CREATE TABLE Users (" +
                "user_id       CHAR(36)      PRIMARY KEY DEFAULT(UUID())," +
                "user_name     VARCHAR(64)   NOT NULL," +
                "user_email    VARCHAR(128)  NOT NULL," +
                "user_avatar   VARCHAR(64)   NOT NULL," +
                "user_salt     CHAR(32)      NOT NULL," +
                "user_dk       CHAR(32)      NOT NULL," +
                "user_created  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "user_deleted  DATETIME      NULL" +
                ") ENGINE = INNODB, DEFAULT CHARSET = utf8mb4";
        try(Statement statement = dbService.getConnection().createStatement()){
            statement.executeUpdate(sql);
            return true;
        }
        catch(SQLException ex) {
            System.err.println(ex.getMessage());
            System.out.print(sql);
            return false;
        }
    }

}
