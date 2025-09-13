import java.util.Scanner;
public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
    // customer form
        System.out.println("What's your full name?");
        String name = sc.nextLine();

        System.out.println("What's your login?");
        String login = sc.nextLine();

        System.out.println("What's your password?");
        String password = sc.nextLine();
    // object
         User user = new User(name, login, password);

         sc.close();


    }
}