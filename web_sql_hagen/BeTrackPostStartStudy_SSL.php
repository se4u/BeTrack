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
$date = $_POST['date'];
$time = $_POST['time'];
$SessionKey = '';


$age = '';
$relationship = '';
$contraception = '';
$avgperiodlenght = '';
$avgmenstrualcycle = '';
$date = '';
$time = '';

//fetch session key(s) from the database
//$sql = "select * from BetrackSessionKeys where UserId=".$userid;
//for test
$sql = "select * from BetrackSessionKeys where UserId='92228206-9b2a-363f-86ed-8086d6f2746d'";
$result = mysqli_query($con, $sql) or die("Error in Selecting " . mysqli_error($con));

//fetch data and stored into variables
while($row = mysqli_fetch_assoc($result)){
  $BlobSessionkey = $row['BlobSessionkey'];
}

//Decrypt the session key
$data = base64_decode(strtr($BlobSessionkey, '-_', '+/')); 
$rc = openssl_private_decrypt($data, $SessionKey, openssl_pkey_get_private($pri, "cedric"),OPENSSL_PKCS1_PADDING);

if ($rc === false) {
	echo 'decrypt failed';
	//$contraception = 'decrypt failed: ' . strtr($encrypted, '-_', '+/');
}
else {
	echo 'decrypt sessionkey succesful: '.$SessionKey.'/n/r';
	//Decrypt the data
	$data = base64_decode($encrypted); 
	$rc = openssl_private_decrypt($data, $SessionKey, openssl_pkey_get_private($pri, "cedric"),OPENSSL_PKCS1_PADDING);
	list($age, $relationship, $contraception, $avgperiodlenght, $avgmenstrualcycle) = explode(chr (30), $plain);
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
