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
    
    public boolean judgeTheMes(String mes, String ask) {
        if (ask.startsWith("qdate")) {
            return isTheDate(mes, ask.substring(6).trim());
        } else if (ask.startsWith("qsend")) {
            return isHeSend(mes, ask.substring(6).trim());
        } else if (ask.startsWith("qrecv")) {
            return isHeReceive(mes, ask.substring(6).trim());
        } else {
            System.out.println("Wrong Ask");
            return false;
        }
        
    }
    
    /**
     * @param mes  要匹配的信息
     * @param date 保证了一定是两边都是空格的一个日期
     * @return 是不是匹配的，即要不要输出
     */
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
