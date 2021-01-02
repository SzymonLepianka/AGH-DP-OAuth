package Group28.OAuth.Model;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

public class Authorization {

    public static void Authorize()
    {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var accessTokenCookie = WebUtils.getCookie(request, "AccessToken");
        var refreshTokenCookie = WebUtils.getCookie(request, "RefreshToken");
        var authCodeCookie = WebUtils.getCookie(request, "AuthCode");
        if(accessTokenCookie == null && refreshTokenCookie == null && authCodeCookie == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if(accessTokenCookie != null) {
            var accessToken = accessTokenCookie.getValue();
            System.out.println(accessToken);
        }
        // TODO Failure - redirect to login page
    }

}
