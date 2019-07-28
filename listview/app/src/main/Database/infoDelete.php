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

// First  test if get something or not
echo "username:".$search_name."xxx<br>" ;



// Now just test Delete Action operation
$sql = "Delete from test_new_2 where username='$search_name'";

if ($con->query($sql) === TRUE) {
    echo "Record delete successfully";
} else {
    echo "Error delete record: " . $con->error;
}


?>
