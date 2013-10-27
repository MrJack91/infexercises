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