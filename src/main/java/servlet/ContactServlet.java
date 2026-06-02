package servlet;

import dao.ContactDAO;
import dao.ContactDAOImpl;
import model.Contact;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet(name = "ContactServlet", urlPatterns = {"/contacts"})
public class ContactServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", Pattern.CASE_INSENSITIVE);
    private final ContactDAO contactDAO = new ContactDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "search":
                    searchContacts(request, response);
                    break;
                case "delete":
                    deleteContact(request, response);
                    break;
                default:
                    listContacts(request, response);
                    break;
            }
        } catch (SQLException ex) {
            log("Database error on GET", ex);
            request.setAttribute("errorMessage", "Unable to process your request at this time.");
            request.setAttribute("exception", ex);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "add":
                    addContact(request, response);
                    break;
                case "update":
                    updateContact(request, response);
                    break;
                case "delete":
                    deleteContact(request, response);
                    break;
                default:
                    listContacts(request, response);
                    break;
            }
        } catch (SQLException ex) {
            log("Database error on POST", ex);
            handleSQLException(request, response, ex);
        }
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("add-contact.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String idParam = request.getParameter("id");
        int id = parseInt(idParam);
        List<Contact> contacts = contactDAO.getAllContacts();
        Contact contactToEdit = contacts.stream()
                .filter(contact -> contact.getId() == id)
                .findFirst()
                .orElse(null);

        if (contactToEdit == null) {
            request.setAttribute("errorMessage", "Contact not found.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("contact", contactToEdit);
        request.getRequestDispatcher("edit-contact.jsp").forward(request, response);
    }

    private void listContacts(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<Contact> contacts = contactDAO.getAllContacts();
        request.setAttribute("contacts", contacts);
        request.getRequestDispatcher("contact-list.jsp").forward(request, response);
    }

    private void searchContacts(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String keyword = request.getParameter("keyword");
        if (keyword == null || keyword.trim().isEmpty()) {
            listContacts(request, response);
            return;
        }
        List<Contact> contacts = contactDAO.searchContacts(keyword);
        request.setAttribute("contacts", contacts);
        request.setAttribute("searchQuery", keyword);
        request.getRequestDispatcher("contact-list.jsp").forward(request, response);
    }

    private void addContact(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String name = sanitize(request.getParameter("name"));
        String email = sanitize(request.getParameter("email"));
        String phone = sanitize(request.getParameter("phone"));

        String validationError = validateContact(name, email);
        if (validationError != null) {
            request.setAttribute("errorMessage", validationError);
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.getRequestDispatcher("add-contact.jsp").forward(request, response);
            return;
        }

        Contact contact = new Contact(name, email, phone);
        try {
            contactDAO.addContact(contact);
            response.sendRedirect(request.getContextPath() + "/contacts?success=Contact added successfully");
        } catch (SQLException ex) {
            handleSQLException(request, response, ex);
        }
    }

    private void updateContact(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = parseInt(request.getParameter("id"));
        String name = sanitize(request.getParameter("name"));
        String email = sanitize(request.getParameter("email"));
        String phone = sanitize(request.getParameter("phone"));

        String validationError = validateContact(name, email);
        if (validationError != null) {
            request.setAttribute("errorMessage", validationError);
            request.setAttribute("contact", new Contact(id, name, email, phone, null));
            request.getRequestDispatcher("edit-contact.jsp").forward(request, response);
            return;
        }

        Contact contact = new Contact(id, name, email, phone);
        try {
            contactDAO.updateContact(contact);
            response.sendRedirect(request.getContextPath() + "/contacts?success=Contact updated successfully");
        } catch (SQLException ex) {
            handleSQLException(request, response, ex);
        }
    }

    private void deleteContact(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        int id = parseInt(request.getParameter("id"));
        contactDAO.deleteContact(id);
        response.sendRedirect(request.getContextPath() + "/contacts?success=Contact deleted successfully");
    }

    private String validateContact(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            return "Name is required.";
        }
        if (email == null || email.trim().isEmpty()) {
            return "Email is required.";
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Please enter a valid email address.";
        }
        return null;
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private String sanitize(String value) {
        return value != null ? value.trim() : "";
    }

    private void handleSQLException(HttpServletRequest request, HttpServletResponse response, SQLException ex) throws ServletException, IOException {
        log("SQL exception", ex);
        String message = "A database error occurred. Please verify your input and try again.";
        if (isDuplicateEmailError(ex)) {
            message = "The email address you entered is already registered. Please use a different email.";
        }
        request.setAttribute("errorMessage", message);
        request.setAttribute("exception", ex);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }

    private boolean isDuplicateEmailError(SQLException ex) {
        String sqlState = ex.getSQLState();
        return "23505".equals(sqlState) || "23000".equals(sqlState);
    }
}
