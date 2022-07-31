import java.math.BigInteger;

public class FormalNum implements Comparable<FormalNum> {
    private BigInteger aa;
    private BigInteger bb;
    
    public FormalNum() {
        aa = new BigInteger("0");
        bb = new BigInteger("0");
    }
    
    public FormalNum(String strNum) {
        if (strNum.equals("x")) {
            aa = new BigInteger("1");
            bb = new BigInteger("1");
        }
        else {
            aa = new BigInteger(strNum);
            bb = new BigInteger("0");
        }
    }
    
    public FormalNum(BigInteger aa, BigInteger bb) {
        this.aa = aa;
        this.bb = bb;
    }
    
    @Override
    public int compareTo(FormalNum other) {
        //指数大的在前
        if (this.bb.min(other.getBb()).equals(this.bb)) {
            return 1;
        } else if (this.bb.max(other.getBb()).equals(this.bb)) {
            return -1;
        }
        //系数大的在前
        if (this.aa.min(other.getAa()).equals(this.aa)) {
            return 1;
        } else if (this.aa.max(other.getAa()).equals(this.aa)) {
            return -1;
        }
        
        return 0;
    }
    
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
}
