package fr.snipeurmap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class SnipeurMap extends JavaPlugin {
    private Timer timerSendRequest;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin SnipeurMap démarré !");


        //on enregistre un timer
        timerSendRequest = new Timer();

        // tick delay
        long delay = 3000;


        // enregistrer la tâche à exécuter
        timerSendRequest.schedule(new TimerTask() {
            @Override
            public void run() {
                //la fonction à exécuter
                sendDataToApi();
            }
        }, 0, delay);


    }

    private String sendDataToApi() {
        // Récupérer tous les joueurs connectés
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        // Créer une liste pour stocker les informations des joueurs
        List<PlayerInfo> playerInfoList = new ArrayList<>();
        // Pour chaque joueur
        for (Player player : players) {
            // Récupérer les positions du joueur
            double joueurX = player.getLocation().getX();
            double joueurY = player.getLocation().getY();
            double joueurZ = player.getLocation().getZ();
            // Récupérer le nom du joueur

            String joueurNom = player.getName();
            String joueurUUID = player.getUniqueId().toString();

            // Ajouter les informations du joueur à la liste
            PlayerInfo playerInfo = new PlayerInfo(joueurX, joueurY, joueurZ, joueurNom,joueurUUID);
            // Ajouter les informations du joueur à la liste
            playerInfoList.add(playerInfo);
        }

        try {
            // Construire l'URL de l'API en PHP
            URL url = new URL("https://api.domain.dmpo/api/v1/savepos");

            // Ouvrir la connexion HTTP
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //method post
            conn.setRequestMethod("POST");
            // on recupere le body (on ne le laisse pas partir)
            conn.setDoOutput(true);

            // Convertir la liste en format JSON
            String jsonBody = convertListToJson(playerInfoList);

            // Envoyer le corps de la requête
            try (OutputStream os = conn.getOutputStream()) {
                // on envoie le body (on utilise UTF-8 pour être assez universel)
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Récupérer la réponse de l'API
            int responseCode = conn.getResponseCode();
            // Ici on va pas afficher pour pas spam la console
            //System.out.println("Code de réponse : " + responseCode);

            // fermeture de la connexion
            conn.disconnect();
        } catch (Exception e) {

            //e.printStackTrace();
            // erreur zut
            getLogger().warning("Erreur lors de l'envoi des positions des joueurs à l'API");
        }
        return null;
    }


    private String convertListToJson(List<PlayerInfo> playerInfoList) {

        // Créer un StringBuilder pour construire la chaîne JSON
        StringBuilder sb = new StringBuilder();
        // Ajouter le début de la chaîne JSON
        sb.append("[");
        // Pour chaque joueur
        for (int i = 0; i < playerInfoList.size(); i++) {
            // Récupérer les informations du joueur
            PlayerInfo playerInfo = playerInfoList.get(i);
            // Ajouter les informations du joueur à la chaîne JSON
            /* Exemple du tableau construit
             *  [
             *  {"x": 0.0, "y": 0.0, "z": 0.0, "nom": "Snipeur060"},
             * ]
             *  */
            sb.append("{\"x\": ")
                    // data x position
                    .append(playerInfo.getX())
                    .append(", \"y\": ")
                    //data y position
                    .append(playerInfo.getY())
                    .append(", \"z\": ")
                    //data z position
                    .append(playerInfo.getZ())
                    .append(", \"nom\": \"")
                    //data name
                    .append(playerInfo.getNom())
                    //append player uuid
                    .append("\", \"uuid\": \"")
                    .append(playerInfo.getuuid())
                    // end of json
                    .append("\"}");
            // Ajouter une virgule si ce n'est pas le dernier joueur
            if (i < playerInfoList.size() - 1) {
                // comme du python
                sb.append(", ");
            }

        }
        // Ajouter la fin de la chaîne JSON
        sb.append("]");
        //
        return sb.toString();
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin SnipeurMap arrêté !");

        //on arrête le timer
        timerSendRequest.cancel();

        //envoyer une requête vide
        try{
            // Construire l'URL de l'API en PHP
            URL url = new URL("https://api.domain.dmpo/api/v1/savepos");

            // Ouvrir la connexion HTTP
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //method post
            conn.setRequestMethod("POST");
            // on recupere le body (on ne le laisse pas partir)
            conn.setDoOutput(true);

            // Convertir la liste en format JSON
            String jsonBody = "[]";

            // Envoyer le corps de la requête
            try (OutputStream os = conn.getOutputStream()) {
                // on envoie le body (on utilise UTF-8 pour être assez universel)
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Récupérer la réponse de l'API
            int responseCode = conn.getResponseCode();
            // Ici on va pas afficher pour pas spam la console
            //System.out.println("Code de réponse : " + responseCode);

            // fermeture de la connexion
            conn.disconnect();
        } catch (Exception e) {

            //e.printStackTrace();
            // erreur zut
            getLogger().warning("Erreur lors de l'envoi des positions des joueurs à l'API");
        }

    }


    private static class PlayerInfo {
        /* Class: PlayerInfo
         *  Description: Classe pour stocker les informations d'un joueur
         * Attributs:
         *  - x: double
         *  - y: double
         *  - z: double
         *  - nom: String
         * Méthodes:
         * - getX(): double
         * - getY(): double
         * - getZ(): double
         * - getNom(): String
         * Constructeur:
         * - PlayerInfo(double x, double y, double z, String nom)
         * Description: Constructeur de la classe PlayerInfo
         * */
        private double x;
        private double y;
        private double z;
        private String nom;

        private String uuid;

        public PlayerInfo(double x, double y, double z, String nom, String uuid) {
            // Constructeur de la classe PlayerInfo
            this.x = x;
            this.y = y;
            this.z = z;
            this.uuid = uuid;
            this.nom = nom;
        }

        public double getX() {
            /* Fonction: getX
             *  Paramètres: aucun
             * Retour: double
             * Description: Retourner la position X du joueur
             *  */
            // Retourner la position X du joueur
            return x;
        }

        public double getY() {
            /* Fonction getY
             *  Paramètres: aucun
             * Retour: double
             * Description: Retourner la position Y du joueur
             * */
            // Retourner la position Y du joueur
            return y;
        }

        public double getZ() {
            /* Fonction: getZ
             *  Paramètres: aucun
             * Retour: double
             * Description: Retourner la position Z du joueur
             * */
            // Retourner la position Z du joueur
            return z;
        }

        public String getNom() {
            /* Fonction: getNom
             *  Paramètres: aucun
             * Retour: String
             * Description: Retourner le nom du joueur
             * */
            //  Retourner le nom du joueur
            return nom;
        }


        public String getuuid(){
            /* Fonction: uuid
             *  Paramètres: aucun
             * Retour: String
             * Description: Retourner l'uuid du joueur
             * */

            return uuid;

        }
    }
}
