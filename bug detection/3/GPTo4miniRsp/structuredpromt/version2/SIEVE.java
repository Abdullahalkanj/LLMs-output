import java.util.*;
/**
 * Simple sieve to list primes up to a given maximum.
 */
public class SIEVE {
    /**
     * Returns true if all entries in arr are true.
     */
    public static boolean all(ArrayList<Boolean> arr) {
        for (boolean value : arr) {
            if (!value) {
                return false;
            }
        }
        return true;
    }
    /**
     * Builds a list of booleans indicating for each prime p whether n % p != 0.
     */
    public static ArrayList<Boolean> list_comp(int n, ArrayList<Integer> primes) {
        ArrayList<Boolean> remaindersNonZero = new ArrayList<>();
        for (Integer p : primes) {
            remaindersNonZero.add(n % p != 0);
        }
        return remaindersNonZero;
    }
    /**
     * Returns a list of all primes from 2 up to max (inclusive).
     */
    public static ArrayList<Integer> sieve(int max) {
        ArrayList<Integer> primes = new ArrayList<>();
        for (int n = 2; n <= max; n++) {
            // Use all(...) to ensure n is not divisible by ANY known prime
            if (all(list_comp(n, primes))) {
                primes.add(n);
            }
        }
        return primes;
    }
}