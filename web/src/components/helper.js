export function extractId(href) {
    let hrefArray = href.split('/');
    return hrefArray[hrefArray.length - 1];
}
