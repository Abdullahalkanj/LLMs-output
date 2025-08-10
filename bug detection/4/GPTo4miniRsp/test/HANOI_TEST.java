import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.Assert;

public class HANOI_TEST {

    @Test(timeout = 3000)
    public void test_0() {
        List result = HANOI.hanoi(1, 1, 3);
        String resultFormatted = QuixFixOracleHelper.format(result, true);
        Assert.assertEquals("[[1,3]]", resultFormatted);
    }

    @Test(timeout = 3000)
    public void test_1() {
        List result = HANOI.hanoi(2, 1, 3);
        String resultFormatted = QuixFixOracleHelper.format(result, true);
        Assert.assertEquals("[[1,2],[1,3],[2,3]]", resultFormatted);
    }

    @Test(timeout = 3000)
    public void test_2() {
        List result = HANOI.hanoi(3, 1, 3);
        String resultFormatted = QuixFixOracleHelper.format(result, true);
        Assert.assertEquals("[[1,3],[1,2],[3,2],[1,3],[2,1],[2,3],[1,3]]", resultFormatted);
    }

    @Test(timeout = 3000)
    public void test_3() {
        List result = HANOI.hanoi(4, 1, 3);
        String resultFormatted = QuixFixOracleHelper.format(result, true);
        Assert.assertEquals("[[1,2],[1,3],[2,3],[1,2],[3,1],[3,2],[1,2],[1,3],[2,3],[2,1],[3,1],[2,3],[1,2],[1,3],[2,3]]", resultFormatted);
    }

    @Test(timeout = 3000)
    public void test_4() {
        List result = HANOI.hanoi(2, 1, 2);
        String resultFormatted = QuixFixOracleHelper.format(result, true);
        Assert.assertEquals("[[1,3],[1,2],[3,2]]", resultFormatted);
    }

    @Test(timeout = 3000)
    public void test_5() {
        List result = HANOI.hanoi(2, 1, 1);
        String resultFormatted = QuixFixOracleHelper.format(result, true);
        Assert.assertEquals("[[1,2],[1,1],[2,1]]", resultFormatted);
    }

    @Test(timeout = 3000)
    public void test_6() {
        List result = HANOI.hanoi(2, 3, 1);
        String resultFormatted = QuixFixOracleHelper.format(result, true);
        Assert.assertEquals("[[3,2],[3,1],[2,1]]", resultFormatted);
    }
}
