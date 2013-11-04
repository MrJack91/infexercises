CLR 0			;Leere Akku
SWDD 0, #530	;Setze Initialwert für Vorzeichen

LWDD 0, #500	;Lade ersten Faktor
SLL				;Logischer Links Shift
LWDD 0, #500	;Lade ersten Faktor
BCD #152		;Wenn Carry-Flag (Zahl negativ) Springe
SWDD 0, #520	;Setze Initialwert für Zähler

LWDD 0, #501	;Lade zweiten Faktor
SWDD 0, #521	;Speichere Faktor zwei für Weiterverarbeitung
SLL				;Logischer Links Shift
LWDD 0, #501	;Lade zweiten Faktor
BCD #162		;Wenn Carry-Flag (Zahl negativ) Springe
LWDD 1, #521	;Lade Zweiten Faktor in Register

CLR 0			;Leere Akku
SWDD 0, #504	;Init Lower
SWDD 0, #506	;Init Higher

LWDD 0, #520	;Lade Zähler
BZD #139		;Wenn Zähler 0 -> Sprung zur Vorzeichenbestimmung
LWDD 0, #504	;Lade Ergebnis Lower
ADD 1			;Addiere Faktor zwei
SWDD 0, #504	;Speichere Lower
BCD #133		;Sprung zur Addition Higher
LWDD 0, #520	;Lade Zähler
DEC				;Runterzählen
SWDD 0, #520	;Speichere Zähler
BD #121



LWDD 0, #506	;Lade Higher
ADDD #1			;Addiere 1
SWDD 0, #506	;Speichere Higher
BD #125			;Rücksprung


LWDD 0, #530	;Lade Vorzeichen
BZD #175			;Wenn 0 -> Ende, Sonst negieren
LWDD 0, #504	;Lade Lower
NOT				;Negiere Lower
ADDD #1			;Addiere 1
SWDD 0, #504	;Speichere Lower
LWDD 0, #506	;lade Higher
NOT				;Negiere
BCD #172			;Wenn Carry Flag
SWDD 0, #506	;Speichere Higher
BD #176

;---------------; Negere Faktor 1
ADDD #-1		; Addiere -1
NOT				; Invertiere
SWDD 0, #520	; Speichere invertierten Faktor
LWDD 0,	#530	; 'Vorzeichen' laden
ADDD #-1		; 'Vorzeichen' Addieren
SWDD 0, #530	; 'Vorzeichen' Speichern
LWDD 0, #520	; Lade Faktor
BD #107			; Rücksprung

;---------------; Negere Faktor 2
ADDD #-1		; Addiere -1
NOT				; Invertiere
SWDD 0, #521	; Speichere invertierten Faktor
LWDD 0,	#530	; 'Vorzeichen' laden
ADDD #-1		; 'Vorzeichen' Addieren
ADDD #2			; Vorzeichen nach Multiplikation: 0: Positiv, 1: Negativ
SWDD 0, #530	; 'Vorzeichen' Speichern
LWDD 0, #521	; Lade Faktor
BD #113			; Rücksprung

;---------------; 
ADDD #1			; Addiere 1
BD #147

END				;Ende
-----
500:	Input Faktor 1
502:	Input Faktor 2
520:	Counter-Variable
504:	Ergebnis Lower
506:	Ergebnis Higher