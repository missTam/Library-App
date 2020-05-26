
import view.UserMenu;

import java.util.Scanner;

public class Main {

    public static UserMenu menu = new UserMenu();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        menu.showUserMenu();
        try {
            if (!menu.performAction(Integer.parseInt(scanner.nextLine()))) {
                menu.failure();
                return;
            }
            menu.success();
            scanner.close();
        } catch (NumberFormatException e) {
            menu.invalidInput();
            menu.failure();
        }
    }
}
