<?php
	include './BeTrackDataBase.php';
    $table_name = $_GET['table_name'];
	
	$pub = file_get_contents('./public.pem');
	$pri = file_get_contents('./private.pem');

    //fetch table rows from mysql db
    $sql = "select * from ".$table_name;
    $result = mysqli_query($con, $sql) or die("Error in Selecting " . mysqli_error($con));

    $data = array();
	$row = array();
	$signature = "";
	$encryptionOk = false;


    while($row[] =mysqli_fetch_assoc($result))
    {
		$data += $row;
    }
	
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

    //close the db connection
    mysqli_close($con);
?>
