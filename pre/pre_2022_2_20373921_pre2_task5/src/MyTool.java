import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class MyTool {
    
    public Adventurer addAdventurer(Scanner scanner, int advId) {
        Adventurer newAdventurer = new Adventurer();
        newAdventurer.setId(advId);
        newAdventurer.setName(scanner.next());
        return newAdventurer;
    }
    
    public ValuableStuff addEquip(Scanner scanner) {
        int equipType = scanner.nextInt();
        ValuableStuff newValuableStuff;
        
        int newId = scanner.nextInt();
        String newName = scanner.next();
        final BigInteger newPrice = new BigInteger(String.valueOf(scanner.nextLong()));
        
        switch (equipType) {
            case 1:
                newValuableStuff = new Bottle(scanner.nextDouble());
                break;
            case 2:
                newValuableStuff = new HealingPotion(scanner.nextDouble(), scanner.nextDouble());
                break;
            case 3:
                newValuableStuff = new ExpBottle(scanner.nextDouble(), scanner.nextDouble());
                break;
            case 4:
                newValuableStuff = new Sword(scanner.nextDouble());
                break;
            case 5:
                newValuableStuff = new RareSword(scanner.nextDouble(), scanner.nextDouble());
                break;
            default:
                newValuableStuff = new EpicSword(scanner.nextDouble(), scanner.nextDouble());
        }
        
        newValuableStuff.setId(newId);
        newValuableStuff.setName(newName);
        newValuableStuff.setPrice(newPrice);
        
        return newValuableStuff;
    }
    
    public Adventurer findTheAdventurer(ArrayList<Adventurer> adventurers, int advId) {
        for (Adventurer adv : adventurers) {
            if (adv.getId() == advId) {
                return adv;
            }
        }
        return null;
    }
}
