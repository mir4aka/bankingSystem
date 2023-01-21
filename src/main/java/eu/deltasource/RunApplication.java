package eu.deltasource;

public class RunApplication {
    public void run() {

        Owner niki4 = new Owner("nikic", "Go5ov");
        Owner mirki4 = new Owner("mirki4", "Go5sov");

        BankInstitution dsk = new BankInstitution("dsk", "sdf");

        BankAccount nikic = new BankAccount(niki4, "02", dsk, "frew", "GBP", 30, "CurrentAccount");

        BankAccount mirkic = new BankAccount(mirki4, "02", dsk, "frewe", "USD", 33, "CurrentAccount");

        try {
            dsk.transferMoney(nikic, mirkic, 10);
            dsk.transferMoney(nikic, mirkic, 10);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(nikic.getBankStatements());




    }
}
