import Database.Database;

public class Main {
    /**
     *
     * TODO : add a buffered image to game window
     */
    public static void main(String[] args) throws Exception {
        Database database = Database.get();
        database.createSavesTable();
        Game game = new Game();
        game.startGame();
    }
}