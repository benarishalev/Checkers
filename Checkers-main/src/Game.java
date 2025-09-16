import java.util.Random;

public class Game {
    private String currentTurn;

    public Game() {
        // Randomly select the starting color
        currentTurn = new Random().nextBoolean() ? "white" : "black";
    }

    public String getCurrentTurnColor() {
        return currentTurn;
    }

    public void switchTurn() {
        currentTurn = (currentTurn == "white") ? "black" : "white";
    }

    public void resetGame() {
        // Reset the game to the initial state
        currentTurn = new Random().nextBoolean() ? "white" : "black";
    }
}
