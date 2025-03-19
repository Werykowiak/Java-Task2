package demo.task1;

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
            System.out.println("Wybierz opcje: \n1 - stwórz konto \n2 - znajdź konto \n3 - wpłać środki \n4 - wyswietl stan konta \n5 - wypłać środki \n6 - przelej środki \n7 - wyjdź");
            int choice;
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            switch (choice){
                case 1:
                    String name, address;
                    System.out.println("Podaj imie");
                    name = scanner.next();
                    System.out.println("Podaj adres");
                    address = scanner.next();
                    _bank.createAccount(name, address);
                    LOGGER.fine("Account created");
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    return;
            }
        }
    }


}
