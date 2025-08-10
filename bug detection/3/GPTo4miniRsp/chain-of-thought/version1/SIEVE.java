import java.util.ArrayList;

public class SIEVE {

    public static boolean all(ArrayList<Boolean> arr) {
        for (boolean value : arr) {
            if (!value) { return false; }
        }
        return true;
    }

    public static ArrayList<Boolean> list_comp(int n, ArrayList<Integer> primes) {
        ArrayList<Boolean> built = new ArrayList<>();
        for (Integer p : primes) {
            built.add(n % p > 0);
        }
        return built;
    }

    public static ArrayList<Integer> sieve(int max) {
        ArrayList<Integer> primes = new ArrayList<>();
        for (int n = 2; n <= max; n++) {
            if (all(list_comp(n, primes))) {
                primes.add(n);
            }
        }
        return primes;
    }
}
