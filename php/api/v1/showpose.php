<?php
// Lire le contenu du fichier positions.txt
$fileContent = file_get_contents('positions.txt');

// Séparer les lignes en un tableau
$lines = explode("\n", $fileContent);

// Tableau pour stocker les positions des joueurs
$playerPositions = [];

// Parcourir les lignes et extraire les informations des joueurs
foreach ($lines as $line) {
  // Ignorer les lignes vides
  if (!empty($line)) {
    // Séparer les informations du joueur
    $playerInfo = explode(",", $line);
    $nom = $playerInfo[0];
    $x = $playerInfo[1];
    $y = $playerInfo[2];
    $z = $playerInfo[3];
	$carteX = $x;
	$carteY =  $z;
    // Ajouter les informations du joueur au tableau des positions
    $playerPositions[] = array(
      'nom' => $nom,
      'x' => $carteX,
      'y' => $y,
      'z' => $carteY
    );
  }
}

// Renvoyer les positions des joueurs au format JSON
header('Content-Type: application/json');
echo json_encode($playerPositions);
?>
