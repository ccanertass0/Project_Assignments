`timescale 1ns/10ps

module full_adder(
    input A, B, Cin,
    output S, Cout
);

	// Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!

wire s1, c1, c2;
half_adder HA1 (A, B, s1, c1);
half_adder HA2 (s1, Cin, S, c2);

or G1(Cout, c1, c2);

endmodule
