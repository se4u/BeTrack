<?php
	include './BeTrackDataBase.php';
	
	$pub = file_get_contents('./public.pem');
	$pri = file_get_contents('./private.pem');


    //fetch table rows from mysql db
    $sql = "select * from BetrackConf";

    $result = mysqli_query($con, $sql) or die("Error in Selecting " . mysqli_error($con));


    $data = array();
	$signature = "";
	$encryptionOk = false;

    $data[] =mysqli_fetch_assoc($result);


    
	//Compute the sha
	$privatekey =  openssl_pkey_get_private($pri, "cedric");

	$encryptionOk = openssl_sign(json_encode($data), $signature, $privatekey, SHA256);
	
	if($encryptionOk === false)
	{
		if ($privatekey === false) {
			echo "No private key !!";
		} else {
			echo "KO";
		}
	}
	else 
	{
		$object = new stdClass();
		$object->Signature = base64_encode($signature);
		$signature_json[] = $object;

		$dataJson = array_merge( $data, $signature_json );
		echo json_encode($dataJson);

	}

	openssl_free_key($privatekey);

    //close the db con
    mysqli_close($con);
?>
