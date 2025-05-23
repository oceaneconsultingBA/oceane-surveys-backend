# Guide de Déploiement - Application Oceane-Enquetes

Ce document décrit les procédures pour déployer et maintenir l'application Oceane-Enquetes sur une instance EC2. L'application est composée d'un backend Spring Boot, d'un frontend Angular, et utilise PostgreSQL comme base de données.

## Connexion à l'Instance EC2

```bash
ssh -i "/chemin/vers/votre-cle.pem" ec2-user@votre-ip-ec2
cd ~/oceane-enquetes
```

## Structure du Projet sur EC2

Le projet est organisé comme suit sur l'instance EC2 :

```
~/oceane-enquetes/
├── docker-compose.yml
├── app/                  # Application Spring Boot
├── db/                   # Données PostgreSQL
└── nginx/
    ├── www/              # Fichiers du frontend Angular
    └── conf/             # Fichiers de configuration Nginx
        └── default.conf  # Configuration principale de Nginx
```

## Déploiement du Backend (Spring Boot)

### 1. Préparation du JAR sur votre machine locale

```bash
# Dans votre projet Spring Boot
mvn clean package
```

### 2. Transfert du JAR vers EC2

```bash
scp -i "/chemin/vers/votre-cle.pem" target/votre-application.jar ec2-user@votre-ip-ec2:~/oceane-enquetes/app/
```

### 3. Redémarrage de l'application sur EC2

```bash
# Sur l'instance EC2
cd ~/oceane-enquetes
docker-compose down
docker-compose up -d
```

Ou pour redémarrer uniquement l'application sans toucher aux autres services :

```bash
docker-compose up -d app
```

## Déploiement du Frontend (Angular)

### 1. Construction de l'application sur votre machine locale

```bash
# Configuration production avec la bonne URL d'API
ng build --configuration production
```

### 2. Transfert des fichiers vers EC2

```bash
scp -i "/chemin/vers/votre-cle.pem" -r dist/nom-de-votre-app/* ec2-user@votre-ip-ec2:~/oceane-enquetes/nginx/www/
```

### 3. Redémarrage du serveur web sur EC2

```bash
# Sur l'instance EC2
cd ~/oceane-enquetes
docker-compose restart web
```

Ou redémarrer tous les services si nécessaire :

```bash
docker-compose down
docker-compose up -d
```

## Gestion des Conteneurs Docker

### Afficher l'état des conteneurs

```bash
docker-compose ps
```

### Gestion des services

```bash
# Redémarrer tous les services
docker-compose restart

# Arrêter tous les services
docker-compose down

# Démarrer tous les services
docker-compose up -d

# Redémarrer un service spécifique
docker-compose restart app   # Pour l'application Spring Boot
docker-compose restart db    # Pour PostgreSQL
docker-compose restart web   # Pour Nginx
```

## Consultation des Logs

```bash
# Voir les logs de tous les services
docker-compose logs

# Suivre les logs en temps réel
docker-compose logs -f

# Voir les logs d'un service spécifique
docker-compose logs app    # Logs de Spring Boot
docker-compose logs db     # Logs de PostgreSQL
docker-compose logs web    # Logs de Nginx
```

## Accès à la Base de Données

### Connexion à PostgreSQL via terminal

```bash
docker exec -it enquetes-db psql -U postgres -d enquetes
```

### Commandes SQL utiles

```sql
-- Lister toutes les tables
\dt

-- Décrire la table surveys
\d+ surveys

-- Afficher toutes les enquêtes
SELECT * FROM surveys;

-- Quitter PostgreSQL
\q
```

### Connexion via un client SQL

Vous pouvez également utiliser un client SQL comme pgAdmin, DBeaver ou DataGrip avec les paramètres suivants :

- **Hôte** : votre-ip-ec2
- **Port** : 5432
- **Base de données** : enquetes
- **Utilisateur** : postgres
- **Mot de passe** : votre-mot-de-passe

## Modification du fichier docker-compose.yml

Pour modifier la configuration des services, éditez le fichier docker-compose.yml :

```bash
cd ~/oceane-enquetes
nano docker-compose.yml
```

Après modification, redémarrez les services :

```bash
docker-compose down
docker-compose up -d
```

## Résolution des Problèmes Courants

### Si l'application ne démarre pas

1. Vérifiez les logs :
   ```bash
   docker-compose logs app
   ```

2. Vérifiez que le JAR a bien été transféré :
   ```bash
   ls -la ~/oceane-enquetes/app/
   ```

### Si le frontend n'affiche pas les dernières modifications

1. Vérifiez que les fichiers ont bien été transférés :
   ```bash
   ls -la ~/oceane-enquetes/nginx/www/
   ```

2. Videz le cache de votre navigateur ou utilisez la navigation privée pour tester.

### Problèmes de connexion à la base de données

1. Vérifiez que le conteneur PostgreSQL est bien en cours d'exécution :
   ```bash
   docker-compose ps
   ```

2. Vérifiez les logs de la base de données :
   ```bash
   docker-compose logs db
   ```