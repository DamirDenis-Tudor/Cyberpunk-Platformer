import Database.Database;

public class Main {
    public static void main(String[] args) throws Exception {
        Database database = Database.get();
        database.createSavesTable();
        Game game = new Game();
        game.startGame();
    }
}