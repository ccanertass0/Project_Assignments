`timescale 1ns/10ps
module half_adder_tb;

  // Your code goes here. DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!

  reg A, B;
  wire S, C;

  // UUT (Unit Under Test)
  half_adder HA (.A(A), .B(B), .S(S), .C(C));

  initial begin
    $dumpfile("result.vcd");
    $dumpvars;
    #10 A = 0; B = 0;
    #10 B = 1;
    #10 A = 1; B = 0;
    #10 B = 1;
    #10 $finish;
  end

endmodule
