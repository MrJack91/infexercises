Multiplikation Binär 2x 16 Bit Zahl in 32 Bit

Algorithmus
==========
Move Zahl 2 in Zahl 4 (Zahl 3 + 4 sind das 16 Bit Resultat)
solange eine zahl halbiert werden kann, die andere Zahl verdoppeln
Zahl 1 halbieren (16 Bit Zahl)
zahl 1 in reg laden
zahl 1 halbieren (SRA)
überprüfen ob Zahl 1 grösser 0 sonst, fertig.
Zahl 3+4 verdoppeln (32 Bit Zahl)
zahl 3 in reg laden
zahl 3 verdoppeln (SLL)
Wenn CarryBit 1 denn merke in Zahl 5: 1
zahl 4 in reg laden
zahl 4 verdoppeln (SLA)
addiere Zahl 5
zahl 1 ist jetzt +/- 1
zahl 2 ist jetzt +/- n (n ist das Ergebnis ohne Vorzeichen)


ASSEMBLER
==========
Speicher:
500		Zahl 1	Faktor 1
501		Zahl 2	Faktor 2
502		Zahl 3	Resultat 1 Teil (erste 16 Bit)
503		Zahl 4	Resultat 2 Teil (zweite 16 Bit)
504		Zahl 5	Temporäre Werte
505		Zahl 6	Wert 0 für etwas zu "merken"
506		Zahl 7	Wert 1 für etwas zu "merken"


LWDD 0, #501			; Lese Zahl 2 und speichere in 2. Byte vom Resultat
SWDD 0, #503

// Zahl 1 halbieren
LWDD 0, #500
SWDD 0, #504		; merke kurzfristig, falls wir schon fertig sind (rollback)
SRA
SWDD 0, #500		; aktualisiere Zahl 1

// check abbruch
BZD [ADRESSE NACH B]				; ist Zahl 1 == 0 überspringe den Loop, sonst mache weiter

// verdopple die grosse 32 Bit Zahl (Ergebniss)
LWDD 0, #503			; Lade Resultat Teil 2
SLL
SWDD 0, #503		; verdoppelter Teil 2 abspeichern
LWDD 1, #506			; Notiz für den Überlauf in Reg 1 (vorläufig, falls CF wird der Reset übersprungen)
BCD					; falls carry bit gesetzt wurde merke 1 in temp (auf nächster Zeile)
LWDD 1, #505			; Reset Merkung des CF
LWDD 0, #502			; Lade Resultat Teil 1
SLL
ADD 1				; Addiere den Wert (Notiz) aus dem Reg 1
SWDD 0, #502		; verdoppelter Teil 1 abspeichern
BD [AUF BNZ]			; Immer nach Loop Anfang springen





ASSEMBLER für mini PC
Funktioniert für 2x eine Zahl von 2^x
LWDD 0, #501
SWDD 0, #503
LWDD 0, #500
SWDD 0, #504
SRA
SWDD 0, #500
BZD 118
LWDD 0, #503
SLL
SWDD 0, #503
LWDD 1, #506
BCD 113
LWDD 1, #505
LWDD 0, #502
SLL
ADD 1
SWDD 0, #502
BD 102
END


Speicher
4
16384
0
0
0
0
1

Speicher
16
32768
0
0
0
0
1






Notes
1000 * 100
500 * 200
250 * 400
125 * 800
62 * 1600 + 800
31 * 3200 + 800
15 * 6400 + 800 + 3200
7 * 12800 + 800 + 3200 + 6400
3 * 25600 + 800 + 3200 + 6400 + 12800
1 * 51200 + 800 + 3200 + 6400 + 12800 + 25600



1000 * -100
500 * -200
250 * -400
125 * -800
62 * -1600 + -800
31 * -3200 + -800
15 * -6400 + -800 + -3200
7 * -12800 + -800 + -3200 + 6400
3 * -25600 + -800 + -3200 + 6400 + 12800
1 * -51200 + -800 + -3200 + -6400 + -12800 + -25600



-1000 * 100
-500 * 200
-250 * 400
-125 * 800
-62 * 1600 + -800
-31 * 3200 + -800
-15 * 6400 + -800 + -3200
-7 * 12800 + -800 + -3200 + -6400
-3 * 25600 + -800 + -3200 + -6400 + -12800
-1 * 51200 + -800 + -3200 + -6400 + -12800 + -25600


check ob negativ: SLL -> check CarryBit()







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
;*	    m16s2,m16s3,mcnt16s)
;*
;***************************************************************************

;***** Subroutine Register Variables

.def	mc16sL	=r16	 ;multiplicand low byte
.def	mc16sH	=r17	 ;multiplicand high byte
.def	mp16sL	=r18	 ;multiplier low byte
.def	mp16sH	=r19	 ;multiplier high byte
.def	m16s0	=r18	 ;result byte 0 (LSB)
.def	m16s1	=r19	 ;result byte 1
.def	m16s2	=r20	 ;result byte 2
.def	m16s3	=r21	 ;result byte 3 (MSB)
.def	mcnt16s	=r22	 ;loop counter

;***** Code
mpy16s:	clr	m16s3	 ;clear result byte 3
sub	m16s2,m16s2	;clear result byte 2 and carry
ldi	mcnt16s,16	;init loop counter
m16s_1:	brcc	m16s_2	 ;if carry (previous bit) set
add	m16s2,mc16sL	;    add multiplicand Low to result byte 2
adc	m16s3,mc16sH	;    add multiplicand High to result byte 3
m16s_2:	sbrc	mp16sL,0	;if current bit set
sub	m16s2,mc16sL	;    sub multiplicand Low from result byte 2
sbrc	mp16sL,0	;if current bit set
sbc	m16s3,mc16sH	;    sub multiplicand High from result byte 3
asr	m16s3	 ;shift right result and multiplier
ror	m16s2
ror	m16s1
ror	m16s0
dec	mcnt16s	 ;decrement counter
brne	m16s_1	 ;if not done, loop more
ret