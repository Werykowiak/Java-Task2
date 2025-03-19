package demo.task1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository{
    private ArrayList<Account> accounts = new ArrayList<>();
    private long idgen;

    private Long nextId() {
        return idgen++;
    }

    @Override
    public Account create(String name, String address, BigDecimal balance) {
        Account a = new Account(nextId(), name, address, balance);
        accounts.add(a);
        return new Account(a);
    }

    @Override
    public void save(Account account) {
        if (account != null && account.getId() != null) {
            Account found = findById(account.getId()).orElseThrow(IllegalArgumentException::new);
            found.setName(account.getName());
            found.setAddress(account.getAddress());
            found.setBalance(account.getBalance());
        } else throw new IllegalArgumentException();
    }

    @Override
    public Optional<Account> findById(Long id) {
        if (id == null) throw new IllegalArgumentException();
        return accounts.stream()
                .filter(account -> id.equals(account.getId()))
                .findFirst();
    }

    @Override
    public Optional<Account> findByNameAndAddress(String name, String address) {
        return accounts.stream().filter(a->a.getName().equals(name) && a.getAddress().equals(address)).findFirst();
    }
}
