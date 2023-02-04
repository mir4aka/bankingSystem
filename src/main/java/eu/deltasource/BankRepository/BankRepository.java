package eu.deltasource.BankRepository;

import eu.deltasource.model.BankAccount;
import eu.deltasource.model.BankInstitution;

import java.util.Collection;

public interface BankRepository {
    void addBank(BankInstitution bankInstitution);

    BankInstitution findBank(String name);

    boolean addBankAccountToBank(BankAccount bankAccount, BankInstitution bankInstitution);
}
