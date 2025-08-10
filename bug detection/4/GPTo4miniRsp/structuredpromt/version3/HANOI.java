import java.util.*;

public class HANOI {
    public static List<Pair<Integer,Integer>> hanoi(int height, int start, int end) {
        // — Validate inputs
        if (height < 0) {
            throw new IllegalArgumentException("height must be non-negative");
        }
        if (start < 1 || start > 3 || end < 1 || end > 3 || start == end) {
            throw new IllegalArgumentException("start and end must be distinct pegs 1, 2, or 3");
        }
    
        List<Pair<Integer,Integer>> steps = new ArrayList<>();
    
        if (height > 0) {
            // FIX: compute helper peg in O(1) rather than via PriorityQueue
            int helper = 6 - start - end;
            // recurse moving height−1 disks from start to helper
            steps.addAll(hanoi(height - 1, start, helper));
            // FIX: move the largest disk from start to end (not to helper)
            steps.add(new Pair<>(start, end));
            // recurse moving height−1 disks from helper to end
            steps.addAll(hanoi(height - 1, helper, end));
        }
    
        return steps;
    }


    public static class Pair<F, S> {
        private F first; 
        private S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public void setFirst(F first) {
            this.first = first;
        }

        public void setSecond(S second) {
            this.second = second;
        }

        public F getFirst() {
            return first;
        }

        public S getSecond() {
            return second;
        }

        @Override
        public String toString() {
            return "(" + String.valueOf(first) + ", " + String.valueOf(second) + ")";
        }
    }
}
