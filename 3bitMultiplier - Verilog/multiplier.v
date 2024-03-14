`timescale 1ns/10ps

module multiplier (
    input [2:0] A, B,
    output [5:0] P
);

	// Your code goes here.  DO NOT change anything that is already given! Otherwise, you will not be able to pass the tests!

	wire a0, a1, a2, a3, a4, a5, a6, a7;
	wire c1, c2, c3, c4, c5;
	wire s1, s2;
	and G1(P[0], A[0], B[0]);  //P0 DONEEE!!
	and G2(a0, A[1], B[0]);  // a0 will be a B input for HA1.
	and G3(a1, A[0], B[1]);  // a1 will be a A input for HA1.


	half_adder HA1(.A(a1), .B(a0), .C(c1), .S(P[1])); // c1 will be the Cin input for FA1. P1 DONEEEEE!
	

	and G4(a2, A[2], B[0]); //a2 will be the B input of HA2.
	and G5(a3, A[1], B[1]); //a3 will be the A input of HA2.
	half_adder HA2(.A(a3), .B(a2), .C(c2), .S(s1)); // s1 will be the B input of FA1. c2 will be the Cin input of FA2.

	
	and G7(a5, A[0], B[2]);

	//assign a5 = A[0] & B[2];  a5 will be the A input of FA1.   kinda G8.
	//We got all the inputs needed for FA1.
	
	full_adder FA1(.A(a5), .B(s1), .Cin(c1), .S(P[2]), .Cout(c3)); //c3 will be the B input of HA3. PA[2] DONNEEEEEEEE!.
	

	and G6(a4, A[2], B[1]);   // a4 will be B input of FA2.
	and G8(a6, A[1], B[2]);   // a6 will be A input of FA2.
	//We got all the inputs needed for FA2.
	full_adder FA2(.A(a6), .B(a4), .Cin(c2), .Cout(c4), .S(s2)); //s2 will the A input of HA3. c4 will be the B input of FA2.

	half_adder HA3(.A(s2), .B(c3), .C(c5), .S(P[3])); // c5 will be the Cin input of FA3. P[3] DONEEEEE!!!!!  

	and G9(a7, A[2], B[2]);



	 full_adder FA3(.A(a7), .B(c4), .Cin(c5), .S(P[4]), .Cout(P[5])); // everyting is done. 
	
	
	



endmodule
