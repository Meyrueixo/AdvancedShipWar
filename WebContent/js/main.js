var elem = document.getElementById('myCanvas'),
  elemLeft = elem.offsetLeft,
  elemTop = elem.offsetTop,
  taillex = elem.getAttribute('width')/10,
  tailley = elem.getAttribute('height')/10,
  context = elem.getContext('2d'),
  elements = [];
var info = document.getElementById('info');
var infoserv = document.getElementById('infoserv');
var caseActu;

// Add event listener for `click` events.
elem.addEventListener('click', function(event) {
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

function render()
{
  // Render elements.
  elements.forEach(function(element) {

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

  });
}
/*--------------------web sockect -----------*/
var ws = new WebSocket("ws://localhost:8080/AdvancedShipWar/AdvancedShipWarGame");
ws.onopen = function(){
	
};
ws.onmessage = function(message){
    document.getElementById("chatlog").textContent += message.data + "\n";
};
/*fonction envoi de message*/
function sendMessage(message){
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
