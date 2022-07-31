import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;
//摆烂了，main函数强行压到60行

public class MainClass {
    public static void main(String[] args) {
        int advId;
        ArrayList<Adventurer> adventurers = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        int m = input.nextInt();
        for (int i = 0; i < m; i++) { //m个操作
            int op = input.nextInt();
            advId = input.nextInt();
            if (op == 1) {
                Adventurer newPerson = new Adventurer();
                newPerson.setId(advId);
                newPerson.setName(input.next());
                newPerson.setBottles(new ArrayList<>());
                newPerson.setTotalPrice(new BigInteger("0"));
                adventurers.add(newPerson);
            }
            else if (op == 2) {
                for (Adventurer item : adventurers) {
                    if (item.getId() == advId) {
                        //先加瓶子
                        Bottle newBottle = new Bottle();
                        newBottle.setId(input.nextInt());
                        newBottle.setName(input.next());
                        newBottle.setPrice(input.nextLong());
                        newBottle.setCapacity(input.nextDouble());
                        item.getBottles().add(newBottle);
                        //然后维护价值总和和最大值
                        BigInteger priceOfNewBottle;//新瓶子的价值
                        priceOfNewBottle = new BigInteger(String.valueOf(newBottle.getPrice()));
                        item.setTotalPrice(item.getTotalPrice().add(priceOfNewBottle));
                        if (newBottle.getPrice() > item.getMaxPrice()) {
                            item.setMaxPrice(newBottle.getPrice());
                        }
                        break;
                    }
                }
            }
            else if (op == 3) {
                int botId = input.nextInt();
                for (Adventurer item : adventurers) {
                    if (item.getId() == advId) { // 找冒险者
                        for (Bottle theBottle : item.getBottles()) { // 找那个要删掉的bottle
                            if (theBottle.getId() == botId) {
                                BigInteger priceOfTheBottle;
                                String theBottlesPrice = String.valueOf(theBottle.getPrice());
                                priceOfTheBottle = new BigInteger(theBottlesPrice);// 脑瘫的风格检查
                                item.setTotalPrice(item.getTotalPrice().subtract(priceOfTheBottle));
                                item.getBottles().remove(theBottle);
                                item.findMaxPrice();
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            else if (op == 4 || op == 5) {
                for (Adventurer j : adventurers) {
                    if (j.getId() == advId && op == 4) { System.out.println(j.getTotalPrice()); }
                    if (j.getId() == advId && op == 5) { System.out.println(j.getMaxPrice()); }
                }
            }
        }
    }
}

/*本来要放在第二个main那里
                        ArrayList<Bottle> newBottles = item.getBottles();
                        System.out.println("之前列表中有" + item.getBottles().size() + "个");
                        newBottles.add(newBottle);
                        item.setBottles(newBottles);
                        System.out.println("之后列表中有" + item.getBottles().size() + "个");
                        System.out.println("那个newBottles列表中有" + newBottles.size() + "个");*/
// 使用以上的方法主要是为了删除。（遍历+删除+可能要的set）
// 也可以试试不用这种方法来删除，看看能不能查下标然后删除（感觉一样？不知道有没有相应的函数来用）