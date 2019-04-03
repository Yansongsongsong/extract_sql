# ExtractSQL
The toolkit with ANTLR4 to extract the sql code from one plain text.

### Overview
This project provides one command line interface. You can check the detailed usage with cli.
This project will format the result as `.csv`.

### Usage
```shell
git clone https://github.com/Yansongsongsong/extract_sql.git
cd extract_sql 
mvn package -DskipTests
java -jar ./target/extractSQL.jar 
```
the display is here
```shell
usage: extractSQL
 -col <column>   optional. only these columns will be considered when
                 processing. count from 0. e.g. -col 1 2
                 if you both use -col and -COL, just use -col
 -COL <column>   optional. these columns will be ignored when processing.
                 count from 0. e.g. -COL 1 2
                 if you both use -col and -COL, just use -col
 -csv <file>     required. the path for .csv file to be processed
 -help           print this message
 -out <file>     optional. the path for file to store sql

```

the usage
```shell
# default outfile name is out.csv
java -jar ./target/extractSQL.jar -csv source.csv
# define the outfile name
java -jar ./target/extractSQL.jar -csv source.csv -out target.csv
```