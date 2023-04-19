import Database.Database;

public class Main {
    /**
     *
     * TODO : 0) comment the code
     *        1) Implement movable platform & ascending helicopter.
     *        2) Implement gunner enemy.
     */
    public static void main(String[] args) throws Exception {
        Database database = Database.get();
        database.createSavesTable();
        Game game = new Game();
        game.startGame();
        // TODO -> AFTER DESERIALIZATION EACH DYNAMIC COMPONENT SHOULD HAVE A DATA RECALIBRATION FUNCTION

    }
}