<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
	<head>
	    <meta charset="utf-8" />
		<link rel="stylesheet" href="css/bootstrap.min.css" />
	    <link rel="stylesheet" href="css/StyleSheet.css" />
		<meta name="viewport" content="width=device-width, initial-scale=1"/>
	    <title>Le jeu</title>
	</head>
	<body>
		<header> 
			<h1>Advanced Ship War</h1>
		</header>
		<section>
			<div id="infoPlateau">
				<p>Identifient du jeu</p>
				<p id="idJeu">${IdJeu}</p>
					<p>Identifient du Client :</p>
				<p id="idClient">${IdSession}</p>
			</div>
			<div id="debug">
				<textarea id="consolLog" readonly
					style="margin: 0px; height: 110px; width: 477px;"></textarea>
				<br />
				<textarea id="consolMsg" type="text" width="500" height="50" /></textarea>
				<button type="submit" id="sendButton" onClick="postToServer()">Send
					Json!</button>
				<button type="submit" id="sendButton" onClick="closeConnect()">End
					connection</button>
					
				<p id="info">h</p>
				<p id="infoserv">h</p>
			</div>
		
			<div class="container-fluid">
				<div class="row grid">
					<div class="col-md-6">
						<canvas id="PlateauAdversaire" width="500" height="500"
							style=" border: 1px solid #ccc;" />
					</div>
					<div class="col-md-6">
						<canvas id="PlateauMaison" width="500" height="500"
							style=" border: 1px solid #ccc;" />
					</div>
				</div>
				<div class="row grid">
					<div class="col-xs-8">
						<button id="boutonPoserMine">Poser une mine</button>
						<button id="boutonMarquer">Marquer case</button>
						<button id="boutonTirerMissile">Tirer un missile</button>
						<button id="truc">Truc</button>
						<button id="truc">Truc</button>
						<button id="truc">Truc</button>
						<button id="truc">Truc</button>
					</div>
				</div>
				<div class="row grid">
					<div class="col-xs-8">
						<textarea id="chatlog" readonly class="form-control grid" rows="3"></textarea>	
					</div>
				</div>
				<div class="row grid">
					<div class="col-xs-7">
						<input id="msgChat" type="text" class="form-control" placeholder="Text input"  />
					</div>
					<div class="col-xs-3">
						<button type="button" id="sendButtonChat" class="btn btn-default" onClick="MessageChat()">Envoi</button>
					</div>
				</div>
			</div>
	
		</section>
	</body>
	<script src="js/jquery.min.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js" type="text/javascript"></script>
	<script src="js/main.js" type="text/javascript"></script>
</html>
