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

function getXMLHttpRequest() {
  var xhr = null;

  if (window.XMLHttpRequest || window.ActiveXObject) {
    if (window.ActiveXObject) {
      try {
        xhr = new ActiveXObject("Msxml2.XMLHTTP");
      } catch(e) {
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
      }
    } else {
      xhr = new XMLHttpRequest();
    }
  } else {
    alert("Votre navigateur ne supporte pas l'objet XMLHTTPRequest...");
    return null;
  }

  return xhr;
}
function request(callback) {

  var xhr = getXMLHttpRequest();

    xhr.onreadystatechange = function() {
      if (xhr.readyState == 4 && (xhr.status == 200 || xhr.status == 0)) {
        callback(xhr.responseText);
      }
    };

    xhr.open("GET", "insta.php", true);
    xhr.send(null);
}

function readData(sData) {
  // On peut maintenant traiter les données sans encombrer l'objet XHR.
  if (sData != -1) {
    infoserv.textContent = ("nb click " + sData);
  } else {
    infoserv.textContent = ("Y'a eu un problème");
  }
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
