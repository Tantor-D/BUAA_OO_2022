import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class Expr {
    private ArrayList<Factor> factors; //  记得改构造函数，要让它指向一个地方
    
    public Expr(String strNum) { // 从数字直接到一个常数Function
        factors = new ArrayList<>();
        factors.add(new Factor(strNum));
    }
    
    public Expr() { // 无参构造器，默认是常数0
        factors = new ArrayList<>();
        factors.add(new Factor("0"));
    }
    
    public Expr addFunc(Expr func1) {
        // 相加就是全部垒到一起然后refine一下
        // 本身是会新建空间的
        // 不会影响参与运算的两个func的实例化空间
        Expr newFunc = new Expr();
        for (int i = 0; i < this.factors.size(); i++) {
            newFunc.factors.add(new Factor(this.factors.get(i).getAa(),
                    this.factors.get(i).getBb()));
        }
        
        for (int i = 0; i < func1.factors.size(); i++) {
            newFunc.factors.add(new Factor(func1.factors.get(i).getAa(),
                    func1.factors.get(i).getBb()));
        }
        
        newFunc.refineFunc();
        return newFunc;
    }
    
    public Expr posFunc() {
        Expr newFunc = new Expr();
        for (int i = 0; i < this.factors.size(); i++) {
            newFunc.factors.add(new Factor(this.factors.get(i).getAa(),
                    this.factors.get(i).getBb()));
        }
        //newFunc.formalNums.addAll(this.formalNums);
        newFunc.refineFunc();
        return newFunc;
    }
    
    public Expr negFunc() {
        Expr newFunc = new Expr();
        // newFunc.formalNums.addAll(this.formalNums);
        for (int i = 0; i < this.factors.size(); i++) {
            //newFunc.formalNums.add(this.formalNums.get(i));
            newFunc.factors.add(new Factor(this.factors.get(i).getAa(),
                    this.factors.get(i).getBb()));
        }
        for (Factor item : newFunc.factors) {
            item.setAa(item.getAa().multiply(new BigInteger("-1")));
        }
        newFunc.refineFunc();
        return newFunc;
    }
    
    public Expr powFunc(int x) {
        Expr newFunc = new Expr();
        if (x == 0) { //特判为0
            newFunc.factors.add(new Factor(new BigInteger("1"), new BigInteger("0")));
            newFunc.refineFunc();
            return newFunc;
        }
        
        for (int i = 0; i < this.factors.size(); i++) {
            newFunc.factors.add(new Factor(this.factors.get(i).getAa(),
                    this.factors.get(i).getBb()));
        }
        for (int i = 1; i < x; i++) { // x-1次
            newFunc = newFunc.mulFunc(this);//乘完后会自动refine的
        }
        return newFunc;
    }
    
    public Expr mulFunc(Expr func1) {
        Expr newFunc = new Expr();
        this.refineFunc();
        func1.refineFunc();//这里加上是为了避免出现刚刚生成了一个新的Function，但是是0
        if (this.factors.isEmpty() || func1.factors.isEmpty()) { // 其中一个为0
            newFunc.refineFunc();
            return newFunc;
        }
        
        for (int i = 0; i < this.factors.size(); i++) {
            for (int j = 0; j < func1.factors.size(); j++) {
                Factor newFactor = new Factor();
                
                Factor num1 = this.getFormalNums().get(i);
                Factor num2 = func1.getFormalNums().get(j);
                
                newFactor.setAa(num1.getAa().multiply(num2.getAa()));
                newFactor.setBb(num1.getBb().add(num2.getBb()));
                newFunc.factors.add(newFactor);
            }
        }
        newFunc.refineFunc();
        return newFunc;
    }
    
    public void refineFunc() {
        //排序 累加 删除
        Collections.sort(this.factors);
        //累加
        for (int i = factors.size() - 1; i > 0; i--) {
            if (factors.get(i).getBb().equals(factors.get(i - 1).getBb())) {
                factors.get(i - 1).setAa(factors.get(i - 1).getAa().
                        add(factors.get(i).getAa()));//这是在加
            }
        }
        //删除 , 有两种情况：一个是指数相同，留最前面的那一个；另一个是系数为0
        for (int i = factors.size() - 1; i > 0; i--) {
            if (factors.get(i).getBb().equals(factors.get(i - 1).getBb())) {
                factors.remove(i);
            } else if (factors.get(i).getAa().equals(new BigInteger("0"))) {
                factors.remove(i);
            }
        }
        if (!factors.isEmpty() && factors.get(0).getAa().equals(new BigInteger("0"))) {
            factors.remove(0);//这样子保证了不会漏删，不会出现0* x^7，这种x高次但是系数为0的情况。但同时也可能会使列表为空
        }
        
    }
    
    public void printInf() {
        // 结果为0，特判
        if (factors.isEmpty()) {
            System.out.println(0);
            return;
        }
        BigInteger bigInteger0 = new BigInteger("0");
        
        for (int i = 0; i < factors.size(); i++) { // 把正数放到第一个
            if (factors.get(i).getAa().min(bigInteger0).equals(bigInteger0)) { //是正数
                if (i == 0) {
                    break;
                } else { // 开始操作
                    Factor item = new Factor(factors.get(i).getAa(),
                            factors.get(i).getBb());
                    factors.set(i, factors.get(0));
                    factors.set(0,item);
                    break;
                }
            }
        }
        
        for (int i = 0; i < factors.size(); i++) {  // 每个数自己决定前面的正负号，第一个数要特殊处理
            //先定正负号
            if (factors.get(i).getAa().max(bigInteger0).equals(bigInteger0)) { //负数
                System.out.print("-");
            } else {
                if (i != 0) { // 第一个数的正号可以忽略
                    System.out.print("+");
                }
            }
            //然后定系数输不输出
            if (factors.get(i).getBb().equals(new BigInteger("0"))) { //如果指数次方为0,输出系数
                System.out.print(factors.get(i).getAa().abs());
            } else { // 判断系数是不是1，输不输出
                if (factors.get(i).getAa().abs().equals(new BigInteger("1"))) { // 系数是1
                    if (factors.get(i).getBb().equals(new BigInteger("1"))) { // 1次方，不输出指数
                        System.out.print("x");
                    } else {
                        System.out.print("x**" + factors.get(i).getBb());
                    }
                } else {
                    if (factors.get(i).getBb().equals(new BigInteger("1"))) { // 1次方，不输出指数
                        System.out.print(factors.get(i).getAa().abs() + "*x");
                    } else {
                        System.out.print(factors.get(i).getAa().abs() +
                                "*x**" + factors.get(i).getBb());
                    }
                    
                }
            }
        }
        
    }
    
    public ArrayList<Factor> getFormalNums() {
        return factors;
    }
}
