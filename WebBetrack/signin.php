<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<title>BeTrack sign in</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<!-- Bootstrap -->
		<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
		<link href="css/signin.css" rel="stylesheet" media="screen">
		<link href="css/ie10-viewport-bug-workaround.css" rel="stylesheet" media="screen">
	</head>

	<body>

	<div class="container">
		<form class="form-signin">

		<div class="text-xs-center">
			<h2 class="form-signin-heading">BeTrack</h2>
			<img class="img-responsive" src="img/ic_launcher-web.png" height="42" width="42"/>
		</div>

		<label for="inputEmail" class="sr-only">Username</label>
		<input name="myusername" id="myusername" type="text" class="form-control" placeholder="Username" required autofocus>
		<label for="inputPassword" class="sr-only">Password</label>
		<input name="mypassword" id="mypassword" type="password" class="form-control" placeholder="Password" required>

		<button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
		<div id="message"></div>
	</form>

	</div> <!-- /container -->


	<script src="js/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/ie10-viewport-bug-workaround.js"></script>
	<script src="js/login.js"></script>
	</body>
</html>
