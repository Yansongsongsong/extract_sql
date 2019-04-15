# 进度同步
## 一. 数据处理

### 1.1 SQL代码摘出

使用[extract_sql](https://github.com/Yansongsongsong/extract_sql)这个toolkit，可以把杂乱无章的文字中存在的sql代码给提取出来。具体的usage可以见reademe。是一个命令行工具，他的cli显示结果是这样的：

```shell
$ extractSQL
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

### 1.2 SQL Parser

#### 1.2.1 grammar file

SQL Parser 是很重要的一步，因为之后的模型和算法会用到Parser出来的结果。在这里我处理sql的Parser使用的是ANTLR4。对于ANTLR4来说，分析一门语言，最重要的是写出这门语言类似BNF定义的语法文件，ANTLR4中称为`grammar.g4`文件。

对于我们项目中的SQL Parser，我修改了`g4`文件，让其可以识别出一些比较独特的sql符号（大量出现在了**的数据库中），并将其拆分称两个文件，分别是`lexer.g4` 和`parser.g4`分别定义了词法和语法。

详细的内容可以见[PlSqlLexer.g4](<https://github.com/Yansongsongsong/extract_sql/blob/master/src/main/java/yansong/extractor/PlSqlLexer.g4>)和[PlSqlParser.g4](<https://github.com/Yansongsongsong/extract_sql/blob/master/src/main/java/yansong/extractor/PlSqlParser.g4>)。注意在使用的时候有一处hardcode，需要移除

```g4
@header {
import yansong.extractor.libs.PlSqlBaseParser;
}
```

两个文件都有这样的标识，这个标识表示，当你使用ANTLR4生成语法解析器时，他会在生成的java文件中加上`import yansong.extractor.libs.PlSqlBaseParser;`的头。

如果你不是在编译[extract_sql](https://github.com/Yansongsongsong/extract_sql)这个项目，而是单纯地想要使用antlr4，那么你需要移除上面的`@header`代码。

#### 1.2.2 ANTLR 使用

针对[PlSqlLexer.g4](<https://github.com/Yansongsongsong/extract_sql/blob/master/src/main/java/yansong/extractor/PlSqlLexer.g4>)和[PlSqlParser.g4](<https://github.com/Yansongsongsong/extract_sql/blob/master/src/main/java/yansong/extractor/PlSqlParser.g4>)的使用，完整流程是这样的：

1. 安装antlr4依赖

   - 如果你使用的是MacOS或者linux，你可以执行

     ```shell
     # 需要安装homebrew 见 https://brew.sh/
     # 这样安装，会有两个命令 antlr 和 grun 链接在你的/usr/local/bin/下
     brew install antlr
     ```

   - 其他，安装见[安装方法](https://www.antlr.org/)

2. 构建一个项目并拉取代码

   ```shell
   mkdir grammar && cd grammar
   curl https://raw.githubusercontent.com/Yansongsongsong/extract_sql/master/src/main/java/yansong/extractor/PlSqlLexer.g4 | sed '31,33d' > PlSqlLexer.g4
   curl https://raw.githubusercontent.com/Yansongsongsong/extract_sql/master/src/main/java/yansong/extractor/PlSqlParser.g4 | sed '32,34d' > PlSqlParser.g4
   curl https://raw.githubusercontent.com/Yansongsongsong/extract_sql/master/src/main/java/yansong/extractor/libs/PlSqlBaseLexer.java | sed '1d' > PlSqlBaseLexer.java
   curl https://raw.githubusercontent.com/Yansongsongsong/extract_sql/master/src/main/java/yansong/extractor/libs/PlSqlBaseParser.java | sed '1d' > PlSqlBaseParser.java
   ```

   此时你的项目应该是

   ```shell
   $ ls
   PlSqlBaseLexer.java  PlSqlBaseParser.java PlSqlLexer.g4        PlSqlParser.g4
   ```

3. 构建系统环境

   如果你是通过brew安装的antlr，你需要在你的shell session上添加antlr的classpath。这是一个java的概念。落实的命令行是这样的

   ```shell
   # 如果你是通过brew安装的，一般来说brew安装的包的源代码都在/usr/local/Cellar/下，目前antlr的稳定版本是4.7.2
   aPath=`find /usr -name '*antlr*.jar' | grep 4.7.2`
   export CLASSPATH=".:$aPath:$CLASSPATH"
   ```

   如果你是通过别的方式安装的antlr，你需要找到antlr的jar包所在路径，通过`export CLASSPATH=".:{path_to_antlr}:$CLASSPATH"`这样的方式为你的session添加classpath

4. 生成并编译文件

   ```shell
   # 执行这个命令，生成基本的parser代码，如果你需要编写runtime代码进行业务处理，执行这个就可以了
   # 如果第三步没有做的话，可能会抛找不到antlr 环境的错误
   antlr PlSqlLexer.g4 PlSqlParser.g4
   
   # 如果想要命令行尝试一下antlr，执行下面的代码
   # 编译所有的java代码，让它们变成可执行的文件
   javac *.java
   # 使用antlr提供的gui工具运行代码
   # `PlSql`参数是grammar定义的语法名称
   # `sql_script` 是parser中定义的语法规则之一，表示最高以这个规则去匹配
   # `-gui` 表示用gui的形式体现
   grun PlSql sql_script -gui
   ```

5. 运行

   当你执行完`grun PlSql sql_script -gui`时，你就进入了一个交互式shell界面

   ![](https://raw.githubusercontent.com/Yansongsongsong/PicsHub/archive/pics/test.svg?sanitize=true)

   当你输入完sql代码后，键入`control+D`表示输入完成，稍等片刻会出现如下的图片，点击OK后，程序退出，命令行版的ANTLR使用结束。

   ![](https://raw.githubusercontent.com/Yansongsongsong/PicsHub/archive/pics/20190415032120.png)

### 1.3 ANTLR TO Python

- 如果觉得java太过笨重，可以使用python3和java的桥接。桥接需要安装一个runtime，教程见[source](https://github.com/jszheng/py3antlr4book)。

- [runtime pypi hosted page](https://pypi.org/project/antlr4-python3-runtime)

- 安装完runtime之后，用法和上面所说的没有太大差别了，教程见[这里](https://github.com/antlr/antlr4/blob/master/doc/python-target.md)。

- 除了安装这个runtime环境，如果需要使用上面定义的grammar文件，你还需要将`PlSqlBaseLexer.java`和`PlSqlBaseParser.java`转换成对应的python3文件

- 命令

  ```shell
  antlr -Dlanguage=Python3 PlSqlLexer.g4 PlSqlParser.g4
  ```

### 1.4 ANTLR RUNTIME API

上面只是GUI的一个使用，当你需要进行特殊的遍历，或者编程时，需要查看[API](https://www.antlr.org/api/Java/index.html)。

## 二. 模型/算法选型

### 2.1 RNN

RNN效果不是很理想。我是使用[**dl4mt-tutorial**](https://github.com/nyu-dl/dl4mt-tutorial)这个库的示例代码，尝试构建RNN的解决方案。在这个库中，session0就是RNN（GRU） Language Model，可以用来预测生成后面几个字符或者token。

但是这个预测只限于token级别，有时候可能会生成很怪异的某些token，而不是推荐比较明确的现有的代码片段。

### 2.2 Aroma

Facebook 推出代码智能搜索与推荐工具 Aroma。比较符合我们的使用场景。facebook通过爬取github上的一套包含 5000 个开源 Android 项目的集合，来进行代码推荐。

Facebook 的相关文章可见[这里](https://ai.facebook.com/blog/aroma-ml-for-code-recommendation)。它的论文可见[这里](https://arxiv.org/pdf/1812.01158.pdf)。

Aroma 的代码建议创建流程主要分为三个阶段：

1. 基于特征的搜索

   - 这一步通过对代码语料库进行parse，提取语法树的特征，根据这些特征创建稀疏向量。所有方法的该特征向量将共同组成索引矩阵，以供搜索检索使用。

     ![](https://raw.githubusercontent.com/Yansongsongsong/PicsHub/archive/pics/20190415042822.png)

2. 重新排名与聚类

   - 在方法语法树上应用剪枝以丢弃方法体当中不相关的部分，并仅保留与查询片段匹配度最高的部分，从而根据它们与查询代码的实际相似性进行候选代码片段的重新排名。

3. 交叉：代码建议的创建过程

   - 交叉算法的工作原理，是将第一个代码片段作为“基础”代码，而后针对集群当中的每一种其它方法以迭代方式对其进行剪枝。剪枝过程之后的剩余代码将成为所有方法中的共有代码，并成为代码建议中的组成部分。

