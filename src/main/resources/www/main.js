document.querySelector(".btn").addEventListener("click", () => {

    let paragraph = document.querySelector(".paragraph");
    paragraph.textContent = "Button clicked";
    paragraph.style.color = "red";

    let img = document.querySelector(".img");
    if (img.getAttribute("src") === "wave.png") {
        img.src = "astronaut.png";
    } else {
        img.src = "wave.png";
    }

})