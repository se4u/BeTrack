<?php
include './BeTrackDataBase.php';

$pub = file_get_contents('./public.pem');
$pri = file_get_contents('./private.pem');

if (mysqli_connect_errno($con))
{
   $result = false;
   goto endsession;
}

$useridcypher = $_POST['ParticipantID'];
$encrypted = $_POST['encrypted'];

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
	if (!hash_equals($expected, $hashresult)) {
		echo 'Validity check of data failed SHA from data: '.$expected.' Computed: '.$hashresult;
		echo PHP_EOL;
		$result = false;
		goto endsession;
	}
}

endsession:	
?>
