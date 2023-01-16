package eu.deltasource;

public class RunApplication {
    public void run() {

        Owner niki4 = new Owner("nikic", "Go5ov", "0286786");
        Owner mirki4 = new Owner("mirki4", "Go5sov", "0286786");

        BankInstitution dsk = new BankInstitution("dsk", "sdf");

        BankAccount nikic = new BankAccount(niki4, dsk, "frew", "kef", 30, "CurrentAccount");

        BankAccount mirkic = new BankAccount(mirki4, dsk, "frewe", "kefe", 33, "CurrentAccount");

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




    }
}
