import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SolverTest {

    @Test
    public void init() {
        assertThatThrownBy(() -> new Solver(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testIsSolvable() {
        //WHEN
        boolean result = new Solver(new Board(new int[][] { {0} })).isSolvable();

        //THEN
        assertThat(result).isEqualTo(null);
    }

    @Test
    public void testMoves() {
        //WHEN
        int result = new Solver(new Board(new int[][] { {0} })).moves();

        //THEN
        assertThat(result).isEqualTo(null);
    }

    @Test
    public void testSolution() {
        //WHEN
        Iterable<Board> result = new Solver(new Board(new int[][] { {0} })).solution();

        //THEN
        assertThat(result).isEqualTo(null);
    }

    @Test
    public void testMain() {
        //WHEN
        Solver.main(new String[] { "args" });

        //THEN
    }
}