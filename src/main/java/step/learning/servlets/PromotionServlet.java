package step.learning.servlets;

import com.google.inject.Singleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import step.learning.dal.dao.PromotionDAO;

@Singleton
public class PromotionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PromotionDAO promotionDAO = new PromotionDAO();
        req.setAttribute("promotion", promotionDAO.getAllPromotions());

        req.setAttribute("page-body","promotion");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req,resp);
    }

}
