package vn.iotstar.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.com.entity.Author;
import org.com.service.IAuthorService;
import org.com.service.impl.AuthorServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet(
        urlPatterns = {
                "/admin/categories",
                "/admin/category/add",
                "/admin/category/edit",
                "/admin/category/insert",
                "/admin/category/delete"
        }
)
public class AuthorController_22162042 extends HttpServlet {
    private final IAuthorService categoryService = new AuthorServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String url = req.getRequestURI();

        switch (url) {
            case "/admin/category/add" -> req.getRequestDispatcher("/views/admin/category-add.jsp").forward(req, resp);
            case "/admin/categories" -> listCategories(req, resp);
            case "/admin/category/delete" -> deleteCategory(req, resp);
            case "/admin/category/edit" -> showEditForm(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String url = req.getRequestURI();

        switch (url) {
            case "/admin/category/insert" -> handleCategoryInsertOrUpdate(req, resp, false);
            case "/admin/category/edit" -> handleCategoryInsertOrUpdate(req, resp, true);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listCategories(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Author> categories = categoryService.findAll();
        req.setAttribute("categoryList", categories);
        req.getRequestDispatcher("/views/admin/category-list.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Author author = categoryService.findById(id);
        req.setAttribute("category", author);
        req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);
    }

    private void deleteCategory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            categoryService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleCategoryInsertOrUpdate(HttpServletRequest req, HttpServletResponse resp, boolean isUpdate)
            throws IOException, ServletException {

        String authorName = req.getParameter("authorName");
        String code = req.getParameter("code");

        var categoryBuilder = Author.builder()
                                    .authorName(authorName)
                                    .code(code);

        if (isUpdate) {
//            int id = Integer.parseInt(req.getParameter("id"));
//            categoryBuilder.(categoryId);
        }

        Author authorModel = categoryBuilder.build();

        if (isUpdate) {
            categoryService.update(authorModel);
        } else {
            categoryService.insert(authorModel);
        }

        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }
}