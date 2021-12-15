window.addEventListener('load', function (e) {
    // register handlers for facet buttons
    let facetButtons = document.querySelectorAll('.facet-group button');
    facetButtons.forEach(function (element) {
        element.addEventListener('click', onFacetClick);
    });

    // hide facet buttons in categories that are already selected
    // only one selection allowed per category
    let params = new URLSearchParams(window.location.search);
    if (params.has('facet')) {
        // facet currently selected
        let facetValues = params.getAll('facet');
        for (let i = 0; i <= facetValues.length - 2; i+=2) {
            // get group for current category
            let group = document.getElementById(`${facetValues[i]}-facet-group`);
            group.innerHTML = `<button type="button" class="list-group-item list-group-item-action" disabled>${facetValues[i+1]}</button>`;
        }
    }

});

// facet click handler
function onFacetClick(e) {
    let category = this.dataset.category;
    let label = this.dataset.label;
    let currentURL = window.location.href;
    let redirectURL = `${currentURL}&facet=${encodeURIComponent(category)}&facet=${encodeURIComponent(label)}`;
    window.location.href = redirectURL;
}

window.onload = function () {
    var queryInput = document.getElementById("searchInput")
    queryInput.setAttribute("value", new URLSearchParams(window.location.search).get('query'))
}