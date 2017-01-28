<?php

include './BeTrackCrypto.php';

if($result === false) {
    goto endsession;
}

$notiftime = '';
$date = '';
$time = '';

list($notiftime, $date, $time) = explode(chr (30), $plain);

//Check the data
$userid = strip_tags(trim($userid));
$userid = mysqli_real_escape_string($con, $userid);

$notiftime = strip_tags(trim($notiftime));
$notiftime = mysqli_real_escape_string($con, $notiftime);

$date = strip_tags(trim($date));
$date = mysqli_real_escape_string($con, $date);

$time = strip_tags(trim($time));
$time = mysqli_real_escape_string($con, $time);

$result = mysqli_query($con,"INSERT INTO BetrackNotificationTime (UserId, NotifTime, Date, Time) 
          VALUES ('$userid', '$notiftime', '$date', '$time')");
 
endsession:		  
if($result === true) {
    echo 'OK';
}
else{
    echo("Error description: " . mysqli_error($con));
	echo PHP_EOL;
	echo 'KO';
}

mysqli_close($con);

?>
