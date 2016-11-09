<?php
require_once "DB_Functions.php";
$db = new DB_Functions();
 

$response = array("error" => FALSE);
 
if (isset($_POST['roll']) && isset($_POST['password'])) {
 
    
    $roll = $_POST['roll'];
    $password = $_POST['password'];
 
    
    $user = $db->getUserByRollAndPassword($roll, $password);
 
    if ($user != false) {
        
        $response["error"] = FALSE;
        $response["user"]["name"] = $user["name"];
		$response["user"]["phone"] = $user["phone"];
        $response["user"]["email"] = $user["email"];
		$response["user"]["verified"] = $user["verified_by"];
        $response["user"]["created_at"] = $user["created_at"];
        echo json_encode($response);
    } else {
       
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
} else {
   
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters roll or password is missing!";
    echo json_encode($response);
}
?>