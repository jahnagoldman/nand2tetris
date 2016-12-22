// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.

  @R2
  M=0 // Set R2=0
  @R0
  D=M
  @count
  M=D // initialize count to value in R0 - # of times to add R1

(LOOP)
  @count
  D=M // D=count
  @END
  D;JEQ // if count is 0 goto END
  @R1
  D=M // D=R1
  @R2
  M=D+M // add R1 to sum
  @count
  M=M-1 // decrement the count
  @LOOP
  0;JMP // Goto LOOP
(END)
  @END
  0;JMP // Infinite loop at end
