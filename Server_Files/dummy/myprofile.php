<?php
require_once "DB_Functions.php";
$db = new DB_Functions();
 

$response = array("error" => FALSE);
 
if (isset($_POST['roll']) && !isset($_POST['name']) && !isset($_POST['email']) && !isset($_POST['phone'])) {
 
    
    $roll = $_POST['roll'];
    $random="n1u2l3l4";
	$first="0";
 
    
    $user = $db->getUsersByQuery($roll, $random,$first);
 
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
        $response["error_msg"] = "Roll no is wrong!";
        echo json_encode($response);
    }
}else if(isset($_POST['roll']) && isset($_POST['name']) && isset($_POST['email']) && isset($_POST['phone'])){
	
	$roll = $_POST['roll'];
	$name = $_POST['name'];
	$phone = $_POST['phone'];
	$email = $_POST['email'];
	$user = $db->UpdateUser($name, $roll,$phone,$email);
 
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
        $response["error_msg"] = "Roll no is wrong!";
        echo json_encode($response);
    }
	
}
 else {
   
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters roll is missing!";
    echo json_encode($response);
}
?>