<?php
require_once "DB_Functions.php";
$db = new DB_Functions();
 

$response = array("error" => FALSE);
 
if (isset($_POST['roll_verify']) && isset($_POST['roll'])) {
 
    
    $roll = $_POST['roll'];
    $roll_verify = $_POST['roll_verify'];
    
    $user = $db->VerifyUser($roll_verify, $roll);
	
	$response["error"] = FALSE;
    $response["status"]=$user;
    echo json_encode($response);
   
} else {
    
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters roll or roll_verify missing!";
    echo json_encode($response);
}
?>