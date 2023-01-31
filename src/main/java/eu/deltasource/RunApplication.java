package eu.deltasource;

import java.time.LocalDate;

public class RunApplication {
    public void run() {

        Owner niki4 = new Owner("nikic", "Go5ov");
        Owner mirki4 = new Owner("mirki4", "Go5sov");

        BankInstitution dsk = new BankInstitution("dsk", "sdf");
        BankInstitution raifaizen = new BankInstitution("raifaizen", "sdfewr");

        BankAccount nikic = new BankAccount(niki4, "02", dsk, "frewer", "GBP", 30, "CurrentAccount");

        BankAccount mirkic = new BankAccount(mirki4, "023", raifaizen, "frew", "USD", 30, "CurrentAccount");

        try {
            dsk.transferMoney(nikic, mirkic, 10, LocalDate.of(2000, 10, 20));
            raifaizen.transferMoney(mirkic, nikic, 12, LocalDate.of(2005, 1, 1));
            dsk.transferMoney(nikic, mirkic, 4, LocalDate.of(1224, 11, 22));
//            dsk.transferMoney(nikic, mirkic, 107);
//            dsk.transferMoney(nikic, mirkic, 10);
//            dsk.transferMoney(nikic, mirkic, 130);
//            dsk.transferMoney(nikic, mirkic, 20);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(nikic.allTransactions());
//        System.out.println(mirkic.allTransactions());


    }
}
