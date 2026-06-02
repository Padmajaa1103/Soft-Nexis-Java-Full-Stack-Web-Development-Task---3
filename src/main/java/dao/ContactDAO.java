package dao;

import model.Contact;

import java.sql.SQLException;
import java.util.List;

public interface ContactDAO {
    void addContact(Contact contact) throws SQLException;
    List<Contact> getAllContacts() throws SQLException;
    List<Contact> searchContacts(String keyword) throws SQLException;
    void updateContact(Contact contact) throws SQLException;
    void deleteContact(int id) throws SQLException;
}
