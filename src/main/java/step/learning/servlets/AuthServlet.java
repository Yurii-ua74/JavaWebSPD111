package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dal.dao.UserDao;
import step.learning.dal.dto.User;
import step.learning.services.kdf.KdfService;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;


@Singleton
public class AuthServlet extends HttpServlet {
    // ctr + O -> doGet
    private final KdfService kdfService;
    private final UserDao userDao;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        if(token == null || token.isEmpty()){
            sendRest(resp,"error","Property 'token' required", null);
            return;
        }
        User user = userDao.getUserByToken(token);  // отримання токена еористувача
        if(user==null){
            sendRest(resp,"error","Token invalid or expired", null);
        }
        else {
            JsonObject rest = new JsonObject();
            JsonObject meta = new JsonObject();
            meta.addProperty("service", "auth");
            meta.addProperty("status", "success");
            meta.addProperty("message", "Authorized");
            meta.addProperty("time", Instant.now().getEpochSecond());
            rest.add("meta", meta);

            Gson gson = new GsonBuilder().serializeNulls().create();
            rest.add("data", gson.toJsonTree(user));
            resp.getWriter().print(gson.toJson(rest));
        }
        /* Робота з токенами робиться в окремому "журналі" видачі токенів
        CREATE TABLE Tokens(
        token_id CHAR(36) PRIMARY KEY,
        user_id CHAR(36) NOT NULL,
        token_created DATETIME DEFAULT CURRENT_TIMESTAMP,
        token_expires DATETIME)
        ENGINE=INNODB, DEFAULT CHARSET=utf8mb4
        * */
    }

    @Inject
    public AuthServlet(KdfService kdfService, UserDao userDao) {
        this.kdfService = kdfService;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        if(email == null || email.isEmpty()){
            sendRest(resp,"error","Property 'email' required", null);
            return;
        }
        String password = req.getParameter("password");
        if(password == null || password.isEmpty()){
            sendRest(resp,"error","Property 'password' required", null);
            return;
        }
        User user = userDao.getUserByEmail(email);
        if (user == null){
            sendRest(resp,"error","Credentials rejected", null);
            return;
        }
        if(! user.getDerivedKey().equals(kdfService.derivedKey(password, user.getSalt()))){
            sendRest(resp,"error","Credentials rejected", null);
            return;
        }

        ////////////////////////////////////////////////////////
        String user_id = userDao.getIdByEmail(email);
        System.out.println("User ID from request: " + user_id);
        if(user_id == null || user_id.isEmpty()){
            sendRest(resp,"error","Property 'user_id' required", null);
            return;
        }

        String checkToken = userDao.checkUserToken(user_id);  // перевірка токена
        if(checkToken != null && !checkToken.isEmpty()) {
            sendRest(resp,"success","Credentials during token confirmed", checkToken);
        }
        /////////////////////////////////////////////////////////
        else
        {
        //генеруємо токен доступу користувача
        String token = userDao.generateToken(user);
        sendRest(resp,"success","Credentials new token confirmed", token);
        //sendRest(resp,"success","Credentials confirmed", user.getSalt());
        }
    }

    private void sendRest(HttpServletResponse resp, String status, String message, String token) throws IOException {
        JsonObject rest = new JsonObject();
        JsonObject meta = new JsonObject();
        meta.addProperty("service", "auth");
        meta.addProperty("status", status);
        meta.addProperty("message", message);
        meta.addProperty("time", Instant.now().getEpochSecond());
        rest.add("meta", meta);
        JsonObject data = null;
        if(token != null){
            data = new JsonObject();
            data.addProperty("token", token);
        }
        rest.add("data", data);
        Gson gson = new GsonBuilder().serializeNulls().create();
        resp.getWriter().print(gson.toJson(rest));
    }
}
