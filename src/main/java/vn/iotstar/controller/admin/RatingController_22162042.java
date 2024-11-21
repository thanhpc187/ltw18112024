package vn.iotstar.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.com.entity.Book;
import org.com.entity.Rating;
import org.com.entity.User;
import org.com.service.IBookService;
import org.com.service.IRatingService;
import org.com.service.impl.BookServiceImpl;
import org.com.service.impl.RatingServiceImpl;

import java.io.IOException;

@WebServlet(
        urlPatterns = {
                "/comment",
                "/comment/add",
                "/comment/edit",
                "/comment/insert",
                "/comment/delete"
        }
)
public class RatingController_22162042 extends HttpServlet {
    private final IRatingService commentService = new RatingServiceImpl();
    private final IBookService newsService = new BookServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reviewText = req.getParameter("reviewText");
        String rating1 = req.getParameter("rating");
        String ratingId = req.getParameter("ratingId");
        User user = (User) req.getSession().getAttribute("account");

        Book book = newsService.findById(ratingId);

        Rating rating = Rating.builder()
                .reviewText(reviewText)
                .rating(rating1)
                .user(user)
                .build();

        commentService.save(rating);

        resp.sendRedirect(req.getContextPath() + "/admin/news/details?id=" + ratingId);
    }
}