package vn.iotstar.controller.user;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.com.util.Constant;

import java.io.IOException;

@WebServlet("/user/home")
public class HomeController_22162042a extends HttpServlet {
    @Override
    protected void doGet(
            HttpServletRequest req, HttpServletResponse resp
    ) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher(Constant.Path.USER_HOME);
        requestDispatcher.forward(req, resp);
    }
}
