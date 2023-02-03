package eu.deltasource;

import eu.deltasource.exception.InvalidCurrencyException;
import eu.deltasource.exception.NotAllowedToTransferToTheSameBankAccountException;
import eu.deltasource.exception.TransfersAllowedBetweenCurrentAccountsException;
import eu.deltasource.model.BankAccount;
import eu.deltasource.model.BankInstitution;
import eu.deltasource.model.BankAccountOwner;

import java.time.LocalDateTime;

public class RunApplication {
    public void run() {

        BankAccountOwner niki4 = new BankAccountOwner("nikic", "Go5ov", "02");
        BankAccountOwner mirki4 = new BankAccountOwner("mirki4", "Go5sov", "12");

        BankInstitution dsk = new BankInstitution("dsk", "sdf");
        BankInstitution raifaizen = new BankInstitution("raifaizen", "sdfewr");

        BankAccount nikic = new BankAccount(niki4, dsk, "frewer", "GBP", 30, "CurrentAccount", "SavingsAccount");

        BankAccount mirkic = new BankAccount(mirki4, raifaizen, "freweer", "USD", 30, "CurrentAccount");

        BankService bankService = new BankService();
        try {
//            bankService.depositMoneyToAccount(mirkic, 2, LocalDateTime.of(2026, 3, 2, 16, 15));
//            bankService.withdrawMoneyFromAccount(mirkic, 10, LocalDateTime.of(2025, 3, 2, 15, 25, 22));
//            bankService.depositMoneyToAccount(nikic, 2, LocalDateTime.of(2027, 3, 2, 19, 18, 9));
            bankService.transferMoney(nikic, mirkic, 10, LocalDateTime.of(2028, 3, 2, 19, 18, 9));


//            bankService.withdrawMoneyFromAccount(mirkic, 30, LocalDateTime.of(2028, 3, 2, 19, 18, 9));
        } catch (TransfersAllowedBetweenCurrentAccountsException | InvalidCurrencyException |
                 NotAllowedToTransferToTheSameBankAccountException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(nikic);
////
////
//        System.out.println(mirkic.allTransactions());
        System.out.println(mirkic);
//
//        mirkic.prepareBankStatement(LocalDateTime.of(2002,2,1, 12,12,12), LocalDateTime.of(2029,3,1,12,12,12));
//        nikic.prepareBankStatement(LocalDateTime.of(2002, 2, 1, 12, 12, 12), LocalDateTime.of(2024, 3, 1, 12, 12, 12));
//        System.out.println(nikic.allTransactions());
//        System.out.println(mirkic.allTransactions());

    }
}
