package bag.equip;

import java.util.Scanner;

public class ToolForNewObj {
    public Equip newObj(int equType, Scanner input) {
        Equip obj;
        switch (equType) { //这里只是为了生成出不同的实例对象
            case 1 :
                obj = new Bottle();
                break;
            case 2 :
                obj = new HealingPotion();
                break;
            case 3 :
                obj = new ExpBottle();
                break;
            case 4 :
                obj = new Sword();
                break;
            case 5 :
                obj = new RareSword();
                break;
            default : // 就是6时
                obj = new EpicSword();
        }
        obj.resetVal(equType, input); // 因为用的是方法重写，所以在使用resetVal这个函数时一定是相应的子类的
        return obj;
    }
}