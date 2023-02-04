//package eu.deltasource;
//
//import eu.deltasource.exception.NotEnoughMoneyToWithdrawException;
//import eu.deltasource.model.BankAccount;
//import eu.deltasource.model.BankInstitution;
//import eu.deltasource.model.BankAccountOwner;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//
//class BankServiceTest {
//
//    @Test
//    void testIfTheAvailableAmountInTheAccountIncreases() {
//        //GIVEN
//        BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "02");
//        BankService bankService = new BankService();
//        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
//        BankAccount bankAccount = new BankAccount(owner, bankInstitution, "odsjdspf", "GBP", 30, "CurrentAccount");
//        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);
//
//
//        //WHEN
//        bankService.depositMoneyToAccount(bankAccount, 5, dateTime);
//
//        //THEN
//        Assertions.assertEquals(35, bankAccount.getAvailableAmount());
//    }
//
//    @Test
//    void testIfTheAvailableAmountInTheAccountDecreases() {
//        //GIVEN
//        BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "02");
//        BankService bankService = new BankService();
//        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
//        BankAccount bankAccount = new BankAccount(owner,  bankInstitution, "odsjdspf", "GBP", 30, "CurrentAccount");
//        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);
//
//        //WHEN
//        bankService.withdrawMoneyFromAccount(bankAccount, 5, dateTime);
//
//        //THEN
//        Assertions.assertEquals(25, bankAccount.getAvailableAmount());
//    }
//
//    @Test
//    void testToWithdrawMoneyIfAccountBalanceIsNotEnoughShouldThrowAnException() {
//        //GIVEN
//        BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "012");
//        BankService bankService = new BankService();
//        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
//        BankAccount bankAccount = new BankAccount(owner,  bankInstitution, "odsjdspf", "GBP", 30, "CurrentAccount");
//        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);
//
//        //THEN
//        Assertions.assertThrows(NotEnoughMoneyToWithdrawException.class, () -> {
//            bankService.withdrawMoneyFromAccount(bankAccount, 31, dateTime);
//        });
//    }
//
//    @Test
//    void testIfTheTransferMethodUpdatesBothAccountsBalanceFromGBPtoUSD() {
//        //GIVEN
//        BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "02");
//        BankAccountOwner owner2 = new BankAccountOwner("Gergi", "petrov", "012");
//        BankService bankService = new BankService();
//        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
//        BankInstitution bankInstitution1 = new BankInstitution("raifaizen", "ivan 2");
//        BankAccount bankAccount = new BankAccount(owner,  bankInstitution, "odsjdspf", "GBP", 30, "CurrentAccount");
//        BankAccount bankAccount2 = new BankAccount(owner2,  bankInstitution1, "odsjdspf1", "USD", 30, "CurrentAccount");
//        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);
//
//        //WHEN
//        bankService.transferMoney(bankAccount, bankAccount2, 10, dateTime);
//
//        //THEN
//        Assertions.assertEquals(7.99, bankAccount.getAvailableAmount());
//        Assertions.assertEquals(42.4, bankAccount2.getAvailableAmount());
//    }
//
//    @Test
//    void testIfTheTransferMethodUpdatesBothAccountsBalanceFromBGNtoUSD() {
//        //GIVEN
//        BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "02");
//        BankAccountOwner owner2 = new BankAccountOwner("Gergi", "petrov", "022");
//        BankService bankService = new BankService();
//        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
//        BankInstitution bankInstitution1 = new BankInstitution("raifaizen", "ivan 2");
//        BankAccount bankAccount = new BankAccount(owner,  bankInstitution, "odsjdspf", "BGN", 30, "CurrentAccount");
//        BankAccount bankAccount2 = new BankAccount(owner2,  bankInstitution1, "odsjdspf1", "USD", 30, "CurrentAccount");
//        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);
//
//        //WHEN
//        bankService.transferMoney(bankAccount, bankAccount2, 10, dateTime);
//
//        //THEN
//        Assertions.assertEquals(19.38, bankAccount.getAvailableAmount());
//        Assertions.assertEquals(35.5, bankAccount2.getAvailableAmount());
//    }
//
//    @Test
//    void testIfTheTransferMethodUpdatesBothAccountsBalanceFromBGNtoBGN() {
//        //GIVEN
//        BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "02");
//        BankAccountOwner owner2 = new BankAccountOwner("Gergi", "petrov", "022");
//        BankService bankService = new BankService();
//        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
//        BankInstitution bankInstitution1 = new BankInstitution("raifaizen", "ivan 2");
//        BankAccount bankAccount = new BankAccount(owner,  bankInstitution, "odsjdspf", "BGN", 30, "CurrentAccount");
//        BankAccount bankAccount2 = new BankAccount(owner2,  bankInstitution1, "odsjdspf1", "BGN", 30, "CurrentAccount");
//        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);
//
//        //WHEN
//        bankService.transferMoney(bankAccount, bankAccount2, 10, dateTime);
//
//        //THEN
//        Assertions.assertEquals(17.95, bankAccount.getAvailableAmount());
//        Assertions.assertEquals(40, bankAccount2.getAvailableAmount());
//    }
//
//    @Test
//    void testIfTheTransferMethodUpdatesBothAccountsBalanceFromUSDtoGBP() {
//        //GIVEN
//        BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "02");
//        BankAccountOwner owner2 = new BankAccountOwner("Gergi", "petrov", "022");
//        BankService bankService = new BankService();
//        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
//        BankInstitution bankInstitution1 = new BankInstitution("raifaizen", "ivan 2");
//        BankAccount bankAccount = new BankAccount(owner,  bankInstitution, "odsjdspf", "USD", 30, "CurrentAccount");
//        BankAccount bankAccount2 = new BankAccount(owner2,  bankInstitution1, "odsjdspf1", "GBP", 30, "CurrentAccount");
//        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);
//
//        //WHEN
//        bankService.transferMoney(bankAccount, bankAccount2, 10, dateTime);
//
//        //THEN
//        Assertions.assertEquals(15.25, bankAccount.getAvailableAmount());
//        Assertions.assertEquals(38, bankAccount2.getAvailableAmount());
//    }
//
//    @Test
//    void testIfTheBalanceOfTheAccountIsAsSetInTheObject() {
//        //GIVEN
//        BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "02");
//        BankAccountOwner owner2 = new BankAccountOwner("Gergi", "petrov", "022");
//        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
//        BankInstitution bankInstitution1 = new BankInstitution("raifaizen", "ivan 2");
//        BankAccount bankAccount = new BankAccount(owner,  bankInstitution, "odsjdspf", "GBP", 30, "CurrentAccount");
//        BankAccount bankAccount2 = new BankAccount(owner2,  bankInstitution1, "odsjdspf1", "USD", 30, "CurrentAccount");
//
//        //THEN
//        Assertions.assertEquals(30, bankAccount.getAvailableAmount());
//        Assertions.assertEquals(30, bankAccount2.getAvailableAmount());
//    }
//
//    @Test
//    void testIfTheTransactionsAreBeingStoredCorrectly() {
//        //GIVEN
//        BankAccountOwner owner = new BankAccountOwner("Ivan", "Ivanov", "02");
//        BankAccountOwner owner2 = new BankAccountOwner("Gergi", "petrov", "022");
//        BankInstitution bankInstitution = new BankInstitution("dsk", "ivan 2");
//        BankInstitution bankInstitution1 = new BankInstitution("raifaizen", "ivan 2");
//        BankAccount bankAccount = new BankAccount(owner,  bankInstitution, "odsjdspf", "GBP", 300, "CurrentAccount");
//        BankAccount bankAccount2 = new BankAccount(owner2,  bankInstitution1, "odsjdspf1", "USD", 30, "CurrentAccount");
//        BankService bankService = new BankService();
//        LocalDateTime dateTime = LocalDateTime.of(2028, 1, 1, 1, 10);
//
//        //WHEN
//        bankService.transferMoney(bankAccount, bankAccount2, 10, dateTime);
//
//        //THEN
//        Assertions.assertEquals(1, bankAccount.getAccountTransactions().size());
//        Assertions.assertEquals(1, bankAccount2.getAccountTransactions().size());
//    }
//}