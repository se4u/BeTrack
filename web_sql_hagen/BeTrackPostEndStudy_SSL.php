<?php
include './BeTrackCrypto.php';

if($result === false) {
    goto endsession;
}

$periodstatus = '';
$sociallife1 = '';
$sociallife2 = '';
$mood = '';
$relationship = '';
$contraception = '';
$phoneusage = '';
$date = '';
$time = '';

list($periodstatus, $sociallife1, $sociallife2, $mood, $relationship, $contraception, $phoneusage, $date, $time) = explode(chr (30), $plain);

//Check the data
$userid = strip_tags(trim($userid));
$userid = mysqli_real_escape_string($con, $userid);

$periodstatus = strip_tags(trim($periodstatus));
$periodstatus = mysqli_real_escape_string($con, $periodstatus);

$sociallife1 = strip_tags(trim($sociallife1));
$sociallife1 = mysqli_real_escape_string($con, $sociallife1);

$sociallife2 = strip_tags(trim($sociallife2));
$sociallife2 = mysqli_real_escape_string($con, $sociallife2);

$mood = strip_tags(trim($mood));
$mood = mysqli_real_escape_string($con, $mood);

$relationship = strip_tags(trim($relationship));
$relationship = mysqli_real_escape_string($con, $relationship);

$contraception = strip_tags(trim($contraception));
$contraception = mysqli_real_escape_string($con, $contraception);

$phoneusage = strip_tags(trim($phoneusage));
$phoneusage = mysqli_real_escape_string($con, $phoneusage);

$date = strip_tags(trim($date));
$date = mysqli_real_escape_string($con, $date);

$time = strip_tags(trim($time));
$time = mysqli_real_escape_string($con, $time);

$result = mysqli_query($con,"INSERT INTO BetrackEndStudy (UserId, Period, SocialLife1, SocialLife2, Mood, RelationShip, Contraception, PhoneUsage, Date, Time) 
          VALUES ('$userid ', '$periodstatus', '$sociallife1', '$sociallife2', '$mood', '$relationship', '$contraception', '$phoneusage', '$date', '$time')");
 
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