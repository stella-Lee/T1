package calculator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@org.junit.jupiter.api.Tag("integration")
class CalcIntegrationTest {
	private IAdder adder;
    private ISubtractor subtractor;
    private IMultiplier multiplier;
    private IDivider divider;
    private IFlipper flipper;

    @BeforeEach
    public void setUp() {
/*        adder = new Adder();
        flipper = new Flipper();
        subtractor = new Subtractor(adder, flipper);*/
        flipper = Mockito.mock(IFlipper.class);
        subtractor = Mockito.mock(ISubtractor.class);
        //multiplier = new Multiplier(adder, flipper);
        divider = new Divider(flipper, subtractor); // 단순 구현이면 mock 없이도 테스트 가능
    }

    @Test
    @DisplayName("음수 연산 조합 테스트: 부호 반전 포함")
    public void testNegativeOperations() {
        //assertEquals(-2, subtractor.subtract(3, 5));
        //assertEquals(-6, multiplier.multiply(-2, 3));
        assertEquals(2, divider.divide(-6, -3));
    }

    @Test
    @DisplayName("경계값 테스트: 0과 1에 대한 연산")
    public void testZeroAndOne() {
        //assertEquals(0, multiplier.multiply(0, 100));
        //assertEquals(100, multiplier.multiply(1, 100));
        //assertEquals(0, subtractor.subtract(0, 0));
        assertEquals(1, divider.divide(100, 100));
    }

    @Test
    @DisplayName("add ➝ multiply, subtract ➝ divide 통합 시나리오")
    public void testFullOperation() {
        // 1. add + multiply
        int result = adder.add(2, 3); // 5
        assertEquals(5, result);
        assertEquals(10, multiplier.multiply(result, 2)); // 5 * 2

        // 2. subtract + divide
        result = subtractor.subtract(10, 4); // 6
        assertEquals(3, divider.divide(result, 2)); // 6 / 2
    }

    @Test
    @DisplayName("복합 연산 흐름 테스트: ((3 + 5) - 2) * 2 = 12")
    public void testChainedOperations() {
        // ((3 + 5) - 2) * 2 = 12
        int sum = adder.add(3, 5);
        int diff = subtractor.subtract(sum, 2);
        int product = multiplier.multiply(diff, 2);
        assertEquals(12, product);
    }

}
