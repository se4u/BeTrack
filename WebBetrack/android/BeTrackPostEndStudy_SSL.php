<?php
include './BeTrackCrypto.php';

if($result === false) {
    goto endsession;
}

$relationship = '';
$contraception = '';
$tinder = '';
$phoneusage = '';
$study1 = '';
$study2 = '';
$study3 = '';
$researchapp1 = '';
$researchapp2 = '';
$averageperiodicity = '';
$standarddeviation = '';
$betrackkilled = '';
$date = '';
$time = '';

list($relationship, $contraception, $tinder, $phoneusage, $study1, $study2, $study3, $researchapp1, $researchapp2, $averageperiodicity, $standarddeviation, $betrackkilled, $date, $time) = explode(chr (30), $plain);

//Check the data
$userid = strip_tags(trim($userid));
$userid = mysqli_real_escape_string($con, $userid);

$relationship = strip_tags(trim($relationship));
$relationship = mysqli_real_escape_string($con, $relationship);

$contraception = strip_tags(trim($contraception));
$contraception = mysqli_real_escape_string($con, $contraception);

$tinder = strip_tags(trim($tinder));
$tinder = mysqli_real_escape_string($con, $tinder);

$phoneusage = strip_tags(trim($phoneusage));
$phoneusage = mysqli_real_escape_string($con, $phoneusage);

$study1 = strip_tags(trim($study1));
$study1 = mysqli_real_escape_string($con, $study1);

$study2 = strip_tags(trim($study2));
$study2 = mysqli_real_escape_string($con, $study2);

$study3 = strip_tags(trim($study3));
$study3 = mysqli_real_escape_string($con, $study3);

$researchapp1 = strip_tags(trim($researchapp1));
$researchapp1 = mysqli_real_escape_string($con, $researchapp1);

$researchapp2 = strip_tags(trim($researchapp2));
$researchapp2 = mysqli_real_escape_string($con, $researchapp2);

$averageperiodicity = strip_tags(trim($averageperiodicity));
$averageperiodicity = mysqli_real_escape_string($con, $averageperiodicity);

$standarddeviation = strip_tags(trim($standarddeviation));
$standarddeviation = mysqli_real_escape_string($con, $standarddeviation);

$betrackkilled = strip_tags(trim($betrackkilled));
$betrackkilled = mysqli_real_escape_string($con, $betrackkilled);

$date = strip_tags(trim($date));
$date = mysqli_real_escape_string($con, $date);

$time = strip_tags(trim($time));
$time = mysqli_real_escape_string($con, $time);

$result = mysqli_query($con,"INSERT INTO BetrackEndStudy (UserId, relationship, contraception, tinder, phoneusage, study1, study2, study3, researchapp1, researchapp2, averageperiodicity, standarddeviation, betrackkilled, Date, Time) 
          VALUES ('$userid ', '$relationship', '$contraception', '$tinder', '$phoneusage', '$study1', '$study2', '$study3', '$researchapp1', '$researchapp2', '$averageperiodicity', '$standarddeviation', '$betrackkilled', '$date', '$time')");
 
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