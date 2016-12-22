Jahna Goldman - Project 10: Compiler - Syntax Analysis
Description:
This is my Compiler Part 1 - Syntax Analysis for Project 10. It is a partial compiler, and compiles code from the Jack programming language into XML code.
The JackTokenizer class parses the code into individual tokens by token type. Then, the CompilationEngine class compiles the tokens into
the corresponding XML code and writes it to the output file. There is also a main driver class, called the JackAnalyzer. This program is written using Java 8.
It does not currently work for an entire directory of files, but only 1 file at a time. I attempted to fix this, but am still working on it. Because my
CompilationEngine class has an instance of the Tokenizer class within it, it made it difficult to accommodate for a whole directory without rewriting over the same file.

Compilation:
To compile the code, navigate to the appropriate folder (e.g. src/edu/uchicago/jagoldman) in your terminal and enter the command "javac ./*java".

Running:
To run the program, navigate to the src folder and enter the following command, for example:
"java edu/uchicago/jagoldman/JackAnalyzer file.jack", where file.jack is the name of the file that you wish to compile into XML code from Jack.
Make sure to enter the file with the appropriate parent directories where it is located For example,
"/Users/jahnaariellegoldman/Desktop/nand2tetris/projects/10/ArrayTest/Main.jack" is the complete directory location
for the ArrayTest/Main.jack file on my computer, so I would need to enter all of that.
The Compiler will create the corresponding .xml file within the same directory, with the same first part of the file name.

Test Cases:
After running all of the .jack files (in ArrayTest, ExpressionlessSquare and Square), I used the TextComparer tool to compare all of my
created XML files to the correct ones that come with Nand2Tetris to complete the dry tests. All comparisons ended successfully.
