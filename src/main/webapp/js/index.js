document.getElementById("input1").addEventListener("keyup", function(event) {
    if (event.keyCode === 13) {
        event.preventDefault();
        // document.getElementById("myFormID").submit();
        return false;
    }
});

var rad = document.querySelectorAll('input[type=radio][name="level"]');
rad.forEach(radio => radio.addEventListener('change', () => console.log(radio.value)));
