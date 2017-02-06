<?php
     // Get the password
	$pw = $_POST['password'];

	if ($pw  != 'toto') {
	die('wrong password');
	}

	$file="app/AppBetrack-release.apk";
	$fileSize = filesize($file);
    header('Content-Description: File Transfer');
    header('Content-Type: application/vnd.android.package-archive');
	header('Content-Disposition: attachment; filename='.basename($file));
	header('Content-Transfer-Encoding: binary');
	header('Expires: 0');
	header('Cache-Control: must-revalidate, post-check=0, pre-check=0');
	header('Pragma: public');
	header('Content-Length: ' . $fileSize);
	ob_end_flush();
	readfile($file);

?>
