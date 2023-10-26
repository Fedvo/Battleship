package battleship.game.grid;

public enum GridModificationResult {
    OUT_OF_GAME_GRID,
    SHIPS_TO_CLOSE,
    SHIP_SANK,
    SHIP_PLACED,
    HIT_REGISTERED,
    MISS_REGISTERED,
    WINNER
}
