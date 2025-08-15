package calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Calculator 시스템의 통합 테스트
 * 모든 컴포넌트들이 함께 올바르게 동작하는지 검증
 */
public class CalcIntegrationTest {
    
    // Calculator 메인 클래스 (모든 컴포넌트를 통합)
    public static class Calculator {
        private final IAdder adder;
        private final ISubtractor subtractor;
        private final IMultiplier multiplier;
        private final IDivider divider;
        private final IFlipper flipper;
        
        public Calculator() {
            this.flipper = new Flipper();
            this.adder = new Adder();
            this.subtractor = new Subtractor(adder, flipper);
            this.multiplier = new Multiplier(adder, flipper);
            this.divider = new Divider(flipper, subtractor);
        }
        
        public int add(int a, int b) {
            return adder.add(a, b);
        }
        
        public int subtract(int a, int b) {
            return subtractor.subtract(a, b);
        }
        
        public int multiply(int a, int b) {
            return multiplier.multiply(a, b);
        }
        
        public int divide(int a, int b) throws ArithmeticException {
            return divider.divide(a, b);
        }
        
        public int flip(int a) {
            return flipper.flip(a);
        }
    }
    
    private Calculator calculator;
    
    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
    }
    
    // === 기본 연산 통합 테스트 ===
    
    @Test
    public void testBasicOperations() {
        assertEquals(8, calculator.add(5, 3));
        assertEquals(2, calculator.subtract(5, 3));
        assertEquals(15, calculator.multiply(5, 3));
        assertEquals(1, calculator.divide(5, 3));
    }
    
    @Test
    public void testNegativeNumbers() {
        assertEquals(-2, calculator.add(-5, 3));
        assertEquals(-8, calculator.subtract(-5, 3));
        assertEquals(-15, calculator.multiply(-5, 3));
        assertEquals(-1, calculator.divide(-5, 3));
    }
    
    // === 복합 연산 테스트 (여러 컴포넌트가 함께 동작) ===
    
    @Test
    public void testComplexCalculation() {
        // (10 + 5) * 3 - 8 / 2 = 15 * 3 - 4 = 45 - 4 = 41
        int step1 = calculator.add(10, 5);        // 15
        int step2 = calculator.multiply(step1, 3); // 45
        int step3 = calculator.divide(8, 2);       // 4
        int result = calculator.subtract(step2, step3); // 41
        
        assertEquals(41, result);
    }
    
    @Test
    public void testChainedOperations() {
        // 복잡한 연산 체인: ((20 / 4) + 3) * 2 - 1
        int result = calculator.subtract(
            calculator.multiply(
                calculator.add(
                    calculator.divide(20, 4), 3
                ), 2
            ), 1
        );
        assertEquals(15, result); // ((5) + 3) * 2 - 1 = 8 * 2 - 1 = 16 - 1 = 15
    }
    
    // === 경계값 및 특수 케이스 통합 테스트 ===
    
    @Test
    public void testZeroOperations() {
        assertEquals(5, calculator.add(0, 5));
        assertEquals(-5, calculator.subtract(0, 5));
        assertEquals(0, calculator.multiply(0, 5));
        assertEquals(0, calculator.divide(0, 5));
    }
    
    @Test
    public void testDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> {
            calculator.divide(10, 0);
        });
    }
    
    // === 음수 처리 통합 테스트 (Flipper와의 상호작용) ===
    
    @Test
    public void testNegativeMultiplication() {
        // Multiplier는 내부적으로 Flipper를 사용하여 음수를 처리
        assertEquals(-12, calculator.multiply(3, -4));
        assertEquals(-12, calculator.multiply(-3, 4));
        assertEquals(12, calculator.multiply(-3, -4));
    }
    
    @Test
    public void testNegativeDivision() {
        // Divider는 내부적으로 Flipper를 사용하여 음수를 처리
        assertEquals(-2, calculator.divide(8, -4));
        assertEquals(-2, calculator.divide(-8, 4));
        assertEquals(2, calculator.divide(-8, -4));
    }
    
    @Test
    public void testSubtractionWithFlipper() {
        // Subtractor는 내부적으로 Flipper를 사용하여 뺄셈을 덧셈으로 변환
        assertEquals(7, calculator.subtract(10, 3));
        assertEquals(13, calculator.subtract(10, -3));
        assertEquals(-7, calculator.subtract(-10, -3));
    }
    
    // === 컴포넌트 간 의존성 테스트 ===
    
    @Test
    public void testMultiplierUsesAdder() {
        // Multiplier는 내부적으로 Adder를 반복 호출
        // 3 * 4 = 3 + 3 + 3 + 3
        assertEquals(12, calculator.multiply(3, 4));
        
        // 큰 수의 곱셈도 올바르게 동작하는지 확인
        assertEquals(100, calculator.multiply(10, 10));
    }
    
    @Test
    public void testDividerUsesSubtractor() {
        // Divider는 내부적으로 Subtractor를 반복 호출
        // 15 / 3 = 15 - 3 - 3 - 3 - 3 - 3 = 0 (5번 빼기)
        assertEquals(5, calculator.divide(15, 3));
        
        // 나머지가 있는 나눗셈
        assertEquals(3, calculator.divide(10, 3)); // 10 / 3 = 3 나머지 1
    }
    
    // === 성능 및 안정성 테스트 ===
    
    @Test
    public void testLargeNumbers() {
        // 큰 수에 대한 연산이 올바르게 동작하는지 확인
        assertEquals(2000000, calculator.add(1000000, 1000000));
        assertEquals(1000000, calculator.multiply(1000, 1000));
    }
    
    @Test
    public void testOverflowBehavior() {
        // Java int 오버플로우 동작 확인
        int result = calculator.add(Integer.MAX_VALUE, 1);
        assertEquals(Integer.MIN_VALUE, result);
    }
    
    // === 전체 시나리오 테스트 ===
    
    @Test
    public void testCalculatorScenario() {
        // 실제 계산기 사용 시나리오
        // 수식: (100 - 25) * 2 + 10 / 5 - 3
        
        int temp1 = calculator.subtract(100, 25);    // 75
        int temp2 = calculator.multiply(temp1, 2);   // 150
        int temp3 = calculator.divide(10, 5);        // 2
        int temp4 = calculator.add(temp2, temp3);    // 152
        int result = calculator.subtract(temp4, 3);   // 149
        
        assertEquals(149, result);
    }
    
    @Test
    public void testMixedSignOperations() {
        // 다양한 부호가 섞인 복잡한 연산
        // (-8 + 3) * (-2) / 4 + 1
        
        int step1 = calculator.add(-8, 3);           // -5
        int step2 = calculator.multiply(step1, -2);  // 10
        int step3 = calculator.divide(step2, 4);     // 2
        int result = calculator.add(step3, 1);       // 3
        
        assertEquals(3, result);
    }
}
/*
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
        adder = new Adder();
        flipper = new Flipper();
        subtractor = new Subtractor(adder, flipper);
        flipper = Mockito.mock(IFlipper.class);
        subtractor = Mockito.mock(ISubtractor.class);
        multiplier = new Multiplier(adder, flipper);
        divider = new Divider(flipper, subtractor); // 단순 구현이면 mock 없이도 테스트 가능
    }

    @Test
    @DisplayName("음수 연산 조합 테스트: 부호 반전 포함")
    public void testNegativeOperations() {
        assertEquals(-2, subtractor.subtract(3, 5));
        assertEquals(-6, multiplier.multiply(-2, 3));
        assertEquals(2, divider.divide(-6, -3));
    }

    @Test
    @DisplayName("경계값 테스트: 0과 1에 대한 연산")
    public void testZeroAndOne() {
        assertEquals(0, multiplier.multiply(0, 100));
        assertEquals(100, multiplier.multiply(1, 100));
        assertEquals(0, subtractor.subtract(0, 0));
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
}*/
