package Group28.OAuth.Model;

import java.util.Map;

public class CreatingAuthorizationCode extends State {

    //Singleton
    private static CreatingAuthorizationCode instance = new CreatingAuthorizationCode();

    private CreatingAuthorizationCode() {}

    public static CreatingAuthorizationCode instance() {
        return instance;
    }

    //TODO: Business logic and state transition
    @Override
    public void updateState(Context context,  Map<String, String> params)
    {
        System.out.println("CreatingAuthorizationCode");
        //TODO: zmiana stanu
//        context.setCurrentState([nowy stan].instance());
    }

}
