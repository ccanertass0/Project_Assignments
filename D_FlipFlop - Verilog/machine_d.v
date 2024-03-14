module machine_d(
    input wire x,
    input wire CLK,
    input wire RESET,
    output wire F,
    output wire [2:0] S
);


    // Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!
    wire wire1, wire2, wire3;
    assign wire1 = S[2] | (S[1] & ~x);
    assign wire2 = (~S[1] & ~x) | (S[2] & S[1]) | (S[1] & x);
    assign wire3 = (~S[0] & x) | (S[0] & ~x);
    dff dffA(.D(wire1), .CLK(CLK), .RESET(RESET), .Q(S[2]));
    dff dffB(.D(wire2), .CLK(CLK), .RESET(RESET), .Q(S[1]));
    dff dffC(.D(wire3), .CLK(CLK), .RESET(RESET), .Q(S[0]));

    assign F = S[2] & S[1] & ~S[0];



endmodule

