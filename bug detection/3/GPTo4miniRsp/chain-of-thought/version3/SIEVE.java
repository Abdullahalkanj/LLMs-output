import java.util.*;
/**
 * Simple prime sieve via trial division.
 */
public class SIEVE {
    public static boolean all(ArrayList<Boolean> arr) {
        for (boolean value : arr) {
            if (!value) {
                return false;
            }
        }
        return true;
    }
    public static ArrayList<Boolean> list_comp(int n, ArrayList<Integer> primes) {
        ArrayList<Boolean> built = new ArrayList<>();
        int limit = (int) Math.sqrt(n);
        for (Integer p : primes) {
            if (p > limit) {
                break;
            }
            built.add(n % p != 0);
        }
        return built;
    }
    /**
     * Return all primes from 2 up to max (inclusive).
     */
    public static ArrayList<Integer> sieve(int max) {
        ArrayList<Integer> primes = new ArrayList<>();
        for (int n = 2; n <= max; n++) {
            // only add n if it's not divisible by any prior prime
            if (all(list_comp(n, primes))) {
                primes.add(n);
            }
        }
        return primes;
    }
    // Simple test
    public static void main(String[] args) {
        System.out.println(sieve(30));  // [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
    }
}