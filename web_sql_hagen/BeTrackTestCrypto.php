<?php

$pub = file_get_contents('./public.pem');
$pri = file_get_contents('./private.pem');

echo "<br><br>";
var_dump($pub);
echo "<br><br>";
var_dump($pri);

$plain = 'Helloworld';
echo "<br><br>Plaintext: $plain";

openssl_public_encrypt($plain, $encrypted, $pub, OPENSSL_PKCS1_PADDING);
$TextEncrypted = 'awB89T+ESMS7SPSJN8+aBFyPUwoYg75GXeWKeEFPQ0IZul1cIOmmo94sNpg75lOU/Gx9xsIOpy/Xg7g+tmg0e/9heYe536kbBsI+KROtkKm9Goan4LHu8XK30cPFGa68kIXYAD6fQX2ctlSQdNRLKqJ7poIjzutyZ8tqwXnmh/XMANYncN94IE/YEmCJoSc62OSAR97/MM0z8lI5JNybssTsSlaJ79asnbQGXWZ3C6djg82mH+k7lNQ4s1goftmx0XJrKO5JnKItFSwisc35QVAZQRBLxDJ1zyHNOtSy3WdVuY7dR9NycOmwEl8cngr0SbxNCfNGg94pEfbAyN79mQ==';
echo "<br><br>Encrypted: " . $TextEncrypted;
echo "<br><br>PHP Encrypted: " . base64_encode($encrypted);
echo "<br><br>PHP Encrypted: " . $encrypted;

$TextEncrypted = base64_encode($encrypted);
$decrypt = base64_decode($TextEncrypted);
$plain = '';
openssl_private_decrypt($decrypt, $plain, openssl_pkey_get_private($pri, "cedric"),OPENSSL_PKCS1_PADDING);

echo "<br><br>Decrypted: " . $plain;

?>