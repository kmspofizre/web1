let yCoord = null;
let rValue = null;
let xCoord = null;
let prevElem = null;
let prevEq = false;
let loaded = false;
let toggleLoaded = false;


window.addEventListener('load', function(){
  const applicantForm = document.getElementById('pform');
  applicantForm.addEventListener('submit', handleFormSubmit);
})


function handleFormSubmit(event) {
  event.preventDefault();
  let checkCounter = 0;
  var cont = document.getElementById("loader");

  if (yCoord == null){
    cont.innerText = "You should choose Y";
    if (!toggleLoaded){
      toggleLoader();
      toggleLoaded = true;
    }
    loaded = true;
  }
  else {

    var radios = document.getElementsByName('r');

    for (var i = 0, length = radios.length; i < length; i++) {
      if (radios[i].checked) {
        rValue = radios[i].value;
        checkCounter += 1;
      }
    }
    if (checkCounter != 1){
      cont.innerText = "Choose one option for R";
      if (!toggleLoaded){
          toggleLoader();
          toggleLoaded = true;
      }
      
      loaded = true;
    }
    else{
      xCoord = document.getElementById('x').value;
      console.log(Number(xCoord))
      if (isNaN(xCoord) || !(-3 <= xCoord && xCoord <= 3)){
        cont.innerText = "X should be in -3...3 range";
        if (!toggleLoaded){
          toggleLoader();
          toggleLoaded = true;
      }
        loaded = true;
      }
      else {
      
        let coords = {
          x: xCoord,
          y: yCoord,
          r: rValue
        }
        fetch('/fcgi-bin/web_lab.jar', 
          {method: 'POST',
            headers: {
              'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(coords)
          })
        .then(
          response => {
            if(!response.ok) {
              throw new Error(`${response.status}`);
            }
            return response.text();
          }
        )
        .then(function (answer){
          var res = JSON.parse(answer);
          var table = document.getElementById("tres"),
          tbody = table.getElementsByTagName("tbody")[0];
          var row = document.createElement("tr");
          var isHit = document.createElement("td");
          var x = document.createElement("td");
          var y = document.createElement("td");
          var r = document.createElement("td");
          if (res.result == true){
            isHit.innerText = "OK";
          }
          else {
            isHit.innerText = "MISS";
          }
          x.innerText = res.x;
          y.innerText = res.y;
          r.innerText = res.r;
          row.appendChild(x);
          row.appendChild(y);
          row.appendChild(r);
          row.appendChild(isHit);
          tbody.appendChild(row);
          if (toggleLoaded == true){
            cont.innerText = "";
            toggleLoader();
            toggleLoaded = false;
          }
        })
      }
    }
  }
  for (var i = 0, length = radios.length; i < length; i++) {
    radios[i].checked = false;
  }
  yCoord = null;
  xCoord = null;
  document.getElementById('x').value = "";
  // if (prevElem != null){
  //   prevElem.classList.toggle('change_color');
  // }
  // prevElem = null;
  // prevEq = false;
}


function toggleLoader() {
  const loader = document.getElementById('loader');
  loader.classList.toggle('hidden');
}


// function resetColor(){
//   var yS = document.getElementsByName('y');
//   for (var j = 0, len = yS.length; j < len; j++) {
//     yS[j].style.backgroundColor = 'yellow';
//   }
// }


// function yClick(clickedElement) {
//   if (prevElem == clickedElement){
//     if (yCoord == null){
//       yCoord = clickedElement.value;
//     }
//     else {
//       yCoord = null;
//     }
    
//     prevElem.classList.toggle('change_color');
//     prevEq = true;
//     return;
//   }
//   if (prevElem != null && prevEq != true){
//     prevElem.classList.toggle('change_color');
//   }
//   clickedElement.classList.toggle('change_color');
//   yCoord = clickedElement.value;
//   prevElem = clickedElement;
//   prevEq = false;
//   wait(10);
// }

function yClick(clickedElement){
  yCoord = clickedElement.value;
}