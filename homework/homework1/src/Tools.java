import java.util.HashMap;

public class Tools {
    
    public void opAdd(HashMap<String, Expr> hashMap, String curExpr) {
        Parser myParse = new Parser();
    
        //先从字符串中取出两个操作数，然后得到两个function，然后调用function的操作，相加得到新function
        String opNum1 = myParse.getOpNum1(curExpr);
        String opNum2 = myParse.getOpNum2(curExpr);
        Expr func1 = myParse.transToFunction(hashMap,opNum1);
        Expr func2 = myParse.transToFunction(hashMap,opNum2);
        Expr newFunc = func2.addFunc(func1);  // 会自己建新的空间，直接指向它就ok了
        
        //得到新key，新加入一个function进hashmap
        String thisKey = myParse.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
    
    public void opPos(HashMap<String, Expr> hashMap, String curExpr) {
        Parser myParse = new Parser();
        
        //取出操作数，是func就直接copy，是数字的话生成后直接copy
        String opNum1 = myParse.getOpNum1(curExpr);
        Expr func1 = myParse.transToFunction(hashMap,opNum1);
        Expr newFunc = func1.posFunc(); // 此函数就是为了新建一个实例化对象
        
        //得到新key，新加入一个function进hashmap
        String thisKey = myParse.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
    
    public void opSub(HashMap<String, Expr> hashMap, String curExpr) {
        Parser myParse = new Parser();
        
        //func1和func2如果是hashmap中的值的话，是已存在的对象，不要去改，negfunc后的func3是新的对象
        String opNum1 = myParse.getOpNum1(curExpr);
        String opNum2 = myParse.getOpNum2(curExpr);
        Expr func1 = myParse.transToFunction(hashMap,opNum1);
        Expr func2 = myParse.transToFunction(hashMap,opNum2);
        Expr func3 = func2.negFunc();
        Expr newFunc = func1.addFunc(func3);  // 会自己建新的空间，直接指向它就ok了
    
        //得到新key，新加入一个function进hashmap
        String thisKey = myParse.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
    
    public void opNeg(HashMap<String, Expr> hashMap, String curExpr) {
        Parser myParse = new Parser();
    
        //取出操作数，是func就直接copy，是数字的话生成后直接copy
        String opNum1 = myParse.getOpNum1(curExpr);
        Expr func1 = myParse.transToFunction(hashMap,opNum1);
        Expr newFunc = func1.negFunc();
    
        //得到新key，新加入一个function进hashmap
        String thisKey = myParse.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
    
    public void opMul(HashMap<String, Expr> hashMap, String curExpr) {
        Parser myParse = new Parser();
        
        String opNum1 = myParse.getOpNum1(curExpr);
        String opNum2 = myParse.getOpNum2(curExpr);
        Expr func1 = myParse.transToFunction(hashMap,opNum1);
        Expr func2 = myParse.transToFunction(hashMap,opNum2);
        Expr newFunc = func1.mulFunc(func2);
    
        String thisKey = myParse.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
   
    public void opPow(HashMap<String, Expr> hashMap, String curExpr) {
        Parser myParse = new Parser();
    
        String opNum1 = myParse.getOpNum1(curExpr);
        String opNum2 = myParse.getOpNum2(curExpr);
        Expr func1 = myParse.transToFunction(hashMap,opNum1);
        Expr newFunc = func1.powFunc(Integer.parseInt(opNum2));
    
        String thisKey = myParse.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
    
    public void opNewNum(HashMap<String, Expr> hashMap, String curExpr) {
        Parser myParse = new Parser();
        
        //就把数加到里边就ok了
        String opNum1 = myParse.getSpecialNum(curExpr);
        Expr newFunc = new Expr(opNum1);
    
        //得到新key，新加入一个function进hashmap
        String thisKey = myParse.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
    
}
