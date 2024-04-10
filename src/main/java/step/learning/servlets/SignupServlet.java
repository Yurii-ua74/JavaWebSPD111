package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import step.learning.dal.dao.UserDao;
import step.learning.dal.dto.User;
import step.learning.services.form.FormParseResult;
import step.learning.services.form.FormParseService;
import step.learning.services.kdf.KdfService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Singleton
public class SignupServlet extends HttpServlet {
    private final FormParseService formParseService;
    private final UserDao userDao;
    private final KdfService kdfService;

    @Inject
    public SignupServlet(FormParseService formParseService, UserDao userDao, KdfService kdfService) {
        this.formParseService = formParseService;
        this.userDao = userDao;
        this.kdfService = kdfService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute( "page-body", "signup" ) ;
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FormParseResult parseResult = formParseService.parse(req);
        Map<String, String> fields = parseResult.getFields();
        Map<String, FileItem> files = parseResult.getFiles();

        String userName = fields.get("user-name");
        if(userName == null || userName.isEmpty()) {
            sendRest(resp, "error", "Property 'user-name' required", null);
            return;
        }
        String userEmail = fields.get("user-email");
        if(userEmail == null || userEmail.isEmpty()) {
            sendRest(resp, "error", "Property 'user-email' required", null);
            return;
        }
        String userPassword = fields.get("user-password");
        if(userPassword == null || userPassword.isEmpty()) {
            sendRest(resp, "error", "Property 'user-password' required", null);
            return;
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(userName);
        user.setEmail(userEmail);
        user.setSalt(kdfService.derivedKey(UUID.randomUUID().toString(), ""));
        user.setDerivedKey(kdfService.derivedKey(userPassword, user.getSalt()));

        FileItem avatar = files.get("user-avatar");
        if(avatar != null) {
            // avatar - не обов'язкове поле, але якщо воно є то проходить перевірку
            String path = req.getServletContext().getRealPath("/") +
                    "img" + File.separator + "avatar" + File.separator;
            // визначаємо тип файлу (розширення)
            int dotPosition = avatar.getName().lastIndexOf('.');
            if(dotPosition < 0) {
                sendRest(resp, "error", "Avatar file must have extension", null);
                return;
            }
            String ext = avatar.getName().substring(dotPosition);
            // формуємо нове ім'я, зберігаємо розширення
            String savedName;
            File savedFile;
            do{
                savedName = UUID.randomUUID() + ext;
                savedFile = new File(path, savedName);
            }while(savedFile.exists());

            try{
                avatar.write( savedFile );
                user.setAvatar( savedName );
            }
            catch(Exception ex) {
                System.err.print(ex.getMessage());
            }
        }
        // реєструємо користувача у БД передаючи в метод sendRest результат
        if(userDao.registerUser(user)) {
            sendRest(resp, "success", "User registered", user.getId().toString());
        }
        else {
            sendRest(resp, "error", "Internal error, look at server's logs", null);
        }
    }

    // Метод для відправлення результату
    private void sendRest(HttpServletResponse resp, String status, String message, String id ) throws IOException {
        // Формування відповіді у форматі JSON
        // Створення нового об'єкту JsonObject з назвою rest.
        JsonObject rest = new JsonObject();
        // Створення нового об'єкту JsonObject з назвою meta.
        JsonObject meta = new JsonObject();
        // Додавання властивостей до об'єкту meta
        meta.addProperty("service", "signup");
        meta.addProperty("status", status);
        meta.addProperty("message", message);
        meta.addProperty("time", Instant.now().getEpochSecond());
        // Додавання об'єкту meta як вкладеного об'єкту в об'єкт rest
        rest.add("meta", meta);

        // Оголошення змінної data як об'єкту JsonObject.
        JsonObject data = null;
        // Якщо значення id не є null, то створюється новий об'єкт JsonObject
        // з однією властивістю id, яка містить значення id
        if( id != null ) {
            data = new JsonObject();
            data.addProperty("id", id);
        }
        // Додавання об'єкту data як вкладеного об'єкту в об'єкт rest.
        rest.add("data", data);

        // Відправлення відповіді на відповідну сторінку
        // Створення нового об'єкту Gson, який використовується
        // для перетворення об'єкту rest в формат JSON.
        Gson gson = new GsonBuilder().serializeNulls().create();
        // Перетворення об'єкту rest в формат JSON і вивід його
        // до Writer потоку resp, який потім буде відправлений клієнту.
        resp.getWriter().print( gson.toJson( rest ) );
    }
}
