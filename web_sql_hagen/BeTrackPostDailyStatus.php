<?php
$con=mysqli_connect("gman.myd.infomaniak.com","gman_unihagen","mghLOzq27HwX","gman_unihagen");
if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}

$userid = $_POST['ParticipantID'];
$periodstatus = $_POST['Period'];
$sociallife1 = $_POST['SocialLife1'];
$sociallife2 = $_POST['SocialLife2'];
$mood = $_POST['Mood'];
$date = $_POST['Date'];

$result = mysqli_query($con,"INSERT INTO BetrackDailyStatus (UserId, PeriodStatus, SocialLife1, SocialLife2, Mood, Date) 
          VALUES ('$userid ', '$periodstatus', '$sociallife1', '$sociallife2', '$mood', '$date')");
 
if($result == true) {
    echo '{"query_result":"SUCCESS"} ';
}
else{
    echo '{"query_result":"FAILURE"}';
    echo("Error description: " . mysqli_error($con));
}
mysqli_close($con);
?>
