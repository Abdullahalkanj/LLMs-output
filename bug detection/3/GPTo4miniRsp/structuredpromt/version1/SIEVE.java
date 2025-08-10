import java.util.ArrayList;
public class SIEVE {
    /**
     * Returns a list of all primes <= max, using trial division.
     */
    public static ArrayList<Integer> sieve(int max) {
        ArrayList<Integer> primes = new ArrayList<>();
        if (max < 2) {
            return primes;              // nothing to do if max < 2
        }
        // 2 is the first prime
        primes.add(2);
        // check only odd numbers from 3 up to max
        for (int n = 3; n <= max; n += 2) {
            boolean isPrime = true;
            int limit = (int) Math.sqrt(n);       // only test up to sqrt(n)
            for (int p : primes) {
                if (p > limit) {
                    break;                        // no need to check further
                }
                if (n % p == 0) {
                    isPrime = false;              // found a divisor
                    break;
                }
            }
            if (isPrime) {
                primes.add(n);                    // add only if no divisors found
            }
        }
        return primes;
    }
    // Example main to test:
    public static void main(String[] args) {
        int max = 30;
        ArrayList<Integer> result = sieve(max);
        System.out.println("Primes up to " + max + ": " + result);
    }
}