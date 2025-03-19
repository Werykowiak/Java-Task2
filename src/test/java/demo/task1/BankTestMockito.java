package demo.task1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BankTestMockito {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BankImpl bank;

    @Test
    void test_create_account() {
        when(accountRepository.create("x","y", BigDecimal.ZERO)).thenReturn(new Account(1L,"x","y", BigDecimal.ZERO));
        when(accountRepository.findByNameAndAddress(anyString(),anyString())).thenReturn(Optional.empty());

        Long id = bank.createAccount("x","y");
        assert id != null;

        verify(accountRepository).create("x","y", BigDecimal.ZERO);
        verify(accountRepository).findByNameAndAddress(anyString(),anyString());
    }
    @Test
    void test_findAccount(){
        when(accountRepository.findByNameAndAddress(anyString(),anyString())).thenReturn(Optional.of(new Account(1L,"x","y", BigDecimal.ZERO)));

        Long foundId = bank.findAccount("X", "Y");
        assert foundId != null;

        verify(accountRepository,atLeastOnce()).findByNameAndAddress(anyString(),anyString());
    }
    @Test
    void test_findAccount_not_found() {
        when(accountRepository.findByNameAndAddress(anyString(),anyString())).thenReturn(Optional.empty());

        Long foundId = bank.findAccount("X", "Y");
        assert foundId == null;

        verify(accountRepository,atLeastOnce()).findByNameAndAddress(anyString(),anyString());
    }
    @Test
    void test_deposit() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new Account(1L,"x","y", BigDecimal.ZERO)));

        bank.deposit(1L, BigDecimal.TEN);
        assert bank.getBalance(1L).compareTo(BigDecimal.TEN) == 0;

        verify(accountRepository,atLeastOnce()).findById(1L);
    }
    @Test
    void test_deposit_not_found() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(Bank.AccountIdException.class,()->{
            bank.deposit(1L, BigDecimal.TEN);
        });
        verify(accountRepository,atLeastOnce()).findById(1L);
    }
    @Test
    void test_getBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new Account(1L,"x","y", BigDecimal.TEN)));

        assert bank.getBalance(1L) != null;
        assert bank.getBalance(1L).compareTo(BigDecimal.TEN) == 0;

        verify(accountRepository,atLeastOnce()).findById(1L);
    }
    @Test
    void test_getBalance_not_found() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(Bank.AccountIdException.class,()->{
            bank.getBalance(1L);
        });
        verify(accountRepository,atLeastOnce()).findById(1L);
    }
    @Test
    void test_withdraw() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new Account(1L,"x","y", BigDecimal.TEN)));

        bank.withdraw(1L, BigDecimal.TEN);
        assert bank.getBalance(1L) == BigDecimal.ZERO;

        verify(accountRepository,atLeastOnce()).findById(1L);
    }
    @Test
    void test_withdraw_not_found() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(Bank.AccountIdException.class,()->{
            bank.withdraw(1L,BigDecimal.TEN);
        });
        verify(accountRepository,atLeastOnce()).findById(1L);
    }
    @Test
    void test_withdraw_no_funds() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new Account(1L,"x","y", BigDecimal.ZERO)));

        assertThrows(Bank.InsufficientFundsException.class,()->{
            bank.withdraw(1L,BigDecimal.TEN);
        });

        verify(accountRepository,atLeastOnce()).findById(1L);
    }
    @Test
    void  test_transfer() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new Account(1L,"x","y", BigDecimal.TEN)));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(new Account(2L,"xx","yy", BigDecimal.ZERO)));

        bank.transfer(1L, 2L, BigDecimal.TEN);
        assert bank.getBalance(1L) == BigDecimal.ZERO;
        assert bank.getBalance(2L) == BigDecimal.TEN;

        verify(accountRepository,atLeastOnce()).findById(any());
    }
    @Test
    void test_transfer_not_found() {
        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(Bank.AccountIdException.class,()->{
            bank.transfer(1L, 2L, BigDecimal.TEN);
        });

        verify(accountRepository,atLeastOnce()).findById(any());
    }
    @Test
    void test_transfer_no_funds() {
        when(accountRepository.findById(any())).thenReturn(Optional.of(new Account(1L,"x","y", BigDecimal.ZERO)));


        assertThrows(Bank.InsufficientFundsException.class,()->{
            bank.transfer(1L, 2L, BigDecimal.TEN);
        });

        verify(accountRepository,atLeastOnce()).findById(any());
    }
    @Test
    void test_transfer_one_exist() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new Account(1L,"x","y", BigDecimal.TEN)));
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(Bank.AccountIdException.class,()->{
            bank.transfer(1L, 2L, BigDecimal.TEN);
        });

        verify(accountRepository,atLeastOnce()).findById(any());
    }

}
