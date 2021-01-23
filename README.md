//Copyright Traistaru Andreea-Maria
Etapa 2 - Proiect POO - Sistem Energetic

Implementare :
     
    -> clasele sunt organizate in pachete in functie de functionalitatea respectivelor clase:
            - pachetele "input" si respectiv "output" contin clasele necesare pentru preluarea 
            inputului, respectiv pentru crearea outputului;
            - pachetul "strategies" contine implementarea pattern-ului Strategy;
            - pachetul "data" contine clasele de consumator, distributor si producator cu care 
            se lucreaza, precum si clasele care implementeaza simularile lunare;
            - pachetul "entities" contine design pattern-ul Factory;
    
    -> design pattern-ul Observer se implementeaza intre un Subiect si distribuitori, 
    prin intermediul Subiectului care contine o lista cu observatorii(adica distribuitorii) 
    pe care ii notifica atunci cand se modifica energia unui producer;
  
Functionare :

    -> in clasa Main se vor citi  toate campurile din fisierul JSON;
    -> tot aici se executa runda initiala apoi se parcurg update-urile lunare, iar acestea sunt 
    realizate prin intermediul clasei MonthlySimulation;
    -> apoi prin intermediul clasei PayDebts sunt consumatorii si distribuitorii platesc 
    cheltuielile lunare si isi primesc veniturile;
    -> producatorii isi actualizeaza si ei cantitatea de energie, iar apoi notifica
    distribuitorii;
   
    
   
            