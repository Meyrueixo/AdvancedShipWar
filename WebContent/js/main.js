var idJeu = document.getElementById('idJeu').textContent;
var idJoueur = document.getElementById('idClient').textContent;
var plateauAdversaire = creationPlateau('PlateauAdversaire');
var plateauMaison = creationPlateau('PlateauMaison');
var info = document.getElementById('info');
var infoserv = document.getElementById('infoserv');
var boutonMarquer = document.getElementById('boutonMarquer');
var caseTemp =null;

function creationPlateau(idPlateau){
	/** constructeur de plateau pour simplifier */
	var canvas = document.getElementById(idPlateau);
	var Plateau = {	zone: canvas ,
			elemLeft : canvas.offsetLeft,
			elemTop : canvas.offsetTop,
			taillex : canvas.getAttribute('width')/10,
			tailley : canvas.getAttribute('height')/10,
			context : canvas.getContext('2d'),
			elements : [],
			nomPlateau : idPlateau
	};
	var compt = 1;
	for(i = 0;i <10;i ++){

		for(j = 0;j <10;j ++){

			Plateau.elements.push({
				colour: '#05EFFF',
				width: Plateau.taillex,
				height: Plateau.tailley,
				top: (Plateau.taillex*i),
				left: (Plateau.tailley*j),
				name : compt,
				type : 'vide',
				etat : 'inactif'
			});
			compt++;
		}
	}// Add element.
	
	return Plateau 
}


//Add event listener for `click` events.
plateauAdversaire.zone.addEventListener('click', function(event){eventSurPlateau(event,plateauAdversaire);}, false);

plateauMaison.zone.addEventListener('click', function(event){eventSurPlateau(event,plateauMaison);}, false);


function eventSurPlateau(event,Plateau){
	var x = (event.layerX ),
	y = (event.layerY);
	infoserv.textContent = (" x : " + x + " y : "+y);
	// Collision detection between clicked offset and element.
	Plateau.elements.forEach(function(element) {
		if (y > element.top && y < element.top + element.height
				&& x > element.left && x < element.left + element.width) {
			selectCase(element);
			//Plateau.caseActu = element;
			info.textContent = ("Plateau selectionne : "+Plateau.nomPlateau+" Element selectioner : " + element.name +" eventy :"+ event.pageY+" eventx : " + x +
					" x :"+x + " y :"+ y +" Plateau elemLeft :"+Plateau.elemLeft +" Plateau elemTop :"+Plateau.elemTop);
		}
	});

}
function getBlaze(){
	//TODO blaze
}
function selectCase(element){	
	if (caseTemp !=null){
			caseTemp.etat = 'inactif';
			element.etat = 'actif';
			caseTemp = element;
			render(plateauMaison);
			render(plateauAdversaire);
		
	}
	else{
		element.etat = 'actif';
		caseTemp = element;
		render(plateauMaison);
		render(plateauAdversaire);
	}
	// etat actif + rendu du plateau actu
	// etat inactif ancienne case / rendu autre plateau
}


render(plateauAdversaire);
render(plateauMaison);

function poserMine(){
	if(caseTemp.type === 'mine')
	{
		alert("Une mine est déjà posée ici");
	}else{
		caseTemp.type = 'mine'
	}
	render();
}

function marquerCase(Plateau){
	if(Plateau.caseActu.etat === 'marque')
	{
		Plateau.caseActu.etat = 'inactif';
	}else{
		if (Plateau.caseActu.type != 'mine')
			Plateau.caseActu.etat = 'marque';
	}

	
	render(Plateau)
}

function renderBis(element, Plateau){
	if(element.type == 'mine')
	{
		Plateau.context.fillStyle = '#0500FF';
		Plateau.context.fillRect(element.left, element.top, element.width, element.height);
	}else{
		if (element.etat == 'actif'){
				Plateau.context.fillStyle = '#008000';
				Plateau.context.fillRect(element.left, element.top, element.width, element.height);
			}
		else{
			if (element.etat === 'marque'){
				Plateau.context.fillStyle = '#FF0000';
				Plateau.context.fillRect(element.left, element.top, element.width, element.height);
			}
			else {
				Plateau.context.fillStyle = '#05EFFF';
				Plateau.context.fillRect(element.left, element.top, element.width, element.height);
			}

		}

	}

	Plateau.context.strokeStyle = '#000000';

	Plateau.context.strokeRect(element.left, element.top, element.width, element.height);
	Plateau.context.fillStyle =  '#000000';
	Plateau.context.fillText(element.name,(element.left+(element.width/2)) ,(element.top +(element.height/2)));
}

function render(Plateau){
	// Render elements.
	Plateau.elements.forEach(function(element){	
		renderBis(element,Plateau);	
	});
}
function MessageChat(){
 var text =	document.getElementById("msgChat").value;
 var chat = {message: text,destinataire:"tous"};
	sendJson("chat", chat);
 
}
/*--------------------web sockect -----------*/
var ws = new WebSocket("ws://localhost:8080/AdvancedShipWar/AdvancedShipWarGame");
ws.onopen = function(){
	connexionPartie();
};
function connexionPartie(){
	var connect = {idgame: idJeu,TokenPlayer:idJoueur };
	sendJson("connect", connect);
	
}
/*reception message*/
ws.onmessage = function(message){
	try{
		var objectJson = JSON.parse(message.data);
		if(objectJson.hasOwnProperty("chat")){
			document.getElementById("chatlog").textContent += objectJson.chat + "\n";
		}
	}catch(exection){
		document.getElementById("consolLog").textContent += message.data + "\n";
	}
	
};
/*creation du json*/
function sendJson(typeObjet, objet){
	var messformat = ("{ \""+typeObjet+"\":"+JSON.stringify(objet)+"}");
	sendMessage(messformat);
}

/*fonction envoi de message*/
function sendMessage(message){
	var messageForm = {}
	ws.send(message);
}

//propre a la console de debug
function postToServer(){
	sendMessage(document.getElementById("consolMsg").value);
	document.getElementById("msg").value = "";
}
function closeConnect(){
	ws.close();
}
