Jahna Goldman - Project 8: VM Translator II 2
Description:
This is my VM (Part 2) Translator for Project 8. It translates virtual machine language files (individually or in a directory) into assembly language files.
It allows for arithmetic and memory access commands, as well as prorgam flow and function calling commands of the VM language. I used the three classes to structure the program as specified in the book:
Main (VMTranslator) to run the program, Parser to read and parse VM commands, & CodeWriter to translate the VM commands to Hack assembly code and write them to the appropriate file.
It is written using Java 8.
Compilation:
To compile the code, navigate to the appropriate folder (e.g. src/edu/uchicago/jagoldman) in your terminal and enter the command "javac ./*java".
Running:
To run the program, navigate to the src folder and enter the following command, for example:
"java edu/uchicago/jagoldman/VMTranslator directory", where directory is the name of the directory that contains the files you wish to translate from VM language to Hack assembly code.
Make sure to enter the directory with the appropriate parent directories where it is located For example,
/Users/jahnaariellegoldman/Desktop/nand2tetris/projects/07/MemoryAccess/BasicLoop" is the complete directory location
for the BasicLoop folder on my computer, so I would need to enter all of that. The VM translator will translate the .vm files into 1 .asm file.

I ran all of the test files/directories (BasicLoop, FibonacciSeries, FibonacciElement, SimpleFunction, NestedCall, and StaticsTest) on the VM translator I created
to produce corresponding .asm files. Then, I ran each of these .asm files on the CPU Emulator comparing each with the corresponding .tst
file in the project files. My VM Translator produced .asm files that passed all of the tests.
