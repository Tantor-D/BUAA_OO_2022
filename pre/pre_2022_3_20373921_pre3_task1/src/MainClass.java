import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        /*File inFile = new File("in1.txt");
        Scanner input = null;
        try {
            input = new Scanner(new File("in1.txt"));
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }*/
        
        while (input.hasNext()) {
            String inn = input.nextLine();
            String[] aa = inn.split(";");
            for (String ss : aa) {
                String toPrint = ss.trim();
                if (toPrint.isEmpty()) {
                    continue;
                }
                System.out.print(toPrint);
                if (toPrint.charAt(toPrint.length() - 1) != ';') {
                    System.out.println(";");
                } else {
                    System.out.print('\r');
                }
            }
        }
    }
}
