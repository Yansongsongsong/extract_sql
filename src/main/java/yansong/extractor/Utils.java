package yansong.extractor;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.cli.*;
import org.apache.commons.csv.CSVRecord;
import yansong.extractor.libs.PlSqlLexer;
import yansong.extractor.libs.PlSqlParser;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @Author yansong
 * @Date 2019/4/2 22:05
 */
public class Utils {
    public static void facade(String inputFile, String outputFile) throws IOException {
        List<String[]> list = new ArrayList<String[]>();

        try {
            List<CSVRecord> read = CSVHelper.read(inputFile, false);
            for (CSVRecord csvRecord : read) {
                // Accessing values by the names assigned to each column
                Iterator<String> it = csvRecord.iterator();
                while (it.hasNext()){
                    String[] row = Utils.extractSQL(it.next());
                    if(row.length != 0){
                        list.add(row);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        CSVHelper.write(outputFile, new String[] {"sources", "sqls"}, list);
        CSVHelper.write(outputFile, new String[]{}, list);
    }

    public static boolean checkNumber(String[] columns){
        for (String n : columns) {
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

    public static String[] extractSQL(String text){
        String str = text.replaceAll("(call:[\\w|\\_]+\\()", "\n");
        ANTLRInputStream antlrInputStream = new ANTLRInputStream(str);


        PlSqlLexer lexer = new PlSqlLexer(antlrInputStream);

        CommonTokenStream tokens = new CommonTokenStream( lexer );
        PlSqlParser parser = new PlSqlParser( tokens );
        PlSqlParser.Sql_scriptContext ctx = parser.sql_script();
        ParseTree tree = parser.sql_script();
        TokenStream ts = parser.getTokenStream();

        List<String> list = new LinkedList<String>();

        for (PlSqlParser.Unit_statementContext rctx: ctx.unit_statement()) {
            try{
                String sql = ts.getText(rctx.data_manipulation_language_statements());
                if(sql.length() != 0 && !sql.equals("(") && !sql.equals("(@")){
                    list.add(sql);
                }
            }catch (NullPointerException e){
            }
        }

        String[] res = new String[list.size()];
        return list.toArray(res);
    }
}
