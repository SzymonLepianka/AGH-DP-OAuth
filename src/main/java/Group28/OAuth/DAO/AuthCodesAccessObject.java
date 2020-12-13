package Group28.OAuth.DAO;

import Group28.OAuth.Domain.AuthCode;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AuthCodesAccessObject implements IDataBaseAccessObject<AuthCode>{
    private Connection connection;

    public AuthCodesAccessObject(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<AuthCode> readAll() throws SQLException {
        return null;
    }

    @Override
    public AuthCode readById(Long id) throws SQLException {
        return null;
    }

    @Override
    public AuthCode create(AuthCode object) throws SQLException {
        return null;
    }

    @Override
    public AuthCode update(AuthCode object) throws SQLException {
        return null;
    }

    @Override
    public void remove(AuthCode object) throws SQLException {

    }
}
