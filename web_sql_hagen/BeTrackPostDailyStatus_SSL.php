<?php

include './BeTrackCrypto.php';

if($result === false) {
    goto endsession;
}

$periodstatus = '';
$sociallife1 = '';
$sociallife2 = '';
$phoneusage = '';
$mood = '';
$date = '';
$time = '';

list($periodstatus, $sociallife1, $sociallife2, $phoneusage, $mood, $date, $time) = explode(chr (30), $plain);

//Check the data
$userid = strip_tags(trim($userid));
$userid = mysqli_real_escape_string($con, $userid);

$periodstatus = strip_tags(trim($periodstatus));
$periodstatus = mysqli_real_escape_string($con, $periodstatus);

$sociallife1 = strip_tags(trim($sociallife1));
$sociallife1 = mysqli_real_escape_string($con, $sociallife1);

$sociallife2 = strip_tags(trim($sociallife2));
$sociallife2 = mysqli_real_escape_string($con, $sociallife2);

$phoneusage = strip_tags(trim($phoneusage));
$phoneusage = mysqli_real_escape_string($con, $phoneusage);

$mood = strip_tags(trim($mood));
$mood = mysqli_real_escape_string($con, $mood);

$date = strip_tags(trim($date));
$date = mysqli_real_escape_string($con, $date);

$time = strip_tags(trim($time));
$time = mysqli_real_escape_string($con, $time);

$result = mysqli_query($con,"INSERT INTO BetrackDailyStatus (UserId, PeriodStatus, SocialLife1, SocialLife2, PhoneUsage, Mood, Date, Time) 
          VALUES ('$userid ', '$periodstatus', '$sociallife1', '$sociallife2', '$phoneusage', '$mood', '$date', '$time')");
 
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
