package eu.deltasource;

import eu.deltasource.exception.InvalidCurrencyException;
import eu.deltasource.exception.NotAllowedToTransferToTheSameBankAccountException;
import eu.deltasource.exception.TransfersAllowedBetweenCurrentAccountsException;
import eu.deltasource.model.BankAccount;
import eu.deltasource.model.BankInstitution;
import eu.deltasource.model.Owner;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RunApplication {
    public void run() {

        Owner niki4 = new Owner("nikic", "Go5ov");
        Owner mirki4 = new Owner("mirki4", "Go5sov");

        BankInstitution dsk = new BankInstitution("dsk", "sdf");
        BankInstitution raifaizen = new BankInstitution("raifaizen", "sdfewr");

        BankAccount nikic = new BankAccount(niki4, "02", dsk, "frewer", "BGN", 30, "CurrentAccount", "SavingsAccount");

        BankAccount mirkic = new BankAccount(mirki4, "022", raifaizen, "freweer", "BGN", 30, "CurrentAccount");

        BankService bankService = new BankService();
        try {
            bankService.depositMoneyToAccount(mirkic, 2, LocalDateTime.of(2026, 3, 2, 16, 15));
            bankService.withdrawMoneyFromAccount(mirkic, 10, LocalDateTime.of(2025, 3, 2, 15, 25, 22));
            bankService.depositMoneyToAccount(nikic, 2, LocalDateTime.of(2027, 3, 2, 19, 18, 9));
            bankService.transferMoney(nikic, mirkic, 10, LocalDateTime.of(2028, 3, 2, 19, 18, 9));

        } catch (TransfersAllowedBetweenCurrentAccountsException | InvalidCurrencyException |
                 NotAllowedToTransferToTheSameBankAccountException e) {
            System.out.println(e.getMessage());
        }

//        System.out.println(nikic);
////
////
//        System.out.println(mirkic.allTransactions());
//        System.out.println(mirkic);
//
//        mirkic.prepareBankStatement(LocalDateTime.of(2002,2,1, 12,12,12), LocalDateTime.of(2024,3,1,12,12,12));
        nikic.prepareBankStatement(LocalDateTime.of(2002, 2, 1, 12, 12, 12), LocalDateTime.of(2024, 3, 1, 12, 12, 12));
//        System.out.println(nikic.allTransactions());
//        System.out.println(mirkic.allTransactions());

    }
}
