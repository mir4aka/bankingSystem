package eu.deltasource;

public class RunApplication {
    public void run() {
        BankInstitution dsk = new BankInstitution("DSK", "kiril i metodi 20", 2);
        BankInstitution fibank = new BankInstitution("fibank", "dvaise kilograma 2", 20);

        Owner owner = new Owner("Ivan", "Ivanov", "20");
        Owner owner2 = new Owner("Ema", "Ivanova", "25");
        Owner ownerStefan = new Owner("Stefan", "Go5ov", "02");

        BankAccount bankAccount = new BankAccount(owner, dsk, "2035", "LV", 20.0, "CurrentAccount");
        BankAccount bankAccount2 = new BankAccount(owner2, fibank, "20356","USD", 2.0, "CurrentAccount");
        BankAccount bankAccount3 = new BankAccount(ownerStefan, fibank, "537849", "LV", 250, "CurrentAccount");

        try {
//            bankAccount.depositMoneyToAccount(1500);
//            bankAccount.depositMoneyToAccount(1500);
//            bankAccount.withdrawMoneyFromAccount(1500);
//
//            bankAccount2.depositMoneyToAccount(1000);
//            bankAccount2.depositMoneyToAccount(225);
//            bankAccount2.depositMoneyToAccount(175);
//            bankAccount2.withdrawMoneyFromAccount(400);
//
//            bankAccount3.depositMoneyToAccount(200);
//            bankAccount3.depositMoneyToAccount( 300);
//            bankAccount3.withdrawMoneyFromAccount(750);

            bankAccount.transferMoney(bankAccount2, 5);
            bankAccount.transferMoney(bankAccount2, 5);

//            bankAccount3.withdrawMoneyFromAccount(20);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(dsk);
        System.out.println(fibank);

        System.out.println(bankAccount.allTransactions());
        System.out.println(bankAccount2.allTransactions());
        System.out.println(bankAccount3.allTransactions());

        System.out.println(bankAccount);
        System.out.println(bankAccount2);
        System.out.println(bankAccount3);
    }
}
