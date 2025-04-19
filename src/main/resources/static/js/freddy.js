document.addEventListener("DOMContentLoaded", () => {
    const screamer = document.getElementById("screamer");
    const screamSound = new Audio("/mp3/freddy.mp3");
    screamer.style.backgroundImage = "url('/img/freddy.webp')";
    
    let screamerTime = 200;

    function activateScreamer() {
        try {
            screamSound.play();
            setTimeout(() => {
                screamSound.pause();
                screamSound.currentTime = 0;
            }, screamerTime);
        } catch(error) {
            console.error("Error al reproducir el sonido:", error);
        }
        for(let i=0; i<3; i++) {
            screamer.style.display = "block";
            setTimeout(() => {
                screamer.style.display = "none";
            }, screamerTime);
        }
    }

    setTimeout(activateScreamer, 3*1000);
});