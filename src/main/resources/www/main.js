document.querySelector(".btn").addEventListener("click",()=>{

   let paragraph = document.querySelector(".paragraph");
   paragraph.textContent = "Button clicked";
   paragraph.style.color = "red";
})