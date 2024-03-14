`timescale 1ns/10ps
module full_adder_tb;
reg A, B, Cin;


wire S, Cout;



//Full adder
full_adder fa (.A(A), .B(B), .Cin(Cin), .S(S), .Cout(Cout));

  initial begin
    $dumpfile("fa.vcd");
    $dumpvars;

    #10 A = 0; B = 0; Cin = 0;
    #10 Cin = 1;
    #10 B = 1; Cin = 0;
    #10 Cin = 1;
    #10 A = 1; B = 0; Cin = 0;
    #10 Cin = 1;
    #10 B = 1; Cin = 0;
    #10 Cin = 1;

    #10 $finish;
  end
endmodule
