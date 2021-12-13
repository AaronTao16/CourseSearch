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
    httpRequest.open('GET', '/search?level=' + query + " " + currentValue);
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