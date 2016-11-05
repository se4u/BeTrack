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
$date = '';
$time = '';


list($age, $relationship, $contraception, $avgperiodlenght, $avgmenstrualcycle, $date, $time) = explode(chr (30), $plain);

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

$date = strip_tags(trim($date));
$date = mysqli_real_escape_string($con, $date);

$time = strip_tags(trim($time));
$time = mysqli_real_escape_string($con, $time);

$result = mysqli_query($con,"INSERT INTO BetrackStartStudy (UserId, Age, RelationShip, Contraception, AvgPeriodLenght, AvgMenstrualCycle, Date, Time) 
          VALUES ('$userid ', '$age', '$relationship', '$contraception', '$avgperiodlenght', '$avgmenstrualcycle', '$date', '$time')");

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
