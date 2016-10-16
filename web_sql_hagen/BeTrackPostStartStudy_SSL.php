<?php

$pub = file_get_contents('./public.pem');
$pri = file_get_contents('./private.pem');


$con=mysqli_connect("gman.myd.infomaniak.com","gman_unihagen","mghLOzq27HwX","gman_unihagen");
if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}

$userid = $_POST['ParticipantID'];
$encrypted = $_POST['encrypted'];
$plain = '';


$age = '';
$relationship = '';
$contraception = '';
$avgperiodlenght = '';
$avgmenstrualcycle = '';
$date = '';
$time = '';
$data = base64_decode(strtr($encrypted, '-_', '+/')); 
$rc = openssl_private_decrypt($data, $plain, openssl_pkey_get_private($pri, "cedric"),OPENSSL_PKCS1_PADDING);

if ($rc === false) {
	$contraception = 'decrypt failed: ' . strtr($encrypted, '-_', '+/');
}
else {
	list($age, $relationship, $contraception, $avgperiodlenght, $avgmenstrualcycle, $date, $time) = explode(chr (30), $plain);
}

$result = mysqli_query($con,"INSERT INTO BetrackStartStudy (UserId, Age, RelationShip, Contraception, AvgPeriodLenght, AvgMenstrualCycle, Date, Time) 
          VALUES ('$userid ', '$age', '$relationship', '$contraception', '$avgperiodlenght', '$avgmenstrualcycle', '$date', '$time')");
 
if($result == true) {
    echo '{"query_result":"SUCCESS"} ';
}
else{
    echo '{"query_result":"FAILURE"}';
    echo("Error description: " . mysqli_error($con));
}
mysqli_close($con);
?>
