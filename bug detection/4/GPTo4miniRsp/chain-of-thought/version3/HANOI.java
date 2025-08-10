
import java.util.*;

/**
 * Recursive solution to the Towers of Hanoi puzzle.
 * @author derricklin (refactored)
 */
public class HANOI {
    /**
     * Default entry point: move `height` disks from peg 1 to peg 3.
     */
    public static List<Pair<Integer,Integer>> hanoi(int height) {
        return hanoi(height, 1, 3);
    }

    /**
     * Moves `height` disks from peg `start` to peg `end`, returning the list of moves.
     * @param height number of disks (> 0)
     * @param start  starting peg (1, 2, or 3)
     * @param end    target peg (1, 2, or 3), must differ from `start`
     * @return list of (fromPeg, toPeg) moves
     * @throws IllegalArgumentException if pegs invalid or equal
     */
    public static List<Pair<Integer,Integer>> hanoi(int height, int start, int end) {
        if (start < 1 || start > 3 || end < 1 || end > 3) {
            throw new IllegalArgumentException("Pegs must be in {1, 2, 3}");
        }
        if (start == end) {
            throw new IllegalArgumentException("Start and end pegs must differ");
        }

        List<Pair<Integer,Integer>> steps = new ArrayList<>();

        if (height > 0) {
            // The only remaining peg is 6 - start - end since 1+2+3 = 6.
            int helper = 6 - start - end;

            // Move n-1 disks from start to helper
            steps.addAll(hanoi(height - 1, start, helper));

            // Move the bottom disk from start to end
            steps.add(new Pair<>(start, end));

            // Move n-1 disks from helper to end
            steps.addAll(hanoi(height - 1, helper, end));
        }

        return steps;
    }


    /**
     * Simple generic Pair class.
     */
    public static class Pair<F, S> {
        private F first;
        private S second;

        public Pair(F first, S second) {
            this.first  = first;
            this.second = second;
        }

        public F getFirst()  { return first; }
        public S getSecond() { return second; }

        public void setFirst(F first)   { this.first = first; }
        public void setSecond(S second) { this.second = second; }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;
            Pair<?,?> p = (Pair<?,?>) o;
            return Objects.equals(first, p.first)
                && Objects.equals(second, p.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }
}
