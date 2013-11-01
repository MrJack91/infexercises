

-Lade Faktoren
-Shift Links (logisch) Faktor 1
-Shift Rechts (

500		Input: Faktor 1
502		Input: Faktor 2
600		Tmp: Vorzeichen des Endresultates (0: positiv, 1: negativ)
602		Tmp: Positiver Faktor 1
604		Tmp: Positiver Faktor 2
606		Tmp: Shift-Speicher (zu addierende Zahl nach SLL bei 32-Bit-Zahl)
702		32-Bit-Faktor Teil 1 ('hinten' / LSB)
704		32-Bit-Faktor Teil 2 ('vorne' / MSB)


----------------------
CLR R0			; Akku löschen
SWDD R0 #702	; Zweiter-16-Bit-Faktor mit 0 initialisieren
LWDD R0 #500 	; Lade ersten Faktor
SLL				; Logischer Links Shift
BCD #...		; Wenn Carry-Flag (Zahl negativ) Springe zu 1)
;2) -----------------
SWDD R0 #602	; Speichere ersten Faktor
LWDD R0 #502 	; Lade zweiten Faktor
SLL				; Logischer Links Shift
BCD #...		; Wenn Carry-Flag (Zahl negativ) Springe zu 3)
SWDD R0 #604	; Speichere zweiten Faktor
;4) -----------------
BZ #			; Wenn Akku = 0 Sprung zu E) ;TODO: Do Loop, wenn 1

LWDD R0 #602	; Ersten Faktor laden
SLL				; Faktor nach Links shiften
SWDD R0 #704	; Faktor speichern
BCD #			; Wenn Carry-Flag Sprung zu 5) (überlauf addieren)
CLR R0			; Akku leeren
SWDD R0 #606	; Speichern
;6) -----------------
LWDD R0 #702	; Faktor laden (Teil 2)
SLL				; Links shift fortsetzen
LWDD R1 #606	; Lade zu addierende Zahl
ADD R1			; Addieren (Shift abgeschlossen)
LWDD R0 #604	; Zweiten Faktor laden
SRL				; Faktor / 2
BCD #			; Wenn Carry Flag -> Addiere einmal den vorangehenden Wert

;1) -----------------
ADDD #-1		; Addiere -1
NOT				; Invertiere
SWDD R0 #602	; Speichere invertierten Faktor
CLR R0			; Akku leeren
ADDD #-1		; 'Vorzeichen' Addieren
SWDD R0 #600	; 'Vorzeichen' Speichern
LWDD R0 #602	; Lade Faktor
BD #			; Sprung zu 2)

;3) -----------------
ADDD #-1		; Addiere -1
NOT				; Invertiere
SWDD R0 #604	; Speichere invertierten Faktor
LWDD R0	#600	; 'Vorzeichen' laden
ADDD #-1		; 'Vorzeichen' Addieren
SWDD R0 #600	; 'Vorzeichen' Speichern
ADDD #2			; Vorzeichen nach Multiplikation: 0: Positiv, 1: Negativ
SWDD R0 #600	; 'Vorzeichen' Speichern
LWDD R0 #604	; Lade Faktor
BD #			; Sprung zu 4)

;5) -----------------
CLR R0			; Akku leeren
ADDD 1			; Zu addierende Zahl festlegen
SWDD R0 #606	; Zu addierende Zahl speichern
BD #			; Sprung zu 6)


;E) -----------------
END


;---------------------
LWDD R1	#502	;Multiplikand (x) laden
LWDD R0	#502	;Multiplikand nochmals laden
...				;Multiplikand negieren

LWDD R2	#502	;
CLR R3			; Clear
CLR R0			; Clear Akku
				; Set Counter = y
LWDD R0 #606	; Load Counter
BZD #..			; Sprung ans Ende



http://ftp.csci.csusb.edu/schubert/tutorials/csci313/w04/TL_Booth.pdf
Booth Algorithmus:
-x = zahl mit den wenigsten 01 / 10 wechsel, y= die andere
Initialisiere Register: u(0) v(1) x(2) = x x-1(3)= 0
	-Wiederhole y mal
	-Wenn LSB v(1) == 1 && x-1(3) == 0
		-> u(0) - y
	-WEnn LSB v(1) == 0 && x-1(3) == 1
		-> u(0) + y
	-Wenn LSB v(1) == x-1(3)
		-> nichts zu tun

	-Set x-1(3) = LSB x(2)
	-Arithmetischer Rechts-Shift für u(0)
	-Arithmetischer Rechts-Shift für v(0)
	-Logischer Rechts-Shift für x(2)





-------------------------
CLR R0
CLR R1
LWDD R2, #500 ;Ersten Faktor in das 2. Register laden (Zähler)
LWDD R3, #502 ;Zweiter Faktor in das 3. Register laden

SWDD R0, #602 ;
SWDD R2, #604 
LWDD R0, #604
BZD #

DEC
BZD #

LWDD R0 #602
SLL
BCD #
SWDD R0 #602
LWDD R0 #604
DEC
DEC
SWDD R0 #604
BD #


LWDD R0 #602
ADD R3
BCD 

SWDD R0 #602
LWDD R0 #604
DEC
SWDD R0 #604
BD #

-------------------------




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
	
;* Booth-Algorithmu:http://www.ecs.umass.edu/ece/koren/arith/simulator/Booth/
;* http://www.csci.csusb.edu/schubert/tutorials/csci313/w04/TB_BoothTutorial.pdf