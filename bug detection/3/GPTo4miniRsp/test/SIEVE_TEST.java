// package GPTo4miniRsp.basicprompt.test;


//import GPTo4miniRsp.basicpromt.SIEVE;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.Assert;

public class SIEVE_TEST {
    @org.junit.Test(timeout = 3000)
    public void test_0() throws Exception {
        ArrayList result = SIEVE.sieve(1);
        String formatted = QuixFixOracleHelper.format(result, true);
        org.junit.Assert.assertEquals("[]", formatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_1() throws Exception {
        ArrayList result = SIEVE.sieve(2);
        String formatted = QuixFixOracleHelper.format(result, true);
        org.junit.Assert.assertEquals("[2]", formatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_2() throws Exception {
        ArrayList result = SIEVE.sieve(4);
        String formatted = QuixFixOracleHelper.format(result, true);
        org.junit.Assert.assertEquals("[2,3]", formatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_3() throws Exception {
        ArrayList result = SIEVE.sieve(7);
        String formatted = QuixFixOracleHelper.format(result, true);
        org.junit.Assert.assertEquals("[2,3,5,7]", formatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_4() throws Exception {
        ArrayList result = SIEVE.sieve(20);
        String formatted = QuixFixOracleHelper.format(result, true);
        org.junit.Assert.assertEquals("[2,3,5,7,11,13,17,19]", formatted);
    }

    @org.junit.Test(timeout = 3000)
    public void test_5() throws Exception {
        ArrayList result = SIEVE.sieve(50);
        String formatted = QuixFixOracleHelper.format(result, true);
        org.junit.Assert.assertEquals("[2,3,5,7,11,13,17,19,23,29,31,37,41,43,47]", formatted);
    }
}
