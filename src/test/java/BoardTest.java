import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource
    public void isGoal(int[][] tiles, boolean expected) {
        assertThat(new Board(tiles).isGoal()).isEqualTo(expected);
    }

    private static Stream<Arguments> isGoal() {
        return Stream.of(
                Arguments.of(
                        new int[][] {  {1,2},  {3,0}  }, true
                ),
                Arguments.of(
                        new int[][] {  {2,1},  {3,0}  }, false
                ),
                Arguments.of(
                        new int[][] {  {0,1},  {2,3}  }, false
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    public void equals(int[][] tiles, int[][] otherTiles, boolean expected) {
        assertThat(new Board(tiles).equals(new Board(otherTiles))).isEqualTo(expected);
    }

    private static Stream<Arguments> equals() {
        return Stream.of(
                Arguments.of(
                        new int[][] {  {1,2},  {3,0}  }, new int[][] {  {1,2},  {3,0}  }, true
                ),
                Arguments.of(
                        new int[][] {  {2,1},  {3,0}  }, new int[][] {  {1,2},  {3,0}  }, false
                )
        );
    }
}