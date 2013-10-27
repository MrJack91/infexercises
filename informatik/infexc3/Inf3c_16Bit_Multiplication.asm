CLR R0
CLR R1
LWDD R2, #500 ;Ersten Faktor in das 2. Register laden (Zähler)
LWDD R3, #502 ;Zweiter Faktor in das 3. Register laden



-Faktoren laden
-....	; 1. |
-Wenn ein Faktor 0 -> Sprung ans Ende		; Sprung zu 4.
-Wenn nicht:
	-Wenn Zähler grösser 1:
		-SLL ; Links Shift des Akku (Kein Arithmetischer Shift, da das Ganze am Ende die korrekte Zahl sein muss, sonst wird das Vorzeichen bei jedem Block gesetzt)
		-BCD #... ; Wenn CarryFlag gesetzt Sprung zu 2.
		-...	  ; 3. | Zähler herunterzählen
		-...		; Evtl. Register umschichten
		-BD #....	; Sprung zu 1.

	-Sonst:
		-ADD R...	; Addiere zweiten Faktor
		-BCD #...	;Wenn CarryFlag gesetzt Sprung zu 5.
		-...		;Zähler herunterzählen
		-BD #		;Sprung zu 1.
		
	-...		; 2. | "Umladen" des 1. / 2. Blocks
	-SLL		; Links Shift weiterführen
	-BD #...	; Sprung zu 3.	

	-...		; 5. | Addition mit zweitem Block weiterführen
END	; 4. |










Assembler
;***************************************************************************
;*
;* "mpy16s" - 16x16 Bit Signed Multiplication
;*
;* This subroutine multiplies signed the two 16-bit register variables 
;* mp16sH:mp16sL and mc16sH:mc16sL.
;* The result is placed in m16s3:m16s2:m16s1:m16s0.
;* The routine is an implementation of Booth's algorithm. If all 32 bits
;* in the result are needed, avoid calling the routine with
;* -32768 ($8000) as multiplicand
;*  
;* Number of words	:16 + return
;* Number of cycles	:210/226 (Min/Max) + return
;* Low registers used	:None
;* High registers used  :7 (mp16sL,mp16sH,mc16sL/m16s0,mc16sH/m16s1,
;*			    m16s2,m16s3,mcnt16s)	
;*
;***************************************************************************

;***** Subroutine Register Variables

.def	mc16sL	=r16		;multiplicand low byte
.def	mc16sH	=r17		;multiplicand high byte
.def	mp16sL	=r18		;multiplier low byte
.def	mp16sH	=r19		;multiplier high byte
.def	m16s0	=r18		;result byte 0 (LSB)
.def	m16s1	=r19		;result byte 1
.def	m16s2	=r20		;result byte 2
.def	m16s3	=r21		;result byte 3 (MSB)
.def	mcnt16s	=r22		;loop counter

;***** Code
mpy16s:	clr	m16s3		;clear result byte 3
	sub	m16s2,m16s2	;clear result byte 2 and carry
	ldi	mcnt16s,16	;init loop counter
m16s_1:	brcc	m16s_2		;if carry (previous bit) set
	add	m16s2,mc16sL	;    add multiplicand Low to result byte 2
	adc	m16s3,mc16sH	;    add multiplicand High to result byte 3
m16s_2:	sbrc	mp16sL,0	;if current bit set
	sub	m16s2,mc16sL	;    sub multiplicand Low from result byte 2
	sbrc	mp16sL,0	;if current bit set
	sbc	m16s3,mc16sH	;    sub multiplicand High from result byte 3
	asr	m16s3		;shift right result and multiplier
	ror	m16s2
	ror	m16s1
	ror	m16s0
	dec	mcnt16s		;decrement counter
	brne	m16s_1		;if not done, loop more	
	ret