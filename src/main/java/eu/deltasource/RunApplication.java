package eu.deltasource;

public class RunApplication {
    public void run() {

        Owner niki4 = new Owner("nikic", "Go5ov");
        Owner mirki4 = new Owner("mirki4", "Go5sov");

        BankInstitution dsk = new BankInstitution("dsk", "sdf");

        BankAccount nikic = new BankAccount(niki4, "02", dsk, "frew", "BGN", 30, "CurrentAccount");

        BankAccount mirkic = new BankAccount(mirki4, "012", dsk, "frewe", "USD", 33, "CurrentAccount");

        try {
            dsk.transferMoney(nikic, mirkic, 2);
            dsk.transferMoney(nikic, mirkic, 10);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(niki4);
        System.out.println(mirki4);

        System.out.println(dsk);

        System.out.println(nikic);
        System.out.println(mirkic);

        System.out.println(nikic.allTransactions());
        System.out.println(mirkic.allTransactions());




    }
}
