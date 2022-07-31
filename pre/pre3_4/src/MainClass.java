import java.util.ArrayList;
import java.util.Scanner;

/*
要能够输入信息，并将信息进行切割，分成不同的消息。
分完之后，需要可以进行匹配
预想，一个工具tool，里面有不同的pattern，通过使用tool来进行处理

在主类中判断是哪一种询问，然后直接调用tool来查询
*/
public class MainClass {
    public static void main(String[] args) {
    
        Scanner scanner = new Scanner(System.in);
        
        
        ArrayList<String> strList = new ArrayList<>();
        String strIn;
        ToolToDo myTool = new ToolToDo();
        
        //将输入处理成strList
        while (!(strIn = scanner.nextLine()).equals("END_OF_MESSAGE")) {
            String[] strSplit = strIn.split(";");
            for (String ss : strSplit) {
                String s = ss.trim();
                if (!s.isEmpty()) {
                    strList.add(s.concat(";"));
                }
            }
        }
        
        // 要不要输出
        while (scanner.hasNext()) {
            String ask = scanner.nextLine();
            for (String mes : strList) {
                if (myTool.judgeTheMes(mes, ask)) {
                    System.out.println(mes);
                }
            }
            //System.out.println("New Ask Please");
        }
    }
}
