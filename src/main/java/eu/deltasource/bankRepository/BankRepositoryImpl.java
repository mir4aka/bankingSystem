package eu.deltasource.bankRepository;

import eu.deltasource.model.BankAccount;
import eu.deltasource.model.BankInstitution;

import java.util.ArrayList;
import java.util.List;

public class BankRepositoryImpl implements BankRepository {

    private List<BankInstitution> banks = new ArrayList<>();

    @Override
    public void addBank(BankInstitution bankInstitution) {
        banks.add(bankInstitution);
    }

    /**
     * Finds bank in the bank repository by its name.
     *
     * @param name
     * @return
     */
    @Override
    public BankInstitution findBank(String name) {
        return banks.stream()
                .filter(b -> b.getBankName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean addBankAccountToBank(BankAccount bankAccount, BankInstitution bankInstitution) {
        return bankInstitution.getBankAccounts().add(bankAccount);
    }
}
