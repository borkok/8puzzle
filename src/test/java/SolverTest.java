import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SolverTest {

    @Test
    public void init_exceptions() {
        assertThatThrownBy(() -> new Solver(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource
    public void solution(Board initial, int expectedMoves,
                         List<Board> expectedSolution) {
        //WHEN
        Solver result = new Solver(initial);

        //THEN
        assertThat(result.moves()).isEqualTo(expectedMoves);
        assertThat(result.solution())
                .hasSameSizeAs(expectedSolution)
                .containsOnlyOnceElementsOf(expectedSolution);
    }

    private static Stream<Arguments> solution() {
        return Stream.of(
                Arguments.of(
                        //initial
                        new Board(new int[][] {  {1,2},  {3,0}  }),
                        //moves
                        0,
                        //solution
                        List.of(
                                new Board(new int[][] {  {1,2},  {3,0}  })
                        )
                )
                ,Arguments.of(
                        //initial
                        new Board(new int[][] {  {1,2},  {0,3}  }),
                        //moves
                        1,
                        //solution
                        List.of(
                                new Board(new int[][] {  {1,2},  {3,0}  }),
                                new Board(new int[][] {  {1,2},  {0,3}  })
                        )
                )
                ,Arguments.of(
                        //initial
                        new Board(new int[][] {  {0,1},  {3,2}  }),
                        //moves
                        2,
                        //solution
                        List.of(
                                new Board(new int[][] {  {0,1},  {3,2}  }),
                                new Board(new int[][] {  {1,0},  {3,2}  }),
                                new Board(new int[][] {  {1,2},  {3,0}  })
                        )
                )
        );
    }
}