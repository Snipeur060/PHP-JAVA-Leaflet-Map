<?php
// Vider le contenu du fichier
$file = fopen('positions.txt', 'w');
fclose($file);

// Récupérer le corps de la requête
$jsonBody = file_get_contents('php://input');

// Convertir le JSON en tableau associatif
$data = json_decode($jsonBody, true);

// Vérifier si des données ont été reçues
if (!empty($data)) {
    // Ouvrir le fichier en mode append (ajout à la fin du fichier)
    $file = fopen('positions.txt', 'a');

    // Parcourir les joueurs et enregistrer leurs positions dans le fichier
    foreach ($data as $player) {
        $x = $player['x'];
        $y = $player['y'];
        $z = $player['z'];
        $nom = $player['nom'];
		$uuid = $player['uuid'];

        // Formatage de la ligne à enregistrer dans le fichier
        $line = "$nom,$x,$y,$z,$uuid\n";

        // Écrire la ligne dans le fichier
        fwrite($file, $line);
    }

    // Fermer le fichier
    fclose($file);
    //https://stackoverflow.com/questions/20620300/http-content-type-header-and-json
    header("Content-Type: Application/Json");
      echo '{"saved":"True"}';
} else {
     header("Content-Type: Application/Json");
        echo '{"saved":"False"}';
}

?>
