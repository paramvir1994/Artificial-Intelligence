package games_post;

public class StateNim extends State {
    public int coins = 13;

    public StateNim() {}

    public StateNim(StateNim state) {
        this.coins = state.coins;
    }

    public String toString() {
        return "" + this.coins;
    }
}
