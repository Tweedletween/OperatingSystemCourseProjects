/**
 * Created by miaoyanz on 10/24/16.
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class BankerDemo {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int num_of_clients;
        ArrayList<Integer> credits = new ArrayList<Integer>();
        ArrayList<Integer> max_needs = new ArrayList<Integer>();
        ArrayList<Integer> current_needs = new ArrayList<Integer>();
        int banker_bal;
        boolean safe = true;
        int num_of_completion = 0;
        FileWriter fw;
        try {
            fw = new FileWriter("out.txt");

            for (; ; ) {
                System.out.println("Enter no. of clients: ");
                if (!sc.hasNextInt()) {
                    System.out.println("No. of clients should only be integers!!!");
                    sc.next(); // discard
                    continue;
                }
                num_of_clients = sc.nextInt();
                if (num_of_clients > 0) {
                    try {
                        fw.write("Entered No of Clients: " + num_of_clients + "\n");
                    } catch (IOException e) {
                        System.out.println("couldnt write");
                    }
                    break;
                } else {
                    System.out.println("No. of clients should be > 0!!!");
                }
            }

            System.out.println("Enter each client's credit: ");
            for (int i = 0; i < num_of_clients; i++) {
                for (; ; ) {
                    if (!sc.hasNextInt()) {
                        System.out.println("Each client's credit should only be integers!!!");
                        sc.next(); // discard
                        credits.clear();
                        i = 0;
                        System.out.println("Enter each client's credit: ");
                        continue;
                    }
                    credits.add(sc.nextInt());
                    if (credits.get(i) >= 0) {
                        break;
                    } else {
                        System.out.println("Each client's credit should be >= 0!!!");
                        sc.next();
                        credits.clear();
                        i = 0;
                        System.out.println("Enter each client's credit: ");
                    }
                }
            }
            fw.write("The Clients' credits: " + credits.toString() + "\n");

            System.out.println("Enter each client's max need: ");
            for (int j = 0; j < num_of_clients; j++) {
                for (; ; ) {
                    if (!sc.hasNextInt()) {
                        System.out.println("Each client's max need should only be integers!!!");
                        sc.next(); // discard
                        max_needs.clear();
                        j = 0;
                        System.out.println("Enter each client's max need: ");
                        continue;
                    }
                    max_needs.add(sc.nextInt());
                    if (max_needs.get(j) >= 0) {
                        break;
                    } else {
                        System.out.println("Each client's max need should be >= 0!!!");
                        max_needs.clear();
                        j = 0;
                        System.out.println("Enter each client's max need: ");
                    }
                }
            }
            fw.write("The Clients' max need: " + max_needs.toString() + "\n");

            for (; ; ) {
                System.out.println("Enter Bank's Balance: ");
                if (!sc.hasNextInt()) {
                    System.out.println("Bank's Balance should only be integers!!!");
                    sc.next(); // discard
                    continue;
                }
                banker_bal = sc.nextInt();
                if (banker_bal >= 0) {
                    fw.write("Entered Bank's Balance:" + banker_bal  + "\n");
                    break;
                } else {
                    System.out.println("No. of Bank's Balance should be >= 0!!!");
                }
            }

            for (int k = 0; k < num_of_clients; k++) {
                current_needs.add(max_needs.get(k) - credits.get(k));
            }
            fw.write("The Clients' current need is: " + current_needs.toString() + "\n");

            boolean find;
            fw.write("Banker's Algorithm begins!!! \n");
            while (num_of_completion < num_of_clients && safe) {
                int it = 0;
                find = false;
                while (!find && it < num_of_clients) {
                    if (current_needs.get(it) != -1 && current_needs.get(it) <= banker_bal) {
                        banker_bal += credits.get(it);
                        //set to be -1 marks that the client has been satisfied, and not need to check again
                        current_needs.set(it, -1);
                        credits.set(it, 0);
                        num_of_completion++;
                        find = true;
                    }
                    it++;
                }
                if (!find) {
                    safe = false;
                }
                fw.write("The Clients' current need is:" + current_needs.toString() + "\n");
            }

            if (safe) {
                System.out.println("It's safe!");
                fw.write("It's safe! \n");
            } else {
                System.out.println("It's not safe!");
                fw.write("It's not safe! \n");
            }

            fw.close();
        }catch (IOException e) {
            System.out.println("Write output fail!!! \n");
        }
    }
}
