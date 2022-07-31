import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Factor implements Comparable<Factor> {
    private BigInteger aa;
    private BigInteger bb;
    private HashMap<TriFac, BigInteger> sinMap;
    private HashMap<TriFac, BigInteger> cosMap;
    
    public boolean judgeIfCanAdd(Factor other) {
        // (0) aa,bb 一定是相同的
        if (!this.bb.equals(other.bb) || !this.aa.equals(other.aa)) {
            return false;
        }
        
        //（1） sin和cos的size相加肯定是相同的 && 一定是差的绝对值为1
        if (sinMap.size() + cosMap.size() != other.sinMap.size() + other.cosMap.size()) {
            return false;
        }
        if (Math.abs(sinMap.size() - other.sinMap.size()) != 1) {
            return false;
        }
        
        Factor fac1;
        Factor fac2;
        if (sinMap.size() < other.sinMap.size()) {
            fac1 = this; // fac1的sinMap较小
            fac2 = other;
        } else {
            fac1 = other;
            fac2 = this;
        }
        
        for (TriFac tri : fac1.sinMap.keySet()) {
            if (!fac2.sinMap.containsKey(tri)) {
                return false;
            }
            if (!fac2.sinMap.get(tri).equals(fac1.sinMap.get(tri))) {
                return false;
            }
        }
        
        for (TriFac tri : fac2.cosMap.keySet()) {
            if (!fac1.cosMap.containsKey(tri)) {
                return false;
            }
            if (!fac1.cosMap.get(tri).equals(fac2.cosMap.get(tri))) {
                return false;
            }
        }
        
        TriFac redundantTri = null;
        for (TriFac tri : fac2.sinMap.keySet()) {
            if (fac1.sinMap.containsKey(tri)) {
                continue;
            }
            redundantTri = tri;
            break;
        }
        
        if (!fac2.sinMap.get(redundantTri).equals(new BigInteger("2"))) {
            return false;
        }
        if (!fac1.cosMap.containsKey(redundantTri)) {
            return false;
        }
        if (!fac1.cosMap.get(redundantTri).equals(new BigInteger("2"))) {
            return false;
        }
        return true;
    }
    
    public Factor mergeTwoFac(Factor other) {
        // 合并的函数，注意要新建空间
        // 还有一种思路，删掉列表中的一个sin^2,然后删掉另一个factor
        Factor newFac = new Factor();
        
        newFac.setAa(new BigInteger(this.aa.toString()));
        newFac.setBb(new BigInteger(this.bb.toString()));
        
        Factor fac1;
        Factor fac2;
        if (sinMap.size() < other.sinMap.size()) {
            fac1 = this; // fac1的sinMap较小
            fac2 = other;
        } else {
            fac1 = other;
            fac2 = this;
        }
        
        for (TriFac tri : fac1.sinMap.keySet()) {
            newFac.sinMap.put(tri.copyTheTriFac(), new BigInteger(fac1.sinMap.get(tri).toString()));
        }
        for (TriFac tri : fac2.cosMap.keySet()) {
            newFac.cosMap.put(tri.copyTheTriFac(), new BigInteger(fac2.cosMap.get(tri).toString()));
        }
        
        return newFac;
    }
    
    public Factor mulFac(Factor other) {
        Factor newFac = new Factor();
        
        // 把others的值都copy一遍
        newFac.setAa(new BigInteger(other.getAa().toString()));
        newFac.setBb(new BigInteger(other.getBb().toString()));
        for (TriFac key : other.sinMap.keySet()) {
            newFac.sinMap.put(key.copyTheTriFac(),
                    new BigInteger(other.sinMap.get(key).toString()));
        }
        for (TriFac key : other.cosMap.keySet()) {
            newFac.cosMap.put(key.copyTheTriFac(),
                    new BigInteger(other.cosMap.get(key).toString()));
        }
        
        // 然后相乘
        newFac.aa = newFac.aa.multiply(this.aa);
        newFac.bb = newFac.bb.add(this.bb);
        for (TriFac key : this.sinMap.keySet()) {
            if (newFac.sinMap.containsKey(key)) { // 如果有这个sin了，那么指数相加
                newFac.sinMap.replace(key, newFac.sinMap.get(key).add(sinMap.get(key)));
            } else {
                newFac.sinMap.put(key.copyTheTriFac(), new BigInteger(sinMap.get(key).toString()));
            }
        }
        for (TriFac key : this.cosMap.keySet()) {
            if (newFac.cosMap.containsKey(key)) {
                newFac.cosMap.replace(key, newFac.cosMap.get(key).add(cosMap.get(key)));
            } else {
                newFac.cosMap.put(key.copyTheTriFac(), new BigInteger(cosMap.get(key).toString()));
            }
        }
        // 最后refine一下
        newFac.refineFac();
        
        return newFac; // 保证newFac中的值都是有着自己的独立的空间的
    }
    
    public void refineFac() {
        //这里的refine并不考虑对sin(常数)进行化简操作
        //看看有无可以sin(0)，直接清空这个Factor
        for (TriFac key : sinMap.keySet()) {
            if (key.getCc().equals(BigInteger.ZERO)) {
                this.setAa(new BigInteger("0"));
                this.setBb(new BigInteger("0"));
                this.sinMap.clear();
                this.cosMap.clear();
                return;
            }
        }
        
        //看看有无cos(0),清除这一个cos
        Iterator<Map.Entry<TriFac, BigInteger>> iterator = cosMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<TriFac, BigInteger> entry = iterator.next();
            if (entry.getKey().getCc().equals(BigInteger.ZERO)) {
                iterator.remove();
            }
        }
        
        //对常数使用sin(2a)=2sin(a)cos(a)
        boolean hasFind = true;
        while (hasFind) {
            hasFind = false;
            for (TriFac tri : sinMap.keySet()) {
                if (!tri.getDd().equals(BigInteger.ZERO)) {
                    continue; // 不是常数，跳过
                } else { // 是常数，接着做
                    if (cosMap.containsKey(tri)) { // 可以试着化简
                        BigInteger minExp =
                                new BigInteger(sinMap.get(tri).min(cosMap.get(tri)).toString());
                        BigInteger divNum = new BigInteger("2");
                        divNum = divNum.pow(minExp.intValue());
                        if (aa.remainder(divNum).equals(BigInteger.ZERO)) { // 可以整除，意味着可以优化
                            hasFind = true;
                            aa = aa.divide(divNum);
                            sinMap.put(new TriFac(tri.getCc().multiply(new
                                    BigInteger("2")), BigInteger.ZERO), minExp);
                            if (sinMap.get(tri).equals(minExp)) {
                                sinMap.remove(tri);
                            } else {
                                sinMap.replace(tri, sinMap.get(tri).subtract(minExp));
                            }
                            if (cosMap.get(tri).equals(minExp)) {
                                cosMap.remove(tri);
                            } else {
                                cosMap.replace(tri, cosMap.get(tri).subtract(minExp));
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public Factor copyTheFac() {
        Factor newFac = new Factor();
        newFac.aa = new BigInteger(this.aa.toString());
        newFac.bb = new BigInteger(this.bb.toString());
        for (TriFac key : this.sinMap.keySet()) {
            newFac.sinMap.put(key.copyTheTriFac(), new BigInteger(this.sinMap.get(key).toString()));
        }
        for (TriFac key : this.cosMap.keySet()) {
            newFac.cosMap.put(key.copyTheTriFac(), new BigInteger(this.cosMap.get(key).toString()));
        }
        return newFac;
    }
    
    @Override
    public int compareTo(Factor other) {
        //先按b，再按a，然后按sin的size，接着是cos的size
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
    
    boolean isEqual(Factor other) { // 判断两个Factor是不是一样的
        if (this == other) {
            return true;
        }
        if (!bb.equals(other.bb)) {
            return false;
        }
        if (sinMap.size() != other.sinMap.size() || cosMap.size() != other.cosMap.size()) {
            return false;
        }
        for (TriFac fac : other.sinMap.keySet()) {
            if (!sinMap.containsKey(fac) || !sinMap.get(fac).equals(other.sinMap.get(fac))) {
                return false; // 没有这个索引 或者 有这个索引，但是指数项不一样大
            }
        }
        for (TriFac fac : other.cosMap.keySet()) {
            if (!cosMap.containsKey(fac) || !cosMap.get(fac).equals(other.cosMap.get(fac))) {
                return false;
            }
        }
        return true;
    }
    
    public void printFactor(boolean isFirst) {
        boolean havePrintSth = true;
        // 处理a和开头的正负
        if (aa.abs().equals(BigInteger.ONE)) { // aa == +-1
            havePrintSth = false;
            if (aa.equals(BigInteger.ONE)) {
                if (!isFirst) { // 不是第一个factor才输出
                    System.out.print("+");
                }
            } else { System.out.print("-"); }
        } else { // aa != +-1
            if (aa.compareTo(BigInteger.ZERO) > 0) {
                if (!isFirst) {
                    System.out.print("+");
                }
            } else { System.out.print("-"); }
            System.out.print(aa.abs());
        }
        
        //处理bb
        if (!bb.equals(BigInteger.ZERO)) {
            if (havePrintSth) {
                System.out.print("*x");
            } else { System.out.print("x"); }
            
            if (bb.equals(new BigInteger("2"))) {
                System.out.print("*x");
            } else if (!bb.equals(BigInteger.ONE)) {
                System.out.print("**" + bb);
            }
            havePrintSth = true;
        } // bb = 0， 不输出x， 不影响havePrint的结果
        
        for (TriFac tri : this.sinMap.keySet()) {
            if (!havePrintSth) {
                System.out.print("sin(");
                havePrintSth = true;
            } else {
                System.out.print("*sin(");
            }
            tri.printTriFac();
            System.out.print(")");
            if (!sinMap.get(tri).equals(BigInteger.ONE)) {
                System.out.print("**" + sinMap.get(tri));
            }
        }
        for (TriFac tri : this.cosMap.keySet()) {
            if (!havePrintSth) {
                System.out.print("cos(");
                havePrintSth = true;
            } else {
                System.out.print("*cos(");
            }
            
            tri.printTriFac();
            System.out.print(")");
            if (!cosMap.get(tri).equals(BigInteger.ONE)) {
                System.out.print("**" + cosMap.get(tri));
            }
        }
        
        if (!havePrintSth) { //什么都没输出，又不是0，只会是 a=1, b=0
            System.out.print("1");
        }
    }
    
    /////////////////////////////////////////////////////////////////////
    //构造函数
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
        this.sinMap = new HashMap<>();
        this.cosMap = new HashMap<>();
    }
    
    public Factor() {
        this.aa = new BigInteger("0");
        this.bb = new BigInteger("0");
        this.sinMap = new HashMap<>();
        this.cosMap = new HashMap<>();
    }
    
    /////////////////////////////////////////////////////////////////////
    //sets and gets
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
    
    public HashMap<TriFac, BigInteger> getSinMap() {
        return sinMap;
    }
    
    public void setSinMap(HashMap<TriFac, BigInteger> sinMap) {
        this.sinMap = sinMap;
    }
    
    public HashMap<TriFac, BigInteger> getCosMap() {
        return cosMap;
    }
    
    public void setCosMap(HashMap<TriFac, BigInteger> cosMap) {
        this.cosMap = cosMap;
    }
}

