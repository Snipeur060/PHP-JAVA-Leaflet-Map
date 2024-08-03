package fr.snipeurmap;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Timer;
import java.util.TimerTask;

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

            }
        }, 0, delay);


    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin SnipeurMap arrêté !");

        //on arrête le timer
        timerSendRequest.cancel();

    }
}
