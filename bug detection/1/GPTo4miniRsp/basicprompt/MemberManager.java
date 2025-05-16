// ChatGPT4omini output using basic prompt


// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// The reason your manager never actually comes up with any IDs (and after the first member is created, your `initializeData()` call dead–locks in an infinite loop) is that your random‐string generator never appends any characters:

// ```java
// private String generateRandomAlphanumericString() {
//   String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//   StringBuilder sb = new StringBuilder(6);
//   // BUG: i < 0 means this loop body never executes
//   for (int i = 0; i < 0; i++) {
//     int index = random.nextInt(alphanumeric.length());
//   }
//   return sb.toString(); // always ""
// }
// ```

// Because it always returns the empty string (`""`), your `generateUniqueId()` does

// 1.  `uniqueId = ""`
// 2.  `isIdTaken("")` → false on the first go, so returns `""`
// 3.  You put the first member in the map under key `""`
// 4.  Next time, `isIdTaken("")` → true, so you loop forever.

// ---

// ## The fix

// Change your loop to run six times—and actually append the randomly chosen character to the `StringBuilder`:

// ```java
// private String generateRandomAlphanumericString() {
//   String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//   StringBuilder sb = new StringBuilder(6);
//   for (int i = 0; i < 6; i++) {
//     int index = random.nextInt(alphanumeric.length());
//     sb.append(alphanumeric.charAt(index));
//   }
//   return sb.toString();
// }
// ```

// With that in place, `generateUniqueId()` will eventually produce a non‐empty string, break out of its `do…while`, and you’ll get a usable ID.

// ---

// ### (Bonus) A couple of quick cleanups

// While you’re in here, you might also want to:

// 1. **Wire your new members into the `members` map** in `createMember`  
//    ```java
//    public Member createMember(String name, String email, String phoneNumber) {
//      if (isEmailUnique(email) && isPhoneNumberUnique(phoneNumber)) {
//        String uniqueId = generateUniqueId();
//        Member newMember = new Member(name, email, phoneNumber, uniqueId);
//        members.put(uniqueId, newMember);
//        return newMember;
//      }
//      return null;
//    }
//    ```

// 2. **Fix your “unique” checks** so they actually compare to the right fields and return the correct boolean:
//    ```java
//    public boolean isEmailUnique(String email) {
//      for (Member m : members.values()) {
//        if (m.getEmail().equalsIgnoreCase(email)) {
//          return false;  // already in use
//        }
//      }
//      return true;        // not found → unique
//    }

//    public boolean isPhoneNumberUnique(String phoneNumber) {
//      for (Member m : members.values()) {
//        if (m.getMobilePhone().equals(phoneNumber)) {
//          return false;
//        }
//      }
//      return true;
//    }
//    ```

// With those in place, your ID generation will work and your basic `createMember(…)` flow will correctly reject duplicates and store new members.
// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


// Research Team:
// 7 bugs out of 26 have passed