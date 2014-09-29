package sk.insomnia.tools.timeUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RowingRaceTimeUtilTest {

    private static final double rowingSpeed = 600;
    private static final double rowingTime = 6000.9;
    @Test
    public void testRowingSpeedAsParams() throws Exception {
        String[] asParams = RowingRaceTimeUtil.rowingSpeedAsParams(rowingSpeed);
        assertEquals("10:00", asParams[0]);
    }

    @Test
    public void testRowingTimeAsParams() throws Exception {
        String[] asParams = RowingRaceTimeUtil.rowingTimeAsParams(rowingTime);
        assertEquals("01:00.90", asParams[0]);
    }

    @Test
    public void testFormatRowingTime() throws Exception {
        assertEquals("01:00.90", RowingRaceTimeUtil.formatRowingTime(rowingTime));
    }
}