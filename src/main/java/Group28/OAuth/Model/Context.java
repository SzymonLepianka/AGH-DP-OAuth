package Group28.OAuth.Model;
public class Context {

    private State currentState;
    private String Id;

    public Context(State currentState)
    {
        this.currentState = currentState;

        if(currentState == null) {
            this.currentState = Failure.instance();
        }
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public String getId() {
        return Id;
    }

    public void setId(String packageId) {
        this.Id = packageId;
    }

    public void changeState(State state) {
        this.currentState = state;
    }
}