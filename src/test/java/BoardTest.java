import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardTest {
    @Test
    public void init_immutable() {
        int[][] tiles = new int[][] {
                {1,2},
                {3,4}
        };
        Board board = new Board(tiles);
        tiles[0] = new int[0];
        tiles[1] = new int[0];

        assertThat(board.toString()).contains("1","2","3","4");
    }
}