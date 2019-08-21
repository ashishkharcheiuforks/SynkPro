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



//if there is a post request move ahead 
if($_SERVER['REQUEST_METHOD']=='POST'){
 
    //getting the name from request 
    $json  = file_get_contents('php://input');
   

   $decodedJson = json_decode($json, true);

   $error = false;
   $dataArr = $decodedJson['data'];

   //var_dump($dataArr);
   
   $insertedIds = array();

   for($i=0;$i<sizeof($dataArr);$i++){

       // echo 'note_title '. $dataArr[$i]['note_title'].'</br>';

        //creating a statement to insert to database 
        $stmt = $conn->prepare("INSERT INTO notes (note_title, note_content, note_by, note_rating, note_is_synced) VALUES (?,?,?,?,?)");
        
        $isSynced = 1;

        //binding the parameter to statement 
        $stmt->bind_param("sssii", 
            $dataArr[$i]['note_title'],
            $dataArr[$i]['note_content'],
            $dataArr[$i]['note_by'],
            $dataArr[$i]['note_rating'],
            $isSynced
        );
        
        if($stmt->execute()){
            $tmp = array();
            $tmp["id"] = $stmt->insert_id;
            array_push($insertedIds, $tmp);
           // $insertedIds[$i] = $stmt->insert_id; 
            $error = false;
            //echo 'Inserted';
        }else{
            $error = true;
            break;
        }


   }
   
      if(!$error){
          
       $response['error'] = $error; 
       $response['message'] = 'Inserted successfully'; 
      //$response['insertedIds'] = $insertedIds;
       echo json_encode($response);
       
    }else{

        $response['error'] = $error; 
        $response['message'] = 'Please try later'; 
        echo json_encode($response);

    }
   
    //var_dump($decodedJson);

}else{
    $response['error'] = true; 
    $response['message'] = "Invalid request method"; 
    echo json_encode($response);
}

?>