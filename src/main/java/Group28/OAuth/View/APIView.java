package Group28.OAuth.View;

import Group28.OAuth.Model.State.Response;

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
<<<<<<< Updated upstream
=======

    public String validToken(boolean response){
        if(response){
            return "Token valid";
        }
        else{
            return "Token not valid";
        }
    }
    public void refreshToken(Response response, HttpServletResponse httpServletResponse){
        if(response.content instanceof List){
            var list = (List<String>) response.content;
            Cookie cookieRefreshToken = new Cookie("RefreshToken", list.get(1));
            httpServletResponse.addCookie(cookieRefreshToken);
        }
    }

    public String revokeToken(boolean response){
        if(response){
            return "Token revoked";
        }
        else{
            return "Token not revoked";
        }
    }
    public String revokeGrantType(boolean response){
        if(response){
            return "GrantType revoked";
        }
        else{
            return "GrantType not revoked";
        }
    }
>>>>>>> Stashed changes
}
