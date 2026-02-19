package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;

public class MemoryAuthDao implements AuthDAO{
    private ArrayList<AuthData> auths = new ArrayList<>();
    public void createAuth(AuthData authData) {
        auths.add(authData);
    }

    public AuthData getAuth(String authToken) {
        for (AuthData auth: auths){
            if (auth.authToken().equals(authToken)){
                return auth;
            }
        }
        return null;
    }

    public void deleteAuth(String authToken) {
        AuthData auth = getAuth(authToken);
        auths.remove(auth);
    }

    public void clear() {
        auths = new ArrayList<>();
    }
}
