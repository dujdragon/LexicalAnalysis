/**
 * 保存输出的格式
 */
import org.apache.commons.lang.StringUtils;
public class Token {
    // 表示第几个token
    int id;
    // token的值
    String value;
    // 编码
    int code;
    // token的类型
    String type;
    // token的行号
    int index;
    // 表示是否合法
    boolean isLegal;

    public Token(int id, String value, int code, String type, int index, boolean isLegal) {
        this.id = id;
        this.value = value;
        this.code = code;
        this.type = type;
        this.index = index;
        this.isLegal = isLegal;
    }

    @Override
    public String toString() {
        return StringUtils.rightPad(id + "", 11) +
                StringUtils.rightPad(value, 10) +
                StringUtils.rightPad(code + "", 11) +
                StringUtils.rightPad(type, 9) +
                StringUtils.rightPad(index + "", 11) +
                StringUtils.rightPad(isLegal ? "合法" : "不合法", 11) + "\n";
    }
}
