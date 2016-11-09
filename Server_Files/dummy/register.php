<?php
 
require_once "DB_Functions.php";
$db = new DB_Functions();
 

$response = array("error" => FALSE);
 
if (isset($_POST['name']) && isset($_POST['roll']) && isset($_POST['phone']) && isset($_POST['email']) && isset($_POST['password'])) {
 
    
    $name = $_POST['name'];
	$roll = $_POST['roll'];
	$phone = $_POST['phone'];
    $email = $_POST['email'];
    $password = $_POST['password'];
 
   
    if ($db->isUserExisted($roll)) {
        
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed";
		echo json_encode($response);
        
    } else {
		
       
        $user = $db->storeUser($name,$roll,$password,$phone ,$email);
        if ($user) {
			
            
            $response["error"] = FALSE;
            $response["user"]["name"] = $user["name"];
			$response["user"]["roll"] = $user["roll"];
			$response["user"]["phone"] = $user["phone"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
            
            echo json_encode($response);
        } else {
            
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    
}
?>