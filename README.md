# Medical Scheduling System
Acest proiect presupune implementarea unui sistem de programări medicale.
Proiectul este structurat în 4 pachete (Domain, Exceptions, Persistence, Services), după cum urmează:

## Domain
Pachetul conține entitățile de bază ale aplicației:
* client;
* medic;
* service;
* appointment;
* room.

Pe lângă acestea, se folosesc clase auxiliare, precum:  
Clasa _Person_ reprezintă o persoană, fiind superclasă pentru clasele _Client_ și _Medic_.

Clasele _Date_ și _DateTime_ rețin informații despre datele calendaristice stocate în celelalte clase
(_DateTime_ derivă din clasa _Date_ și conține în plus ora și minutul).
De exemplu, un client are o dată de naștere (tipul Date), în timp ce o programare stochează atât data, cât și ora (tipul DateTime).


Fiecare medic are o specializare și fiecare serviciu oferit aparține unei specializări.
Specializările sunt stocate într-un enum, denumit _Speciality_.


## Exceptions
Pachetul conține clase pentru tratarea excepțiilor. Acestea vor fi adăugate ulterior.

## Persistence
Pachetul conține clase ce rețin datele despre entitățile de bază menționate în Domain:
* AppointmentRepository;
* ClientRepository;
* MedicRepository;
* RoomRepository;
* ServiceRepository.

Fiecare dintre acestea permit operații de inserare, editare, ștergere și accesare a datelor, implementând interfața _GenericRepository_.

## Services
Pachetul conține clase care oferă servicii, grupate în funcție de tipul datelor accesate:
* AppointmentServices;
* ClientServices;
* MedicServices;
* ServiceServices.

Serviciile folosesc operațiile de bază definite în clasele din pachetul Persistence,
adăugând verificări suplimentare.

De exemplu, pentru a putea adăuga o programare, trebuie să fie deja stocate informații despre medicul, clientul și serviciul
pentru care se realizează programarea. În plus, intervalul orar în care aceasta se desfășoară trebuie sa nu se suprapuna cu
alte programări la același medic, iar specializarea medicului trebuie să corespundă cu specializarea din care face parte serviciul.


## View
Conține o clasă care intermediază relația dintre utilizator si aplicație, având următoarele utilități:
* afișarea meniului;
* citirea datelor oferite de utilizator;
* apelarea serviciilor în funcție de opțiune.

Meniul oferă posibilitatea de a adăuga și afișa date despre clienți/medici/servicii/programări și de a modifica sau șterge programări după anumite criterii. 
