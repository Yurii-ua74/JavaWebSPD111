package step.learning.servlets;

import com.google.inject.Singleton;
import step.learning.dal.dao.PromotionDAO;
import step.learning.dal.dto.PromotionDTO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Singleton
public class PromotionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PromotionDAO promotionDAO = new PromotionDAO();
        req.setAttribute("promotion", promotionDAO.getAllPromotions());

        req.setAttribute("page-body","promotion");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req,resp);
    }
}
