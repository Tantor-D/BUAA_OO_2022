import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Function { // 时刻考虑function为空的情况
    private ArrayList<Factor> factors;
    
    public Function copyTheFunc() {
        Function newFunc = new Function();
        for (Factor fac : this.factors) {
            newFunc.factors.add(fac.copyTheFac());
        }
        return newFunc;
    }
    
    public void refinefunc() {
        // 为 0 的话就是空的
        //
        //
        //
    }
    
    public void mergeFactors() {
        // 删0
        Iterator<Factor> iterator = factors.iterator();
        while (iterator.hasNext()) {
            Factor theFac = iterator.next();
            if (theFac.equalZero()) {
                iterator.remove();
            }
        }
        
        // 排序 + 合并同类项
        Collections.sort(this.factors);
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0; i < factors.size(); i++) {
                for (int j = i + 1; j < factors.size(); j++) {
                    if (factors.get(i).judgeCanMerge(factors.get(j))) {
                        Factor newFac = factors.get(i).addFac(factors.get(j));
                        factors.add(newFac);
                        factors.remove(j);
                        factors.remove(i);
                        flag = true;
                        break;
                    }
                }
            }
        }
        
        // 删0 + 排序
        iterator = factors.iterator();
        while (iterator.hasNext()) {
            Factor theFac = iterator.next();
            if (theFac.equalZero()) {
                iterator.remove();
            }
        }
        Collections.sort(this.factors);
    }
    
    public boolean equalZero() { //这个可能会根据状况修改，因为hashmap的key不可能为空
        return factors.isEmpty();
    }
    
    public void printFunc() {
        if (factors.isEmpty()) {
            System.out.print("0");
            return;
        }
        
        boolean isFirst = true;
        for (Factor fac : factors) {
            fac.printFac(isFirst);
            isFirst = false;
        }
    }
    
    public void specialPrint() {
        if (factors.get(0).getBb().equals(BigInteger.ZERO)
                && !factors.get(0).getAa().equals(BigInteger.ZERO)
                && factors.get(0).getSinMap().isEmpty() && factors.get(0).getCosMap().isEmpty()) {
            System.out.print(factors.get(0).getAa());
            return;
        }
        if (factors.get(0).getAa().equals(BigInteger.ONE)
                && !factors.get(0).getBb().equals(BigInteger.ZERO)
                && factors.get(0).getSinMap().isEmpty() && factors.get(0).getCosMap().isEmpty()) {
            if (factors.get(0).getBb().equals(new BigInteger("1"))) {
                System.out.print("x");
            } else {
                System.out.print("x**" + factors.get(0).getBb());
            }
            
        }
        
    }
    
    public boolean judgeOnlyOne() {
        if (factors.size() != 1) {
            return false;
        }
        
        if (factors.get(0).getBb().equals(BigInteger.ZERO)
                && !factors.get(0).getAa().equals(BigInteger.ZERO)
                && factors.get(0).getSinMap().isEmpty() && factors.get(0).getCosMap().isEmpty()) {
            return true;
        } // 常数
        
        if (factors.get(0).getAa().equals(BigInteger.ONE)
                && !factors.get(0).getBb().equals(BigInteger.ZERO)
                && factors.get(0).getSinMap().isEmpty() && factors.get(0).getCosMap().isEmpty()) {
            return true;
        }
        
        return false;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    ///// 各种题目要求的操作
    public Function addFunc(Function other) {
        Function newFunc = other.copyTheFunc();
        for (Factor fac : this.factors) {
            newFunc.factors.add(fac.copyTheFac());
        }
        newFunc.mergeFactors();
        return newFunc;
    }
    
    public Function posFunc() {
        return this.copyTheFunc();
    }
    
    public Function negFunc() {
        Function newFunc = this.copyTheFunc();
        for (Factor fac : newFunc.factors) {
            fac.setAa(fac.getAa().multiply(new BigInteger("-1")));
        }
        newFunc.mergeFactors();
        return newFunc;
    }
    
    public Function subFunc(Function other) {
        Function negFuncOther = other.negFunc();
        return this.addFunc(negFuncOther); //已经合并过了
    }
    
    public Function mulFunc(Function other) {
        Function newFunc = new Function();
        for (Factor fac1 : this.factors) {
            for (Factor fac2 : other.factors) {
                newFunc.factors.add(fac1.mulFac(fac2));
            }
        }
        newFunc.mergeFactors();
        return newFunc;
    }
    
    public Function powFunc(BigInteger xx) { // 注意对0的处理
        if (xx.equals(BigInteger.ZERO)) {
            return new Function("1");
        }
        Function newFunc = this.copyTheFunc();
        BigInteger times = new BigInteger(xx.toString());
        times = times.subtract(BigInteger.ONE);
        while (times.compareTo(BigInteger.ZERO) > 0) {
            times = times.subtract(BigInteger.ONE);
            newFunc = newFunc.mulFunc(this);
        }
        // 乘法里其实已经mergeFactor过了
        newFunc.mergeFactors();
        return newFunc;
    }
    
    public Function sinFunc() { // 看一下是不是每一种操作都考虑到了为空的情况
        Function newFunc = new Function();
        Factor newFac = new Factor();
        
        if (this.factors.isEmpty()) {
            newFac.setAa(BigInteger.ZERO);
            newFac.setBb(BigInteger.ZERO);
        } else {
            newFac.setAa(BigInteger.ONE);
            newFac.setBb(BigInteger.ZERO);
            newFac.getSinMap().put(this.copyTheFunc(), BigInteger.ONE);
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
        } else {
            newFac.setAa(BigInteger.ONE);
            newFac.setBb(BigInteger.ZERO);
            newFac.getCosMap().put(this.copyTheFunc(), BigInteger.ONE);
        }
        newFunc.factors.add(newFac);
        return newFunc;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    ///// 构造函数 根据需要加构造函数
    public Function(String strNum) { // 要么是x，要么是常数
        this.factors = new ArrayList<>();
        factors.add(new Factor(strNum));
    }
    
    public Function() {
        this.factors = new ArrayList<>();
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    ///// 方法重写
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Function)) {
            return false;
        }
        Function other = (Function) o;
        if (this.factors.size() != other.factors.size()) {
            return false;
        }
        // 此后size一定n是相等的
    
        boolean hasFind = false;
        for (Factor fac : this.factors) { // 这个的正确性建立在合并了同类项的基础上
            hasFind = false;
            for (Factor otherFac : other.factors) {
                if (fac.equals(otherFac)) {
                    hasFind = true;
                    break;
                }
            }
            if (!hasFind) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        BigInteger retHash = new BigInteger("5381");
        for (int i = 0; i < factors.size(); i++) {
            retHash = retHash.multiply(new BigInteger("33")).add(factors.get(i).getAa())
                    .add(factors.get(i).getBb().multiply(new BigInteger("4")));
        }
        return retHash.intValue();
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    //// gets and sets
    public ArrayList<Factor> getFactors() {
        return factors;
    }
    
    public void setFactors(ArrayList<Factor> factors) {
        this.factors = factors;
    }
}
