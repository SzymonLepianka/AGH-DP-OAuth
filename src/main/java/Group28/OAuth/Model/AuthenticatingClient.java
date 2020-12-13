package Group28.OAuth.Model;

import java.util.Map;

public class AuthenticatingClient extends State{
    AuthenticatingClient(Context context) {
        super(context);

    }

//    Singleton
    private static AuthenticatingClient instance = new AuthenticatingClient();
    private AuthenticatingClient() {}
    public static AuthenticatingClient instance() {
        return instance;
    }

    //TODO: Business logic and state transition
    @Override
    public void updateState(Context context,  Map<String, String> params)
    {
//        System.out.println("AuthenticatingClient");
//        context.setCurrentState([nowy stan].instance());
    }

}
