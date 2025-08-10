import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;

import main.Registry;
import main.Member;
public class RegistryTest {

    @Test
    void testUniqueIdsAreGenerated() {
        Registry registry = new Registry("dummy.txt");

        for (int i = 0; i < 100; i++) {
            String name = "User" + i;
            String email = "user" + i + "@example.com";
            registry.addNewMember(name, email);
        }

        HashSet<String> ids = new HashSet<>();
        for (Member member : registry.getMembers()) {
            assertTrue(ids.add(member.getMemberId()), "Duplicate ID found: " + member.getMemberId());
        }
    }

    @Test
    void testEmailExistsDetection() {
        Registry registry = new Registry("dummy.txt");

        registry.addNewMember("Alice", "alice@example.com");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            registry.addNewMember("Another Alice", "alice@example.com");
        });

        assertTrue(exception.getMessage().contains("This email address is already used by another member."));
    }
}
