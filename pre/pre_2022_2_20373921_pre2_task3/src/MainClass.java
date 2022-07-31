import bag.equip.Equip;
import bag.equip.ToolForNewObj;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * 任务分析
 * 冒险者有装备，装备支持查询“价格之和”、“价格的最大值”、装备数量、打印所有装备的属性
 * 只需要支持添加装备和删除装备，无需实现对装备的修改
 * <p>
 * 1冒险者只有id和姓名
 * 2增加装备：想方设法在main函数中只需要一句话搞定它
 * <p>
 * 大体实现思路，新增一个类equip，是所有装备的父类，其支持新增各种子类装备
 * 建一个equip的ArrayList，然后根据输入的类来具体实现新建的类（写一个函数来实现，看看能不能写在父类的方法中，返回它）
 * 类型创建：  新增一个类，有一个方法，叫做newObj，可以实现在那里面建一个新的实例化对象，然后用方法resetValue新建值，然后返回引用
 * 以上就实现了操作2
 * <p>
 * 3删装备，在冒险者的方法中新增一个函数，叫deleteEquip(id)
 * <p>
 * 4冒险者需要始终维护一个和，新增时加，删除时减
 * <p>
 * 5写一个方法来实现这一步，就是简单的返回arraylist的size
 * <p>
 * 6这个虽然用的是equip的引用类型，但是实际调用方法的时候会调用真实的方法
 *
 */
/*
9
1 1 Person1
1 2 Person2
2 1 1 3 bottle1 10 5
2 1 6 4 sword1 20 7 0.6
2 2 3 5 bottle2 15 3 8
6 1
7 2 5
3 1 3
5 1
 */
// 第6步的话要好好看，理解重写和对象引用，所以父类中一定要有printInf() 和 resetVal()这两个方法
// 想想type的问题如何去解决，想把它放在工具类中来解决，它在初始化时就可以解决问题了，直接赋值
// 想想构造方法的继承，两个地方，一个是我想直接构造equip时就把type搞了，另一个是bottle的子类需要使用bottle的构造方法
public class MainClass {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<Adventurer> adventurers = new ArrayList<>();
        int m = input.nextInt();
        while (m-- > 0) {
            //System.out.println("A New Cycle");
            int op = input.nextInt();
            //System.out.println("OP=" + op);
            if (op == 1) {
                Adventurer newAdventurer = new Adventurer();
                newAdventurer.resetVal(input);
                adventurers.add(newAdventurer);
                //System.out.println(adventurers);
            }
            else {
                int advId = input.nextInt();
                if (op == 2) {
                    //System.out.println("I am here");
                    int equType = input.nextInt();
                    ToolForNewObj aaTool = new ToolForNewObj();
                    //System.out.println("EquipType = " + equType);
                    Equip newEquip = aaTool.newObj(equType, input);
                    //newEquip.printInf();
                    for (Adventurer adv : adventurers) {
                        if (adv.getId() == advId) {
                            //System.out.println("ID=" + adv.getId() + "   Name=" + adv.getName());
                            //adv.getEquips().add(newEquip);
                            adv.addEquip(newEquip);
                        }
                    }
                }
                else if (op == 3) {
                    int equId = input.nextInt();
                    for (Adventurer adv : adventurers) {
                        if (adv.getId() == advId) {
                            adv.deleteEquip(equId);
                        }
                    }
                }
                else { // op == 4 5 6 7
                    for (Adventurer adv : adventurers) {
                        if (adv.getId() == advId) {
                            if (op == 4) { System.out.println(adv.getTotalPrice()); }
                            else if (op == 5) { System.out.println(adv.getMaxPrice()); }
                            else if (op == 6) { System.out.println(adv.findNumOfEquips()); }
                            else if (op == 7) {
                                int equId = input.nextInt();
                                for (Equip equItem : adv.getEquips()) {
                                    if (equItem.getId() == equId) {
                                        equItem.printInf();
                                    }
                                }
                            }
                            else if (op == 8) {
                                for (Adventurer xx : adventurers) {
                                    System.out.println("ID = " + xx.getId());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}