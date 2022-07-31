import java.util.HashMap;

public class Parser {
    public String getFx(String curExpr) {
        String[] strs = curExpr.trim().split(" ");
        return strs[0];
    }
    
    public String getOpNum1(String curExpr) {
        String[] strs = curExpr.trim().split(" ");
        return strs[2];
    }
    
    public String getOpNum2(String curExpr) {
        String[] strs = curExpr.trim().split(" ");
        return strs[3];
    }
    
    public String getSpecialNum(String curExpr) {
        String[] strs = curExpr.trim().split(" ");
        return strs[1];
    }
    
    public Function transToFunction(HashMap<String, Function> hashMap, String strNum) {
        // 注意，如果是数字，得到的是一个新的实例化对象。 如果是标签，得到的是hashmap中的对象
        if (strNum.contains("f")) {
            return hashMap.get(strNum);
        } else {
            return new Function(strNum);// x或者是一个常数
        }
    }
}
