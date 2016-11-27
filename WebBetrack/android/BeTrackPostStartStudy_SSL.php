<?php

include './BeTrackCrypto.php';

if($result === false) {
    goto endsession;
}

$age = '';
$relationship = '';
$contraception = '';
$avgperiodlenght = '';
$avgmenstrualcycle = '';
$sociallife1 = '';
$sociallife2 = '';
$mood1 = '';
$mood2 = '';
$mood3 = '';
$mood4 = '';
$date = '';
$time = '';


list($age, $relationship, $contraception, $avgperiodlenght, $avgmenstrualcycle, $sociallife1, $sociallife2, $mood1, $mood2, $mood3, $mood4, $date, $time) = explode(chr (30), $plain);

//Check the data
$userid = strip_tags(trim($userid));
$userid = mysqli_real_escape_string($con, $userid);

$age = strip_tags(trim($age));
$age = mysqli_real_escape_string($con, $age);

$relationship = strip_tags(trim($relationship));
$relationship = mysqli_real_escape_string($con, $relationship);

$contraception = strip_tags(trim($contraception));
$contraception = mysqli_real_escape_string($con, $contraception);

$avgperiodlenght = strip_tags(trim($avgperiodlenght));
$avgperiodlenght = mysqli_real_escape_string($con, $avgperiodlenght);

$avgmenstrualcycle = strip_tags(trim($avgmenstrualcycle));
$avgmenstrualcycle = mysqli_real_escape_string($con, $avgmenstrualcycle);

$sociallife1 = strip_tags(trim($sociallife1));
$sociallife1 = mysqli_real_escape_string($con, $sociallife1);

$sociallife2 = strip_tags(trim($sociallife2));
$sociallife2 = mysqli_real_escape_string($con, $sociallife2);

$mood1 = strip_tags(trim($mood1));
$mood1 = mysqli_real_escape_string($con, $mood1);

$mood2 = strip_tags(trim($mood2));
$mood2 = mysqli_real_escape_string($con, $mood2);

$mood3 = strip_tags(trim($mood3));
$mood3 = mysqli_real_escape_string($con, $mood3);

$mood4 = strip_tags(trim($mood4));
$mood4 = mysqli_real_escape_string($con, $mood4);

$date = strip_tags(trim($date));
$date = mysqli_real_escape_string($con, $date);

$time = strip_tags(trim($time));
$time = mysqli_real_escape_string($con, $time);

$result = mysqli_query($con,"INSERT INTO BetrackStartStudy (UserId, Age, RelationShip, Contraception, AvgPeriodLenght, AvgMenstrualCycle, SocialLife1, SocialLife2, Mood1, Mood2, Mood3, Mood4, Date, Time) 
          VALUES ('$userid ', '$age', '$relationship', '$contraception', '$avgperiodlenght', '$avgmenstrualcycle', '$sociallife1', '$sociallife2', '$mood1', '$mood2', '$mood3', '$mood4', '$date', '$time')");

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
