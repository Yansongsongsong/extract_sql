import yansong.extractor.Prompt;
import org.junit.Test;

/**
 * @Author yansong
 * @Date 2019/4/2 22:18
 */
public class PromptTest {

    @Test
    public void main(){
        Prompt prompt = new Prompt();
        prompt.cli(new String[]{"-help"});
        prompt.cli(new String[]{"-col", "123123", "123123"});
        prompt.cli(new String[]{"-COL", "123123", "123123"});
        prompt.cli(new String[]{"-csv", "file", "-col", "12", "1234"});
    }
}
