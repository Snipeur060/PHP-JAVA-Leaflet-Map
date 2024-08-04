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
}
