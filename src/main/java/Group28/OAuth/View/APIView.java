package Group28.OAuth.View;

import Group28.OAuth.Domain.AccessToken;
import Group28.OAuth.Model.Response;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public class APIView {
    public void showToken(Response response, HttpServletResponse httpServletResponse){
        if(response.content instanceof List){
            var list = (List<String>) response.content;
            Cookie cookieAccessToken = new Cookie("AccessToken", list.get(0));
            Cookie cookieRefreshToken = new Cookie("RefreshToken", list.get(1));
            httpServletResponse.addCookie(cookieAccessToken);
            httpServletResponse.addCookie(cookieRefreshToken);
        }
    }
}
