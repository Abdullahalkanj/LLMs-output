import java.util.*;
/**
 * Simple prime-sieve via trial division.
 */
public class SIEVE {
    /** Returns true iff every entry in arr is true. */
    public static boolean all(ArrayList<Boolean> arr) {
        for (boolean value : arr) {
            if (!value) {
                return false;
            }
        }
        return true;
    }
    /** Returns true iff any entry in arr is true. */
    public static boolean any(ArrayList<Boolean> arr) {
        for (boolean value : arr) {
            if (value) {
                return true;
            }
        }
        return false;
    }
    public static ArrayList<Boolean> list_comp(int n, List<Integer> primes) {
        ArrayList<Boolean> built = new ArrayList<>();
        for (int p : primes) {
            built.add(n % p > 0);
        }
        return built;
    }
    /**
     * Returns all primes ≤ max.
     */
    public static ArrayList<Integer> sieve(int max) {
        ArrayList<Integer> primes = new ArrayList<>();
        for (int n = 2; n <= max; n++) {
            // FIXED: require that *all* previous primes fail to divide n
            if (all(list_comp(n, primes))) {
                primes.add(n);
            }
        }
        return primes;
    }
    /** Example main to demonstrate usage. */
    public static void main(String[] args) {
        int limit = 50;
        List<Integer> primesUpTo50 = sieve(limit);
        System.out.println("Primes ≤ " + limit + ": " + primesUpTo50);
    }
}

