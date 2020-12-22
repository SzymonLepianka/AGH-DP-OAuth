package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.ClientApp;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class VerifyingDataFromClient extends State {
    VerifyingDataFromClient(Context context) {
        super(context);
    }

    //Singleton
    private static VerifyingDataFromClient instance = new VerifyingDataFromClient();
    public VerifyingDataFromClient() {}
    public static VerifyingDataFromClient instance() {
        return instance;
    }

    //TODO: Business logic and state transition
    @Override
    public void updateState(Context context,  Map<String, String> params)  {
        System.out.println("VerifyingDataFromClient");
        //TODO: zmiana stanu
//        context.setCurrentState([nowy stan].instance());
    }
    @Override
    public boolean validate(Context context,  Map<String, String> params)  throws SQLException{
        System.out.println("VerifyingDataFromClient");
        System.out.println(params.toString());
        IDatabaseEditor dbEditor = DatabaseEditor.getInstance();
        if(dbEditor.getAppsAccessObject().readById((Long.parseLong(params.get("clientID")))) != null){
            return true;
        }
        return false;
    }

    }
