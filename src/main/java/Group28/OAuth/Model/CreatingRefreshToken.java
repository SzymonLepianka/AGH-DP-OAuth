package Group28.OAuth.Model;

import java.util.Map;

public class CreatingRefreshToken extends State {

    //Singleton
    private static CreatingRefreshToken instance = new CreatingRefreshToken();

    private CreatingRefreshToken() {}

    public static CreatingRefreshToken instance() {
        return instance;
    }

    //TODO: Business logic and state transition
    @Override
    public void updateState(Context context,  Map<String, String> params)
    {
//        System.out.println("CreatingAccessToken");
        //TODO: zmiana stanu
//        context.setCurrentState([nowy stan].instance());
    }
}
