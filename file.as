.wordsize 8	;
.regcnt 4	;
.maxmem 0x00010000	;
.pos 0x0
	MOV Stack, sp	;
	MOV Stack, fp	;
main: 
	STP fp, [sp, -32]!			;
	MOV fp, sp 					;
	FMOV x0, 18					; 
	STUR x0, [fp, 16]			;
	LDUR x0, [fp, 16]			;
	MOV x1, 19					; 
	LDURH x1, [fp, 10]			;
	STURH x1, [fp, 10]			;
	BL calculate				;
	LDP fp, [sp], 32			;
	HALT						;
calculate:
	STP fp, [sp, -32]! 			;
	MOV fp, sp		;
	MOV x2, 1					;
	STURW x2, [fp, 24]			;
	MOV x3, 3					;
	STURW x3, [fp, 16]			;
	LDURSW x2, [fp, 24]			;
	LDURSW x3, [fp, 16]			;
	AND x4, x2, x3				;
	STURW x4, [fp, 8]			;
	LDURSW x4, [fp, 8]			;
	LDURSW x2, [fp, 24]			;
	SUB x5, x4, x2				; 
	STURW x5, [fp, 0]			;
	LDURSW x5, [fp, 0]			;
	CBZ x5, calByte				;
	NOP							;
	LDP fp, [sp], 32			;
	RET 						;
calByte: 
	STP fp, [sp, -16]! 			;
	MOV x6, 7					; 
	STURB x6, [fp, 15]			;
	LDURB x6, [fp, 15]			;
	ADDI x6, x6, 10				;
	STURB x6, [fp, 15]			;
	LDP fp, [sp], 16			;
	RET 						;
.pos 0x100
.double 0x7		;
.single 0x7		;
.half 0x7		;
.byte 0x7		;
.pos 0x140
Stack: 