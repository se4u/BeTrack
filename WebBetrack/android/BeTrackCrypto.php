<?php
include './BeTrackDataBase.php';

$pub = file_get_contents('./public.pem');
$pri = file_get_contents('./private.pem');

if(!function_exists('hash_equals'))
{
    function hash_equals($str1, $str2)
    {
        if(strlen($str1) != strlen($str2))
        {
            return false;
        }
        else
        {
            $res = $str1 ^ $str2;
            $ret = 0;
            for($i = strlen($res) - 1; $i >= 0; $i--)
            {
                $ret |= ord($res[$i]);
            }
            return !$ret;
        }
    }
}	

$result = false;

if (mysqli_connect_errno($con))
{
   echo 'Error connecting to the database';
   echo PHP_EOL;
   $result = false;
   goto endsession;
}

$useridcypher = $_POST['ParticipantID'];
$encrypted = $_POST['encrypted'];

$data = base64_decode(strtr($useridcypher, '-_', '+/')); 
$privatekey =  openssl_pkey_get_private($pri, "cedric");
$rc = openssl_private_decrypt($data, $userid, $privatekey, OPENSSL_PKCS1_OAEP_PADDING);

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
	echo 'Error querrying the database';
	echo PHP_EOL;
	$result = false;
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
	else
	{
		$result = true;
	}
}

endsession:	
?>
