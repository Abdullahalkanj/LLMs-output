package modeltest;

import model3.MemberManager;
import model3.Member;
import model3.Item;
import model3.Contract;
import model3.Timee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class MemberManagerTest {

    @Test
    void testCreateMember_success() {
        MemberManager manager = new MemberManager();
        Member member = manager.createMember("Alice", "alice@example.com", "5550001111");
        assertNotNull(member);
        assertEquals("Alice", member.getName());
    }

    @Test
    void testDeleteMember_validId() {
        MemberManager manager = new MemberManager();
        Member member = manager.createMember("Bob", "bob@example.com", "5556667777");
        boolean deleted = manager.deleteMember(member.getId());
        assertTrue(deleted);
        assertNull(manager.getMemberById(member.getId()));
    }

    @Test
    void testDeleteMember_nullId() {
        MemberManager manager = new MemberManager();
        boolean result = manager.deleteMember(null);
        assertFalse(result); // deletion should fail for null ID
    }

    @Test
    void testIsEmailUnique_true() {
        MemberManager manager = new MemberManager();
        assertTrue(manager.isEmailUnique("new@email.com"));
    }

    @Test
    void testIsPhoneNumberUnique_false() {
        MemberManager manager = new MemberManager();
        assertFalse(manager.isPhoneNumberUnique("1234567890"));
    }

    @Test
    void testListMemberEmailId_contentsMatch() {
        MemberManager manager = new MemberManager();
        var result = manager.listMemberEmailId();
        assertEquals(3, result.size());
        for (String detail : result) {
            assertTrue(detail.contains("Name:"));
            assertTrue(detail.contains("Email:"));
            assertTrue(detail.contains("ID:"));
        }
    }

    @Test
    void testDoesEmailExist_true() {
        MemberManager manager = new MemberManager();
        assertTrue(manager.doesEmailExist("m1"));
    }

    @Test
    void testUpdateMemberInformation_success() {
        MemberManager manager = new MemberManager();
        Member member = manager.createMember("Initial", "initial@example.com", "9998887777");
        String id = member.getId();

        boolean result = manager.updateMemberInformation(id, "Updated Name", "updated@example.com", "1112223333");
        assertTrue(result);

        Member updated = manager.getMemberById(id);
        assertEquals("Updated Name", updated.getName());
        assertEquals("updated@example.com", updated.getEmail());
        assertEquals("1112223333", updated.getMobilePhone());
    }

    @Test
    void testUpdateMemberInformation_emptyFields() {
        MemberManager manager = new MemberManager();
        Member member = manager.createMember("John", "john@example.com", "1231231234");
        String id = member.getId();

        boolean result = manager.updateMemberInformation(id, "", "", "");
        assertTrue(result);

        Member updated = manager.getMemberById(id);
        assertEquals("John", updated.getName());
        assertEquals("john@example.com", updated.getEmail());
        assertEquals("1231231234", updated.getMobilePhone());
    }

    @Test
    void testUpdateMemberInformation_invalidId() {
        MemberManager manager = new MemberManager();
        boolean result = manager.updateMemberInformation("invalid-id", "Name", "email", "phone");
        assertFalse(result);
    }

    @Test
    void testItemExists_true() {
        MemberManager manager = new MemberManager();
        Member member = manager.createMember("Lender", "lender@example.com", "1112223333");

        Item item = new Item("Tool", "Drill", "Electric drill", 10);
        member.addItem(item);

        assertTrue(manager.itemExists("Drill"));
    }

    @Test
    void testItemIsAvailable_true() {
        MemberManager manager = new MemberManager();
        Member member = manager.createMember("Owner", "owner@example.com", "1111111111");

        Item item = new Item("Camping", "Tent", "2-person tent", 5);
        member.addItem(item);

        assertTrue(item.isItemAvailable(3, 5));
        assertTrue(manager.itemIsAvailable("Tent", 3, 5));
    }

    @Test
    void testItemIsAvailable_false_unavailableRange() {
        MemberManager manager = new MemberManager();
        Member member = manager.createMember("Owner", "owner@example.com", "1111111111");

        Item item = new Item("Camping", "Tent", "2-person tent", 5);
        Timee timee = new Timee();
        Contract contract = new Contract(member, member, item, 3, 5, timee);
        item.addContract(contract);
        member.addItem(item);

        assertFalse(manager.itemIsAvailable("Tent", 4, 6));
    }

    @Test
    void testBorrowerHasEnoughCredits_true() {
        MemberManager manager = new MemberManager();
        Member lender = manager.createMember("Lender", "lend@example.com", "1234567899");
        Member borrower = manager.createMember("Borrower", "borrow@example.com", "9876543210");

        // Add two items to give borrower 200 credits
        borrower.addItem(new Item("Test", "X1", "Dummy1", 1));
        borrower.addItem(new Item("Test", "X2", "Dummy2", 1));

        Item item = new Item("Electronics", "Laptop", "Dell XPS", 10);
        lender.addItem(item);

        assertTrue(manager.borrowerHasEnoughCredits(lender.getId(), borrower.getId(), "Laptop", 1, 5));
    }

    @Test
    void testBorrowerHasEnoughCredits_false() {
        MemberManager manager = new MemberManager();
        Member lender = manager.createMember("Lender", "lend@example.com", "1234567899");
        Member borrower = manager.createMember("Borrower", "borrow@example.com", "9876543210");

        // Only 1 item = 100 credits
        borrower.addItem(new Item("Test", "Y", "Low Credit", 1)); // 100 credits only

        // 3-day rental x 45 credits = 135 needed
        Item item = new Item("Photography", "Camera", "DSLR", 45);
        lender.addItem(item);

        assertFalse(manager.borrowerHasEnoughCredits(lender.getId(), borrower.getId(), "Camera", 1, 3));
    }


    @Test
    void testGetAllMembersVerboseDetails_containsExpectedData() {
        MemberManager manager = new MemberManager();
        Member lender = manager.createMember("VerboseUser", "verbose@example.com", "4445556666");
        Member borrower = manager.createMember("Borrower", "borrow@example.com", "9990001111");

        Item item = new Item("Furniture", "Chair", "Wooden chair", 5);
        Timee timee = new Timee();
        Contract contract = new Contract(lender, borrower, item, 1, 3, timee);
        item.addContract(contract);
        lender.addItem(item);

        var details = manager.getAllMembersVerboseDetails();
        assertFalse(details.isEmpty());

        boolean foundVerboseUser = false;
        boolean foundChair = false;
        for (String entry : details) {
            if (entry.contains("VerboseUser")) foundVerboseUser = true;
            if (entry.contains("Chair")) foundChair = true;
        }

        assertTrue(foundVerboseUser, "VerboseUser should appear in member details");
        assertTrue(foundChair, "Chair should appear in item details");
    }

    @Test
    void testGetAllItemsDetails_containsItemInfo() {
        MemberManager manager = new MemberManager();
        Member lender = manager.createMember("ItemUser", "item@example.com", "1230004567");
        Member borrower = manager.createMember("Other", "other@example.com", "8889990000");

        Item item = new Item("Transport", "Bike", "Mountain bike", 8);
        Timee timee = new Timee();
        Contract contract = new Contract(lender, borrower, item, 2, 4, timee);
        item.addContract(contract);
        lender.addItem(item);

        var details = manager.getAllItemsDetails();
        assertFalse(details.isEmpty());

        boolean foundBike = false;
        boolean foundDescription = false;
        boolean foundCost = false;

        for (String entry : details) {
            if (entry.contains("Bike")) foundBike = true;
            if (entry.contains("Mountain bike")) foundDescription = true;
            if (entry.contains("Cost Per Day: 8")) foundCost = true;
        }

        assertTrue(foundBike, "Bike name should be in item details");
        assertTrue(foundDescription, "Bike description should be present");
        assertTrue(foundCost, "Cost per day should be present");
    }

    @Test
    void testAddItemToMember_success() {
        MemberManager manager = new MemberManager();
        Member member = manager.createMember("ItemAdder", "adder@example.com", "1111112222");

        boolean result = manager.addItemToMember(member.getId(), "Books", "Java Book", "Learn Java", 5);
        assertTrue(result, "Item should be added successfully");

        Member updated = manager.getMemberById(member.getId());
        List<Item> items = updated.getItems();

        assertFalse(items.isEmpty(), "Member should have at least one item");
        Item item = items.get(0);
        assertEquals("Java Book", item.getName());
        assertEquals("Books", item.getCategory());
        assertEquals("Learn Java", item.getDescription());
        assertEquals(5, item.getCostPerDay());
    }

    @Test
    void testAddItemToMember_invalidMember() {
        MemberManager manager = new MemberManager();

        boolean result = manager.addItemToMember("non-existent-id", "Books", "Java Book", "Learn Java", 5);
        assertFalse(result, "Should return false if member doesn't exist");
    }

}
