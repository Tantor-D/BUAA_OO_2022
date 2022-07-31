import java.math.BigInteger;
import java.util.HashMap;

public class MyTool {
    private final Parser myParser = new Parser();
    
    public void opAdd(HashMap<String, Function> hashMap, String curExpr) {
        String opNum1 = myParser.getOpNum1(curExpr);
        String opNum2 = myParser.getOpNum2(curExpr);
        Function func1 = myParser.transToFunction(hashMap, opNum1);
        Function func2 = myParser.transToFunction(hashMap, opNum2);
        Function newFunc = func1.addFunc(func2);
        
        String thisKey = myParser.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
    
    public void opPos(HashMap<String, Function> hashMap, String curExpr) {
        //取出操作数，是func就直接copy，是数字的话生成后直接copy
        String opNum1 = myParser.getOpNum1(curExpr);
        Function func1 = myParser.transToFunction(hashMap, opNum1);
        Function newFunc = func1.posFunc(); // 此函数就是为了新建一个实例化对象
        
        //得到新key，新加入一个function进hashmap
        String thisKey = myParser.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
    
    public void opNeg(HashMap<String, Function> hashMap, String curExpr) {
        String opNum1 = myParser.getOpNum1(curExpr);
        Function func1 = myParser.transToFunction(hashMap, opNum1);
        Function newFunc = func1.negFunc();
        
        String thisKey = myParser.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
    
    public void opSub(HashMap<String, Function> hashMap, String curExpr) {
        String opNum1 = myParser.getOpNum1(curExpr);
        String opNum2 = myParser.getOpNum2(curExpr);
        Function func1 = myParser.transToFunction(hashMap, opNum1);
        Function func2 = myParser.transToFunction(hashMap, opNum2);
        Function newFunc = func1.subFunc(func2);
        
        String thisKey = myParser.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
    
    public void opMul(HashMap<String, Function> hashMap, String curExpr) {
        String opNum1 = myParser.getOpNum1(curExpr);
        String opNum2 = myParser.getOpNum2(curExpr);
        Function func1 = myParser.transToFunction(hashMap, opNum1);
        Function func2 = myParser.transToFunction(hashMap, opNum2);
        Function newFunc = func1.mulFunc(func2);
    
        String thisKey = myParser.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
    
    public void opPow(HashMap<String, Function> hashMap, String curExpr) {
        String opNum1 = myParser.getOpNum1(curExpr);
        String opNum2 = myParser.getOpNum2(curExpr);
        Function func1 = myParser.transToFunction(hashMap, opNum1);
        
        Function newFunc = func1.powFunc(new BigInteger(opNum2));
    
        String thisKey = myParser.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
        
    }
    
    public void opNewNum(HashMap<String, Function> hashMap, String curExpr) {
        String opNum = myParser.getSpecialNum(curExpr);
        Function newFunc = new Function(opNum);
    
        String thisKey = myParser.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
    
    public void opSin(HashMap<String, Function> hashMap, String curExpr) {
        String opNum = myParser.getOpNum1(curExpr);
        //Function sinNumFunc = new Function(opNum);
        Function sinNumFunc = myParser.transToFunction(hashMap, opNum);
        Function newFunc = sinNumFunc.sinFunc();
    
        String thisKey = myParser.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
    
    public void opCos(HashMap<String, Function> hashMap, String curExpr) {
        String opNum = myParser.getOpNum1(curExpr);
        //Function cosNumFunc = new Function(opNum);
        Function cosNumFunc = myParser.transToFunction(hashMap, opNum);
        Function newFunc = cosNumFunc.cosFunc();
    
        String thisKey = myParser.getFx(curExpr);
        hashMap.put(thisKey, newFunc);
    }
}
