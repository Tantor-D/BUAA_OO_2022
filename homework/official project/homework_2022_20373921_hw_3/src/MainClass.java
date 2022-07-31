import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;

import java.util.HashMap;

public class MainClass {
    // 最重要的事情是 Function的标准化，三角函数的合并
    // Function的标准化
    // 重写Function的equal 和 hashcode
    
    // 统一约定： Func的形式必须绝对统一，否则sin，cos的化简会寄
    
    public static void main(String[] args) {  // todo 乘除法都去考虑一下为空的情况
        HashMap<String, Function> hashMap = new HashMap<>();
        MyTool myTool = new MyTool();
        ExprInput scanner = new ExprInput(ExprInputMode.ParsedMode);
        int n = scanner.getCount();
        
        for (int i = 0; i < n; i++) {
            String curExpr = scanner.readLine();
            //String curExpr = scanner.nextLine();
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
            } else if (curExpr.matches(".+sin.+")) {
                myTool.opSin(hashMap, curExpr);
            } else if (curExpr.matches(".+cos.+")) {
                myTool.opCos(hashMap, curExpr);
            } else { //最后一种数字
                myTool.opNewNum(hashMap, curExpr);
            }
            //System.out.print("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx   i = " + i);
            /*int t = i + 1;
            hashMap.get("f" + t).finalRefineFunc();
            System.out.print("Function" + t + "= ");
            hashMap.get("f" + t).printFunc();
            System.out.println();*/
        }
        
        /*for (int i = 1; i <= n; i++) {
            //hashMap.get("f" + i).refineFunc();
            System.out.print("Function" + i + "= ");
            hashMap.get("f" + i).printFunc();
            System.out.println();
        }*/
        //hashMap.get("f" + n).finalRefineFunc();
        hashMap.get("f" + n).printFunc();
        
    }
    
}
