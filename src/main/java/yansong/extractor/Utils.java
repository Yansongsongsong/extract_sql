package yansong.extractor;

import org.apache.commons.cli.*;

import java.io.File;
import java.util.Arrays;

/**
 * @Author yansong
 * @Date 2019/4/2 22:05
 */
public class Utils {
    public static boolean checkNumber(String[] columns){
        for (String n :
                columns) {
            try{
                Integer.parseInt(n);
            } catch (NumberFormatException e){
                return false;
            }
        }

        return true;
    }

    public static boolean checkFile(String furl){
        File file = new File(furl);
        return file.exists();
    }
}
