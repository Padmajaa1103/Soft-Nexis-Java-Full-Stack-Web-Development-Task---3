package dao;

import model.Contact;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ContactDAOImpl implements ContactDAO {

    private static final String INSERT_SQL = "INSERT INTO contacts (name, email, phone) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT id, name, email, phone, created_at FROM contacts ORDER BY created_at DESC";
    private static final String SEARCH_SQL = "SELECT id, name, email, phone, created_at FROM contacts WHERE LOWER(name) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?) ORDER BY created_at DESC";
    private static final String UPDATE_SQL = "UPDATE contacts SET name = ?, email = ?, phone = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM contacts WHERE id = ?";

    @Override
    public void addContact(Contact contact) throws SQLException {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getEmail());
            statement.setString(3, contact.getPhone());
            statement.executeUpdate();
        }
    }

    @Override
    public List<Contact> getAllContacts() throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                contacts.add(mapContact(resultSet));
            }
        }
        return contacts;
    }

    @Override
    public List<Contact> searchContacts(String keyword) throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        String wildcard = "%" + keyword.trim() + "%";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_SQL)) {
            statement.setString(1, wildcard);
            statement.setString(2, wildcard);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    contacts.add(mapContact(resultSet));
                }
            }
        }
        return contacts;
    }

    @Override
    public void updateContact(Contact contact) throws SQLException {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getEmail());
            statement.setString(3, contact.getPhone());
            statement.setInt(4, contact.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteContact(int id) throws SQLException {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Contact mapContact(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String phone = resultSet.getString("phone");
        Timestamp createdAtTimestamp = resultSet.getTimestamp("created_at");
        LocalDateTime createdAt = createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null;
        return new Contact(id, name, email, phone, createdAt);
    }
}
