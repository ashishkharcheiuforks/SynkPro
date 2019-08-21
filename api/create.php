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
   

   $dataArr = json_decode($json, true);

   $error = false;

  // echo 'note_title '. $dataArr[$i]['note_title'].'</br>';

    
        //var_dump($dataArr);

        //creating a statement to insert to database 
        $stmt = $conn->prepare("INSERT INTO notes (note_title, note_content, note_by, note_rating,note_is_synced) VALUES (?,?,?,?,?)");
            
        //binding the parameter to statement 
        $stmt->bind_param("sssii",   
            $dataArr['note_title'],
            $dataArr['note_content'],
            $dataArr['note_by'],
            $dataArr['note_rating'],
            $dataArr['note_is_synced']
        );
        
        if($stmt->execute()){
            $error = false;
            //echo 'Inserted';
        }else{
            $error = true;
        }
   
      if(!$error){
          
       $response['error'] = $error; 
       $response['message'] = 'Inserted successfully'; 
       $response['note_id'] = $stmt->insert_id; 
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