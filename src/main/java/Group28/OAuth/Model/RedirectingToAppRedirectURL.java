package Group28.OAuth.Model;

import java.util.Map;

public class RedirectingToAppRedirectURL extends State {

    //Singleton
    private static RedirectingToAppRedirectURL instance = new RedirectingToAppRedirectURL();

    private RedirectingToAppRedirectURL() {}

    public static RedirectingToAppRedirectURL instance() {
        return instance;
    }

    //TODO: Business logic and state transition
    @Override
    public void updateState(Context context, Map<String, String> params)
    {
//        System.out.println("RedirectingToAppRedirectURL");
        //TODO: zmiana stanu
//        context.setCurrentState([nowy stan].instance());
    }
}
