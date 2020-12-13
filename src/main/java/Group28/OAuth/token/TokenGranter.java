package Group28.OAuth.token;


import Group28.OAuth.Domain.ClientApp;

import java.rmi.ServerException;
import java.util.Map;

public interface TokenGranter {

    Map<String, Object> grant(ClientApp client, String grantType, Map<String, String> parameters) throws ServerException;

    default void validateGrantType(ClientApp client, String grantType) {

    }
}