import java.util.ArrayList;
import java.util.Scanner;
//import java.io.File;
//import java.io.PrintStream;

public class MainClass {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int m = scanner.nextInt();
        ArrayList<Adventurer> adventurers = new ArrayList<>();
        MyTool myTool = new MyTool();
        
        while (m-- > 0) {
            int type = scanner.nextInt();
            int advId = scanner.nextInt();
            Adventurer theAdventurer;
            switch (type) {
                case 1:
                    adventurers.add(myTool.addAdventurer(scanner, advId));
                    break;
                case 2:
                    theAdventurer = myTool.findTheAdventurer(adventurers, advId);
                    theAdventurer.getValuableStuffs().add(myTool.addEquip(scanner));
                    break;
                case 3:
                    theAdventurer = myTool.findTheAdventurer(adventurers, advId);
                    theAdventurer.deleteValuableStuff(scanner.nextInt());
                    break;
                case 4:
                    theAdventurer = myTool.findTheAdventurer(adventurers, advId);
                    System.out.println(theAdventurer.calPrice());
                    break;
                case 5:
                    theAdventurer = myTool.findTheAdventurer(adventurers, advId);
                    System.out.println(theAdventurer.findMaxPrice());
                    break;
                case 6:
                    theAdventurer = myTool.findTheAdventurer(adventurers, advId);
                    System.out.println(theAdventurer.getValuableStuffs().size());
                    break;
                case 7:
                    theAdventurer = myTool.findTheAdventurer(adventurers, advId);
                    theAdventurer.printTheValuableStuff(scanner.nextInt());
                    break;
                case 8:
                    theAdventurer = myTool.findTheAdventurer(adventurers, advId);
                    theAdventurer.useIt(theAdventurer);
                    break;
                case 9:
                    theAdventurer = myTool.findTheAdventurer(adventurers, advId);
                    theAdventurer.printInf();
                    break;
                default: //就是10
                    theAdventurer = myTool.findTheAdventurer(adventurers, advId);
                    Adventurer hiredAdv = myTool.findTheAdventurer(adventurers, scanner.nextInt());
                    theAdventurer.getValuableStuffs().add(hiredAdv);
                    // todo 能加吗? 引用是子类不是 arraylist 直接相关的类型
            }
        }
    }
}
