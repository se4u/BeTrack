<?php
$con=mysqli_connect("gman.myd.infomaniak.com","gman_unihagen","mghLOzq27HwX","gman_unihagen");
if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}

$userid = $_POST['ParticipantID'];
$application = $_POST['Application'];
$datestart = $_POST['DateStart'];
$datestop = $_POST['DateStop'];
$timestart = $_POST['TimeStart'];
$timestop = $_POST['TimeStop'];
 
$result = mysqli_query($con,"INSERT INTO BetrackStudy (UserId, Application, DateStart, DateStop, TimeStart, TimeStop) 
          VALUES ('$userid ', '$application', '$datestart', '$datestop', '$timestart', '$timestop' )");
 
if($result == true) {
    echo '{"query_result":"SUCCESS"} ';
}
else{
    echo '{"query_result":"FAILURE"}';
    echo("Error description: " . mysqli_error($con));
}
mysqli_close($con);
?>
