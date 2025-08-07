package calculator;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

class DividerTest {

	private IDivider divider;

    @BeforeEach
    void setUp() {
        IFlipper flipper = Mockito.mock(IFlipper.class);
        ISubtractor subtractor = Mockito.mock(ISubtractor.class);

        Mockito.when(flipper.flip(-2)).thenReturn(2);
        Mockito.when(flipper.flip(-5)).thenReturn(5);

        Mockito.when(subtractor.subtract(5, 2)).thenReturn(3);
        Mockito.when(subtractor.subtract(3, 2)).thenReturn(1);
        Mockito.when(subtractor.subtract(2, 2)).thenReturn(0);

        divider = new Divider(flipper, subtractor);
    }

    @Test
    void divideByZeroTest() {
        Assertions.assertThatThrownBy(() -> divider.divide(10, 0))
                .isInstanceOf(ArithmeticException.class)
                .hasMessage("Division by zero");
    }

    @ParameterizedTest
    @CsvSource({
            "5, 2, 2",
            "5, -2, -2",
            "-5, 2, -2",
            "-5, -2, 2",
            "0, 2, 0",
            "2, 2, 1",
            "2, 3, 0"
    })
    void divideTest(int first, int second, int result) {
        Assertions.assertThat(divider.divide(first, second)).isEqualTo(result);
    }

}
