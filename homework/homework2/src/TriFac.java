import java.math.BigInteger;
import java.util.Objects;

public class TriFac {
    private BigInteger cc;
    private BigInteger dd;
    
    public TriFac copyTheTriFac() { // 把当前这个TriFac给copy了，返回的是一个全新的空间
        TriFac newTriFac = new TriFac();
        newTriFac.setCc(new BigInteger(cc.toString()));
        newTriFac.setDd(new BigInteger(dd.toString()));
        return newTriFac;
    }
    
    public void printTriFac() { // 必不存在为0的情况
        if (dd.equals(BigInteger.ZERO)) { //是常数
            System.out.print(cc);
        } else {
            //指数为1
            if (dd.equals(BigInteger.ONE)) {
                System.out.print("x");
            } else {
                System.out.print("x**" + dd);
            }
            
        }
    }
    
    //////////////////////////////////////////////////////////
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true; // 地址相同，同一对象
        }
        if (!(o instanceof TriFac)) {
            return false;
        }
        TriFac triFac = (TriFac) o;
        return getCc().equals(triFac.getCc()) && getDd().equals(triFac.getDd());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getCc(), getDd());
    }
    
    //////////////////////////////////////////////////////////
    //构造函数
    public TriFac(BigInteger cc, BigInteger dd) {
        // 保证用的是新的空间，不会重叠
        this.cc = new BigInteger(cc.toString());
        this.dd = new BigInteger(dd.toString());
    }
    
    public TriFac() {
        // 保证有新的空间
        this.cc = new BigInteger("0");
        this.dd = new BigInteger("0");
    }
    
    //////////////////////////////////////////////////////////
    // gets and sets
    public BigInteger getCc() {
        return cc;
    }
    
    public void setCc(BigInteger cc) {
        this.cc = cc;
    }
    
    public BigInteger getDd() {
        return dd;
    }
    
    public void setDd(BigInteger dd) {
        this.dd = dd;
    }
}
