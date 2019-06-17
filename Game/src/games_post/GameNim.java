package games_post;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class GameNim extends Game {
    int winningScore = 10;
    int losingScore = -10;
    int neutralScore = 0;

    public GameNim() {
        this.currentState = new StateNim();
    }

    public boolean isWinState(State state) {
        StateNim nimState = (StateNim)state;
        if (nimState.coins == 1) {
            return true;
        }
        return false;
    }

    public boolean isStuckState(State state) {
        // Impossible to be stuck
        return false;
    }

    public Set<State> getSuccessors(State state) {
        if (this.isWinState(state) || this.isStuckState(state)) {
            return null;
        }

        StateNim nimState = (StateNim)state;

        Set<State> successors = new HashSet<State>();
        StateNim successorState;

        for (int i = 1; i <= 3; i++) {
            if (nimState.coins - i >= 1) {
                successorState = new StateNim(nimState);
                successorState.coins -= i;
                successorState.player = state.player == 0 ? 1 : 0;

                successors.add(successorState);
            }
        }

        return successors;
    }

    public double eval(State state) {   
        if (this.isWinState(state)) {
            int previousPlayer = state.player == 0 ? 1 : 0;

            if (previousPlayer == 0) {
                // computer wins
                return this.winningScore;
            } else {
                // human wins
                return this.losingScore;
            }
        }

        return this.neutralScore;
    }

    public static void main(String[] args) throws Exception {
        Game game = new GameNim();
        Search search = new Search(game);
        int depth = 13;

        //needed to get human's move
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            StateNim nextState = null;

            switch (game.currentState.player) {
                // Computer
                case 0:
                    nextState = (StateNim)search.bestSuccessorState(depth);
                    nextState.player = 0;
                    System.out.println("Computer: \n" + nextState);
                    break;

                // Human
                case 1:
                    // Get human's move
                    System.out.print("Enter your *valid* move> ");
                    int coinsToTake = Integer.parseInt(in.readLine());

                    nextState = new StateNim((StateNim)game.currentState);
                    nextState.player = 1;
                    nextState.coins -= coinsToTake;
                    System.out.println("Human: \n" + nextState);
                    break;
            }

            game.currentState = nextState;
            // Change player
            game.currentState.player = game.currentState.player == 0 ? 1 : 0;

            // Determine winner
            if (game.isWinState(game.currentState)) {
                // last move was by the computer
                if (game.currentState.player == 1) {
                    System.out.println("Computer wins!");
                } else {
                    System.out.println("You win!");
                }
                break;
            }

            if (game.isStuckState(game.currentState)) { 
                System.out.println("Tie!");
                break;
            }
        }
    }
}
