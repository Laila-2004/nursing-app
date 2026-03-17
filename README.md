# 🏥 NursingApp — Mise en relation Patients / Infirmières

Application en ligne de commande développée avec **Spring Boot**, permettant
à des patients de réserver des infirmières à domicile et à des infirmières
de gérer leur planning et leurs disponibilités.

---

## ✨ Fonctionnalités

### 👤 Côté Patient
- Connexion sécurisée par email / mot de passe
- Recherche d'infirmières (toutes ou par ville)
- Consultation des disponibilités de chaque infirmière
- Réservation d'un créneau avec motif
- Suivi de ses réservations (EN_ATTENTE, CONFIRMEE, REFUSEE)
- Modification du profil (nom, ville)

### 👩‍⚕️ Côté Infirmière
- Consultation du planning complet
- Acceptation ou refus des demandes en attente
- Ajout / suppression de disponibilités (jour + plage horaire)
- Modification du profil (nom, ville)

---

## 🛠️ Stack technique

| Technologie | Rôle |
|---|---|
| Java 17 | Langage |
| Spring Boot 3.2 | Framework principal (sans Web) |
| Spring Data JPA | Accès base de données |
| H2 (in-memory) | Base de données embarquée |
| Lombok | Réduction du code boilerplate |
| Scanner | Interface console interactive |

---

## 📁 Architecture
```
src/main/java/com/nursing/
├── entity/          → Modèles JPA (User, Disponibilite, Reservation)
├── repository/      → Interfaces Spring Data JPA
├── service/         → Logique métier (Auth, User, Reservation, Disponibilite)
├── menu/            → Interface console (MenuAuth, MenuPatient, MenuInfirmiere)
├── config/          → Initialisation des données mock
└── NursingApplication.java
```

---

## ▶️ Lancer l'application

### Prérequis
- Java 17+
- Maven 3.6+

### Commandes
```bash
# Cloner le projet
git clone https://github.com/Laila-2004/nursing-app.git
cd nursing-app

# Compiler et lancer
mvn spring-boot:run
```

---

## 🔑 Comptes de démonstration

| Rôle | Email | Mot de passe |
|---|---|---|
| Patient | ahmed@patient.ma | 1234 |
| Patient | fatima@patient.ma | 1234 |
| Infirmière | nadia@infirmiere.ma | 1234 |
| Infirmière | sanaa@infirmiere.ma | 1234 |
| Infirmière | khadija@infirmiere.ma | 1234 |

---

## 🗄️ Modèle de données
```
User
 ├── id, nom, email, motDePasse, role (PATIENT | INFIRMIERE), ville

Disponibilite
 ├── id, infirmiere (User), jour (DayOfWeek), heureDebut, heureFin

Reservation
 ├── id, patient (User), infirmiere (User), date, heure, motif
 └── statut : EN_ATTENTE | CONFIRMEE | REFUSEE
```

---

## 👨‍💻 Auteur
LAILA MASSKOU.
