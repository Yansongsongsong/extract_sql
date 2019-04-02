package yansong.extractor;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.csv.CSVRecord;
import yansong.extractor.libs.PlSqlLexer;
import yansong.extractor.libs.PlSqlParser;
import yansong.extractor.libs.PlSqlParserBaseListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author yansong
 * @Date 2019/2/27 23:48
 */
public class Main {
    public static void main(String [] args) throws Exception {
//        String str = "#1、集中交易插入沪港通及深港通 ( call:sqlserver_Database(INSERT INTO secuid ([serverid], [custid], [market], [secuid], [rptsecuid], [insidesecuid], [orgid], [name], [idtype], [idno], [regflag], [bondreg], [seat], [secucls], [foreignflag], [seculevel], [secuseq], [seculimit], [status], [creditflag], [opendate], [fullname], [secuotherkind1], [secuotherkind2], [secuotherkinds]) VALUES ('6', '@custid@', 'S', '@shenA@', '@shenA@', '@shenA@', '@orgid@', '@name@', '0', '@login_id_no@', 'B', '0', '      ', '0', '0', '0', '0', '', '0', '0', '20180612', '@name@', '0', '1', ''),p) call:sqlserver_Database(INSERT INTO secuid ([serverid], [custid], [market], [secuid], [rptsecuid], [insidesecuid], [orgid], [name], [idtype], [idno], [regflag], [bondreg], [seat], [secucls], [foreignflag], [seculevel], [secuseq], [seculimit], [status], [creditflag], [opendate], [fullname], [secuotherkind1], [secuotherkind2], [secuotherkinds]) VALUES ('6', '@custid@', '5', '@huA@', '@huA@', '@huA@', '@orgid@', '@name@', '0', '@login_id_no@', '1', '0', '      ', '0', '0', '0', '0', '', '0', '0', '20180612', '@name@', '0', '1', ''),p) #2、综合理财插入沪港通及深港通 call:oracle_Database(insert into stockholder (BRANCH_NO, STOCK_ACCOUNT, EXCHANGE_TYPE, BUSIN_ACCOUNT, BUSINSYS_ID, INTERNAL_ACCOUNT, CLIENT_ID, MAIN_FLAG, ASSET_PROP, ORDINAL, ID_KIND, ID_NO, HOLDER_NAME, FULL_NAME, HOLDER_KIND, HOLDER_LEVEL, REPORT_LEVEL, HOLDER_STATUS, HOLDER_RIGHTS, HOLDER_RESTRICTION, REGFLAG, SEAT_NO, BONDREG, OPEN_DATE, POSITION_STR, ACODE_ACCOUNT) values (3609, '@shenA@', 'S', '@busin_account@', 3, '@shenA@', '@client_id@', '1', '0', 0, '0', '@login_id_no@', '@name@', '@name@', '0', '0', ' ', '0', ' ', ' ', '1', ' ', 'd', 20180612, '03609000336090000072997003000S0100001789', ' '),HS_ACCT) call:oracle_Database(insert into stockholder (BRANCH_NO, STOCK_ACCOUNT, EXCHANGE_TYPE, BUSIN_ACCOUNT, BUSINSYS_ID, INTERNAL_ACCOUNT, CLIENT_ID, MAIN_FLAG, ASSET_PROP, ORDINAL, ID_KIND, ID_NO, HOLDER_NAME, FULL_NAME, HOLDER_KIND, HOLDER_LEVEL, REPORT_LEVEL, HOLDER_STATUS, HOLDER_RIGHTS, HOLDER_RESTRICTION, REGFLAG, SEAT_NO, BONDREG, OPEN_DATE, POSITION_STR, ACODE_ACCOUNT) values (3609, '@huA@', '5', '@busin_account@', 3, '@insidesecuid_huA@', '@client_id@', '1', '0', 0, '0', '@login_id_no@', '@name@', '@name@', '0', '0', ' ', '0', ' ', ' ', '1', ' ', 'd', 20180612, '036090003360900000729970030005A000001788', ' '),HS_ACCT) #3、集中交易证券账户销户 call:sqlserver_Database(UPDATE secuid set status ='3' where custid='@custid@',p) #4、综合理财证券账户销户 call:oracle_Database(update stockholder set HOLDER_STATUS ='3' where client_id = '@client_id@',HS_ACCT)"
//                .replaceAll("(call:[\\w|\\_]+\\()", "\n");
//        System.out.println(str);
//
//        ANTLRInputStream antlrInputStream = new ANTLRInputStream(str);
//
//
//        PlSqlLexer lexer = new PlSqlLexer(antlrInputStream);
//
//        CommonTokenStream tokens = new CommonTokenStream( lexer );
//        PlSqlParser parser = new PlSqlParser( tokens );
//        PlSqlParser.Sql_scriptContext ctx = parser.sql_script();
//        ParseTree tree = parser.sql_script();
//        TokenStream ts = parser.getTokenStream();
//
//        for (PlSqlParser.Unit_statementContext rctx: ctx.unit_statement()) {
//            try{
//                System.out.println(ts.getText(rctx.data_manipulation_language_statements()));
//            }catch (NullPointerException e){
//            }
//        }
        List<String[]> list = new ArrayList<String[]>();

        try {
            List<CSVRecord> read = CSVHelper.read("/Users/yansong/analysis/out/table_names.csv", false);
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

        CSVHelper.write("/Users/yansong/analysis/extract_sql/ExtractSQLProject/test1.csv",
                new String[] {"sources", "sqls"},
                list);

    }

}
