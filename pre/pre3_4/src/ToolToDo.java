import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 此工具的函数能够读入需要匹配的东西并返回true 或 false
 */
public class ToolToDo {
    private static final String STRDATE = "(?<year>\\d{1,4})/(?<month>0?[1-9]|1[0-2])/" +
            "(?<day>[1,2]\\d|3[0,1]|0?\\d)";
    private static final String STRASKDATE = "(?<year>\\d{1,4}|)/(?<month>0?[1-9]|1[0-2]|)/" +
            "(?<day>[1,2]\\d|3[0,1]|0?\\d|)";
    private static final String STRSENDER = "-(?<sender>[A-Za-z0-9]+)[@:]";
    private static final String STRRECEIVER = "@(?<receiver>[A-Za-z0-9]+) ";
    private static final String STRTXT = ":\"(?<text>.*?)\";";
    private static final String STRA = "a{2,3}b{2,4}c{2,4}";
    private static final String STRB = "a{2,3}b{2,1000000}c{2,4}";
    private static final String STRADISCONTINUOUS = "(a.*?){2,3}(b.*?){2,4}(c.*?){2,4}";
    private static final String STRBDISCONTINUOUS = "(a.*?){2,3}(b.*?){2,1000000}(c.*?){2,4}";
    
    public boolean judgeTheMes(String mes, String ask) {
        if (ask.startsWith("qdate")) {
            return isTheDate(mes, ask.substring(6).trim());
        } else if (ask.startsWith("qsend")) {
            return isHeSend(mes, ask.substring(6).trim());
        } else if (ask.startsWith("qrecv")) {
            return isHeReceive(mes, ask.substring(6).trim());
        } else if (ask.startsWith("qmess")) {
            return isTheMess(mes, ask.charAt(6), ask.substring(8).trim());
        } else {
            System.out.println("Wrong Ask");
            return false;
        }
    }
    
    public boolean isTheMess(String mes, char kind, String ask) {
        Pattern patternToFindTxt = Pattern.compile(STRTXT);
        Matcher matcher = patternToFindTxt.matcher(mes);
        //拿到txt
        String txt;
        if (matcher.find()) {
            txt = matcher.group("text");
        } else {
            return false;
        }
        
        //一个个去match
        Pattern pattern;
        Matcher matcherTxt;
        switch (ask.trim()) {
            case "1":
                if (kind == 'A') {
                    pattern = Pattern.compile("^" + STRA);
                } else {
                    pattern = Pattern.compile("^" + STRB);
                }
                break;
            case "2":
                if (kind == 'A') {
                    pattern = Pattern.compile(STRA + "$");
                } else {
                    pattern = Pattern.compile(STRB + "$");
                }
                break;
            case "3":
                if (kind == 'A') {
                    pattern = Pattern.compile(STRA);
                } else {
                    pattern = Pattern.compile(STRB);
                }
                break;
            case "4":
                if (kind == 'A') {
                    pattern = Pattern.compile(STRADISCONTINUOUS);
                } else {
                    pattern = Pattern.compile(STRBDISCONTINUOUS);
                }
                break;
            default:
                return false;
        }
        matcherTxt = pattern.matcher(txt);
        return matcherTxt.find();
    }
    
    public boolean isTheDate(String mes, String date) { // 得到各自的年月日，然后比，当都匹配时输出
        Pattern patternDate = Pattern.compile(STRDATE);
        Matcher matcher = patternDate.matcher(mes);
        matcher.find();
        Pattern patternAskDate = Pattern.compile(STRASKDATE);
        Matcher matcherAsk = patternAskDate.matcher(date);
        matcherAsk.find();
        
        if (matcherAsk.group("year").isEmpty() ||
                Integer.parseInt(matcher.group("year")) ==
                        Integer.parseInt(matcherAsk.group("year"))) {
            if (matcherAsk.group("month").isEmpty() ||
                    Integer.parseInt(matcher.group("month")) ==
                            Integer.parseInt(matcherAsk.group("month"))) {
                if (matcherAsk.group("day").isEmpty() ||
                        Integer.parseInt(matcher.group("day")) ==
                                Integer.parseInt(matcherAsk.group("day"))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isHeSend(String mes, String senderName) {
        Pattern patternSender = Pattern.compile(STRSENDER);
        Matcher matcher = patternSender.matcher(mes);
        if (matcher.find()) {
            return matcher.group("sender").equals(senderName);
        }
        return false;
    }
    
    public boolean isHeReceive(String mes, String receiverName) {
        Pattern patternReceiver = Pattern.compile(STRRECEIVER);
        Matcher matcher = patternReceiver.matcher(mes);
        if (matcher.find()) {
            return matcher.group("receiver").equals(receiverName);
        }
        return false;
    }
    
}
