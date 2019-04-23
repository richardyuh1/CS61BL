public class GregorianDate extends Date {

    private static final int[] MONTH_LENGTHS = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };

    public GregorianDate(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
    }

    @Override
    public int dayOfYear() {
        int precedingMonthDays = 0;
        for (int m = 1; m < month; m += 1) {
            precedingMonthDays += getMonthLength(m);
        }
        return precedingMonthDays + dayOfMonth;
    }

    public Date nextDate() {
        int mLength = getMonthLength(this.month);
        int m = this.month;
        int d = this.dayOfMonth;
        int y = this.year;
        if (this.dayOfYear() == 365) {
            y += 1;
            d = 1;
            m = 1;
        } else if (d + 1 > mLength) {
            d = 1;
            m += 1;
        } else {
            d += 1;
        }
        GregorianDate newDate = new GregorianDate(y, m, d);
        return newDate;
    }

    private static int getMonthLength(int m) {
        return MONTH_LENGTHS[m - 1];
    }
}
