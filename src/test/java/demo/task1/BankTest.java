package demo.task1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BankTest {
    private BankImpl bank;

    @BeforeEach
    void setup() {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        bank = new BankImpl(accountRepository);
    }

    @Test
    void test_create_account() {
        Long createdId = bank.createAccount("John", "Doe");
        assert createdId != null;
        assert bank.getBalance(createdId) == BigDecimal.ZERO;
        Long newCreatedId = bank.createAccount("John", "Doe");
        assert newCreatedId != null;
        assert createdId.equals(newCreatedId);
    }
    @Test
    void test_findAccount() {
        Long createdId = bank.createAccount("John", "Doe");
        assert createdId != null;
        Long foundId = bank.findAccount("John", "Doe");
        assert foundId != null;
        assert createdId.equals(foundId);
    }
    @Test
    void test_findAccount_not_found() {
        Long foundId = bank.findAccount("John", "Doe");
        assert foundId == null;
    }
    @Test
    void test_deposit() {
        Long createdId = bank.createAccount("John", "Doe");
        bank.deposit(createdId, BigDecimal.TEN);
        assert bank.getBalance(createdId).compareTo(BigDecimal.TEN) == 0;
    }
    @Test
    void test_deposit_not_found() {
        assertThrows(Bank.AccountIdException.class,()->{
            bank.deposit(1L,BigDecimal.TEN);
        });
    }
    @Test
    void test_getBalance() {
        Long createdId = bank.createAccount("John", "Doe");
        assert bank.getBalance(createdId) != null;
    }
    @Test
    void test_getBalance_not_found() {
        assertThrows(Bank.AccountIdException.class,()->{
            bank.getBalance(1L);
        });
    }
    @Test
    void test_withdraw() {
        Long createdId = bank.createAccount("John", "Doe");
        bank.deposit(createdId, BigDecimal.TEN);
        bank.withdraw(createdId, BigDecimal.TEN);
        assert bank.getBalance(createdId) == BigDecimal.ZERO;
    }
    @Test
    void test_withdraw_not_found() {
        assertThrows(Bank.AccountIdException.class,()->{
            bank.withdraw(1L,BigDecimal.TEN);
        });
    }
    @Test
    void test_withdraw_no_funds() {
        Long createdId = bank.createAccount("John", "Doe");
        assertThrows(Bank.InsufficientFundsException.class,()->{
            bank.withdraw(createdId,BigDecimal.TEN);
        });
    }
    @Test
    void  test_transfer() {
        Long createdId1 = bank.createAccount("John", "Doe");
        Long createdId2 = bank.createAccount("John2", "Doe2");
        bank.deposit(createdId1, BigDecimal.TEN);
        bank.transfer(createdId1, createdId2, BigDecimal.TEN);
        assert bank.getBalance(createdId1) == BigDecimal.ZERO;
        assert bank.getBalance(createdId2) == BigDecimal.TEN;
    }
    @Test
    void test_transfer_not_found() {
        assertThrows(Bank.AccountIdException.class,()->{
            bank.transfer(1L, 2L, BigDecimal.TEN);
        });
    }
    @Test
    void test_transfer_no_funds() {
        Long createdId1 = bank.createAccount("John", "Doe");
        Long createdId2 = bank.createAccount("John2", "Doe2");
        assertThrows(Bank.InsufficientFundsException.class,()->{
            bank.transfer(createdId1, createdId2, BigDecimal.TEN);
        });
    }
    @Test
    void test_transfer_one_exists() {
        Long createdId1 = bank.createAccount("John", "Doe");
        bank.deposit(createdId1, BigDecimal.TEN);
        assertThrows(Bank.AccountIdException.class,()->{
            bank.transfer(createdId1, 2L, BigDecimal.TEN);
        });
    }
}
