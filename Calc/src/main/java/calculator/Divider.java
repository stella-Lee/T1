package calculator;

public class Divider implements IDivider {

    private IFlipper flipper;
    private ISubtractor subtractor;

    public Divider(IFlipper flipper, ISubtractor subtractor) {
        this.flipper = flipper;
        this.subtractor = subtractor;
    }

    public Divider(ISubtractor subtractor) {
        this.subtractor = subtractor;
    }

    @Override
    public int divide(int a, int b) throws ArithmeticException {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }

        boolean negative = false;
        if (a < 0) {
            a = flipper.flip(a);
            negative = !negative;
        }
        if (b < 0) {
            b = flipper.flip(b);
            negative = !negative;
        }

        int result = 0;
        int remainder = a;
        while (remainder >= b) {
            remainder = subtractor.subtract(remainder, b);
            result++;
        }

        return result * (negative ? -1 : 1);
    }
}