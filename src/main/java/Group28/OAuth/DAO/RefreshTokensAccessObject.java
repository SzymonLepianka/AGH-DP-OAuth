package Group28.OAuth.DAO;

import Group28.OAuth.Domain.AccessToken;
import Group28.OAuth.Domain.RefreshToken;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class RefreshTokensAccessObject implements IDataBaseAccessObject<RefreshToken>{
    private Connection connection;

    public RefreshTokensAccessObject(Connection connection) {
        this.connection = connection;
    }

    private RefreshToken createRefreshTokenFromResult(ResultSet rs) throws SQLException {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(rs.getLong("refresh_token_id"));
        refreshToken.setExpiresAt(rs.getDate("expires_at"));
        refreshToken.setRevoked(rs.getBoolean("revoked"));
        AccessToken accessToken = DatabaseEditor.getInstance().getAccessTokensAccessObject().readById(rs.getLong("access_token_id"));
        refreshToken.setAccessToken(accessToken);
        return refreshToken;
    }

    @Override
    public List<RefreshToken> readAll() throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery("select * from refresh_tokens");
        List<RefreshToken> ResultList = new LinkedList<>();
        while(result.next()) {
            ResultList.add(createRefreshTokenFromResult(result));
        }
        return ResultList;
    }

    @Override
    public RefreshToken readById(Long id) throws SQLException {
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery(String.format("select * from refresh_tokens where refresh_token_id=%f", id));
        RefreshToken refreshToken = createRefreshTokenFromResult(result);
        return  refreshToken;
    }

    @Override
    public RefreshToken create(RefreshToken object) throws SQLException {
        String query = " insert into refresh_tokens (expires_at, revoked, access_token_id)" + " values (?, ?, ?)";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        preparedStmt.setDate(1, object.getExpiresAt());
        preparedStmt.setBoolean(2, object.isRevoked());
        preparedStmt.setLong(3, object.getAccessToken().getId());
        preparedStmt.execute();
        return object;
    }

    @Override
    public RefreshToken update(RefreshToken object) throws SQLException {
        String query = "update refresh_tokens set expires_at = ?, revoked = ?, access_token_id = ? where refresh_token_id = ?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        // Set
        preparedStmt.setDate(1, object.getExpiresAt());
        preparedStmt.setBoolean(2, object.isRevoked());
        preparedStmt.setLong(3, object.getAccessToken().getId());
        // Where
        preparedStmt.setLong(4,object.getId());
        preparedStmt.execute();
        return object;
    }

    @Override
    public void remove(RefreshToken object) throws SQLException {
        String query = "DELETE FROM refresh_tokens WHERE refresh_token_id = ?";
        PreparedStatement preparedStmt = this.connection.prepareStatement(query);
        // Where
        preparedStmt.setLong(1, object.getId());
        preparedStmt.execute();
    }
}
