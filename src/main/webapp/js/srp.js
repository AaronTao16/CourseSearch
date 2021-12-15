window.addEventListener('load', function (e) {
    // register handlers for facet buttons
    let facetButtons = document.querySelectorAll('#facet-group button');
    facetButtons.forEach(function (element) {
        element.addEventListener('click', onFacetClick);
    });
});

// facet click handler
function onFacetClick(e) {
    let category = this.dataset.category;
    let label = this.dataset.label;
    let currentURL = window.location.href;
    let redirectURL = `${currentURL}&facet=${encodeURIComponent(category)}&facet=${encodeURIComponent(label)}`;
    window.location.href = redirectURL;
}