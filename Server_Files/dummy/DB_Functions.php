<?php
 

 
class DB_Functions {
 
    private $conn;
 
    
    function __construct() {
        require_once 'DB_Connect.php';
        
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    
    function __destruct() {
         
    }
 
    
    public function storeUser($name,$roll,$pass,$phone,$email) {
        $stmt = $this->conn->prepare("INSERT INTO students(name,roll,password,phone,email, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $name, $roll, $pass, $phone, $email);
        $result = $stmt->execute();
        $stmt->close();
		
       
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM students WHERE roll = ?");
            $stmt->bind_param("s", $roll);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
 
    public function getUserByRollAndPassword($roll, $password) {
 
        $stmt = $this->conn->prepare("SELECT * FROM students WHERE roll = ?");
 
        $stmt->bind_param("s", $roll);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            
          
            if ($user['password'] == $password) {
               
                return $user;
            }
        } else {
            return NULL;
        }
    }
	public function VerifyUser($roll_verify,$roll) {
		$stmt = $this->conn->prepare("SELECT * FROM students WHERE roll = ?");
		$stmt->bind_param("s", $roll);
		if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
			$stmt->close();
			
            if ($user['verified_by'] != "none") {
 			$stmt = $this->conn->prepare("UPDATE students SET verified_by= ? WHERE roll= ?");
			$stmt->bind_param("ss", $roll,$roll_verify);
			if ($stmt->execute()) {
            return "done";            
        }else{
			return "error";
		}
			}else{
			return "not verified";
		}
		}else {
            return NULL;
        }
    }
	
	
	
	
	
public function getUsersByQuery($query1,$query2,$last) {
 
        $stmt = $this->conn->prepare("SELECT * FROM students WHERE (roll like '%$query1%' or roll like '%$query2%'or name like '%$query1%' or name like '%$query2%') and roll > '$last' order by 'name';");
 
        
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
                return $user;
            }
         else {
            return NULL;
        }
    }
 
    
    public function isUserExisted($roll) {
        $stmt = $this->conn->prepare("SELECT roll from students WHERE roll = ?");
 
        $stmt->bind_param("s", $roll);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            
            $stmt->close();
            return true;
        } else {
            
            $stmt->close();
            return false;
        }
    }
 
    
    
 
}
 
?>