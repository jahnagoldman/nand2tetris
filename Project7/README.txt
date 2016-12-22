Jahna Goldman - Project 7: VM 1 :: Stack Arithmetic
Description:
This is my VM (Part 1) Translator for Project 6. It translates virtual machine language files (individually or in a directory) into assembly language files.
It allows for arithmetic and memory access commands in this part of the VM language translator. I used the three classes to structure the program as specified in the book:
Main (VMTranslator) to run the program, Parser to read and parse VM commands, & CodeWriter to translate the VM commands to Hack assembly code and write them to the appropriate file.
It is written using Java 8.
Compilation:
To compile the code, navigate to the appropriate folder (src/edu/uchicago/jagoldman)
in your terminal and enter the command "javac ./*java".
Running:
To run the program, navigate to the src folder and enter the following command:
"java edu/uchicago/jagoldman/VMTranslator filename.vm", where filename is the name of the file you wish to translate from VM language to Hack assembly code.
Make sure to enter the filename.vm with the appropriate directory where it is located For example,
/Users/jahnaariellegoldman/Desktop/nand2tetris/projects/07/MemoryAccess/BasicTest/BasicTest.vm" is the complete file name with directory location
for the BasicTest.vm file on my computer, so I would need to enter all of that. You can also use a directory name with .vm files inside of it
instead of an individual .vm file and that will translate all of the .vm files into Assembly code and write to one .asm file.

I ran all of the test files (BasicTest.vm, PointerTest.vm, StaticTest.vm, SimpleAdd.vm, StackTest.vm) on the VM translator I created
to produce corresponding .asm files. Then, I ran each of these .asm files on the CPU Emulator comparing each with the corresponding .tst
file in the project files. My VM Translator produced .asm files that passed all of the tests.
