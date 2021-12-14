var prevValue = 0;
var currentValue;
if (currentValue === undefined) currentValue = 0;
var httpRequest;

function handleClick(myRadio) {
    // alert('Old value: ' + currentValue);
    // alert('New value: ' + myRadio.value);
    prevValue = currentValue;
    currentValue = myRadio.value;
    if(prevValue === currentValue) return

    httpRequest = new XMLHttpRequest();

    if (!httpRequest) {
        console.log('Giving up :( Cannot create an XMLHTTP instance');
        return false;
    }

    const urlParams = new URLSearchParams(window.location.search);
    const query = urlParams.get('query')
    console.log(query)
    httpRequest.onreadystatechange = alertContents;
    httpRequest.open('GET', '/search?query=' + query + " " + currentValue);
    httpRequest.send();
}

function alertContents() {
    try {
        if (httpRequest.readyState === XMLHttpRequest.DONE) {
            if (httpRequest.status === 200) {
                console.log(httpRequest.responseText)
                // ${courseList} = httpRequest.responseText;
            } else {
                alert('There was a problem with the request.');
            }
        }
    }
    catch( e ) {
        alert('Caught Exception: ' + e.description);
    }
}

function nextPage(next){
    var currentPage = parseInt(new URLSearchParams(window.location.search).get('page')) + 1
    var query = new URLSearchParams(window.location.search).get('query')
    console.log(currentPage)
    next.setAttribute("href", "/search?page="+ currentPage +"&query=" + query)
}

function prePage(pre){
    var currentPage = parseInt(new URLSearchParams(window.location.search).get('page')) - 1
    var query = new URLSearchParams(window.location.search).get('query')
    console.log(currentPage)
    pre.setAttribute("href", "/search?page="+ currentPage +"&query=" + query)
}

function goToPage(cur){
    var currentPage = cur.innerHTML
    var query = new URLSearchParams(window.location.search).get('query')
    cur.setAttribute("href", "/search?page="+ currentPage +"&query=" + query)
}

window.onload = function () {

    document.getElementById("searchInput").addEventListener("keyup", function(event) {
        console.log(event.key)
        if (event.key === "Enter") {
            event.preventDefault();
            var query = document.getElementById("searchInput").value
            window.location.replace("/search?page=1&query=" + query);
        }
    });

    var queryInput = document.getElementById("searchInput")
    queryInput.setAttribute("value", new URLSearchParams(window.location.search).get('query'))

    var currentPage = new URLSearchParams(window.location.search).get('page')

    if(currentPage === document.querySelectorAll('a[id=l1]')[0].innerHTML){
        var link1 = document.getElementsByClassName("page-item")[1]
        link1.setAttribute("class", link1.getAttribute("class") + " " + "active")
        var pre = document.getElementsByClassName("page-item")[0]
        pre.setAttribute("class", pre.getAttribute("class") + " " + "disabled")
    }

    if(currentPage === document.querySelectorAll('a[id=l2]')[0].innerHTML){
        var link2 = document.getElementsByClassName("page-item")[2]
        link2.setAttribute("class", link2.getAttribute("class") + " " + "active")
    }

    if(currentPage === document.querySelectorAll('a[id=l3]')[0].innerHTML){
        var link3 = document.getElementsByClassName("page-item")[3]
        link3.setAttribute("class", link3.getAttribute("class") + " " + "active")
    }

};