package others;

import bag.equip.Equip;
import bag.equip.ToolForNewObj;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * 任务分析
 * 相比上一个任务，多了排序，使用装备，打印冒险者信息
 * <1>
 *  先排好序（对arraylist）
 * <2>
 *  在使用装备的时候，要注意异常抛出的问题
 *  写一个方法叫做use，其需要可以改变冒险者的状态 and 输出相应的信息 and 修改装备的状态
 * <3>
 *  冒险者信息是最好完成的
 *
 *  简单实现逻辑
 *  操作8：
 *    排序一次，然后使用
 *  操作9：
 *    打印状态
 *
 *  要加的：
 *  （1）装备增加use方法
 *  （2）adventure增加方法，叫useAllEquip()
 */

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
                    int equType = input.nextInt();
                    ToolForNewObj aaTool = new ToolForNewObj();
                    Equip newEquip = aaTool.newObj(equType, input);
                    for (Adventurer adv : adventurers) {
                        if (adv.getId() == advId) {
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
                else { // op == 4 5 6 7 8 9
                    for (Adventurer adv : adventurers) {
                        if (adv.getId() == advId) {
                            if (op == 4) {
                                adv.renewTotalPrice();
                                System.out.println(adv.getTotalPrice());
                            }
                            else if (op == 5) {
                                adv.renewMaxPrice();
                                System.out.println(adv.getMaxPrice());
                            }
                            else if (op == 6) { System.out.println(adv.calNumOfEquips()); }
                            else if (op == 7) {
                                int equId = input.nextInt();
                                for (Equip equItem : adv.getEquips()) {
                                    if (equItem.getId() == equId) {
                                        equItem.printInf();
                                    }
                                }
                            }
                            else if (op == 8) { adv.useAllEquip(); }
                            else if (op == 9) { adv.printAllInf(); }
                        }
                    }
                }
            }
        }
    }
}