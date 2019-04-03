import org.junit.Test;
import yansong.extractor.Main;
import yansong.extractor.Prompt;
import yansong.extractor.Utils;

/**
 * @Author yansong
 * @Date 2019/4/2 22:18
 */
public class UtilsTest {
    @Test
    public void Test(){
        System.out.println(Utils.checkFile("/Users/yansong/analysis/extract_sql/ExtractSQLProject/pom.xml"));
    }

    @Test
    public void Test0(){
        Main.main(new String[]{"-csv", "/Users/yansong/analysis/extract_sql/ExtractSQLProject/pom.xml"});
    }

    @Test
    public void Test1(){
        Main.main(new String[]{"-csv",
                "/Users/yansong/analysis/extract_sql/ExtractSQLProject/pom.xml",
                "-out", "./test.csv"});
    }
}
