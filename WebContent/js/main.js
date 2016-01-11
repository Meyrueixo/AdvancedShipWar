
var elemAdversaire = document.getElementById('PlateauAdversaire'),
  elemLeft = elemAdversaire.offsetLeft,
  elemTop = elemAdversaire.offsetTop,
  taillex = elemAdversaire.getAttribute('width')/10,
  tailley = elemAdversaire.getAttribute('height')/10,
  context = elemAdversaire.getContext('2d'),
  elements = [];

var elemMaison = document.getElementById('PlateauMaison'),
	elemLeft = elemAdversaire.offsetLeft,
	elemTop = elemAdversaire.offsetTop,
	taillex = elemAdversaire.getAttribute('width')/10,
	tailley = elemAdversaire.getAttribute('height')/10,
	context = elemAdversaire.getContext('2d'),
	elements = [];
var info = document.getElementById('info');
var infoserv = document.getElementById('infoserv');
var caseActu;

function creationPlateau( idPlateau){
	/** constructeur de plateau pour simplifier */
var canvas = document.getElementById(idPlateau);
var Plateau = {	zone: canvas ,
		elemLeft : canvas.offsetLeft,
		elemTop : canvas.offsetTop,
		taillex : canvas.getAttribute('width')/10,
		tailley : canvas.getAttribute('height')/10,
		context : canvas.getContext('2d'),
		elements : []
	};
 return Plateau 
}

// Add event listener for `click` events.
elemAdversaire.addEventListener('click', function(event) {
  var x = event.pageX - elemLeft,
    y = event.pageY - elemTop;

  // Collision detection between clicked offset and element.
  elements.forEach(function(element) {
    if (y > element.top && y < element.top + element.height
      && x > element.left && x < element.left + element.width) {
      info.textContent = ("element selectioner " + element.name);

      if (caseActu != null && caseActu.etat != 'marque'){
        caseActu.etat= 'inactif';

      }
      if (element.etat != 'marque'){
        element.etat = 'actif';
      }


      caseActu = element;
      //request(readData);
      /*if(element.type === 'mine')
      {
        element.type = 'vide'
      }else{
        element.type = 'mine'
      }*/
      render();
    }
  });

}, false);


var compt = 1;
for(i = 0;i <10;i ++){

  for(j = 0;j <10;j ++){

    elements.push({
      colour: '#05EFFF',
      width: taillex,
      height: tailley,
      top: (taillex*i),
      left: (tailley*j),
      name : compt,
      type : 'vide',
      etat : 'inactif'
    });
    compt++;
  }
}// Add element.
render();


function poserMine(){
  if(caseActu.type === 'mine')
  {
    alert("Une mine est déjà posée ici");
  }else{
    caseActu.type = 'mine'
  }
  render();
}

function marquerCase(){
  if(caseActu.etat === 'marque')
  {
    caseActu.etat = 'inactif';
  }else{
    if (caseActu.type != 'mine')
    caseActu.etat = 'marque';
  }
  render();
}

function renderBis(element){
	if(element.type == 'mine')
    {
      context.fillStyle = '#0500FF';
      context.fillRect(element.left, element.top, element.width, element.height);
    }else{
      if (element.etat === 'actif'){
        context.fillStyle = '#008000';
        context.fillRect(element.left, element.top, element.width, element.height);
      }
      else{
        if (element.etat === 'marque'){
          context.fillStyle = '#FF0000';
          context.fillRect(element.left, element.top, element.width, element.height);
        }
        else {
          context.fillStyle = element.colour;
          context.fillRect(element.left, element.top, element.width, element.height);
        }

      }

    }

    context.strokeStyle = '#000000';

    context.strokeRect(element.left, element.top, element.width, element.height);
    context.fillStyle =  '#000000';
    context.fillText(element.name,(element.left+(element.width/2)) ,(element.top +(element.height/2)));
}

function render(){
  // Render elements.
  elements.forEach(renderBis);
}
/*--------------------web sockect -----------*/
var ws = new WebSocket("ws://localhost:8080/AdvancedShipWar/AdvancedShipWarGame");
ws.onopen = function(){
	
};
/*reception message*/
ws.onmessage = function(message){
    document.getElementById("chatlog").textContent += message.data + "\n";
};
/*fonction envoi de message*/
function sendMessage(message){
	var messageForm = {}
	ws.send(message);
}

//propre a la console de debug
function postToServer(){
	sendMessage(document.getElementById("msg").value);
    document.getElementById("msg").value = "";
}
function closeConnect(){
    ws.close();
}
