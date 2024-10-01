let yCoord = null;
let rValue = null;
let xCoord = null;
let prevElem = null;
let prevEq = false;


window.addEventListener('load', function(){
  const applicantForm = document.getElementById('pform');
  applicantForm.addEventListener('submit', handleFormSubmit);
})


function handleFormSubmit(event) {
  event.preventDefault();
  let checkCounter = 0;

  if (yCoord == null){
    toggleLoader();
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
      toggleLoader();
    }
    else{
      alert(checkCounter);
      alert(rValue);
      xCoord = document.getElementById('x').value;
      alert(Number(xCoord) <= 3)
      console.log(Number(xCoord))
      if (isNaN(xCoord) || !(-3 <= xCoord && xCoord <= 3)){
        toggleLoader();
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
          alert(res['result']);
          alert(res['x']);
          alert(res['y']);
          alert(res['r']);
          

          // some really good response handler here
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
  if (prevElem != null){
    prevElem.classList.toggle('change_color');
  }
  prevElem = null;
  prevEq = false;
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


function yClick(clickedElement) {
  if (prevElem == clickedElement){
    yCoord = null;
    prevElem.classList.toggle('change_color');
    prevEq = true;
    return;
  }
  if (prevElem != null && prevEq != true){
    prevElem.classList.toggle('change_color');
  }
  clickedElement.classList.toggle('change_color');
  yCoord = clickedElement.value;
  prevElem = clickedElement;
  prevEq = false;
}