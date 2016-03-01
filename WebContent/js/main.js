var idJeu = document.getElementById('idJeu').textContent;
var idJoueur = document.getElementById('idClient').textContent;
var plateauAdversaire = creationPlateau('PlateauAdversaire');
var plateauMaison = creationPlateau('PlateauMaison');
var info = document.getElementById('info');
var infoserv = document.getElementById('infoserv');
var boutonMarquer = document.getElementById('boutonMarquer');
var boutonPoserMine = document.getElementById('boutonPoserMine');
var boutonTirerMissile = document.getElementById('boutonTirerMissile');
var boutonDirection = document.getElementById('direction');
var etatJeu = document.getElementById('etatJeu');
var boutonPoseBateau = document.getElementById('PoseBateau');
var poinAction = document.getElementById('poinAction');

//selecteur de bateau
/*var estPorteAvion = document.getElementById('estPorteAvion');
var estCroiseur =  document.getElementById('estCroiseur');
var estSousMarin = document.getElementById('estSous-marin');
var estTorpilleur = document.getElementById('estTorpilleur');*/



//Add event listener for `click` events.
plateauAdversaire.zone.addEventListener('click', function(event){eventSurPlateau(event,plateauAdversaire);}, false);
plateauMaison.zone.addEventListener('click', function(event){eventSurPlateau(event,plateauMaison);}, false);
boutonMarquer.addEventListener('click',marquerCase,false);
boutonPoserMine.addEventListener('click',poserMine,false);
boutonTirerMissile.addEventListener('click',tirerMissile,false);
boutonDirection.addEventListener('click',changeDirection,false);
//boutonPoseBateau.addEventListener('click',"poserBateau(this.form.typeBateau)",false);



var intervalID;

var mine =  new Image();
mine.src ='assets/mine.png';
var caseTemp =null;



function preparation(){
	intervalID = setInterval(Timer,999);
	var dureeCoup= 120;


}

function stopTimer() {
	clearInterval(intervalID);
	etatJeu.innerHTML="terminé";
	//finDeTour();
}
function Timer()
{
	var s=dureeCoup;
	var m=0;
	var h=0;
	if(s<0){

		stopTimer();	
	}
	else{
		if(s>59){
			m=Math.floor(s/60);
			s=s-m*60
		}
		if(m>59){
			h=Math.floor(m/60);
			m=m-h*60
		}
		if(s<10){
			s="0"+s
		}
		if(m<10){
			m="0"+m
		}
		etatJeu.innerHTML="Temps restent : "+m+":"+s;
	}
	dureeCoup=dureeCoup-1;


}
function finDeTour(){
	boutonPoserMine.disabled = true;
	boutonTirerMissile.disabled =true;
}
function debutDeTour(){
	boutonPoserMine.disabled = false;
	boutonTirerMissile.disabled = false;
}

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
	var compt = 0;
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
				option :'vide',
				etat : 'inactif',
				select : 'non',
				orientation :'vertical',
				touche : 0
			});
			compt++;
		}
	}// Add element.

	return Plateau 
}



function changeDirection(){
	if(boutonDirection.textContent === 'N'){
		boutonDirection.textContent = 'E'
	}
	else if(boutonDirection.textContent == 'E'){
		boutonDirection.textContent = 'S'	
	}
	else if(boutonDirection.textContent == 'S'){
		boutonDirection.textContent = 'O'
	}
	else if(boutonDirection.textContent =='O'){
		boutonDirection.textContent = 'N'
	}

}

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
		caseTemp.select = 'non';
		element.select = 'oui';
		caseTemp = element;
		render(plateauMaison);
		render(plateauAdversaire);

	}
	else{
		element.select = 'oui';
		caseTemp = element;
		render(plateauMaison);
		render(plateauAdversaire);
	}
	// etat actif + rendu du plateau actu
	// etat inactif ancienne case / rendu autre plateau
}


render(plateauAdversaire);
render(plateauMaison);

function poserBateau(radio){
	if(caseTemp.type === 'bateau')
	{
		alert("Un bateau est déjà posée ici");
	}else{
		var type;
		for (var i=0; i<radio.length;i++) {
			if (radio[i].checked) {
				type = radio[i].value;
			}
		}
		var bateau = {pos : caseTemp.name,
				type:type,
				orientation:boutonDirection.textContent
		};
		caseTemp.select = 'non';
		sendJson("bateauPlacement",  bateau);
	}
}

function poserMine(event){
	if(caseTemp.type === 'mine')
	{
		alert("Une mine est déjà posée ici");
	}else{
		var caseMine = {pos: caseTemp.name};
		caseTemp.select = 'non';
		sendJson("mine", caseMine);
	}
	//render(plateauAdversaire);
}

function tirerMissile(event){
	if (caseTemp != null){
		var idCase = { pos : caseTemp.name};
		caseTemp.select = 'non';
		sendJson("tir",idCase);
	}
}

function marquerCase(event){
	if (caseTemp !=null){
		if (caseTemp.etat === 'marque'){
			caseTemp.etat = 'inactif';
			caseTemp.select = 'non';
			caseTemp= null;
		}
		else{
			caseTemp.etat = 'marque';
			caseTemp.select = 'non';
			caseTemp=null;
		}
		render(plateauAdversaire);
		render(plateauMaison);
	}
}
function renduBateau(bateau,plateau){
	var taille = 2;
	if(bateau.type =="PorteAvion"){
		taille = 5;
	}else if(bateau.type == "Croiseur"){
		taille = 4;
	}else if(bateau.type =="SousMarin"){
		taille = 3;
	}else{}

	var baseElement = plateau.elements[bateau.pos]

	var boxBateau;
	if(bateau.orientation=='O'){

		boxBateau= {x:(baseElement.left-(baseElement.width*(taille-1))),
				y:baseElement.top,
				h:baseElement.height,
				w:baseElement.width*taille}; 

	}else if(bateau.orientation=='E'){
		boxBateau= {x:baseElement.left,
				y:baseElement.top,
				h:baseElement.height,
				w:baseElement.width*taille}; 

	}else if(bateau.orientation=='S'){
		boxBateau= {x:baseElement.left,
				y:baseElement.top,
				h:baseElement.height*taille,
				w:baseElement.width}; 

	}else{
		boxBateau= {x:baseElement.left,
				y:(baseElement.top-(baseElement.height*(taille-1))),
				h:(baseElement.height*taille),
				w:baseElement.width}; 
	}


	plateau.elements.forEach(function(element) {

		if((element.left >= boxBateau.x + boxBateau.w)      // trop à droite
				|| (element.left + element.width <= boxBateau.x) // trop à gauche
				|| (element.top >= boxBateau.y + boxBateau.h) // trop en bas
				|| (element.top + element.height <= boxBateau.y)) {// trop en haut

			if(element.option== bateau.type){
				element.type = 'vide';
			}

		} else{
			element.type ='bateau';
			element.option = bateau.type;

		}

	});
	render(plateau);



}
function renduMarque(Plateau,element){
	if (element.etat === 'marque'){
		Plateau.context.fillStyle = '#FF0000';
	}else{
		Plateau.context.fillStyle = '#05EFFF';
	}
}

function renderBis(element, Plateau){

	if (element.select == 'oui'){
		Plateau.context.fillStyle = '#008000';
		Plateau.context.fillRect(element.left, element.top, element.width, element.height);
	}
	else{
		if(element.type == 'bateau'){
			Plateau.context.fillStyle = ' #008000';
			Plateau.context.fillRect(element.left, element.top, element.width, element.height);
			Plateau.context.fillStyle = ' #808080';
			Plateau.context.fillRect(element.left, element.top, element.width-10, element.height);

		}else{
			renduMarque(Plateau,element);
			if(element.type == 'mine')
			{
				
				Plateau.context.fillRect(element.left, element.top, element.width, element.height);
				Plateau.context.drawImage(mine,element.left, element.top, element.width, element.height);
			}else{

				
				Plateau.context.fillRect(element.left, element.top, element.width, element.height);

			}


		}
	}
	if(element.touche  == 1){
		//rater
		Plateau.context.beginPath();
		Plateau.context.strokeStyle  = '#000000';
		Plateau.context.moveTo(element.left, element.top);
		Plateau.context.lineTo(element.width+element.left, element.height+element.top);
		Plateau.context.stroke();
	}else if(element.touche == 2){
		//toucher
		Plateau.context.strokeStyle = '#FF0000';
		Plateau.context.beginPath();
		Plateau.context.moveTo(element.left, element.top);
		Plateau.context.lineTo(element.width+element.left, element.height+element.top);
		Plateau.context.moveTo(element.width+element.left, element.top);
		Plateau.context.lineTo(element.left, element.height+element.top);
		Plateau.context.stroke();
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
	var textbox = document.getElementById("msgChat");
	var text =	textbox.value;
	var chat = {message: text,destinataire:"tous"};
	sendJson("chat", chat);
	textbox.value = "";

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
		var plateau = plateauAdversaire;
		if(objectJson.hasOwnProperty("plateau")){
			if(objectJson.plateau == 1){
				plateau = plateauMaison;
			}
		}
		if(objectJson.hasOwnProperty("chat")){
			document.getElementById("chatlog").textContent += objectJson.chat + "\n";
		}
		if(objectJson.hasOwnProperty("mine")){
			plateau.elements[objectJson.mine.pos].type = 'mine';
			render(plateau);
		}
		if(objectJson.hasOwnProperty("tir")){
			document.getElementById("chatlog").textContent += objectJson.tir + "\n"
			plateau.elements[objectJson.tir.pos].touche = objectJson.tir.resultat;
		}
		if(objectJson.hasOwnProperty("bateau")){
			renduBateau(objectJson.bateau,plateau);
			document.getElementById("chatlog").textContent += objectJson.bateau.type + "\n"
		}
		if(objectJson.hasOwnProperty("erreur")){
			alert(objectJson.erreur);
			document.getElementById("chatlog").textContent += objectJson.erreur + "\n"
		}
		if(objectJson.hasOwnProperty("pointAction")){
			poinAction.textContent = "Points d'action : " + objectJson.pointAction;
		
		}
	}catch(exection){
		document.getElementById("consolLog").textContent += message.data + "\n"+exection;
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
	document.getElementById("chatlog").textContent += "Connexion fermé\n"
}
