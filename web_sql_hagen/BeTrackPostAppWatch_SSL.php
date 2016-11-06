<?php
include './BeTrackCrypto.php';

if($result === false) {
    goto endsession;
}

$application = '';
$datestart = '';
$datestop = '';
$timestart = '';
$timestop = '';

list($application, $datestart, $datestop, $timestart, $timestop) = explode(chr (30), $plain);

//Check the data
$userid = strip_tags(trim($userid));
$userid = mysqli_real_escape_string($con, $userid);

$application = strip_tags(trim($application));
$application = mysqli_real_escape_string($con, $application);

$datestart = strip_tags(trim($datestart));
$datestart = mysqli_real_escape_string($con, $datestart);

$datestop = strip_tags(trim($datestop));
$datestop = mysqli_real_escape_string($con, $datestop);

$timestart = strip_tags(trim($timestart));
$timestart = mysqli_real_escape_string($con, $timestart);

$timestop = strip_tags(trim($timestop));
$timestop = mysqli_real_escape_string($con, $timestop);
 
$result = mysqli_query($con,"INSERT INTO BetrackStudy (UserId, Application, DateStart, DateStop, TimeStart, TimeStop) 
          VALUES ('$userid ', '$application', '$datestart', '$datestop', '$timestart', '$timestop' )");
 
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
