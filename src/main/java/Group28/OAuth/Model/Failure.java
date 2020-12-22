package Group28.OAuth.Model;

import java.util.Map;

public class Failure extends State {
    //Singleton
    private static Failure instance = new Failure();

    private Failure() {}

    public static Failure instance() {
        return instance;
    }

    //TODO: Business logic and state transition
    @Override
    public void updateState(Context context,  Map<String, String> params)
    {
//        System.out.println("Failure");
        //TODO: zmiana stanu
//        context.setCurrentState([nowy stan].instance());
    }
}
