<?php
/* INIT mysli conn
$db = new mysqli(__SERVERNAME__, __username__, __password__);
*/
/*
Sql DATABASE : (id,nom, x, y, z, heure,uuid)
id : int Auto Increment
nom: VARCHAR 255
x: VARCHAR 255
y: VARCHAR 255
z: VARCHAR 255
heure: VARCHAR 255
uuid: VARCHAR 255
*/
/*if (in_array($_SERVER['REMOTE_ADDR'],$autorizedip) != true) {
	echo "Fatal Error: Ip not whitelist for this";
	die();
}*/

// Effectuer une requête vers l'API
$url = "https://web.domain.fr/api/v1/showpos";
$response = file_get_contents($url);
// Vérifier si la requête a réussi
if ($response !== false and $response != "[]") {
    // Convertir les données JSON en un tableau associatif PHP
    $data = json_decode($response, true);

    // Préparer les valeurs pour la requête SQL
    $values = array();
    foreach ($data as $playerData) {
        $nom = $playerData['nom'];
        $x = floatval($playerData['x']);
        $y = floatval($playerData['y']);
        $z = floatval($playerData['z']);
		$uuid =  $playerData['uuid'];
        $heure = date("Y-m-d H:i:s"); // Obtenir l'heure actuelle

        // Ajouter les valeurs à la liste des valeurs pour la requête SQL
        $values[] = "('$nom', $x, $y, $z, '$heure','$uuid')";
    }

    // Construire la requête SQL
    $sql = "INSERT INTO playersposlog (nom, x, y, z, heure,uuid) VALUES " . implode(",", $values);

    // Exécuter la requête SQL
    if ($db->query($sql) === TRUE) {
        echo "Données insérées avec succès.\n";
    } else {
        echo "Erreur lors de l'insertion des données : " . $db->error . "\n";
    }

} else {
    echo "Échec de la requête vers l'API.";
}
?>
