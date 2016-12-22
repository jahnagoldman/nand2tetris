How to Compile:
First, make sure you have Java downloaded, which you can check by using the command "java -version" in your terminal.
To compile the code, go to your terminal and change to the correct directory that contains the .java file.
Use the command "javac StripWhiteSpace.java".
You should now see a file called "StripWhiteSpace.class" in the same directory.



How to Run:
In your terminal, change to the directory that contains edu.uchicago.jagoldman.StripWhiteSpace. This would likely be the src package.
Once you are there, to run the program, use either one of the following commands:

If you wish to keep the comments:
java edu.uchicago.jagoldman.StripWhiteSpace [filename.in]
(i.e. java edu.uchicago.jagoldman.stripWhiteSpace hello.in)

If you wish to remove the comments:
java edu.uchicago.jagoldman.StripWhiteSpace no-comments [filename.in]
(i.e. java edu.uchicago.jagoldman.StripWhiteSpace no-comments hello.in)

Please include the appropriate path/directory for the file if not in the same one as the executable. For example, you might have "/Users/jahnaariellegoldman/hello.in".
As a note, the transformed file will be written to the same directory where the existing file resides.



What Works & Doesn't Work:
The program will not work if you put "no-comments" after the file name: the command must be in the aforementioned order.
This will only work for regular text files.
If you do not enter a filename, nothing will happen, but if you enter an invalid file name, an error message will print to the terminal.
The comments must be written with two slashes to be properly removed, as such: // this is a comment
The white spaces removed will be those in between words/characters, and empty spaces between lines. The lines will still stay separated and will not be combined into a single line.
Everything should be fully functioning otherwise.