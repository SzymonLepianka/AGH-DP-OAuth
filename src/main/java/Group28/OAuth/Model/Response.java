package Group28.OAuth.Model;

import Group28.OAuth.Domain.AuthCode;

public class Response {

//    public enum TYPE {
//        CODE,
//        ACCESS_AND_REFRESH,
//        FAIL
//    }

//    TYPE type;
    public Object content;
    public String redirect;

    public Response(String redirect, Object content) {
        this.content = content;
        this.redirect = redirect;
    }
}