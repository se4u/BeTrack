<?php

$pub = file_get_contents('./public.pem');
$pri = file_get_contents('./private.pem');

include './BeTrackDataBase.php';

if (mysqli_connect_errno($con))
{
   echo '{"query_result":"ERROR"}';
}

$useridcypher = $_POST['ParticipantID'];
$BlobSessionkey = $_POST['Sessionkey'];
$date = $_POST['Date'];
$time = $_POST['Time'];

$data = base64_decode(strtr($useridcypher, '-_', '+/')); 
$privatekey =  openssl_pkey_get_private($pri, "cedric");
$rc = openssl_private_decrypt($data, $userid, $privatekey, OPENSSL_PKCS1_OAEP_PADDING);

if ($rc === false) {
	echo 'decrypt userid RSA failed: '.$useridcypher;
	echo PHP_EOL;
	$result = false;
	goto endsession;
}

$userid = strip_tags(trim($userid));
$userid = mysqli_real_escape_string($con, $userid);


$data = base64_decode(strtr($BlobSessionkey, '-_', '+/')); 
$rc = openssl_private_decrypt($data, $sessionkey, openssl_pkey_get_private($pri, "cedric"),OPENSSL_PKCS1_OAEP_PADDING);

if ($rc === false) {
	echo 'decrypt sessionkey RSA failed '.$BlobSessionkey;
	echo PHP_EOL;
	$result = false;
	goto endsession;
}

$sessionkey = strip_tags(trim($sessionkey));

$sessionkey = mysqli_real_escape_string($con, $sessionkey);

$result = mysqli_query($con,"DELETE FROM BetrackSessionKeys where UserId='".$userid."'");

if ($result === false) {
	goto endsession;
}

$date = strip_tags(trim($date));
$date = mysqli_real_escape_string($con, $date);

$time = strip_tags(trim($time));
$time = mysqli_real_escape_string($con, $time);

$result = mysqli_query($con,"INSERT INTO BetrackSessionKeys (UserId, Sessionkey, Date, Time) 
          VALUES ('$userid', '$sessionkey', '$date', '$time')");

endsession:
		  
if($result === true) {
    echo 'OK';
}
else{
    echo("Error description: " . mysqli_error($con));
	echo PHP_EOL;
	echo 'KO';
}

openssl_free_key($privatekey);
mysqli_close($con);
?>
