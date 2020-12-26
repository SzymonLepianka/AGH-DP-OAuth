package Group28.OAuth.token;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class AccessTokenBuilder {

    String secretKey;
    Timestamp createdAt;
    Timestamp expiresAt;
    String scopes;
    Long clientID;
    String userID;

    public AccessTokenBuilder(Timestamp createdAt, Timestamp expiresAt, String scopes, Long clientID, Long userID, Long appSecret) {
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.scopes = scopes;
        this.clientID = clientID;
        this.userID = String.valueOf(userID);
        this.secretKey = String.valueOf(appSecret);
    }

    @Override
    public String toString() {
        return "TokenBuilder{" +
                "secretKey='" + secretKey + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", scopes='" + scopes + '\'' +
                ", clientID=" + clientID +
                ", userID='" + userID + '\'' +
                '}';
    }

    private static Map<String, Object> createHead() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("typ", "JWT");
        map.put("alg", "HS256");
        return map;
    }

    public String generateToken() {

        // The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHead())
                .setIssuedAt(createdAt)
                .setExpiration(expiresAt)
                .claim("scopes", scopes)
                .setSubject(userID)
//                .claim("clientID", "clientID_example")
                .signWith(SignatureAlgorithm.HS256, signingKey);

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
}
