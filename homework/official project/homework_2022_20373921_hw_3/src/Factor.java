import java.math.BigInteger;
import java.util.HashMap;
import java.util.Objects;

public class Factor implements Comparable<Factor> {
    private BigInteger aa;
    private BigInteger bb;
    private HashMap<Function, BigInteger> sinMap;
    private HashMap<Function, BigInteger> cosMap;
    
    // todo function作为hashmap的key不能随便改，一旦加如就不可乱改，要改也要是删掉旧的加个新的
    public Factor mulFac(Factor other) { // 此时已经假定Function key都是最简的
        Factor newFac = this.copyTheFac();
        newFac.setAa(newFac.aa.multiply(other.aa));
        newFac.setBb(newFac.bb.add(other.bb));
        for (Function key : other.sinMap.keySet()) {
            if (newFac.sinMap.containsKey(key)) {
                newFac.sinMap.put(key.copyTheFunc(),
                        newFac.sinMap.get(key).add(other.sinMap.get(key)));
            } else {
                newFac.sinMap.put(key.copyTheFunc(),
                        new BigInteger(other.sinMap.get(key).toString()));
            }
        }
        for (Function key : other.cosMap.keySet()) {
            if (newFac.cosMap.containsKey(key)) {
                newFac.cosMap.put(key.copyTheFunc(),
                        newFac.cosMap.get(key).add(other.cosMap.get(key)));
            } else {
                newFac.cosMap.put(key.copyTheFunc(),
                        new BigInteger(other.cosMap.get(key).toString()));
            }
        }
        newFac.refineFac();
        return newFac;
    }
    
    public Factor addFac(Factor other) { //虽然用不上，但是还是实现了对加0的支持
        if (this.equalZero()) {
            return other.copyTheFac();
        }
        if (other.equalZero()) {
            return this.copyTheFac();
        }
        Factor newFac = other.copyTheFac();
        newFac.aa = newFac.aa.add(this.aa);
        newFac.refineFac();
        return newFac;
    }
    
    public void refineFac() {
        // todo 考虑在此进行更多的化简，例如 2sinx cosx = sin2x ,cos中的Func的化简和合并
        // aa为0，其它的直接全删，aa为0意味着就是0
        if (aa.equals(BigInteger.ZERO)) {
            bb = BigInteger.ZERO;
            sinMap.clear();
            cosMap.clear();
            return;
        }
        
        // sin为 0
        for (Function key : sinMap.keySet()) {
            if (key.equalZero()) {
                aa = BigInteger.ZERO;
                bb = BigInteger.ZERO;
                sinMap.clear();
                cosMap.clear();
                return;
            }
        }
        
        // cos 为0
        boolean flag = true;
        while (flag) {
            flag = false;
            for (Function key : cosMap.keySet()) {
                if (key.equalZero()) {
                    cosMap.remove(key);
                    flag = true;
                    break;
                }
            }
        }
        this.refineFac2();
    }
    
    public void refineFac2() {
        // sin 中的Func加不加负号
        boolean flag = true;
        while (flag) {
            flag = false;
            for (Function key : sinMap.keySet()) {
                if (key.getFactors().size() == 1) {
                    if (key.getFactors().get(0).getAa().compareTo(BigInteger.ZERO) < 0) {
                        Function newKey = key.copyTheFunc();
                        newKey.getFactors().get(0).setAa(newKey.getFactors().get(0).getAa().abs());
                        sinMap.put(newKey, new BigInteger(
                                cosMap.get(key).multiply(new BigInteger("-1")).toString()));
                        sinMap.remove(key);
                        flag = true;
                        break;
                    }
                }
            }
        }
        
        // cos 中的Func加不加负号
        flag = true;
        while (flag) {
            flag = false;
            for (Function key : cosMap.keySet()) {
                if (key.getFactors().size() == 1) {
                    if (key.getFactors().get(0).getAa().compareTo(BigInteger.ZERO) < 0) {
                        Function newKey = key.copyTheFunc();
                        newKey.getFactors().get(0).setAa(newKey.getFactors().get(0).getAa().abs());
                        cosMap.put(newKey, new BigInteger(cosMap.get(key).toString()));
                        cosMap.remove(key);
                        flag = true;
                        break;
                    }
                }
            }
        }
    }
    
    public Factor copyTheFac() {
        Factor newFac = new Factor(this.aa, this.bb);
        for (Function func : this.sinMap.keySet()) {
            newFac.sinMap.put(func.copyTheFunc(), new BigInteger(this.sinMap.get(func).toString()));
        }
        for (Function func : this.cosMap.keySet()) {
            newFac.cosMap.put(func.copyTheFunc(), new BigInteger(this.cosMap.get(func).toString()));
        }
        return newFac;
    }
    
    public boolean equalZero() {
        return aa.equals(BigInteger.ZERO);
    }
    
    public boolean judgeCanMerge(Factor other) { // todo 重看一遍
        if (this == other) {
            return true;
        }
        if (aa.equals(BigInteger.ZERO) || other.aa.equals(BigInteger.ZERO)) {
            // 是0的话选择不合并，会将他们删掉的，不要合并
            return false;
        }
        if (!bb.equals(other.bb)) {
            return false;
        }
        if (this.sinMap.size() != other.sinMap.size() ||
                this.cosMap.size() != other.cosMap.size()) {
            return false;
        }
        for (Function key : other.getSinMap().keySet()) {
            if (!this.sinMap.containsKey(key) ||
                    !this.sinMap.get(key).equals(other.sinMap.get(key))) {
                return false;
            }
        }
        for (Function key : other.getCosMap().keySet()) {
            if (!this.cosMap.containsKey(key) ||
                    !this.cosMap.get(key).equals(other.cosMap.get(key))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean printSignal(boolean isFirst) {
        boolean retHavePrintSth = true;
        if (aa.abs().equals(BigInteger.ONE)) { // aa == +-1
            retHavePrintSth = false;
            if (aa.equals(BigInteger.ONE)) {
                if (!isFirst) { // 不是第一个factor才输出
                    System.out.print("+");
                }
            } else {
                System.out.print("-");
            }
        } else { // aa != +-1
            if (aa.compareTo(BigInteger.ZERO) > 0) {
                if (!isFirst) {
                    System.out.print("+");
                }
            } else {
                System.out.print("-");
            }
            System.out.print(aa.abs());
        }
        return retHavePrintSth;
    }
    
    public void printFac(boolean isFirst) {
        boolean havePrintSth = true;
        havePrintSth = this.printSignal(isFirst);
        
        //处理bb
        if (!bb.equals(BigInteger.ZERO)) {
            if (havePrintSth) {
                System.out.print("*x");
            } else {
                System.out.print("x");
            }
            
            if (bb.equals(new BigInteger("2"))) {
                System.out.print("*x");
            } else if (!bb.equals(BigInteger.ONE)) {
                System.out.print("**" + bb);
            }
            havePrintSth = true;
        } // bb = 0， 不输出x， 不影响havePrint的结果
        
        for (Function func : sinMap.keySet()) {
            if (!havePrintSth) {
                System.out.print("sin(");
                havePrintSth = true;
            } else {
                System.out.print("*sin(");
            }
            if (func.judgeOnlyOne()) {
                func.specialPrint();
                System.out.print(")");
            } else {
                System.out.print("(");
                func.printFunc();
                System.out.print("))");
            }
            if (!sinMap.get(func).equals(BigInteger.ONE)) {
                System.out.print("**" + sinMap.get(func));
            }
        }
        
        for (Function func : cosMap.keySet()) {
            if (!havePrintSth) {
                System.out.print("cos(");
                havePrintSth = true;
            } else {
                System.out.print("*cos(");
            }
            if (func.judgeOnlyOne()) {
                func.specialPrint();
                System.out.print(")");
            } else {
                System.out.print("(");
                func.printFunc();
                System.out.print("))");
            }
            if (!cosMap.get(func).equals(BigInteger.ONE)) {
                System.out.print("**" + cosMap.get(func));
            }
        }
        
        if (!havePrintSth) {
            System.out.print("1");
        }
    }
    
    //////////////////////////////////////////////////////////////////////////////////////
    ///方法重写
    @Override
    public int compareTo(Factor other) { //factor一定要有严格的顺序
        if (this.bb.compareTo(other.bb) < 0) {
            return 1;
        } else if (this.bb.compareTo(other.bb) > 0) {
            return -1;
        }
        
        if (this.aa.compareTo(other.aa) < 0) {
            return 1;
        } else if (this.aa.compareTo(other.aa) > 0) {
            return -1;
        }
        
        if (this.sinMap.size() < other.sinMap.size()) {
            return 1;
        } else if (this.sinMap.size() > other.sinMap.size()) {
            return -1;
        }
        
        if (this.cosMap.size() < other.cosMap.size()) {
            return 1;
        } else if (this.cosMap.size() > other.cosMap.size()) {
            return -1;
        }
        
        return 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getAa(), getBb());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Factor)) {
            return false;
        }
        Factor other = (Factor) obj;
        if (!aa.equals(other.aa) || !bb.equals(other.bb)) {
            return false;
        } // 此后aa和bb都是相同的
        if (sinMap.size() != other.sinMap.size() || cosMap.size() != other.cosMap.size()) {
            return false;
        } // 此后sinMap 和 cosMap的size都是一样的
        if (sinMap.size() == 0 && cosMap.size() == 0) {
            return true; // aa==bb 并且没有三角函数
        }
        
        for (Function func : sinMap.keySet()) {
            if (!other.sinMap.containsKey(func)) {
                return false;
            }
            if (!other.sinMap.get(func).equals(this.sinMap.get(func))) {
                return false;
            }
        }
        
        for (Function func : cosMap.keySet()) {
            if (!other.cosMap.containsKey(func)) {
                return false;
            }
            if (!other.cosMap.get(func).equals(this.cosMap.get(func))) {
                return false;
            }
        }
        return true;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    //// 构造函数   todo 根据需要加构造函数
    public Factor() {
        aa = new BigInteger("0");
        bb = new BigInteger("0");
        sinMap = new HashMap<>();
        cosMap = new HashMap<>();
    }
    
    public Factor(String strNum) {
        if (strNum.equals("x")) {
            this.aa = new BigInteger("1");
            this.bb = new BigInteger("1");
        } else {
            this.aa = new BigInteger(strNum.trim());
            this.bb = new BigInteger("0");
        }
        this.sinMap = new HashMap<>();
        this.cosMap = new HashMap<>();
    }
    
    public Factor(BigInteger aa, BigInteger bb) {
        this.aa = new BigInteger(aa.toString());
        this.bb = new BigInteger(bb.toString());
        sinMap = new HashMap<>();
        cosMap = new HashMap<>();
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////
    //// gets and sets
    public BigInteger getAa() {
        return aa;
    }
    
    public void setAa(BigInteger aa) {
        this.aa = aa;
    }
    
    public BigInteger getBb() {
        return bb;
    }
    
    public void setBb(BigInteger bb) {
        this.bb = bb;
    }
    
    public HashMap<Function, BigInteger> getSinMap() {
        return sinMap;
    }
    
    public void setSinMap(HashMap<Function, BigInteger> sinMap) {
        this.sinMap = sinMap;
    }
    
    public HashMap<Function, BigInteger> getCosMap() {
        return cosMap;
    }
    
    public void setCosMap(HashMap<Function, BigInteger> cosMap) {
        this.cosMap = cosMap;
    }
}
