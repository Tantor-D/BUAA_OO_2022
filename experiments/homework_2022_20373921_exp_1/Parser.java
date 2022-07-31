import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private final Pattern numberPattern = Pattern.compile("^[0-9]*$"); // TODO
    private final HashMap<String, Integer> parameters;
    
    public Parser(HashMap<String, Integer> parameters) {
        this.parameters = parameters;
    }
    
    public Operator parse(String expression) {
        int position = findAddOrSub(expression);
        if (position != -1) {
            if (expression.charAt(position) == '+') {
                return new Add(parse(expression.substring(0, position)),
                        parse(expression.substring(position + 1)));
            } else {
                return new Sub(parse(expression.substring(0, position)),
                        parse(expression.substring(position + 1)));
            }
        } else {
            position = findMul(expression);
            if (position != -1) {
                return new Mul(parse(expression.substring(0, position))
                        , parse(expression.substring(position + 1)));
            } else {
                if (!expression.equals("")) {
                    Matcher matcher = numberPattern.matcher(expression);
                    if (matcher.matches()) { //数字
                        return new Num(Integer.parseInt(expression));
                    }
                    else {
                        for (String kk : parameters.keySet()) {
                            if (kk.equals(expression)) {
                                return new Num(parameters.get(kk));
                            }
                        }
                        return new Num(0); // default
                    }
                    // TODO 应该就是返回一个Num类回去，因为所有的加法在此之前都处理完了

                } else {
                    return new Num(0);
                }
            }
        }
    }

    private int findAddOrSub(String expression) {
        int position = -1;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '+' || expression.charAt(i) == '-') {
                position = i;
            }
        }
        return position;
    }

    private int findMul(String expression) {
        int position = -1;   // TODO
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '*') {
                position = i;
            }
        }
        return position;
    }
}
