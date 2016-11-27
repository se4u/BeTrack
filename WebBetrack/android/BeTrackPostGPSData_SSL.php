<?php

include './BeTrackCrypto.php';

if($result === false) {
    goto endsession;
}

$lattitude = '';
$longitude = '';
$date = '';
$time = '';

list($lattitude, $longitude, $date, $time) = explode(chr (30), $plain);

//Check the data
$userid = strip_tags(trim($userid));
$userid = mysqli_real_escape_string($con, $userid);

$lattitude = strip_tags(trim($lattitude));
$lattitude = mysqli_real_escape_string($con, $lattitude);

$longitude = strip_tags(trim($longitude));
$longitude = mysqli_real_escape_string($con, $longitude);

$date = strip_tags(trim($date));
$date = mysqli_real_escape_string($con, $date);

$time = strip_tags(trim($time));
$time = mysqli_real_escape_string($con, $time);

$result = mysqli_query($con,"INSERT INTO BetrackGPS (UserId, Lattitude, Longitude, Date, Time) 
          VALUES ('$userid', '$lattitude', '$longitude', '$date', '$time')");
 
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
