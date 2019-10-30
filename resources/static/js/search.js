'use strict';

// Welcome to the world of Javascript
function decode(text /*: String */) {
  let doc /*: Document */ = new DOMParser().parseFromString(text, "text/html");
  return doc.documentElement.textContent;
}

document.querySelectorAll('.entry_label').forEach(element => {
  const text /*: String */ = element.innerHTML;
  element.innerHTML = decode(text)
});
