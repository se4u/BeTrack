<?php

include './BeTrackCrypto.php';

if($result === false) {
    echo 'Decryption failed!';
    echo PHP_EOL;
    goto endsession;
}


$age = '';
$ethnicity1 = '';
$ethnicity2 = '';
$student = '';
$englishlevel1 = '';
$englishlevel2 = '';
$englishlevel3 = '';
$englishlevel4 = '';
$englishlevel5 = '';
$englishlevel6 = '';
$university1 = '';
$university2 = '';
$university3 = '';
$relationship = '';
$contraception = '';
$maternity = '';
$period1 = '';
$period2 = '';
$period3 = '';
$period4 = '';
$Date = '';
$Time = '';

list($age, $ethnicity1, $ethnicity2, 
	 $student, $englishlevel1, $englishlevel2,
	 $englishlevel3, $englishlevel4, $englishlevel5,
	 $englishlevel6, $university1, $university2,
	 $university3, $relationship, $contraception,
	 $maternity, $period1, $period2,
	 $period3, $period4, $date, $time) = explode(chr (30), $plain);

//Check the data
$userid = strip_tags(trim($userid));
$userid = mysqli_real_escape_string($con, $userid);

$age = strip_tags(trim($age));
$age = mysqli_real_escape_string($con, $age);

$ethnicity1 = strip_tags(trim($ethnicity1));
$ethnicity1 = mysqli_real_escape_string($con, $ethnicity1);

$ethnicity2 = strip_tags(trim($ethnicity2));
$ethnicity2 = mysqli_real_escape_string($con, $ethnicity2);

$student = strip_tags(trim($student));
$student = mysqli_real_escape_string($con, $student);

$englishlevel1 = strip_tags(trim($englishlevel1));
$englishlevel1 = mysqli_real_escape_string($con, $englishlevel1);

$englishlevel2 = strip_tags(trim($englishlevel2));
$englishlevel2 = mysqli_real_escape_string($con, $englishlevel2);

$englishlevel3 = strip_tags(trim($englishlevel3));
$englishlevel3 = mysqli_real_escape_string($con, $englishlevel3);

$englishlevel4 = strip_tags(trim($englishlevel4));
$englishlevel4 = mysqli_real_escape_string($con, $englishlevel4);

$englishlevel5 = strip_tags(trim($englishlevel5));
$englishlevel5 = mysqli_real_escape_string($con, $englishlevel5);

$englishlevel6 = strip_tags(trim($englishlevel6));
$englishlevel6 = mysqli_real_escape_string($con, $englishlevel6);

$university1 = strip_tags(trim($university1));
$university1 = mysqli_real_escape_string($con, $university1);

$university2 = strip_tags(trim($university2));
$university2 = mysqli_real_escape_string($con, $university2);

$university3 = strip_tags(trim($university3));
$university3 = mysqli_real_escape_string($con, $university3);

$relationship = strip_tags(trim($relationship));
$relationship = mysqli_real_escape_string($con, $relationship);

$contraception = strip_tags(trim($contraception));
$contraception = mysqli_real_escape_string($con, $contraception);

$maternity = strip_tags(trim($maternity));
$maternity = mysqli_real_escape_string($con, $maternity);

$period1 = strip_tags(trim($period1));
$period1 = mysqli_real_escape_string($con, $period1);

$period2 = strip_tags(trim($period2));
$period2 = mysqli_real_escape_string($con, $period2);

$period3 = strip_tags(trim($period3));
$period3 = mysqli_real_escape_string($con, $period3);

$period4 = strip_tags(trim($period4));
$period4 = mysqli_real_escape_string($con, $period4);

$Date = strip_tags(trim($Date));
$Date = mysqli_real_escape_string($con, $Date);

$Time = strip_tags(trim($Time));
$Time = mysqli_real_escape_string($con, $Time);

$result = mysqli_query($con,"INSERT INTO BetrackStartStudy (UserId, age, ethnicity1, ethnicity2, student, englishlevel1, englishlevel2, englishlevel3, englishlevel4, englishlevel5, englishlevel6, university1, university2, university3, relationship, contraception, maternity, period1, period2, period3, period4, Date, Time) 
          VALUES ('$userid ', '$age', '$ethnicity1', '$ethnicity2', '$student', '$englishlevel1', '$englishlevel2', '$englishlevel3', '$englishlevel4', '$englishlevel5', '$englishlevel6', '$university1', '$university2', '$university3', '$relationship', '$contraception', '$maternity', '$period1', '$period2', '$period3', '$period4','$date', '$time')");

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
