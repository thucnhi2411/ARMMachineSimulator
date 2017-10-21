.wordsize 8	;
.regcnt 4	;
.maxmem 0x00010000	;
.pos 0x0
	MOV Stack, sp	;
	MOV Stack, fp	;
main: 
	STP fp, [sp, -32]!			;
	MOV fp, sp 					;
	FMOV x0, 18					; // save double val to x0
	LDUR x0, [fp, 24]			;
	STUR x0, [fp, 24]			;
	MOV x1, 19					; // save half val to x2
	LDURH x1, [fp, 22]			;
	STURH x1, [fp, 22]			;
	BL calculate				;
	LDP fp, [sp], 32			;
	HALT						;
calculate:
	STP fp, [sp, -32]! 			;
	MOV fp, sp		;
	MOV x2, 1					; //0001
	STURW x2, [fp, 28]			;
	MOV x3, 3					; //0011
	STURW x3, [fp, 24]			;
	LDURSW x2, [fp, 28]			;
	LDURSW x3, [fp, 24]			;
	AND x4, x2, x3				; //0001
	STURW x4, [fp, 20]			;
	LDURSW x4, [fp, 20]			;
	LDURSW x2, [fp, 28]			;
	SUB x5, x4, x2				; //0000
	STURW x5, [fp, 16]			;
	LDURSW x5, [fp, 16]			;
	CBZ x5, calByte			;
	NOP							;
	LDP fp, [sp], 32			;
	RET 						;
calByte: 
	STP fp, [sp, -16]! 			;
	MOV x6, 65					; // save byte val to x3
	STURB x6, [fp, 15]			;
	LDURB x6, [fp, 15]			;
	ADDI x6, x6, 1				;
	STURB x6, [fp, 15]			;
	LDP fp, [sp], 16			;
	RET 						;
.pos 0x100
Stack: 