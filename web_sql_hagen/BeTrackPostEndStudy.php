<?php
$con=mysqli_connect("gman.myd.infomaniak.com","gman_unihagen","mghLOzq27HwX","gman_unihagen");
if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}

$userid = $_POST['ParticipantID'];
$relationship = $_POST['RelationShip'];
$contraception = $_POST['Contraception'];
$date = $_POST['Date'];

$result = mysqli_query($con,"INSERT INTO BetrackEndStudy (UserId, RelationShip, Contraception, Date) 
          VALUES ('$userid ', '$relationship', '$contraception', '$date')");
 
if($result == true) {
    echo '{"query_result":"SUCCESS"} ';
}
else{
    echo '{"query_result":"FAILURE"}';
    echo("Error description: " . mysqli_error($con));
}
mysqli_close($con);
?>