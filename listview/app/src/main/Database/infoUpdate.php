<?php
$host="localhost";
$db_username="tzching";
$password="00000000";
$db_name="BMI_BMR";

// Done basic connection to MySQL
$con = new mysqli($host,$db_username,$password,$db_name) or die("Unable to connect database");
if(mysqli_connect_error($con)){
    echo "Fail to connect ". mysqli_connect_error();
}

// Here start to choose which operation will be done
// First at all, Get the information from Application
if ($_POST['username'] ==null){
    echo "username nothing! <br>";
}else{
    $search_name =$_POST['username'];

    echo "Success username:$search_name <br>" ;
}

if ($_POST['Height']==NULL){
    echo "Height nothing <br>" ;
}else{
    $Height =$_POST['Height'];

    echo "Success Height:$Height<br>" ;
}
if ($_POST['Weight'] ==null){
    echo "Weight nothing! <br>";
}else{
    $Weight =$_POST['Weight'];

    echo "Success Weight:$Weight <br>" ;
}

if ($_POST['BMI']==NULL){
    echo "BMI nothing <br>" ;
}else{
    $BMI =$_POST['BMI'];

    echo "Success BMI:$BMI<br>" ;
}
if ($_POST['BMR'] ==null){
    echo "BMR nothing! <br>";
}else{
    $BMR =$_POST['BMR'];

    echo "Success BMR:$BMR <br>" ;
}

// First  test if get something or not
echo "username:".$search_name."xxx<br>" ;
echo "Height".$Height."<br>";
echo "Weight".$Weight."<br>";
echo "BMI".$BMI."<br>";
echo "BMR".$BMR."<br>";


// Now just test a Update operation
$sql = "UPDATE test_new_2 SET Height=$Height,Weight=$Weight,BMI=$BMI,Height=$Height WHERE username='$search_name' ";

if ($con->query($sql) === TRUE) {
    echo "Record updated successfully";
} else {
    echo "Error updating record: " . $con->error;
}


?>
