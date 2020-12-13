package Group28.OAuth.token;

import Group28.OAuth.Domain.ClientApp;
import Group28.OAuth.config.CachesEnum;
import io.jsonwebtoken.Jwts;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.rmi.ServerException;
import java.security.KeyPair;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class AccessTokenGranter implements TokenGranter{

    private static final String GRANT_TYPE = "authorization_code";
    private final AuthenticationManager authenticationManager;
    KeyPair keyPair;
    String issuer;
    CacheManager cacheManager;

    public AccessTokenGranter(AuthenticationManager authenticationManager, CacheManager cacheManager, KeyPair keyPair, String issuer) {
        this.authenticationManager = authenticationManager;
        this.cacheManager = cacheManager;
        this.keyPair = keyPair;
        this.issuer = issuer;
    }

    @Override
    public Map<String, Object> grant(ClientApp client, String grantType, Map<String, String> parameters) throws ServerException {

        Map<String, Object> result = new HashMap<>();
        result.put("status", 0);

        String authorizationCode = parameters.get("code");
        String redirectUri = parameters.get("redirect_uri");
        String clientId = parameters.get("client_id");
        String scope = parameters.get("scope");

        if (authorizationCode == null) {
            throw new ServerException("An authorization code must be supplied." + HttpStatus.BAD_REQUEST + "invalid_request kurwo");
        }

        Cache.ValueWrapper storedCode = cacheManager.getCache(CachesEnum.Oauth2AuthorizationCodeCache.name()).get(authorizationCode);

        if (storedCode == null) {
            throw new ServerException("An authorization code must be supplied." + HttpStatus.BAD_REQUEST + "invalid_request kurwo");
        }
        else{

            //TODO: na podstawie storedcode wyciągnąć co to za użytkownik

            Date now = new Date();

            //TODO: to gdzieś umieścić w jakichś parametrach
            int accessTokenValidity = 60 * 60 * 2;
            int refreshTokenValidity = 60 * 60 * 24;

            Date tokenExpiration = Date.from(LocalDateTime.now().plusSeconds(accessTokenValidity).atZone(ZoneId.systemDefault()).toInstant());
            Date refreshTokenExpiration = Date.from(LocalDateTime.now().plusSeconds(refreshTokenValidity).atZone(ZoneId.systemDefault()).toInstant());

            String tokenId = UUID.randomUUID().toString();
            String accessToken = Jwts.builder()
                    .setHeaderParam("alg", "HS256")
                    .setHeaderParam("typ", "JWT")
//                    .claim("accountOpenCode", userInfo.getAccountOpenCode())
                    .setIssuer(issuer)
//                    .setSubject(userInfo.getUsername())
                    .setAudience(clientId)
//                    .claim("roles", userInfo.getAuthorities().stream().map(e -> e.getAuthority()).collect(Collectors.toList()))
                    .setExpiration(tokenExpiration)
                    .setNotBefore(now)
                    .setIssuedAt(now)
                    .setId(tokenId)
                    .compact();

            //TODO: refresh tokem tworzenie

            cacheManager.getCache(CachesEnum.Oauth2AuthorizationCodeCache.name()).evictIfPresent(authorizationCode);

            result.put("access_token", accessToken);
            result.put("token_type", "bearer");
            result.put("expires_in", accessTokenValidity - 1);
//            result.put("accountOpenCode", userInfo.getAccountOpenCode());
            result.put("scope", scope);
            result.put("jti", tokenId);
            result.put("status", 1);
        }
        return result;
    }

    @Override
    public void validateGrantType(ClientApp client, String grantType) {

    }
}
