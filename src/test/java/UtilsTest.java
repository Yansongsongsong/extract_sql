import org.junit.Test;
import yansong.extractor.Main;
import yansong.extractor.Prompt;
import yansong.extractor.Utils;

import java.io.File;

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
        File file = new File("./out.csv");
        file.delete();
    }

    @Test
    public void Test1(){
        Main.main(new String[]{"-csv",
                "/Users/yansong/analysis/extract_sql/ExtractSQLProject/pom.xml",
                "-out", "./test.csv"});
        File file = new File("./test.csv");
        file.delete();
    }

    @Test
    public void Test2(){
        Main.main(new String[]{"-csv",
                this.getClass().getResource("test.csv").getPath(),
                "-out", "./test.csv"});
        File file = new File("./test.csv");
        file.delete();
    }

    @Test
    public void Test3(){
        Main.main(new String[]{"-csv",
                this.getClass().getResource("test.csv").getPath(),
                "-out", "./test.csv",
                "-col", "1", "2"});
        File file = new File("./test.csv");
        file.delete();
    }
    @Test
    public void Test4(){
        Main.main(new String[]{"-csv",
                this.getClass().getResource("test.csv").getPath(),
                "-out", "./test.csv",
                "-COL", "1", "2"});
        File file = new File("./test.csv");
        file.delete();
    }
}
