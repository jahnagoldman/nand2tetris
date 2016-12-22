This is my assembler for Project 6. I used the four classes to structure the program as specified in the book:
Main, Parser, SymbolTable & Code. To compile the code, navigate to the appropriate folder (src/edu/uchicago/jagoldman)
in your terminal and enter the command "javac ./*java". Then, to run, navigate to the src folder and enter the following command:
"java edu/uchicago/jagoldman/Main filename.asm", where filename is the name of the file you wish to translate from Assembly
code to Hack. Make sure to enter the filename.asm with the appropriate directory where it is located For example,
/Users/jahnaariellegoldman/Desktop/nand2tetris/projects/06/pong/Pong.asm" is the complete file name with directory location
for the Pong.asm file on my computer, so I would need to enter all of that.

I tested my program files on the Assembler using them as a comparison to the corresponding .asm file (e.g.
running Max.asm and comparing with my hack code.) It worked for all of them (Max, MaxL, Add, AddL, Rect, RectL, Fill, and Mult)
except for Pong, which I am not sure why. I tried to figure it out with rpint statement and isolating code blocks, but I was not sure why.
 PongL does seem to work.