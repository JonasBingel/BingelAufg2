Hinweis zur Abgabeaufgabe BingelAufg2:
Es sollte ein MVP-Entwurfsmuster implementiert werden. Nach dem
MVP besteht keine Kommunikation zwischen View und Model, da Presenter
den Vermittler bildet. Die gewaehlte Implementierung ist eine passive
View, d.h. sie bekommt nur Table-Model ueberreicht und passt keine 
Komponenten an.
Um eine moeglischt loose Entkopplung der einzelnen Entwurfsmusterteile zu erreichen,
habe ich Observer verwendet, die auf die entsprechenden Events reagieren.

Des Weiteren habe ich weitere Funktionalitaeten ergaenzt: Die GUI ist robuster aufgebaut 
und TopDrei kann mehrmals verwendet werden. 
Auch ein wiederholtes Verwenden der allgemeinen ModelAlleDateien-Suche ist moeglich 
und kann mittels Enter im Textfeld gestartet werden. 
Der Pfad wird zudem ueberprueft bei Bestaetigung mit Enter oder TopDrei-Button und wird bei Non-Existenz rot hinterlegt.