package demo.task1;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 */
public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private static Bank _bank;

    public static void bankInitialize() {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        _bank = new BankImpl(accountRepository);
        LOGGER.info("Bank initialized");
    }

    public static void main(String[] args) {
        bankInitialize();
        System.out.println("Bank");

        while(true) {
            System.out.println("Wybierz opcje: \n" +
                    "1 - stwórz konto \n2 - znajdź konto \n" +
                    "3 - wpłać środki \n4 - wyswietl stan konta \n" +
                    "5 - wypłać środki \n6 - przelej środki \n7 - wyjdź");
            int choice;
            Long id;
            String name, address;

            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            switch (choice){
                case 1:
                    System.out.println("Podaj imie");
                    name = scanner.next();
                    System.out.println("Podaj adres");
                    address = scanner.next();
                    id = _bank.createAccount(name, address);
                    System.out.println("Stworzono konto: "+ id);
                    LOGGER.fine("Account " +"[Name: " + name +", Address: "+address + "] created");
                    break;
                case 2:
                    System.out.println("Podaj imie");
                    name = scanner.next();
                    System.out.println("Podaj adres");
                    address = scanner.next();
                    id = _bank.findAccount(name, address);
                    if (id == null) {
                        System.out.println("Nie znaleziono konta");
                        LOGGER.fine("Account " +"[Name: " + name +", Address: "+address + "] not found");
                    }else{
                        System.out.println("Znaleziono konto ID: "+ id);
                        LOGGER.fine("Account " +"[Name: " + name +", Address: "+address + "] found");
                    }
                    break;
                case 3:
                    System.out.println("Podaj ID");
                    id = scanner.nextLong();
                    System.out.println("Podaj kwotę");
                    BigDecimal amount = new BigDecimal(scanner.next());
                    try{
                        _bank.deposit(id, amount);
                        System.out.println("Pomyślnie wpłacono środki");
                        LOGGER.fine("Deposit successful ID: "+id+", amount: "+amount);
                    }catch(Bank.AccountIdException e){
                        System.out.println("Brak konta o ID: " + id);
                        LOGGER.severe("Deposit failed, no ID: " + id);
                    }
                    break;
                case 4:
                    System.out.println("Podaj ID");
                    id = scanner.nextLong();
                    try{
                        BigDecimal balance = _bank.getBalance(id);
                        System.out.println("Stan konta wynosi: " + balance);
                        LOGGER.fine("Get balance successful ID: "+id+", balance: "+balance);
                    }catch(Bank.AccountIdException e){
                        System.out.println("Brak konta o ID: " + id);
                        LOGGER.severe("Get balance failed, no ID: " + id);
                    }
                    break;
                case 5:
                    System.out.println("Podaj ID");
                    id = scanner.nextLong();
                    System.out.println("Podaj kwotę");
                    BigDecimal withdrawAmount = new BigDecimal(scanner.next());
                    try{
                        _bank.withdraw(id, withdrawAmount);
                        System.out.println("Pomyślnie wypłacono środki");
                        LOGGER.fine("Withdraw successful ID: "+id+", amount: "+withdrawAmount);
                    }catch(Exception e){
                        if(e instanceof Bank.AccountIdException){
                            System.out.println("Brak konta o ID: " + id);
                            LOGGER.severe("Withdraw failed, no ID: " + id);
                        } else if (e instanceof  Bank.InsufficientFundsException) {
                            System.out.println("Brak wystarczających środkó na koncie o ID: " + id);
                            LOGGER.severe("Withdraw failed, Insufficient funds on account ID: " + id);
                        }
                    }
                    break;
                case 6:
                    Long sourceId, destinationId;
                    System.out.println("Podaj ID konta źródłowego");
                    sourceId = scanner.nextLong();
                    System.out.println("Podaj ID konta docelowego");
                    destinationId = scanner.nextLong();
                    BigDecimal transferAmount = new BigDecimal(scanner.next());
                    try {
                        _bank.transfer(sourceId, destinationId, transferAmount);
                        System.out.println("Pomyślnie przelano środki");
                        LOGGER.fine("Transfer successful source ID: "+sourceId+", destination ID: "+ destinationId+", amount: "+transferAmount);
                    }catch (Exception e){
                        if(e instanceof Bank.AccountIdException){
                            System.out.println("Jedno z kont nie istnieje");
                            LOGGER.severe("Transfer failed, no ID");
                        }else if (e instanceof  Bank.InsufficientFundsException) {
                            System.out.println("Brak wystarczających środkó na koncie o ID: " + sourceId);
                            LOGGER.severe("Transfer failed, Insufficient funds on account ID: " + sourceId);
                        }
                    }
                    break;
                case 7:
                    return;
            }
        }
    }


}
