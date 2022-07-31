import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class MainClass {
    public static void main(String[] args) throws FileNotFoundException {
        PrintStream fileOut =new PrintStream(new FileOutputStream
                ("E:\\software_data\\OO_task\\homework\\tester_of_homework9\\input\\in.txt"));
        System.setOut(fileOut);
        int lines = 1000;

        while (lines-- != 0) {
            int op = getRN(0, 8);

            if (op == 0) { // 真正加入的Person id 范围比查询时的范围略小，用于测异常
                System.out.println("ap " + getRN(-5, 5) + " " + getRS() + " " + getRN(0, 2));
            } else if (op == 1) {
                System.out.println("ar " + getRN(-6, 6) + " " + getRN(-6, 6) + " " + getRN(0, 1));
            } else if (op == 2) {
                System.out.println("qv " + getRN(-6, 6) + " " + getRN(-7, 6));
            } else if (op == 3) {
                System.out.println("qps");
            } else if (op == 4) {
                System.out.println("qci " + getRN(-6, 6) + " " + getRN(-6, 6));
            } else if (op == 5) {
                System.out.println("qbs");
            } else if (op == 6) { // 真正加入的Group id在[-2,2]，查询时的范围略大，用于测异常
                System.out.println("ag " + getRN(-2, 2));
            } else if (op == 7) {
                System.out.println("atg " + getRN(-6, 6) + " " + getRN(-3, 3));
            } else {
                System.out.println("dfg " + getRN(-6, 6) + " " + getRN(-3, 3));
            }

        }
    }


    //返回一个 [x,y] 的整数
    public static int getRN(int x, int y) {
        return x + (int)(Math.random() * (y - x + 1));
    }

    //返回一个长度10以内的字符串
    public static String getRS() {
        int length = getRN(1, 10);
        StringBuilder str = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            str.append((char)(getRN(0, 25) + 'a'));
        }
        return str.toString();
    }
}
