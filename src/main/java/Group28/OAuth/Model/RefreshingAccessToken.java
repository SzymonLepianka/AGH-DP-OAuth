package Group28.OAuth.Model;

import java.util.Map;

public class RefreshingAccessToken extends State {
    //Singleton
    private static RefreshingAccessToken instance = new RefreshingAccessToken();

    private RefreshingAccessToken() {}

    public static RefreshingAccessToken instance() {
        return instance;
    }

    //TODO: Business logic and state transition
    @Override
    public void updateState(Context context,  Map<String, String> params)
    {
//        System.out.println("RefreshingAccessToken");
        //TODO: zmiana stanu
//        context.setCurrentState([nowy stan].instance());
    }



}
