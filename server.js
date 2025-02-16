const express = require('express');
const mysql = require('mysql2/promise');
const app = express();
const PORT = 8080;
const cors = require('cors');

// Active CORS pour toutes les origines
app.use(cors());

// Middleware pour parser les requêtes JSON
app.use(express.json());
app.use(express.static('public')); // Servir les fichiers statiques depuis le dossier "public"

// Configuration de la connexion MySQL
const connection = mysql.createPool({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'alert_system',
    waitForConnections: true,
    connectionLimit: 10,
    queueLimit: 0
});

// Route pour créer une alerte
app.post('/createAlert', async (req, res) => {
  const { email, motCle, frequency } = req.body;

  // Valider les données entrantes
  if (!email || !motCle || !frequency) {
      return res.status(400).json({ success: false, error: 'Données manquantes.' });
  }

  try {
      // Utiliser connection.query pour START TRANSACTION
      await connection.query('START TRANSACTION'); // Début de la transaction

      // Vérifier si l'utilisateur existe déjà
      const [userResults] = await connection.execute('SELECT id FROM utilisateurs WHERE email = ?', [email]);
      let userId;

      if (userResults.length > 0) {
          userId = userResults[0].id; // Utilisateur existant
      } else {
          // Insérer un nouvel utilisateur
          const [insertUserResults] = await connection.execute('INSERT INTO utilisateurs (email, date_inscription) VALUES (?, NOW())', [email]);
          userId = insertUserResults.insertId; // ID du nouvel utilisateur
      }

      // Insérer la préférence de l'utilisateur
      await connection.execute('INSERT INTO preferences (utilisateur_id, mot_cle, frequence) VALUES (?, ?, ?)', [userId, motCle, frequency]);

      // Utiliser connection.query pour COMMIT
      await connection.query('COMMIT'); // Validation de la transaction

      console.log('Nouvelle alerte créée :', { email, motCle, frequency });

      // Réponse de succès
      res.json({ success: true, message: 'Alerte créée avec succès!' });
  } catch (error) {
      // Utiliser connection.query pour ROLLBACK en cas d'erreur
      await connection.query('ROLLBACK'); // Annulation de la transaction
      console.error('Erreur lors de la création de l\'alerte:', error);
      res.status(500).json({ success: false, message: 'Erreur lors de la création de l\'alerte.' });
  }
});

// Démarrer le serveur
app.listen(PORT, () => {
    console.log(`Serveur en écoute sur le port ${PORT}`);
});