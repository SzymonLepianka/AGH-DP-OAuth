package Group28.OAuth.Model;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class Authorization {

    // TODO Call Authorize in endpoints!

    public static void Authorize(HttpServletResponse httpServletResponse, String clientID) throws ResponseStatusException, SQLException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var accessTokenCookie = WebUtils.getCookie(request, "AccessToken");
        if(accessTokenCookie == null) {
            httpServletResponse.addHeader("Location", "/web/login");
            httpServletResponse.setStatus(302);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if(accessTokenCookie != null) {
            var accessToken = accessTokenCookie.getValue();
            var validator = new ValidateToken();
            if (!validator.validateToken(Long.parseLong(clientID), accessToken)) {
                httpServletResponse.addHeader("Location", "/web/login");
                httpServletResponse.setStatus(302);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
    }

}
