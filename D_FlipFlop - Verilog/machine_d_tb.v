`timescale 1ns / 1ps
module machine_d_tb;
// Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!
    reg x, CLK, RESET;
    wire F;
    wire [2:0] S;

    // Instantiate the machine_d module
    machine_d uut_d1 (.x(x), .CLK(CLK), .RESET(RESET), .F(F), .S(S));

    always begin
        #10 CLK = ~CLK; //clock changed every 10 unit seconds
    end



    initial begin
        x = 0;
        CLK = 0;
        RESET = 1;
        #20; 
        RESET = 0;
        #20; 
        x = 1;
        #10; 
        $finish;
    end
endmodule