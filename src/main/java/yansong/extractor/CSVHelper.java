package yansong.extractor;

import org.apache.commons.cli.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @Author yansong
 * @Date 2019/4/2 22:05
 */
public class CSVHelper {
    /**
     * @param csvFile CSV文件
     * @param removeHeader 是否抛弃header
     * @return CSV记录链表
     * @throws IOException
     */
    public static List<CSVRecord> read(final String csvFile, final boolean removeHeader) throws IOException {
        CSVFormat format = CSVFormat.EXCEL;
        Reader reader = Files.newBufferedReader(Paths.get(csvFile));
        CSVParser csvParser = new CSVParser(reader, format);
        List<CSVRecord> list = csvParser.getRecords();
        if (removeHeader){
            list.remove(0);
        }
        return list;
    }

    /**
     * 写CSV文件
     * @param csvFile csv文件名称
     * @param fileHeader 文件头
     * @param content 内容
     * @throws IOException
     */
    public static void write(final String csvFile, final String[] fileHeader, List<String[]> content) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvFile));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printRecord(fileHeader);
        for (String[] c : content) {
            csvPrinter.printRecord(Arrays.asList(c));
        }
        csvPrinter.flush();
    }

//    public static void main(String[] args) throws IOException {
//        List<String[]> list = new ArrayList<String[]>();
//        list.add(new String[] {"1", "Sundar Pichai ♥", "CEO", "Google"});
//        list.add(new String[] {"2", "Satya Nadella", "CEO", "Microsoft"});
//        list.add(new String[] {"3", "Tim cook", "CEO", "Apple"});
//        list.add(new String[] {"4", "Mark Zuckerberg", "CEO", "Facebook"});
//        list.add(new String[] {"4", "Mark Zuckerberg", "CEO", "Facebook"});
//        list.add(new String[] {"4", "Mark Zuckerberg", "CEO", "Facebook"});
//        write("/Users/yansong/analysis/extract_sql/ExtractSQLProject/test1.csv",
//                new String[] {"sources", "sqls"},
//                list);
//
//
//        try {
//            List<CSVRecord> red = read("/Users/yansong/analysis/extract_sql/ExtractSQLProject/test1.csv", false);
//            for (CSVRecord csvRecord : red) {
//                // Accessing values by the names assigned to each column
//                System.out.println("Record No - " + csvRecord.getRecordNumber());
//                System.out.println("---------------");
//                Iterator<String> it = csvRecord.iterator();
//                while (it.hasNext()){
//                    System.out.println(it.next());
//                }
//                System.out.println("---------------\n\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

}

