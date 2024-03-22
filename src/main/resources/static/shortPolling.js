let intervalId;

function start() {
    intervalId = setInterval(getData, 1000);
}


function getData() {
    const timestamp = Date.now();


    const date = new Date(timestamp);
    const formattedDate = `${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}`;


    let outcome = document.querySelector("#outcome");
    fetch('/random')
        .then(response => response.json())
        .then(data => {
            outcome.innerHTML += formattedDate + ": " + data + "<br/>";
            console.log('Received data:', data);
        })
        .catch(error => {
            console.error('Error during long polling:', error);
        });
}


function stop() {
    clearInterval(intervalId);
}