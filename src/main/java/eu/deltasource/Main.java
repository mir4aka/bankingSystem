package eu.deltasource;

public class Main {
    public static void main(String[] args) {

        BankInstitution dsk = new BankInstitution("DSK", "Unas", 2);
        BankInstitution fibank = new BankInstitution("fibank", "kg20", 20);

        Owner owner = new Owner("Ivan", "Ivanov", "20");
        Owner owner2 = new Owner("Ema", "Ivanova", "25");
        Owner ownerStefan = new Owner("Stefan", "the maikatiputkt", "02");

        BankAccount bankAccount = new BankAccount(owner, dsk, "2035", "LV", 20.0, "CurrentAccount");
        BankAccount bankAccount2 = new BankAccount(owner2, dsk, "20356","LV", 2.0, "SavingsAccount");
        BankAccount bankAccount3 = new BankAccount(ownerStefan, fibank, "537849", "LV", 250, "CurrentAccount");

        try {

//            bankAccount.depositMoneyToAccount(bankAccount, 1500);
//            bankAccount.depositMoneyToAccount(bankAccount, 1500);
//            bankAccount.withdrawMoneyFromAccount(bankAccount, 1500);

//            bankAccount2.depositMoneyToAccount(bankAccount2, 1000);
//            bankAccount2.depositMoneyToAccount(bankAccount2, 225);
//            bankAccount2.depositMoneyToAccount(bankAccount2, 175);
//            bankAccount2.withdrawMoneyFromAccount(bankAccount2, 400);

//            bankAccount3.depositMoneyToAccount(bankAccount3, 200);
//            bankAccount3.depositMoneyToAccount(bankAccount3, 300);
//            bankAccount3.withdrawMoneyFromAccount(bankAccount3, 750);

            bankAccount.transferMoney(bankAccount2, 5);



        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(bankAccount.allTransactions());
        System.out.println(bankAccount2.allTransactions());
        System.out.println(bankAccount3.allTransactions());

        System.out.println(bankAccount);
        System.out.println(bankAccount2);
        System.out.println(bankAccount3);


    }
}
