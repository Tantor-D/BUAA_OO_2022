import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;

import java.util.HashMap;

public class MainClass {
    public static void main(String[] args) {
        HashMap<String, Function> hashMap = new HashMap<>();
        
        ExprInput scanner = new ExprInput(ExprInputMode.ParsedMode);
        int n = scanner.getCount();
        Tools myTool = new Tools();
        
        for (int i = 0; i < n; i++) {
            String curExpr = scanner.readLine();
            if (curExpr.matches(".+add.+")) {
                myTool.opAdd(hashMap, curExpr);
            } else if (curExpr.matches(".+pos.+")) {
                myTool.opPos(hashMap, curExpr);
            } else if (curExpr.matches(".+sub.+")) {
                myTool.opSub(hashMap, curExpr);
            } else if (curExpr.matches(".+neg.+")) {
                myTool.opNeg(hashMap, curExpr);
            } else if (curExpr.matches(".+mul.+")) {
                myTool.opMul(hashMap, curExpr);
            } else if (curExpr.matches(".+pow.+")) {
                myTool.opPow(hashMap, curExpr);
            } else { //最后一种数字
                myTool.opNewNum(hashMap, curExpr);
            }
            /*System.out.print("f" + String.valueOf(i+1) + " :  ");
            System.out.print("size=" + hashMap.get("f" +
                    String.valueOf(i+1)).getFormalNums().size() + "  ");
            hashMap.get("f" + String.valueOf(i+1)).printInf();
            System.out.println();*/
        }
        
        
        /*for (int i = 1; i <= hashMap.size(); i++) {
            System.out.print("f" + i + " :  ");
            System.out.print("size=" + hashMap.get("f" + i).getFormalNums().size() + "  ");
            hashMap.get("f" + i).printInf();
            System.out.println();
        }*/
        
        //输出结果，将最后一个func的结果标准化输出
        hashMap.get("f" + n).refineFunc();
        hashMap.get("f" + n).printInf();
    }
}
