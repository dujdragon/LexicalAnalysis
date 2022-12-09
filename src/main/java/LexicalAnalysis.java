

import org.apache.commons.lang.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class LexicalAnalysis {
    public static void main(String[] args) {
        // 首先获得要处理的字符串
        /*
        String CFile = "void main()\n" +
                "{\n" +
                "  int sum = 0, a = 5, b = 4, c;\n" +
                "  for (int i = 1; i < 11; i++)\n" +
                "  {\n" +
                "    sum = sum + i;\n" +
                "    for (int i = 1; i < 11; i++)\n" +
                "    {\n" +
                "      a = (1 + 2) * 3;\n" +
                "    }\n" +
                "  }\n" +
                "  if (a > b)\n" +
                "  {\n" +
                "    c = 0;\n" +
                "    if (a > c)\n" +
                "    {\n" +
                "      c = 0;\n" +
                "    }\n" +
                "    else\n" +
                "    {\n" +
                "      c = 1;\n" +
                "    }\n" +
                "  }\n" +
                "  else\n" +
                "  {\n" +
                "    c = 1;\n" +
                "  }\n" +
                "}\n" +
                "#";

         */
        // 将输入变成文件输入
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("请输入文件的绝对路径");
//        String path = scanner.next();
        String path = "D:\\course\\Compiler\\LexicalAnalysis\\a.c";
        Path p = Path.of(path);
        String CFile = null;
        try {
            CFile = Files.readString(p);
        } catch (IOException e) {
            System.out.println("文件路径错误，找不到该文件！");
            return;
        }
        /*
         * 然后对字符串进行预处理
         * 1. 按照\n将字符串分为若干个句子，删除每一行的注解并将句子保存到句子列表中
         * 2. 对每个句子去除其空格
         * 预处理的结果为不包含空格的句子列表
         */
        PreProcessor preProcessor = new PreProcessor(CFile);
        ArrayList<String> phraseList = preProcessor.preProcessRes;
        /*
         * 对句子列表进行遍历扫描
         * 每个句子有一个指针来标记扫描的位置
         * 对每个句子，从指针位置开始判断，并按照流程图进行判断
         * 如果
         */
        LexScanner lexScanner = new LexScanner(phraseList);
        lexScanner.beginScan();
        /*
         * 扫描结束后获得结果集和错误集
         */
        ArrayList<Token> scanRes = lexScanner.scanRes;
        ArrayList<Token> errRes = lexScanner.errRes;

        // 格式化输出
        StringBuilder res = new StringBuilder();
        res.append("对于文件 " + p + " 进行词法分析的结果：\n");
        String title = StringUtils.rightPad("单词序号", 8) +
                StringUtils.rightPad("单词的值", 8) +
                StringUtils.rightPad("单词编码", 8) +
                StringUtils.rightPad("单词类型", 8) +
                StringUtils.rightPad("单词所在行", 8) +
                StringUtils.rightPad("是否合法", 8) + "\n";
        res.append(title);
        for (Token t : scanRes) res.append(t.toString());
        if (errRes.size() > 0) {
            res.append("Error！ 以下为错误信息\n");
            for (Token t : errRes) res.append(t.toString());
        }

        // 输出到文件
        System.out.println(res);
        String outputPath = "D:\\course\\Compiler\\LexicalAnalysis\\" + p.getFileName() + ".txt";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputPath,true);
            fos.write(res.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
