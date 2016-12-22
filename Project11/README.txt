Jahna Goldman - Project 11: Compiler 2 - Code Generation
Description:
This is my Compiler Part 2 - Code Generation for Project 11. It is a full compiler extended from the Syntax Analyzer, and compiles code from the Jack programming language into VM language.
The JackTokenizer class parses the code into individual tokens by token type. Then, the CompilationEngine and VMWriter classes compile the tokens into
the corresponding VM language code and writes it to the output .vm files. There is also a main driver class, called the JackAnalyzer, as well as SymbolTable/Symbol, which keep track of symbols, variables, etc. and their corresponding scope.
This program is written using Java 8. The program works for either a directory of files, or individual files

Compilation:
To compile the code, navigate to the appropriate folder (e.g. src/edu/uchicago/jagoldman) in your terminal and enter the command "javac ./*java".

Running:
To run the program, navigate to the src folder and enter the following command, for example:
"java edu/uchicago/jagoldman/JackAnalyzer file.jack", where file.jack is the name of the file that you wish to compile into vm code from Jack.
Or, instead of file.jack, simply enter the directory name and all .jack files in that directory will be translated into VM code/files.
Make sure to enter the file with the appropriate parent directories where it is located For example,
"/Users/jahnaariellegoldman/Desktop/nand2tetris/projects/11/Average" is the complete directory location
for the Average directory on my computer, so one would need to enter all of that.
The Compiler will create the corresponding .vm file within the same directory, with the same first part of the file name.

Test Cases:
After compiling all of the Project 11 directories (Average, ComplexArrays, ConvertToBin, Pong, Seven, Square) with my Project 11 Compiler,
I used the VMEmulator tool to run the compiled VM files, and all tests passed successfully.