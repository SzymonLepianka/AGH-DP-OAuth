package Group28.OAuth.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.xml.bind.DatatypeConverter;

public class TokenDecoder {

    public Claims decodeToken(String token, String appSecret){
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(appSecret))
                .parseClaimsJws(token).getBody();



        return claims;
    }

}
