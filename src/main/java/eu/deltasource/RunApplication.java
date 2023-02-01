package eu.deltasource;

import eu.deltasource.exceptions.InvalidCurrencyException;
import eu.deltasource.exceptions.NotAllowedToTransferToTheSameBankAccountException;
import eu.deltasource.exceptions.TransfersAllowedBetweenCurrentAccountsException;

import java.time.LocalDate;

public class RunApplication {
    public void run() {

        Owner niki4 = new Owner("nikic", "Go5ov");
        Owner mirki4 = new Owner("mirki4", "Go5sov");

        BankInstitution dsk = new BankInstitution("dsk", "sdf");
        BankInstitution raifaizen = new BankInstitution("raifaizen", "sdfewr");

        BankAccount nikic = new BankAccount(niki4, "02", dsk, "frewer", "GBP", 30, "CurrentAccount");

        BankAccount mirkic = new BankAccount(mirki4, "022", raifaizen, "freweer", "USD", 30, "CurrentAccount");

        BankService bankService = new BankService();
        try {
//            raifaizen.withdrawMoneyFromAccount(mirkic, 10, LocalDate.of(2022, 2, 1));
//            dsk.depositMoneyToAccount(nikic, 500, LocalDate.of(2023, 2, 1));
//            dsk.transferMoney(nikic, mirkic, 10, LocalDate.of(2023, 2, 1));
            bankService.depositMoneyToAccount(nikic, 2, LocalDate.of(2003, 2,1));
            bankService.withdrawMoneyFromAccount(mirkic, 10, LocalDate.of(2003, 2,1));
            bankService.transferMoney(nikic, mirkic, 10, LocalDate.of(2023, 2,1));

        } catch (TransfersAllowedBetweenCurrentAccountsException | InvalidCurrencyException |
                 NotAllowedToTransferToTheSameBankAccountException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(nikic.allTransactions());
        System.out.println(nikic);


        System.out.println(mirkic.allTransactions());
        System.out.println(mirkic);

    }
}
