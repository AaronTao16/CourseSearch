document.getElementById("input1").addEventListener("keyup", function(event) {
    if (event.keyCode === 13) {
        event.preventDefault();
        console.log(event)
        // document.getElementById("myFormID").submit();
        return false;
    }
});