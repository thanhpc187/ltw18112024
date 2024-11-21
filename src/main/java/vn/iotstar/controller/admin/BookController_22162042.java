package vn.iotstar.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.com.entity.Author;
import org.com.entity.Book;
import org.com.service.IAuthorService;
import org.com.service.IBookService;
import org.com.service.impl.AuthorServiceImpl;
import org.com.service.impl.BookServiceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@MultipartConfig
@WebServlet(urlPatterns = { "/admin/book", "/admin/book/add", "/admin/book/edit", "/admin/book/delete",
		"/admin/book/details" })
public class BookController_22162042 extends HttpServlet {
	private final IBookService bookService = new BookServiceImpl();
	private final IAuthorService authorService = new AuthorServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getServletPath();
		switch (action) {
		case "/admin/book/add" -> showAddForm(req, resp);
		case "/admin/book/edit" -> showEditForm(req, resp);
		case "/admin/book/delete" -> deleteNews(req, resp);
		case "/admin/book/details" -> showNewsDetails(req, resp);
		default -> listNews(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getServletPath();
		switch (action) {
		case "/admin/book/add" -> addBook(req, resp);
		case "/admin/book/edit" -> updateBook(req, resp);
		default -> listNews(req, resp);
		}
	}

	private void listNews(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String title = req.getParameter("title");
		int page = Optional.ofNullable(req.getParameter("page")).map(Integer::parseInt).orElse(1);
		int pageSize = Optional.ofNullable(req.getParameter("size")).map(Integer::parseInt).orElse(5);

		List<Book> bookList;
		int totalItems;
		bookList = bookService.findAll();
		if (title != null && !title.isEmpty()) {
			bookList = bookService.searchPaginated(title, page, pageSize);
			totalItems = bookService.countByTitle(title);
			req.setAttribute("title", title);
		} else {
			bookList = bookService.findAll(page, pageSize);
			totalItems = bookService.count();
		}

		int totalPages = (int) Math.ceil((double) totalItems / pageSize);
		req.setAttribute("bookList", bookList);
		req.setAttribute("currentPage", page);
		req.setAttribute("totalPages", totalPages);

		req.getRequestDispatcher("/views/admin/book-list.jsp").forward(req, resp);
	}

	private void showAddForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Author> authors = authorService.findAll();
		req.setAttribute("authors", authors);
		req.getRequestDispatcher("/views/admin/book-add.jsp").forward(req, resp);
	}

	private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		Book existingBook = bookService.findById(id);
		List<Author> categories = authorService.findAll();
		req.setAttribute("news", existingBook);
		req.setAttribute("categories", categories);
		req.getRequestDispatcher("/views/admin/book-edit.jsp").forward(req, resp);
	}

	private void showNewsDetails(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String id = req.getParameter("id");
		Book book = bookService.findById(id);
		req.setAttribute("news", book);
		req.getRequestDispatcher("/views/admin/book-details.jsp").forward(req, resp);
	}

	private void addBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String title = req.getParameter("title");
		String description = req.getParameter("description");
		String publisher = req.getParameter("publisher");
		double price = Double.parseDouble(req.getParameter("price"));
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		Part coverImagePart = req.getPart("coverImageFile");

		String[] authorIds = req.getParameterValues("authorIds");
		List<Author> authors = new ArrayList<>();
		if (authorIds != null) {
			for (String authorIdStr : authorIds) {
				int authorId = Integer.parseInt(authorIdStr);
				Author author = authorService.findById(authorId);
				authors.add(author);
			}
		}

		String coverImage = Paths.get(coverImagePart.getSubmittedFileName()).getFileName().toString();
		String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists())
			uploadDir.mkdir();
		coverImagePart.write(uploadPath + File.separator + coverImage);

		Book book = Book.builder().title(title).description(description).publisher(publisher).price(price)
				.coverImage(coverImage).quantity(quantity).authors(authors).build();

		bookService.insert(book);
		resp.sendRedirect(req.getContextPath() + "/admin/book");
	}

	private void updateBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String id = req.getParameter("id");
		String title = req.getParameter("title");
		String description = req.getParameter("description");
		String publisher = req.getParameter("publisher");
		double price = Double.parseDouble(req.getParameter("price"));
		int authorId = Integer.parseInt(req.getParameter("authorId"));
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		Part coverImagePart = req.getPart("coverImage");

		// Handling file upload
		String coverImage = Paths.get(coverImagePart.getSubmittedFileName()).getFileName().toString();
		String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists())
			uploadDir.mkdir();
		coverImagePart.write(uploadPath + File.separator + coverImage);

		// Fetching author and updating book details
		Author author = authorService.findById(authorId);
		Book book = bookService.findById(id);
		book.setTitle(title);
		book.setDescription(description);
		book.setPublisher(publisher);
		book.setPrice(price);
		book.setCoverImage(coverImage);
		book.setQuantity(quantity);
		book.setAuthors(List.of(author));

		bookService.update(book);
		resp.sendRedirect(req.getContextPath() + "/admin/books");
	}

	private void deleteNews(HttpServletRequest req, HttpServletResponse resp) {
		String id = req.getParameter("id");
		try {
			bookService.delete(id);
			resp.sendRedirect(req.getContextPath() + "/admin/news");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}