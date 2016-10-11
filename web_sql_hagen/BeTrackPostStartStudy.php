<?php
$con=mysqli_connect("gman.myd.infomaniak.com","gman_unihagen","mghLOzq27HwX","gman_unihagen");
if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}

$userid = $_POST['ParticipantID'];
$age = $_POST['Age'];
$relationship = $_POST['RelationShip'];
$contraception = $_POST['Contraception'];
$avgperiodlenght = $_POST['AvgPeriodLenght'];
$avgmenstrualcycle = $_POST['AvgMenstrualCycle'];
$date = $_POST['Date'];
$time = $_POST['Time'];

$result = mysqli_query($con,"INSERT INTO BetrackStartStudy (UserId, Age, RelationShip, Contraception, AvgPeriodLenght, AvgMenstrualCycle, Date, Time) 
          VALUES ('$userid ', '$age', '$relationship', '$contraception', '$avgperiodlenght', '$avgmenstrualcycle', '$date', '$time')");
 
if($result == true) {
    echo '{"query_result":"SUCCESS"} ';
}
else{
    echo '{"query_result":"FAILURE"}';
    echo("Error description: " . mysqli_error($con));
}
mysqli_close($con);
?>
