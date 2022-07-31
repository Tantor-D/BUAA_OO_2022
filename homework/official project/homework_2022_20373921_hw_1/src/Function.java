import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Function {
    private ArrayList<FormalNum> formalNums; //  记得改构造函数，要让它指向一个地方
    
    public Function(String strNum) { // 从数字直接到一个常数Function
        formalNums = new ArrayList<>();
        formalNums.add(new FormalNum(strNum));
    }
    
    public Function() { // 无参构造器，默认是常数0
        formalNums = new ArrayList<>();
        formalNums.add(new FormalNum("0"));
    }
    
    public Function addFunc(Function func1) {
        // 相加就是全部垒到一起然后refine一下
        // 本身是会新建空间的
        // 不会影响参与运算的两个func的实例化空间
        Function newFunc = new Function();
        for (int i = 0; i < this.formalNums.size(); i++) {
            newFunc.formalNums.add(new FormalNum(this.formalNums.get(i).getAa(),
                    this.formalNums.get(i).getBb()));
        }
        
        for (int i = 0; i < func1.formalNums.size(); i++) {
            newFunc.formalNums.add(new FormalNum(func1.formalNums.get(i).getAa(),
                    func1.formalNums.get(i).getBb()));
        }
        
        newFunc.refineFunc();
        return newFunc;
    }
    
    public Function posFunc() {
        Function newFunc = new Function();
        for (int i = 0; i < this.formalNums.size(); i++) {
            newFunc.formalNums.add(new FormalNum(this.formalNums.get(i).getAa(),
                    this.formalNums.get(i).getBb()));
        }
        //newFunc.formalNums.addAll(this.formalNums);
        newFunc.refineFunc();
        return newFunc;
        
    }
    
    public Function negFunc() {
        Function newFunc = new Function();
        // newFunc.formalNums.addAll(this.formalNums);
        for (int i = 0; i < this.formalNums.size(); i++) {
            //newFunc.formalNums.add(this.formalNums.get(i));
            newFunc.formalNums.add(new FormalNum(this.formalNums.get(i).getAa(),
                    this.formalNums.get(i).getBb()));
        }
        for (FormalNum item : newFunc.formalNums) {
            item.setAa(item.getAa().multiply(new BigInteger("-1")));
        }
        newFunc.refineFunc();
        return newFunc;
    }
    
    public Function powFunc(int x) {
        Function newFunc = new Function();
        if (x == 0) { //特判为0
            newFunc.formalNums.add(new FormalNum(new BigInteger("1"), new BigInteger("0")));
            newFunc.refineFunc();
            return newFunc;
        }
        
        for (int i = 0; i < this.formalNums.size(); i++) {
            newFunc.formalNums.add(new FormalNum(this.formalNums.get(i).getAa(),
                    this.formalNums.get(i).getBb()));
        }
        for (int i = 1; i < x; i++) { // x-1次
            newFunc = newFunc.mulFunc(this);//乘完后会自动refine的
        }
        return newFunc;
    }
    
    public Function mulFunc(Function func1) {
        Function newFunc = new Function();
        this.refineFunc();
        func1.refineFunc();//这里加上是为了避免出现刚刚生成了一个新的Function，但是是0
        if (this.formalNums.isEmpty() || func1.formalNums.isEmpty()) { // 其中一个为0
            newFunc.refineFunc();
            return newFunc;
        }
        
        for (int i = 0; i < this.formalNums.size(); i++) {
            for (int j = 0; j < func1.formalNums.size(); j++) {
                FormalNum newFormalNum = new FormalNum();
                
                FormalNum num1 = this.getFormalNums().get(i);
                FormalNum num2 = func1.getFormalNums().get(j);
                
                newFormalNum.setAa(num1.getAa().multiply(num2.getAa()));
                newFormalNum.setBb(num1.getBb().add(num2.getBb()));
                newFunc.formalNums.add(newFormalNum);
            }
        }
        newFunc.refineFunc();
        return newFunc;
    }
    
    public void refineFunc() {
        //排序 累加 删除
        Collections.sort(this.formalNums);
        //累加
        for (int i = formalNums.size() - 1; i > 0; i--) {
            if (formalNums.get(i).getBb().equals(formalNums.get(i - 1).getBb())) {
                formalNums.get(i - 1).setAa(formalNums.get(i - 1).getAa().
                        add(formalNums.get(i).getAa()));//这是在加
            }
        }
        //删除 , 有两种情况：一个是指数相同，留最前面的那一个；另一个是系数为0
        for (int i = formalNums.size() - 1; i > 0; i--) {
            if (formalNums.get(i).getBb().equals(formalNums.get(i - 1).getBb())) {
                formalNums.remove(i);
            } else if (formalNums.get(i).getAa().equals(new BigInteger("0"))) {
                formalNums.remove(i);
            }
        }
        if (!formalNums.isEmpty() && formalNums.get(0).getAa().equals(new BigInteger("0"))) {
            formalNums.remove(0);//这样子保证了不会漏删，不会出现0* x^7，这种x高次但是系数为0的情况。但同时也可能会使列表为空
        }
        
    }
    
    public void printInf() {
        // 结果为0，特判
        if (formalNums.isEmpty()) {
            System.out.println(0);
            return;
        }
        BigInteger bigInteger0 = new BigInteger("0");
        
        for (int i = 0; i < formalNums.size(); i++) { // 把正数放到第一个
            if (formalNums.get(i).getAa().min(bigInteger0).equals(bigInteger0)) { //是正数
                if (i == 0) {
                    break;
                } else { // 开始操作
                    FormalNum item = new FormalNum(formalNums.get(i).getAa(),
                            formalNums.get(i).getBb());
                    formalNums.set(i,formalNums.get(0));
                    formalNums.set(0,item);
                    break;
                }
            }
        }
        
        for (int i = 0; i < formalNums.size(); i++) {  // 每个数自己决定前面的正负号，第一个数要特殊处理
            //先定正负号
            if (formalNums.get(i).getAa().max(bigInteger0).equals(bigInteger0)) { //负数
                System.out.print("-");
            } else {
                if (i != 0) { // 第一个数的正号可以忽略
                    System.out.print("+");
                }
            }
            //然后定系数输不输出
            if (formalNums.get(i).getBb().equals(new BigInteger("0"))) { //如果指数次方为0,输出系数
                System.out.print(formalNums.get(i).getAa().abs());
            } else { // 判断系数是不是1，输不输出
                if (formalNums.get(i).getAa().abs().equals(new BigInteger("1"))) { // 系数是1
                    if (formalNums.get(i).getBb().equals(new BigInteger("1"))) { // 1次方，不输出指数
                        System.out.print("x");
                    } else {
                        System.out.print("x**" + formalNums.get(i).getBb());
                    }
                } else {
                    if (formalNums.get(i).getBb().equals(new BigInteger("1"))) { // 1次方，不输出指数
                        System.out.print(formalNums.get(i).getAa().abs() + "*x");
                    } else {
                        System.out.print(formalNums.get(i).getAa().abs() +
                                "*x**" + formalNums.get(i).getBb());
                    }
                    
                }
            }
        }
        
    }
    
    public ArrayList<FormalNum> getFormalNums() {
        return formalNums;
    }
}
