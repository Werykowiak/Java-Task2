package demo.task1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountRepositoryTest {

    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        accountRepository = new AccountRepositoryImpl();
    }

    @Test
    void test_createAccount() {
        Account account = accountRepository.create("x","y", BigDecimal.ZERO);
        assert account != null;
        assert account.getId() != null;
    }
    @Test
    void test_createAccount_all_null() {
        Account account = accountRepository.create(null,null, null);
        assert account != null;
        assert account.getId() != null;
    }
    @Test
    void test_saveAccount() {
        Account account = accountRepository.create("x","y", BigDecimal.ZERO);
        account.setName("XX");
        account.setAddress("YY");
        account.setBalance(new BigDecimal("100"));
        accountRepository.save(account);
        Optional<Account> foundAccount = accountRepository.findById(account.getId());
        assert foundAccount.isPresent();
        assert foundAccount.get().getId() == account.getId();
        assert foundAccount.get().getName() == account.getName();
        assert foundAccount.get().getAddress() == account.getAddress();
        assert foundAccount.get().getBalance() == account.getBalance();
    }
    @Test
    void test_saveAccount_null_account() {
        assertThrows(IllegalArgumentException.class, () -> {
            accountRepository.save(null);
        });
    }

    @Test
    void test_saveAccount_no_account_in_repository() {
        Account account = new Account(null,"x","y", BigDecimal.ZERO);
        assertThrows(IllegalArgumentException.class, () -> {
            accountRepository.save(account);
        });
    }
    @Test
    void test_findById_null_id() {
        assertThrows(IllegalArgumentException.class, () -> {
            accountRepository.findById(null);
        });
    }
    @Test
    void test_findById_no_account_in_repository() {
        Optional<Account> account = accountRepository.findById(1L);
        assert account.isEmpty();
    }
    @Test
    void test_findById(){
        Account account = accountRepository.create("x","y", BigDecimal.ZERO);
        Optional<Account> foundAccount = accountRepository.findById(account.getId());
        assert foundAccount.isPresent();
        assert foundAccount.get().getId() == account.getId();
        assert foundAccount.get().getName() == account.getName();
        assert foundAccount.get().getAddress() == account.getAddress();
        assert foundAccount.get().getBalance() == account.getBalance();
    }
    @Test
    void test_findByNameAndAddress() {
        Account account = accountRepository.create("x","y", BigDecimal.ZERO);
        Optional<Account> foundAccount = accountRepository.findByNameAndAddress("x","y");
        assert foundAccount.isPresent();
        assert foundAccount.get().getId() == account.getId();
        assert foundAccount.get().getName() == account.getName();
        assert foundAccount.get().getAddress() == account.getAddress();
        assert foundAccount.get().getBalance() == account.getBalance();
    }
    @Test
    void test_findByNameAndAddress_no_account_in_repository() {
        Optional<Account> foundAccount = accountRepository.findByNameAndAddress("x","y");
        assert foundAccount.isEmpty();
    }
    @Test
    void test_findByNameAndAddress_no_account() {
        Account account = accountRepository.create("x","y", BigDecimal.ZERO);
        Optional<Account> foundAccount = accountRepository.findByNameAndAddress("xo","yo");
        assert foundAccount.isEmpty();
    }
    @Test
    void test_findByNameAndAddress_partial_correct() {
        Account account = accountRepository.create("x","y", BigDecimal.ZERO);
        Optional<Account> foundAccount = accountRepository.findByNameAndAddress("x","z");
        assert foundAccount.isEmpty();
    }
}
