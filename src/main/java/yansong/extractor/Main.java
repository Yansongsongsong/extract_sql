package yansong.extractor;

import java.io.IOException;

/**
 * @Author yansong
 * @Date 2019/2/27 23:48
 */
public class Main {
    public static void main(String [] args) {
        Prompt prompt = new Prompt().next(new Prompt.Callback() {
            @Override
            public void next(String csv, String out, String[] col, String[] uCol) {
                try {
                    Utils.facade(csv, out, col, uCol);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        prompt.cli(args);
    }

}
