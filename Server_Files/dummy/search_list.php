<?php
require_once "DB_Functions.php";
$db = new DB_Functions();
 

$response = array("error" => FALSE);
 
if (isset($_POST['query1']) && isset($_POST['query2']) && isset($_POST['last'])) {
 
   
    $query1 = $_POST['query1'];
	$query2 = $_POST['query2'];
	$last = $_POST['last'];
    
 
   
    $user = $db->getUsersByQuery($query1,$query2,$last);
 
    if ($user != false) {
        
        $response["error"] = FALSE;
        $response["user"]["name"] = $user["name"];
		$response["user"]["roll"] = $user["roll"];
		$response["user"]["phone"] = $user["phone"];
        $response["user"]["email"] = $user["email"];
		$response["user"]["verified_by"] = $user["verified_by"];
        $response["user"]["created_at"] = $user["created_at"];
        echo json_encode($response);
    } else {
       
        $response["error"] = TRUE;
        $response["error_msg"] = "No user found";
        echo json_encode($response);
    }
} else {
   
    $response["error"] = TRUE;
    $response["error_msg"] = "Required Query Missing!";
    echo json_encode($response);
}
?>