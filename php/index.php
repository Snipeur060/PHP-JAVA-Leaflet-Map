<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Meta tags  -->
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"  content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

    <title>Snipeur Realtime Map</title>
    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin="">


    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>

    <!-- Leaflet -->
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
        crossorigin="" />
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet-src.js"
        crossorigin=""></script>
	  
	  <script src="
https://cdn.jsdelivr.net/npm/leaflet-rotate-map@0.3.1/leaflet-src.min.js
"></script>
<link href="
https://cdn.jsdelivr.net/npm/leaflet-rotate-map@0.3.1/leaflet.min.css
" rel="stylesheet">

    <!-- Leaflet tools -->
    <script type="text/javascript" src="unminedlib/NoGap.js"></script>
    <script type="text/javascript" src="unminedlib/functionaltilelayer.js"></script>

    <!-- Map info -->
    <script type="text/javascript" src="unminedlib/unmined.map.properties.js"></script>
    <script type="text/javascript" src="unminedlib/unmined.map.regions.js"></script>

    <!-- uNmINeD -->
    <script type="text/javascript" src="unminedlib/unmined.leaflet.js"></script>

  </head>

	
	
	<style>
        .coords-info {
            background-color: rgba(255, 255, 255, 0.8);
            padding: 5px 10px;
            border-radius: 5px;
            position: absolute;
            bottom: 10px;
            right: 10px;
            z-index: 1000;
        }
	
    </style>

	
	<script type="text/javascript">
       $(document).ready(function () {

            // Définition de la fonction de tuile
            var unminedLayer = new L.TileLayer.Functional(
                function (view) {
                    var zoom = view.zoom;
                    zoomFactor = Math.pow(2, zoom);

                    var tileSize = 256;
                    var minTileX = view.tile.column * tileSize / zoomFactor;
                    var minTileY = view.tile.row * tileSize / zoomFactor;
					
			

                    var url = ('tiles/zoom.{z}/{xd}/{yd}/tile.{x}.{y}.' + UnminedMapProperties.imageFormat)
                        .replace('{z}', zoom)
                        .replace('{yd}', Math.floor(view.tile.row / 10))
                        .replace('{xd}', Math.floor(view.tile.column / 10))
                        .replace('{y}', view.tile.row)
                        .replace('{x}', view.tile.column);
                    return url;
                },
                {
                    detectRetina: false,
                    noWrap: true // Bloque le zoom pour éviter que les tuiles inexistantes ne soient affichées
                }
            );


            // Initialisation de la carte
            var map = L.map('map', {
                crs: L.CRS.Simple,
				center: [0, 0],
                minZoom: 0,
                maxZoom: 2,
                layers: [unminedLayer],
                maxBoundsViscosity: 1.0,

            }).setView([0, 0], 0);


            map.attributionControl.setPrefix("Snipeur060 Realtime Map");

            // Contrôle pour afficher les coordonnées
            var coordsDiv = L.control();

            coordsDiv.onAdd = function (map) {
                this._div = L.DomUtil.create('div', 'coords-info');
                this._div.innerHTML = 'Coordonnées : ';
                return this._div;
            };

            coordsDiv.updateCoords = function (latlng) {
                this._div.innerHTML = 'Coordonnées : ' + latlng.lat.toFixed(2) + ', ' + latlng.lng.toFixed(2);
            };

            coordsDiv.addTo(map);

            // Mise à jour des coordonnées lorsque la souris bouge sur la carte
            map.on('mousemove', function (e) {
                coordsDiv.updateCoords(e.latlng);
            });
		   

		   
const playerLayer = L.layerGroup().addTo(map);
const activePlayers = new Set(); // Utilisation d'un Set pour stocker les pseudos des joueurs actifs

function updatePlayerMarker(nom, x, y, z) {
  const existingMarker = playerLayer.getLayers().find(marker => marker.options.nom === nom);

  if (existingMarker) {
    existingMarker.setLatLng([x, z]);
  } else {
	  const iconned = L.icon({
  iconUrl: `https://mc-heads.net/avatar/${nom}`, // Spécifiez le chemin vers votre icône personnalisée
  iconSize: [22, 22], // Définissez la taille de l'icône (en pixels)
  iconAnchor: [10, 15], // Définissez le point d'ancrage de l'icône (position relative à son coin supérieur gauche)
});	
	  
    const marker = L.marker([x, z],{icon: iconned}).addTo(playerLayer);
    marker.options.nom = nom;
    marker.bindPopup(`
	<div style='background-colo:red!important;'>Joueur: ${nom}<br>Position: (${x}, ${z})
	<br>
	 <a target="_blank" href=''
    class="btn border border-primary font-medium text-primary hover:bg-primary hover:text-white focus:bg-primary focus:text-white active:bg-primary/90 dark:border-accent dark:text-accent-light dark:hover:bg-accent dark:hover:text-white dark:focus:bg-accent dark:focus:text-white dark:active:bg-accent/90"
  >
    Voir le joueur
  </a>
  </div>
	`);
  }

  activePlayers.add(nom); // Ajoute le joueur au Set des joueurs actifs
}

function removeInactivePlayerMarkers() {
  const markersToRemove = playerLayer.getLayers().filter(marker => !activePlayers.has(marker.options.nom));

  markersToRemove.forEach(marker => {
    playerLayer.removeLayer(marker);
  });

  activePlayers.clear(); // Réinitialise le Set des joueurs actifs
}

function updatePlayerPositions() {
  fetch('api/v1/showpose')
    .then(response => response.json())
    .then(data => {
      
      data.forEach(player => {
        const { nom, x, y, z } = player;
        updatePlayerMarker(nom, -z, y, x);
      });

      removeInactivePlayerMarkers();
    })
    .catch(error => {
      console.error('Erreur lors de la requête AJAX :', error);
    });
}
updatePlayerPositions();
setInterval(updatePlayerPositions, 500);
		   
		   
		   
	   });
    </script>
 <body>
<div id="map" style="height: 600px; background-color:#192132;position: relative;"></div>
		  </body> 

	
	  <script
  src="https://code.jquery.com/jquery-3.7.0.min.js"
  integrity="sha256-2Pmvv0kuTBOenSvLm6bvfBSSHrUJ+3A7x6P5Ebd07/g="
  crossorigin="anonymous"></script>
</html>

