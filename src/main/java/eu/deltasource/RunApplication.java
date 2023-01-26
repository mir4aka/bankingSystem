package eu.deltasource;

public class RunApplication {
    public void run() {

        Owner niki4 = new Owner("nikic", "Go5ov");
        Owner mirki4 = new Owner("mirki4", "Go5sov");

        BankInstitution dsk = new BankInstitution("dsk", "sdf");
        BankInstitution raifaizen = new BankInstitution("raifaizen", "sdfewr");

        BankAccount nikic = new BankAccount(niki4, "02", dsk, "frewer", "GBP", 303222, "CurrentAccount");

        BankAccount mirkic = new BankAccount(mirki4, "023", raifaizen, "frew", "USD", 33, "CurrentAccount");

        try {
            dsk.transferMoney(nikic, mirkic, 10);
            dsk.transferMoney(nikic, mirkic, 1220);
            dsk.transferMoney(nikic, mirkic, 104);
            dsk.transferMoney(nikic, mirkic, 107);
            dsk.transferMoney(nikic, mirkic, 10);
            dsk.transferMoney(nikic, mirkic, 130);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(nikic.allTransactions());
        System.out.println(mirkic.allTransactions());


    }
}
