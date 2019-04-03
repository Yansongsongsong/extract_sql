package yansong.extractor;

import org.apache.commons.cli.*;

import java.util.Arrays;

/**
 * @Author yansong
 * @Date 2019/4/2 22:05
 */
public class Prompt {
    interface Callback {
        void next(String csv, String out, String[] col, String[] uCol);
    }

    private Callback callback;

    public Prompt next(Callback c){
        this.callback = c;
        return this;
    }

    @SuppressWarnings({ "deprecation", "static-access" })
    public void cli(String[] args){
        Option help = new Option( "help", "print this message" );

        Option inputFile = OptionBuilder.withArgName( "file" )
                .hasArg()
                .withDescription( "required. the path for .csv file to be processed" )
                .create( "csv");

        Option outputFile = OptionBuilder.withArgName( "file" )
                .hasArg()
                .withDescription( "optional. the path for file to store sql" )
                .create( "out" );

        Option exceptColumn = OptionBuilder
                .withArgName( "column" )
                .hasArgs()
                .withDescription( "optional. these columns will be ignored when processing.\n count from 0. " +
                        "e.g. -COL 1 2\n" +
                        "if you both use -col and -COL, just use -col" )
                .create( "COL" );

        Option expectColumn = OptionBuilder.withArgName( "column" )
                .hasArgs()
                .withDescription( "optional. only these columns will be considered when processing. count from 0. " +
                        "e.g. -col 1 2\n" +
                        "if you both use -col and -COL, just use -col" )
                .create( "col" );

        Options options = new Options();

        options.addOption( help );
        options.addOption(expectColumn);
        options.addOption(exceptColumn);
        options.addOption( inputFile );
        options.addOption( outputFile );

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse( options, args );
            if( line.hasOption("help") || line.getOptions().length == 0){
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "extractSQL", options );
                return;
            }

            if( !line.hasOption( "csv" ) ) {
                System.out.println("Please give the path for .csv file");
                return;
            }

            String csvFile = line.getOptionValue("csv");
            String outFile = line.getOptionValue("out");
            outFile = outFile == null? "./out.csv": outFile;

            if(callback == null){
                return;
            }

            if( line.hasOption( "col" ) ) {
                // col exists, just process with cols
                callback.next(csvFile, outFile, line.getOptionValues("col"), null);
            } else {
                if( line.hasOption( "COL" ) ) {
                    // col doesn't exist, process with COL
                    callback.next(csvFile, outFile, null, line.getOptionValues("COL"));
                } else {
                    // have no definition for cols, process with all col
                    callback.next(csvFile, outFile, null, null);
                }
            }
        }catch( ParseException exp ) {
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        }
    }
}
