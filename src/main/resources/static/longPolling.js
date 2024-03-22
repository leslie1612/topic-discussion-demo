let isPolling = false;

function start() {
    if (isPolling) return;
    isPolling = true;
    pollData();
}

function pollData() {
    let outcome = document.querySelector("#outcome");
    fetch('/random')
        .then(response => response.json())
        .then(data => {
            outcome.innerHTML = data;
            console.log('Received data:', data);
            if (isPolling) {
                pollData(); // 再次發送請求
            }
        })
        .catch(error => {
            console.error('Error during long polling:', error);
            if (isPolling) {
                pollData(); // 再次發送請求
            }
        });
}

function stop() {
    isPolling = false;
}