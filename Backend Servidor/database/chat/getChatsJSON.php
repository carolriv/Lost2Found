<?php
	if(isset($_POST["json"])) {
	    $json = $_POST["json"];
	    $json = urldecode($json);
	    $json = str_replace("\\", "",$json);
	    $jsonencode = json_decode($json);

	    $idUser = $jsonencode[0]->id;

	    require_once("chatClass.php");

	    $chatObject = new Chat();
	    $chatListObject = $chatObject->getChats($idUser);
	    
	    echo json_encode($chatListObject, JSON_UNESCAPED_SLASHES);
	}
?>
