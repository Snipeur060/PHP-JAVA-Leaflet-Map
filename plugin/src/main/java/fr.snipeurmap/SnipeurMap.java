package fr.snipeurmap;

import org.bukkit.plugin.java.JavaPlugin;

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
             *  {"x": 0.0, "y": 0.0, "z": 0.0, "nom": "Snipeur060"}
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

    }



    private static class PlayerInfo {
        /* PlayerInfo récuperer les données des joueurs
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
            // Retourner la position X du joueur
            return x;
        }

        public double getY() {
            // Retourner la position Y du joueur
            return y;
        }

        public double getZ() {


            // Retourner la position Z du joueur
            return z;
        }

        public String getNom() {

            //  Retourner le nom du joueur
            return nom;
        }


        public String getuuid(){
            return uuid;

        }
    }
}
