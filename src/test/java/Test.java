import yansong.extractor.Prompt;

/**
 * @Author yansong
 * @Date 2019/4/2 22:18
 */
public class Test {
    public static void main(String[] args){
        Prompt.cli(new String[]{"-help"});
        Prompt.cli(new String[]{"-col", "123123", "123123"});
        Prompt.cli(new String[]{"-COL", "123123", "123123"});
        Prompt.cli(new String[]{"-a", "123123", "123123"});
    }
}
