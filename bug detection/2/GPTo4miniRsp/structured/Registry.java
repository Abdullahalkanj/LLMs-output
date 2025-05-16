// ChatGPT4omini output using structured prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Here are the two bugs I found:

// 1. **NullPointerException in `generateUniqueMemberId()`**  
//    You never initialize `alphanumeric` (it stays `null`), so as soon as you call  
//    ```java
//    alphanumeric.charAt(rnd.nextInt(alphanumeric.length()))
//    ```  
//    you get an NPE.

// 2. **Wrong string comparison (and a stray parenthesis) in `emailExists()`**  
//    ```java
//    if (email == member.getEmail())) {  // ← compares references, plus an extra “)”  
//        return true;
//    }
//    ```  
//    In Java you almost always want `.equals(...)` to compare contents, and you must fix the extra `)`.

// ---

// ### Fix for `generateUniqueMemberId()`

// Define and use a valid character set. For example:

// ```java
// private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

// private String generateUniqueMemberId() {
//   Random rnd = new Random();
//   StringBuilder sb = new StringBuilder(6);

//   while (true) {
//     sb.setLength(0);
//     for (int i = 0; i < 6; i++) {
//       sb.append(ALPHANUMERIC.charAt(rnd.nextInt(ALPHANUMERIC.length())));
//     }
//     String candidateId = sb.toString();
//     if (!memberIdExists(candidateId)) {
//       return candidateId;
//     }
//     // otherwise loop again
//   }
// }
// ```

// - We declare a `static final` `ALPHANUMERIC` string full of allowed characters.  
// - Inside the loop we clear `sb` before building each new candidate.

// ---

// ### Fix for `emailExists()`

// Use `.equals(...)` and remove the stray parenthesis:

// ```java
// private boolean emailExists(String email) {
//   if (email == null || email.isEmpty()) {
//     return false;
//   }
//   for (Member member : members) {
//     if (email.equals(member.getEmail())) {
//       return true;
//     }
//   }
//   return false;
// }
// ```

// ---

// With those two changes in place, your registry will no longer blow up on `generateUniqueMemberId()`, and duplicate-email checking will work correctly.
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
