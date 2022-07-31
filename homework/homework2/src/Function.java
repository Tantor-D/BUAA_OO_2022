import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Function { // 注意考虑运算中Func为空的情况
    private ArrayList<Factor> factors;
    
    ///////////////////////////////////////////////////////////////////////////////
    // 对应几个要做的操作
    public Function addFunc(Function other) {
        Function newFunc = other.copyTheFunc();
        for (Factor fac : this.factors) {
            newFunc.factors.add(fac.copyTheFac());
        }
        newFunc.refineFunc();
        return newFunc;
    }
    
    public Function posFunc() {
        Function newFunc = this.copyTheFunc();
        newFunc.refineFunc();
        return newFunc;
    }
    
    public Function negFunc() {
        Function newFunc = this.copyTheFunc(); // copy后得到的是一个全新的空间
        for (Factor fac : newFunc.factors) {
            fac.setAa(fac.getAa().multiply(new BigInteger("-1")));
        }
        newFunc.refineFunc();
        return newFunc;
    }
    
    public Function subFunc(Function other) {
        Function func1 = other.negFunc();
        return this.addFunc(func1); // add后会自动refine
    }
    
    public Function mulFunc(Function other) {
        Function newFunc = new Function();
        for (Factor fac1 : this.factors) {
            for (Factor fac2 : other.factors) {
                newFunc.factors.add(fac1.mulFac(fac2));
            }
        }
        newFunc.refineFunc();
        return newFunc;
    }
    
    public Function powFunc(BigInteger xx) {
        BigInteger times = new BigInteger(xx.toString());
        times = times.subtract(BigInteger.ONE);
        Function newFunc = new Function();
        if (xx.equals(BigInteger.ZERO)) {
            Factor newFac = new Factor(BigInteger.ONE, BigInteger.ZERO);
            newFunc.factors.add(newFac);
            return newFunc;
        }
        newFunc = this.copyTheFunc();
        while (times.compareTo(BigInteger.ZERO) > 0) {
            times = times.subtract(BigInteger.ONE);
            newFunc = newFunc.mulFunc(this);
        }
        newFunc.refineFunc();
        return newFunc;
    }
    
    public Function sinFunc() { // 复查是不是每种运算都考虑了列表为空，即为0时的情况
        Function newFunc = new Function();
        Factor newFac = new Factor(); // 在此已经有了sinMap和cosMap的空间了，都实例化了且为空，只用管aa和bb就行了
        /*if (this.factors.isEmpty()) {
            newFac.setAa(BigInteger.ZERO);
            newFac.setBb(BigInteger.ZERO);
        } else {
            newFac.setAa(new BigInteger(this.factors.get(0).getAa().toString()));
            newFac.setBb(new BigInteger(this.factors.get(0).getBb().toString()));
        }*/
        if (this.factors.isEmpty()) { // 意味着是0 -> sin(0)
            newFac.setAa(BigInteger.ZERO);
            newFac.setBb(BigInteger.ZERO);
            //列表保持为空
        } else {
            newFac.setAa(BigInteger.ONE);
            newFac.setBb(BigInteger.ZERO);
            TriFac triFacInSin =
                    new TriFac(this.factors.get(0).getAa(), this.factors.get(0).getBb());
            newFac.getSinMap().put(triFacInSin, BigInteger.ONE);
        }
        newFunc.factors.add(newFac);
        return newFunc;
    }
    
    public Function cosFunc() {
        Function newFunc = new Function();
        Factor newFac = new Factor();
        if (this.factors.isEmpty()) {
            newFac.setAa(BigInteger.ONE);
            newFac.setBb(BigInteger.ZERO);
            //sin和cos的hashMap都为空
        } else {
            newFac.setAa(BigInteger.ONE);
            newFac.setBb(BigInteger.ZERO);
            TriFac triFacInCos =
                    new TriFac(this.factors.get(0).getAa(), this.factors.get(0).getBb());
            newFac.getCosMap().put(triFacInCos, BigInteger.ONE);
        }
        newFunc.factors.add(newFac);
        return newFunc;
    }
    
    ////////////////////////////////////////////////////////////////////////////////
    // 功能函数
    public void printFunc() {
        //最后优化一次
        this.finalRefineFunc();
        // 0特判
        if (factors.isEmpty()) {
            System.out.print("0");
            return;
        }
        // 把a>0的放到最前面,此时列表必不为空
        for (int i = 0; i < factors.size(); i++) {
            if (factors.get(i).getAa().compareTo(BigInteger.ZERO) > 0) {
                Factor item = factors.get(i);
                factors.set(i, factors.get(0));
                factors.set(0, item);
                break;
            }
        }
        //开始输出
        boolean isFirst = true;
        for (Factor fac : factors) {
            fac.printFactor(isFirst);
            isFirst = false;
        }
    }
    
    public Function copyTheFunc() {
        Function newFunc = new Function();
        for (Factor fac : this.factors) {
            newFunc.factors.add(fac.copyTheFac());
        }
        return newFunc;
    }
    
    public void finalRefineFunc() {
        //sin^2 + cos^2 = 1
        //首先找有没有平方
        for (Factor fac : factors) {
            fac.refineFac();
        }
        
        boolean haveFind = true;
        while (haveFind) {
            haveFind = false;
            for (int i = 0; i < factors.size(); i++) {
                Factor thisFac = factors.get(i);
                for (int j = i + 1; j < factors.size(); j++) {
                    Factor otherFac = factors.get(j);
                    if (thisFac.judgeIfCanAdd(otherFac)) { // 可以合并
                        haveFind = true;
                        Factor newFac = thisFac.mergeTwoFac(otherFac);
                        factors.add(newFac);
                        factors.remove(j);
                        factors.remove(i);
                        break;
                    }
                }
                if (haveFind) {
                    break;
                }
            }
        }
    }
    
    public void refineFunc() {
        // 先把0给删了
        Iterator<Factor> iterator = factors.iterator();
        while (iterator.hasNext()) {
            Factor theFac = iterator.next();
            if (theFac.getAa().equals(BigInteger.ZERO)) {
                iterator.remove();
            }
        }
        
        // 再排序
        Collections.sort(this.factors);
        
        // 合并同类项
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0; i < factors.size(); i++) {
                for (int j = i + 1; j < factors.size(); j++) {
                    if (factors.get(i).isEqual(factors.get(j))) { //可以合并同类项
                        //合并
                        factors.get(i).setAa(factors.get(i).getAa().add(factors.get(j).getAa()));
                        //删除j这个索引的对象，然后修改flag，然后break出这一重循环
                        factors.remove(j);
                        flag = true;
                        break;
                    }
                }
                if (flag) { //匹配到了，合并之后退出，重新循环
                    break;
                }
            }
        }
        
        // 再删一次0
        iterator = factors.iterator();
        while (iterator.hasNext()) {
            Factor theFac = iterator.next();
            if (theFac.getAa().equals(BigInteger.ZERO)) {
                iterator.remove();
            }
        }
        
        // 再排序
        Collections.sort(this.factors);
    }
    
    /////////////////////////////////////////////////////////////////////////////////
    // 构造函数
    public Function() {
        factors = new ArrayList<>();
    }
    
    public Function(String strNum) {
        this.factors = new ArrayList<>();
        factors.add(new Factor(strNum));
    }
    
    /////////////////////////////////////////////////////////////////////////////////
    // gets and sets
    public ArrayList<Factor> getFactors() {
        return factors;
    }
    
    public void setFactors(ArrayList<Factor> factors) {
        this.factors = factors;
    }
}
