# Projekt Koszyka Zakupowego

Projekt ten jest przykładową implementacją algorytmu dzielącego koszyk zakupowy na minimalną liczbę rodzajów dostaw. Zastosowałem język Java wraz z narzędziem Maven do budowy projektu. Dodatkowo, projekt wykorzystuje bibliotekę `json-simple` do obsługi formatu JSON i testowania jednostkowego przy użyciu JUnit 5.

## Struktura Projektu

W ramach projektu znajdują się następujące elementy:

- **src/main/java/com/ocado/basket**: Katalog zawierający pliki źródłowe projektu.
  - `BasketSplitter.java`: Główna klasa implementująca algorytm dzielący koszyk na rodzaje dostaw.
  - `MainTest.java`: Klasa zawierająca przykładowe wywołania funkcji `split` dla pięciu koszyków zakupowych.
- **src/main/java/com/ocado/exceptions**
  - `TooManyConfigProductsException.java`: Klasa wyjątku w przypadku za dużej ilości produktów w pliku config.json.
  - `TooManyProductsInBasket.java`: Klasa wyjątku w przypadku za dużej ilości produktów w koszyku.
  - `TooManyShippingOptionsException.java`: Klasa wyjątku w przypadku za dużej ilości opcji dostawy na produkt.
  
- **src/test/java/com/ocado/basket**: Katalog zawierający testy jednostkowe.
  - `BasketSplitterTest.java`: Testy jednostkowe sprawdzające podstawowe przypadki funkcji `split`.



## Uwagi Dodatkowe

1. Aby uruchomić projekt za pomocą pliku .jar musi on znajdować się bezpośrednio w katalogu BacketSplitterLib (Związane jest to ze ścieżkami do plików testowych i konfiguracyjnych). Uruchamiamy wtedy funkcje main z klasy MainTest.java.

2. W pliku `MainTest.java` znajdują się wywołania metody `split` dla pięciu koszyków zakupowyc, dwa pierwsze z tych koszyków są podane przez w treści zadania, a reszta została ułożona przeze mnie. Wynik działania metody `split` jest wypisany na wyjście standardowe. Testy jednostkowe zostały umieszczone we wspomnianym wyżej katalogu projektu w klasie BasketSplitterTest.java

