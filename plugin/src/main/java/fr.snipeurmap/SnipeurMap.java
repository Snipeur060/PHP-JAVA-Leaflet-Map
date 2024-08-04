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
           //NE PAS OUBLIER DE FAIRE LA CLASSE PLAYERINFO
           return null;
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
