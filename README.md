# Dice Duel

Ett färgstarkt, textbaserat tärningsspel i Java. Ange ditt namn, antal omgångar och antal sidor på tärningen. Gissa sedan tärningens värde: varje rätt gissning ger en poäng, och efter sista omgången visas resultat, träffsäkerhet och bästa svit.

Spelet använder klasserna `DiceGame`, `GameStart`, `Die`, `Player` och `PlayerInput`, med ANSI-färger från `Helper.TerminalColors`. Efter resultatet kan du välja att spela igen eller avsluta.

Kompilera och kör från projektmappen:

```text
javac -encoding UTF-8 Helper\TerminalColors.java Helper\TerminalPanel.java Die.java Player.java PlayerInput.java GameStart.java DiceGame.java
java DiceGame
```

Använd JDK 17 eller senare och en terminal med stöd för ANSI-färger, till exempel
Windows Terminal eller terminalen i VS Code. Animationen hoppas över när indata
eller utdata omdirigeras. Stäng av den manuellt med `java -Ddice.animation=false DiceGame`.

Kör grafiktester utan extra bibliotek:

```text
javac -encoding UTF-8 -cp . Tests\TerminalGraphicsTest.java
java -Ddice.animation=false -cp ".;Tests" TerminalGraphicsTest
```
