package calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MultiplierTest {
    private IMultiplier multiplier;

    @BeforeAll
    void setUp() {
        IAdder adder = Mockito.mock(IAdder.class);
        IFlipper flipper = Mockito.mock(IFlipper.class);

        Mockito.when(flipper.flip(-3)).thenReturn(3);
        Mockito.when(flipper.flip(3)).thenReturn(-3);
        Mockito.when(flipper.flip(0)).thenReturn(0);

        Mockito.when(adder.add(0, 3)).thenReturn(3);
        Mockito.when(adder.add(3, 3)).thenReturn(6);
        Mockito.when(adder.add(6, 3)).thenReturn(9);
        Mockito.when(adder.add(0, -3)).thenReturn(-3);
        Mockito.when(adder.add(-3, -3)).thenReturn(-6);
        Mockito.when(adder.add(-6, -3)).thenReturn(-9);

        multiplier = new Multiplier(adder, flipper);
    }

    @ParameterizedTest
    @CsvSource({
            "3, 3, 9",
            "3, -3, -9",
            "-3, 3, -9",
            "-3, -3, 9",
            "0, 3, 0",
            "3, 0, 0",
            "0, 0, 0",
    })
    void multiplyTest(int first, int second, int result) {
        Assertions.assertThat(multiplier.multiply(first, second)).isEqualTo(result);
    }
}
