package yansong.extractor;

import org.apache.commons.cli.*;

import java.util.Arrays;

/**
 * @Author yansong
 * @Date 2019/4/2 22:05
 */
public class Prompt {
    @SuppressWarnings({ "deprecation", "static-access" })
    public static void cli(String[] args){
        Option help = new Option( "help", "print this message" );

        Option inputFile = OptionBuilder.withArgName( "file" )
                .hasArg()
                .withDescription(  "the path for .csv file to be processed" )
                .create( "csv");

        Option outputFile = OptionBuilder.withArgName( "file" )
                .hasArg()
                .withDescription( "the path for file to store sql" )
                .create( "out" );

        Option exceptColumn = OptionBuilder
                .withArgName( "column" )
                .hasArgs()
                .withDescription( "these columns will be ignored when processing.\n count from 0. " +
                        "e.g. -COL 1 2\n" +
                        "if you both use -col and -COL, just use -col" )
                .create( "COL" );

        Option expectColumn = OptionBuilder.withArgName( "column" )
                .hasArgs()
                .withDescription( "only these columns will be considered when processing. count from 0. " +
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
            if( !line.hasOption( "csv" ) ) {
                System.out.println("Please give the path for .csv file");
                return;
            }
            if( line.hasOption( "out" ) ) {
                System.out.println(line.getOptionValue( "out" ));
            }
            if( line.hasOption( "col" ) ) {
                System.out.println(Arrays.asList(line.getOptionValues( "col" )));
            }
            if( line.hasOption( "COL" ) ) {
                System.out.println(Arrays.asList(line.getOptionValues( "COL" )));
            }
            if( line.hasOption("help")){
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "extractSQL", options );
            }
        }catch( ParseException exp ) {
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        }
    }
}
