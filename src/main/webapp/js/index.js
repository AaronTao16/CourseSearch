window.onload = function () {
    document.getElementById("input1").addEventListener("keyup", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            var query = document.getElementById("input1").value
            // console.log(document.getElementById("input1").value)
            // var search = document.getElementById("search")
            // search.setAttribute("action", "/search?page=1&query=" + query)
            window.location.replace("/search?page=1&query=" + query);
        }
    });

    var rad = document.querySelectorAll('input[type=radio][name="level"]');
    rad.forEach(radio => radio.addEventListener('change', () => console.log(radio.value)));
}

