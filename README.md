-> serwer:
  - przyjmowanie połączeń
  - wysyłanie komunikató do graczy
  - po dołączeniu dwóch graczy rozpoczyna grę
-> klient:
  - łączy się z serwerem
  - wpisuje ruch
  - przyjmuje ruch od przeciwnika
-> funkcje

<br><br>
## Zasady gry (iteracja 1)
1. W jednej grze uczestniczy dwóch graczy. Gra toczy się na planszy będącej siatką 19 poziomych i 19 pionowych linii tworzących 361 przecięć. Czasami gra się także na planszach 13 na 13 lub 9 na 9.

2. Gracze kładą na przemian czarne i białe kamienie na przecięciu linii. Rozpoczynają czarne. Plansza jest początkowo pusta. Celem gry jest otoczenie własnymi kamieniami obszaru większego niż obszar przeciwnika.

3. Kamieni raz postawionych na planszy nie można zabrać ani przesunąć, mogą natomiast zostać uduszone przez przeciwnika, jeśli stracą wszystkie oddechy. Po zabraniu ostatniego oddechu przeciwnik zabiera z planszy i przechowuje uduszone przez siebie kamienie (zwane jeńcami). Oddechem kamienia nazywamy niezajęte sąsiednie przecięcie (połączone linią z kamieniem). Na przykład kamień stojący samotnie na środku planszy ma cztery oddechy, a w rogu planszy – dwa.


## Wymagania funkcjonalne dotyczące iteracji 1

- W iteracji 1 należy zaimplementować następujące wymagania funkcjonalne (tylko takie!!):

- System powinien działać w oparciu o architekturę klient–serwer. W razie wątpliwości warto przyjrzeć się przykładom z tej strony.

- Gracz za pomocą aplikacji klienckiej powinien móc połączyć się z serwerem i dołączyć do gry.

- Gracz powinien móc wysłać ruch do drugiego gracza. W tej wersji interfejs użytkownika powinien być konsolowy. Z poziomu klienta (na konsoli) należy wpisać ruch, który będzie wysyłany przez serwer do drugiego klienta. Do tego celu potrzebna będzie klasa przechowująca planszę np. Board.

- Wymagane jest zaimplementowanie zasad 1–3 z powyższej listy.

## Dodatkowe wymagania

- Postaraj się tworzyć system starannie i w przemyślany sposób. Wykorzystaj UML, by zaproponować i rozwijać projekt systemu. Systemy chaotyczne, zaprojektowane i pisane na ostatnią chwilę, będą nisko ocenione. Bierz również pod uwagę, że to, co stworzysz, będzie miało wpływ na kolejną listę.

- Stosuj podejście iteracyjne i przyrostowe.

- Postaraj się wykorzystać poznane narzędzia i wzorce. Im więcej ich zastosujesz, tym wyżej będzie ocenione Twoje rozwiązanie. Ich użycie powinno być jednak zawsze uzasadnione.
