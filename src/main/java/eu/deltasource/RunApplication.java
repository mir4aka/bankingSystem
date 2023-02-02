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

        BankAccount nikic = new BankAccount(niki4, "02", dsk, "frewer", "BGN", 30, "CurrentAccount");

        BankAccount mirkic = new BankAccount(mirki4, "022", dsk, "freweer", "BGN", 30, "SavingsAccount");

        BankService bankService = new BankService();
        try {
//            raifaizen.withdrawMoneyFromAccount(mirkic, 10, LocalDate.of(2022, 2, 1));
//            dsk.depositMoneyToAccount(nikic, 500, LocalDate.of(2023, 2, 1));
//            dsk.transferMoney(nikic, mirkic, 10, LocalDate.of(2023, 2, 1));
//            bankService.depositMoneyToAccount(mirkic, 2, LocalDateTime.of(2023, 2,2, 16,15));
//            bankService.withdrawMoneyFromAccount(mirkic, 10, LocalDateTime.of(2023, 2,2, 15, 25, 22));
//            bankService.depositMoneyToAccount(nikic, 2);
//            bankService.transferMoney(nikic, mirkic, 10, LocalDateTime.of(2023, 3,2, 19,18,9));

        } catch (TransfersAllowedBetweenCurrentAccountsException | InvalidCurrencyException |
                 NotAllowedToTransferToTheSameBankAccountException e) {
            System.out.println(e.getMessage());
        }

//        System.out.println(mirkic.getBankName());

        System.out.println(nikic);
//
//
//        System.out.println(mirkic.allTransactions());
        System.out.println(mirkic);

//        mirkic.prepareBankStatement(LocalDateTime.of(2002,2,1), LocalDateTime.of(2004,3,1));
//        nikic.prepareBankStatement(LocalDateTime.of(2002,2,1), LocalDateTime.of(2004,3,1));
//        System.out.println(nikic.allTransactions());
//        System.out.println(mirkic.allTransactions());

    }
}
