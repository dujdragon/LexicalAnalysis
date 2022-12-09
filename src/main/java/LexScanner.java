
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 扫描器
 */
public class LexScanner {
    // 定义类别名称
    private final String[] className = new String[1000];
    // 定义符号表
    private static final HashMap<String, Integer> symbolTable = new HashMap<>();
    // 预处理结果
    private final ArrayList<String> preProcessRes;
    // 扫描结果保存到token数组中
    public ArrayList<Token> scanRes = new ArrayList<>();
    // 错误结果保存到另一个token数组中
    public ArrayList<Token> errRes = new ArrayList<>();

    public LexScanner(ArrayList<String> preProcessRes) {
        this.preProcessRes  = preProcessRes;
        initTable();
    }
    /**
     * 初始化符号表和名称表
     */
    public void initTable() {
        symbolTable.put("标识符", 1);
        symbolTable.put("常数", 2);
        symbolTable.put("auto", 3);
        symbolTable.put("break", 4);
        symbolTable.put("case", 5);
        symbolTable.put("char", 6);
        symbolTable.put("const", 7);
        symbolTable.put("continue", 8);
        symbolTable.put("default", 9);
        symbolTable.put("do", 10);
        symbolTable.put("double", 11);
        symbolTable.put("else", 12);
        symbolTable.put("enum", 13);
        symbolTable.put("extern", 14);
        symbolTable.put("float", 15);
        symbolTable.put("for", 16);
        symbolTable.put("goto", 17);
        symbolTable.put("if", 18);
        symbolTable.put("int", 19);
        symbolTable.put("long", 20);
        symbolTable.put("register", 21);
        symbolTable.put("return", 22);
        symbolTable.put("short", 23);
        symbolTable.put("signed", 24);
        symbolTable.put("sizeof", 25);
        symbolTable.put("static", 26);
        symbolTable.put("struct", 27);
        symbolTable.put("switch", 28);
        symbolTable.put("typedef", 29);
        symbolTable.put("union", 30);
        symbolTable.put("unsigned", 31);
        symbolTable.put("void", 32);
        symbolTable.put("volatile", 33);
        symbolTable.put("while", 34);
        symbolTable.put("+", 35);
        symbolTable.put("-", 36);
        symbolTable.put("*", 37);
        symbolTable.put("/", 38);
        symbolTable.put("=", 39);
        symbolTable.put("<", 40);
        symbolTable.put(">", 41);
        symbolTable.put("&", 42);
        symbolTable.put("|", 43);
        symbolTable.put("(", 44);
        symbolTable.put(")", 45);
        symbolTable.put("{", 46);
        symbolTable.put("}", 47);
        symbolTable.put(";", 48);
        symbolTable.put(",", 49);

        className[1] = "标识符";
        className[2] = "常数";
        for (int i = 3; i <= 34; i ++ ) className[i] = "关键字";
        for (int i = 35; i <= 43; i ++ ) className[i] = "运算符";
        for (int i = 44; i <= 49; i ++ ) className[i] = "界限符";
    }

    public void beginScan() {
        if (preProcessRes == null) return;
        // 遍历所有句子
        for (int i = 0; i < preProcessRes.size(); i ++ ) {
            // 获得句子
            String phrase = preProcessRes.get(i);
            // 遍历句子
            for (int j = 0; j < phrase.length(); ) {
                // 获得一个字符
                char c = phrase.charAt(j);
                if (c == '#') return;
                char[] buffer = new char[100];
                int p = 0;
                if (Character.isAlphabetic(c)) { // 如果是一个字母
                    // 找到后续的数字或者字母
                    buffer[p ++ ] = c;
                    j ++ ;  // 指针往后移动一个单位
                    boolean isKey = false;
                    // 如果有下一个字符，并且字符为数字或者数字就继续收集
                    while (j < phrase.length() &&
                            (Character.isAlphabetic(phrase.charAt(j)) || Character.isDigit(phrase.charAt(j)))) {
                        buffer[p ++ ] = phrase.charAt(j ++ );
                        // 判读一下收集的过程是否出现了关键字
                        buffer[p] = '\0';
                        String s = buildString(buffer);
                        Object m = symbolTable.get(s);
                        if (m != null) { // 如果是关键字
                            isKey = true;
                            int code = symbolTable.get(s); // 获得编码
                            String type = className[code];  // 获得类型
                            boolean isLegal = true;
                            // 将结果保存到token中并加入到结果集中
                            scanRes.add(new Token(scanRes.size() + 1, s, code, type, i + 1, isLegal));
                            break;
                        }
                    }
                    if (isKey) continue;
                    // 整合成字符串
                    String s = buildString(buffer);

                    //  先看看是不是关键字
                    if (symbolTable.get(s) != null) { // 如果是关键字
                        int code = symbolTable.get(s); // 获得编码
                        String type = className[code];  // 获得类型
                        boolean isLegal = true;
                        // 将结果保存到token中并加入到结果集中
                        scanRes.add(new Token(scanRes.size() + 1, s, code, type, i + 1, isLegal));
                    } else { // 如果不是关键字，就当成标识符
                        int code = symbolTable.get("标识符"); // 获得编码
                        String type = className[code];  // 获得类型
                        boolean isLegal = true;
                        // 将结果保存到token中并加入到结果集中
                        scanRes.add(new Token(scanRes.size() + 1, s, code, type, i + 1, isLegal));
                    }
                }
                else if (Character.isDigit(c)) { // 如果字符是一个数字
                    // 将数字字符放到buffer中索引后移，句子指针后移
                    buffer[p ++ ] = c;
                    j ++ ;
                    // 将后面所有的数字字符加到buffer中
                    while (j < phrase.length() && Character.isDigit(phrase.charAt(j))) {
                        buffer[p ++ ] = phrase.charAt(j ++ );
                    }
                    // 数字如果放完后下一个字符是点(.)的话
                    if (phrase.charAt(j) == '.') {
                        // 将点加到buffer中
                        buffer[p ++ ] = phrase.charAt(j ++ );
                        // 如果下一个字符不是数字，则会报错，指针不移动
                        if (!Character.isDigit(phrase.charAt(j))) {
                            String s = buildString(buffer);
                            boolean isLegal = false;
                            // 将结果保存到token中并加入到结果集中
                            errRes.add(new Token(errRes.size() + 1, s, 0, "非法数字", i + 1, isLegal));
                        } else { // 如果是数字，则将后面的字符继续加到buffer中
                            while (Character.isDigit(phrase.charAt(j))) {
                                buffer[p ++ ] = phrase.charAt(j ++ );
                            }
                            String s = buildString(buffer);
                            int code = symbolTable.get("常量"); // 获得编码
                            String type = className[code];  // 获得类型
                            boolean isLegal = true;
                            // 将结果保存到token中并加入到结果集中
                            scanRes.add(new Token(scanRes.size() + 1, s, code, type, i + 1, isLegal));
                        }
                    } else {
                        String s = buildString(buffer);
                        int code = symbolTable.get("常数"); // 获得编码
                        String type = className[code];  // 获得类型
                        boolean isLegal = true;
                        // 将结果保存到token中并加入到结果集中
                        scanRes.add(new Token(scanRes.size() + 1, s, code, type, i + 1, isLegal));
                    }
                }
                else if (isOperator(c) || isBounder(c)) {
                    buffer[p ++ ] = c;
                    j ++ ;
                    String s = buildString(buffer);
                    int code = symbolTable.get(s); // 获得编码
                    String type = className[code];  // 获得类型
                    boolean isLegal = true;
                    // 将结果保存到token中并加入到结果集中
                    scanRes.add(new Token(scanRes.size() + 1, s, code, type, i + 1, isLegal));
                } else {
                    buffer[p ++ ] = c;
                    j ++ ;
                    String s = buildString(buffer);
                    // 出现其他符号则报错
                    boolean isLegal = false;
                    // 将结果保存到token中并加入到结果集中
                    errRes.add(new Token(errRes.size() + 1, s, 0, "非法字符", i + 1, isLegal));
                }
            }
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c== '/' || c == '=' || c == '<' || c == '>' || c == '&' || c == '|';
    }

    private boolean isBounder(char c) {
        return c == '(' || c == ')' || c == '{' || c == '}' || c == ';' || c == ',';
    }

    private String buildString(char[] chars) {
        StringBuilder builder = new StringBuilder();
        for (char aChar : chars) {
            if (aChar != '\0')
                builder.append(aChar);
        }
        return builder.toString();
    }

}
