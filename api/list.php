<?php
 
/*
* Database Constants
* Make sure you are putting the values according to your database here 
*/
define('DB_HOST','localhost');
define('DB_USERNAME','root');       
define('DB_PASSWORD','');
define('DB_NAME', 'synkpro');
 
//Connecting to the database
$conn = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
 
//checking the successful connection
if($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
 
if($_SERVER['REQUEST_METHOD']=='POST'){
 
    //getting the name from request 
    $json  = file_get_contents('php://input');
   

   $decodedJson = json_decode($json, true);

   if(isset($decodedJson['lastSyncTime'])){

    $error = false;

    $response = array();
    $response["data"] = array();
    
    if( strlen($decodedJson['lastSyncTime']) > 0){
       
        $lastSyncTime = $decodedJson['lastSyncTime'];

        $stmt = $conn->prepare("SELECT 
                                note_id,
                                note_title, 
                                note_content, 
                                note_by, 
                                note_rating, 
                                note_is_synced, 
                                note_created_on, 
                                note_last_updated_on 
                                FROM notes
                                WHERE note_created_on
                                BETWEEN ? AND ?
                                ");

        date_default_timezone_set('Asia/Kolkata');
        $currentDate = date('Y-m-d H:i:s');
         $stmt->bind_param("ss",$lastSyncTime, $currentDate);

    }else{

        $stmt = $conn->prepare("SELECT note_id,note_title, note_content, note_by, note_rating, note_is_synced, note_created_on, note_last_updated_on FROM notes");

    }

    $stmt->execute();

    $stmt->bind_result($note_id, $note_title, $note_content, $note_by, $note_rating, $note_is_synced, $note_created_on, $note_last_updated_on);
        
    while($stmt->fetch()){
        $hero  = array();
        $hero['note_id'] = $note_id; 
        $hero['note_title'] = $note_title; 
        $hero['note_content'] = $note_content; 
        $hero['note_by'] = $note_by; 
        $hero['note_rating'] = $note_rating; 
        $hero['note_is_synced'] = $note_is_synced; 
        $hero['note_created_on'] = $note_created_on; 
        $hero['note_last_updated_on'] = $note_last_updated_on; 
        
        array_push($response["data"], $hero); 
        }

        if(count($response["data"]) > 0){
            $response['error'] = false;
            echo json_encode($response);
        }else{

            $resp['error'] = true; 
            $resp['message'] = "No Data found"; 
            echo json_encode($resp);
        }
    
   }else{
    $response['error'] = true; 
    $response['message'] = "Invalid request method"; 
    echo json_encode($response);
   }

}else{
    $response['error'] = true; 
    $response['message'] = "Invalid request method"; 
    echo json_encode($response);
}

?>