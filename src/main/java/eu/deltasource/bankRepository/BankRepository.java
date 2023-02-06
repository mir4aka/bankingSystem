package eu.deltasource.bankRepository;

import eu.deltasource.model.BankAccount;
import eu.deltasource.model.BankInstitution;

public interface BankRepository {
    void addBank(BankInstitution bankInstitution);

    BankInstitution findBank(String name);

    boolean addBankAccountToBank(BankAccount bankAccount, BankInstitution bankInstitution);
}
