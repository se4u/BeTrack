<?php

$pub = file_get_contents('./public.pem');
$pri = file_get_contents('./private.pem');


$con=mysqli_connect("gman.myd.infomaniak.com","gman_unihagen","mghLOzq27HwX","gman_unihagen");
if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}

$useridcypher = $_POST['ParticipantID'];
$encrypted = $_POST['encrypted'];

$age = '';
$relationship = '';
$contraception = '';
$avgperiodlenght = '';
$avgmenstrualcycle = '';
$date = '';
$time = '';

$data = base64_decode(strtr($useridcypher, '-_', '+/')); 
$rc = openssl_private_decrypt($data, $userid, openssl_pkey_get_private($pri, "cedric"),OPENSSL_PKCS1_OAEP_PADDING);

if ($rc === false) {
	echo 'decrypt userid RSA failed: '.$useridcypher;
	echo PHP_EOL;
	$result = false;
	goto endsession;
}

//fetch session key(s) from the database
$sql = "select * from BetrackSessionKeys where UserId='".$userid."'";

$query = mysqli_query($con, $sql) or die("Error in Selecting " . mysqli_error($con));
if ($query === false) {
	goto endsession;
}

//Read the session key
$row = mysqli_fetch_assoc($query);
$partsSessionKey = explode(':', $row["Sessionkey"]);
$query->close();

$SecretKey = base64_decode($partsSessionKey[0]); 
$IntegrityKey = base64_decode($partsSessionKey[1]); 

//Decrypt the data
$parts = explode(':', strtr($encrypted, '-_', '+/'));
$data = base64_decode($parts[2]); 
$expected = trim(preg_replace('/\\\\r|\\\\n|\\\\t/i', ' ', $parts[1])); 
$iv = base64_decode($parts[0]); 

$plain = openssl_decrypt($data, 'AES-128-CBC', $SecretKey, OPENSSL_RAW_DATA, $iv);

if ($plain === false) {
	echo 'decrypt AES failed: '.$parts[2].' Secret key: '.$partsSessionKey[0];
	echo PHP_EOL;
	$result = false;
	goto endsession;
}
else {	
	//Check the hash key
	$ivCipherConcat =  base64_decode($parts[0]).base64_decode($parts[2]);
	$hashresult = base64_encode(hash_hmac('sha256', $ivCipherConcat, $IntegrityKey, true));
	if (hash_equals($expected, $hashresult)) {
		list($age, $relationship, $contraception, $avgperiodlenght, $avgmenstrualcycle, $date, $time) = explode(chr (30), $plain);
	} 
	else {
		echo 'Validity check of data failed SHA from data: '.$expected.' Computed: '.$hashresult;
		echo PHP_EOL;
		$result = false;
		goto endsession;
	}
}

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
