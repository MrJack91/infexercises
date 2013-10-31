LWDD 0, #501			; Lese Zahl 2 und speichere in 2. Byte vom Resultat
SWDD 0, #503

LWDD 0, #500			; Zahl 1 halbieren
SWDD 0, #504			; merke kurzfristig, falls wir schon fertig sind (rollback)
SRA
SWDD 0, #500			; aktualisiere Zahl 1

BZD 122					; check abbruch; ist Zahl 1 == 0 überspringe den Loop, sonst mache weiter

; verdopple die grosse 32 Bit Zahl (Ergebnis)
LWDD 0, #503			; Lade Resultat Teil 2
SLL
SWDD 0, #503			; verdoppelter Teil 2 abspeichern
LWDD 1, #506			; Notiz für den Überlauf in Reg 1 (vorläufig, falls CF wird der Reset übersprungen)
BCD 117					; falls carry bit gesetzt wurde merke 1 in temp (auf nächster Zeile)
LWDD 1, #505			; Reset Merkung des CF
LWDD 0, #502			; Lade Resultat Teil 1
SLL
ADD 1					; Addiere den Wert (Notiz) aus dem Reg 1
SWDD 0, #502			; verdoppelter Teil 1 abspeichern
BD 103					; Immer nach Loop Anfang springen
END
