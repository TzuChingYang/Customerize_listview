<?php
$host="localhost";
$db_username="tzching";
$password="00000000";
$db_name="BMI_BMR";

$con = new mysqli($host,$db_username,$password,$db_name) or die("Unable to connect database");

if(mysqli_connect_error($con)){
    echo "Fail to connect ". mysqli_connect_error();
}

$query = $con->query("Select * from test_new_2");

if($query){
    while($row =$query->fetch_assoc()){
        $output[] =$row ;
    }

    print(json_encode($output));
}

$con->close();
?>

