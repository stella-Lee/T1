package calculator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubtractorTest {

    private ISubtractor subtractor;

    @BeforeAll
    void setUp() {
        IAdder adder = Mockito.mock(IAdder.class);
        IFlipper flipper = Mockito.mock(IFlipper.class);

        Mockito.when(flipper.flip(5)).thenReturn(-5);
        Mockito.when(flipper.flip(0)).thenReturn(0);
        Mockito.when(flipper.flip(-5)).thenReturn(5);

        Mockito.when(adder.add(5, -5)).thenReturn(0);
        Mockito.when(adder.add(5, 5)).thenReturn(10);
        Mockito.when(adder.add(5, 0)).thenReturn(5);
        Mockito.when(adder.add(-5, -5)).thenReturn(-10);

        subtractor = new Subtractor(adder, flipper);
    }

    @ParameterizedTest
    @CsvSource({
            "5, 5, 0",
            "5, -5, 10",
            "5, 0, 5",
            "-5, 5, -10"

    })
    void subtractTest(int first, int second, int result) {
        Assertions.assertThat(subtractor.subtract(first, second)).isEqualTo(result);
    }
}
