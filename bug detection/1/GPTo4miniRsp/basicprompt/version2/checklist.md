
---
15 passed out of 19 Tests

* `testCreateMember_success` {B1, B2, B2.1}   Pass
* `testDeleteMember_validId` {B3}   Pass
* `testDeleteMember_nullId` {B3}    Pass
* `testIsEmailUnique_true` {B4}   Pass
* `testIsPhoneNumberUnique_false` {B5}    Pass
* `testListMemberEmailId_contentsMatch` {B6}    Pass
* `testDoesEmailExist_true` {B7}      Fail
* `testUpdateMemberInformation_success` {B8, B8.1, B8.2}    Pass
* `testUpdateMemberInformation_emptyFields` {B8, B8.1, B8.2}    Pass
* `testUpdateMemberInformation_invalidId` {B8, B8.1, B8.2}    Pass
* `testItemExists_true` {B9}      Fail
* `testItemIsAvailable_true` {B10}      Fail
* `testItemIsAvailable_false_unavailableRange` {B10}      Fail
* `testBorrowerHasEnoughCredits_true` {B11}   Pass
* `testBorrowerHasEnoughCredits_false` {B11}    Pass
* `testGetAllMembersVerboseDetails_containsExpectedData` {B12, B13}   Pass
* `testGetAllItemsDetails_containsItemInfo` {B14, B15}    Pass
* `testAddItemToMember_success` {B16}   Pass
* `testAddItemToMember_invalidMember` {B16}   Pass
---
