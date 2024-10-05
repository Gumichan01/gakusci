'use strict';

function autocomplete(queryInput) {
  /*
    The autocomplete function takes:
    - the text field element (the input)
    - several array of possible autocompleted values, depending of the domain (research, books, ...)
   */
  queryInput.addEventListener("input", function(e) {
    let text = this.value;
    if (!text) {
      return false;
    }

    let researchServices = ["!arxiv", "!hal", "!libgen", "!scihub", "!theses", "!thesis"];
    let bookServices = ["!goodreads", "!openlib", "!penguin"];
    let mangaServices = ["!manga", "!kitsumanga"];
    let animeServices = ["!anidb", "!anime", "!anilist", "!kitsuanime"];
    let musicServices = ["!discogs", "!freesound", "!musicbrainz", "!opengameart"];

    let paperElement = document.getElementById("type_papers");
    let bookElement = document.getElementById("type_books");
    let mangaElement = document.getElementById("type_manga");
    let animeElement = document.getElementById("type_anime");
    let musicElement = document.getElementById("type_music");
    let completionList /*: Array */;

    if (paperElement.checked) {
      completionList = researchServices;
    } else if (bookElement.checked) {
      completionList = bookServices;
    } else if (mangaElement.checked) {
      completionList = mangaServices;
    } else if (animeElement.checked) {
      completionList = animeServices;
    } else if (musicElement.checked) {
      completionList = musicServices;
    }

    clearAutocompletionList();
    let autocompletionListElem = document.createElement("DIV");
    autocompletionListElem.setAttribute("id", this.id + "autocomplete-list");
    autocompletionListElem.setAttribute("class", "autocomplete-items");
    this.parentNode.appendChild(autocompletionListElem);

    completionList.forEach(function(item) {
      if (item.substring(0, text.length).toUpperCase() === text.toUpperCase()) {
        let element = document.createElement("DIV");
        element.innerHTML = "<strong>" + item.substring(0, text.length) + "</strong>";
        element.innerHTML += item.substring(text.length);
        element.innerHTML += "<input type='hidden' value='" + item + "'>";

        element.addEventListener("click", function(e) {
          queryInput.value = this.getElementsByTagName("input")[0].value;
          clearAutocompletionList();
        });

        autocompletionListElem.appendChild(element);
      }
    });
  });

  let currentFocus = -1;
  queryInput.addEventListener("keydown", function(e) {
      let autocompletionElement = document.getElementById(this.id + "autocomplete-list");
      if (autocompletionElement) {
        const DOWN_KEYCODE = 40, UP_KEYCODE = 38, ESCAPE_KEYCODE = 27;
        let autocompletionList = autocompletionElement.getElementsByTagName("div");
        if (e.keyCode === DOWN_KEYCODE ||  e.keyCode === UP_KEYCODE) {
          if (currentFocus >= 0 && currentFocus < autocompletionList.length) {
            autocompletionList[currentFocus].classList.remove("autocomplete-active");
          } else {
            currentFocus = -1;
          }

          if (e.keyCode === DOWN_KEYCODE) {
            currentFocus++;
            if (currentFocus >= autocompletionList.length) {
              currentFocus = 0;
            }
          } else if (e.keyCode === UP_KEYCODE) {
            currentFocus--;
            if (currentFocus < 0) {
              currentFocus = (autocompletionList.length - 1);
            }
          }
          autocompletionList[currentFocus].classList.add("autocomplete-active");
          queryInput.value = autocompletionList[currentFocus].getElementsByTagName("input")[0].value + " ";
        } else if (e.keyCode === ESCAPE_KEYCODE) {
          clearAutocompletionList();
        }
      }
  });

  function clearAutocompletionList() {
    let autocompleteItems = document.getElementsByClassName("autocomplete-items");
    Array.from(autocompleteItems).forEach((item) => item.parentNode.removeChild(item));
    currentFocus = -1;
  }

  document.addEventListener("click", function (e) {
    let target = e.target;
    if (target !== queryInput) {
      clearAutocompletionList();
    }
  });

}

autocomplete(document.getElementById("query"));
