<?php
$con=mysqli_connect("gman.myd.infomaniak.com","gman_unihagen","mghLOzq27HwX","gman_unihagen");
if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}
 
$application = $_GET['application'];
$date = $_GET['date'];
$timestart = $_GET['timestart'];
$timestop = $_GET['timestop'];
 
$result = mysqli_query($con,"INSERT INTO TestTine1 (Application, Date, TimeStart, TimeStop) 
          VALUES ('$application', '$date', '$timestart', '$timestop' )");
 
if($result == true) {
    echo '{"query_result":"SUCCESS"}';
}
else{
    echo '{"query_result":"FAILURE"}';
    echo("Error description: " . mysqli_error($con));
}
mysqli_close($con);
?>