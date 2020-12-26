package Group28.OAuth.token;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class RefreshTokenBuilder {

    String secretKey;
    Timestamp expiresAt;
    Long createdAccessTokenID;


    public RefreshTokenBuilder(Timestamp expiresAt, Long createdAccessTokenID, Long appSecret) {
        this.expiresAt = expiresAt;
        this.secretKey = String.valueOf(appSecret);
        this.createdAccessTokenID = createdAccessTokenID;
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "RefreshTokenBuilder{" +
                "secretKey='" + secretKey + '\'' +
                ", expiresAt=" + expiresAt +
                ", createdAccessTokenID=" + createdAccessTokenID +
                '}';
    }

    private static Map<String, Object> createHead() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("typ", "JWT");
        map.put("alg", "HS256");
        return map;
    }

    public String generateToken(){

        // The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHead())
                .setExpiration(expiresAt)
                .claim("access_token_id", createdAccessTokenID)
                .signWith(SignatureAlgorithm.HS256, signingKey);

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
}
