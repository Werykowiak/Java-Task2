package demo.task1;

import java.math.BigDecimal;
import java.util.Optional;

public class BankImpl implements Bank {

    private AccountRepository accountRepository;

    public BankImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Long createAccount(String name, String address) {
        Optional<Account> account = accountRepository.findByNameAndAddress(name, address);
        if (account.isPresent()) { return account.get().getId(); }
        return accountRepository.create(name,address,BigDecimal.ZERO).getId();
    }

    @Override
    public Long findAccount(String name, String address) {
        Optional<Account> account = accountRepository.findByNameAndAddress(name, address);
        if (account.isPresent()) return account.get().getId();
        return null;
    }

    @Override
    public void deposit(Long id, BigDecimal amount) {
        Optional<Account> account = accountRepository.findById(id);
        if(account.isEmpty()) throw new AccountIdException();
        account.get().setBalance(account.get().getBalance().add(amount));
    }

    @Override
    public BigDecimal getBalance(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if(account.isEmpty()) throw new AccountIdException();
        return account.get().getBalance();
    }

    @Override
    public void withdraw(Long id, BigDecimal amount) {
        Optional<Account> account = accountRepository.findById(id);
        if(account.isEmpty()) throw new AccountIdException();
        if(account.get().getBalance().compareTo(amount) < 0) throw new InsufficientFundsException();
        account.get().setBalance(account.get().getBalance().subtract(amount));
    }

    @Override
    public void transfer(Long idSource, Long idDestination, BigDecimal amount) {
        try {
            withdraw(idSource, amount);
        }catch (Exception e) {
            throw e;
        }
        try{
            deposit(idDestination, amount);
        }catch (Exception e) {
            deposit(idSource, amount);
            throw e;
        }
    }
}
