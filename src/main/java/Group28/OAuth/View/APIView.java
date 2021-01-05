package Group28.OAuth.View;

import Group28.OAuth.Model.State.Response;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public class APIView {
    public void createToken(Response response, HttpServletResponse httpServletResponse) {
        if (response.content instanceof List) {
            setTokenCookie(response, httpServletResponse);
        } else {
            throw new IllegalStateException("response.content isn't instanceof List (thrown in APIView.createToken)");
        }
    }

    public String validToken(boolean response) {
        if (response) {
            return "Access Token is valid";
        } else {
            return "Access Token is not valid";
        }
    }

    public void refreshToken(Response response, HttpServletResponse httpServletResponse) {
        if (response.content instanceof List) {
            setTokenCookie(response, httpServletResponse);
        } else {
            throw new IllegalStateException("response.content isn't instanceof List (thrown in APIView.refreshToken)");
        }
    }

    public String revokeToken(boolean response) {
        if (response) {
            return "Access Token has been revoked successfully";
        } else {
            return "Unable to revoke Access Token";
        }
    }

    public String revokeGrantType(boolean response) {
        if (response) {
            return "GrantType has been revoked successfully";
        } else {
            return "Unable to revoke GrantType";
        }
    }

    private void setTokenCookie(Response response, HttpServletResponse httpServletResponse) {
        if (response.content instanceof List) {
            var list = (List<String>) response.content;
            Cookie cookieAccessToken = new Cookie("AccessToken", list.get(0));
            cookieAccessToken.setPath("/");
            Cookie cookieRefreshToken = new Cookie("RefreshToken", list.get(1));
            cookieRefreshToken.setPath("/");
            httpServletResponse.addCookie(cookieAccessToken);
            httpServletResponse.addCookie(cookieRefreshToken);
        }
    }
}
