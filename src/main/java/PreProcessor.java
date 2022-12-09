

import java.util.ArrayList;

/**
 * 预处理器，用于预处理输入字符串
 */
public class PreProcessor {
    // 用于保存预处理结果
    public ArrayList<String> preProcessRes = new ArrayList<>();

    public PreProcessor(String in) {
        // 将结果按照行数来划分
        String[] strings = in.split("\r\n");
        // 对每一行，删去其空格
        for (String s : strings) {
            String replace = s.replace(" ", "");
            // 保存到结果集中
            preProcessRes.add(replace);
        }
    }
}
