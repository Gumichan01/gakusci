'use strict';

var researchServices = ["!arxiv", "!hal", "!libgen", "!scihub", "!theses", "!thesis"]
var bookServices = ["!goodreads", "!openlib", "!penguin"]
var mangaServices = ["!manga", "!kitsumanga"]
var animeServices = ["!anidb", "!anime", "!anilist", "!kitsuanime"]
var musicServices = ["!discogs", "!freesound", "!musicbrainz", "!opengameart"]

function autocomplete(queryInput, research, books, mangas, animes, music) {
  /*
    The autocomplete function takes:
    - the text field element (the input)
    - several array of possible autocompleted values, depending of the domain (research, books, ...)
   */
  var currentFocus = -1;

  queryInput.addEventListener("input", function(e) {
    var text = this.value;
    if (!text) {
      return false;
    }

    clearAutocompletionList();
    // Select the list of services related to the type of search
    var tpapers = document.getElementById("type_papers")
    var tbooks = document.getElementById("type_books")
    var tmanga = document.getElementById("type_manga")
    var tanime = document.getElementById("type_anime")
    var tmusic = document.getElementById("type_music")
    var completionList;

    if (tpapers.checked) {
      completionList = research
    } else if (tbooks.checked) {
      completionList = books
    } else if (tmanga.checked) {
      completionList = mangas
    } else if (tanime.checked) {
      completionList = animes
    } else if (tmusic.checked) {
      completionList = music
    }

    // Display the autocompletion list
    var autocompletionListElem = document.createElement("DIV");
    autocompletionListElem.setAttribute("id", this.id + "autocomplete-list");
    autocompletionListElem.setAttribute("class", "autocomplete-items");

    this.parentNode.appendChild(autocompletionListElem);
    for (var i = 0; i < completionList.length; i++) {
      if (completionList[i].substring(0, text.length).toUpperCase() == text.toUpperCase()) {
        var element = document.createElement("DIV");
        element.innerHTML = "<strong>" + completionList[i].substring(0, text.length) + "</strong>";
        element.innerHTML += completionList[i].substring(text.length);
        element.innerHTML += "<input type='hidden' value='" + completionList[i] + "'>";

        element.addEventListener("click", function(e) {
          queryInput.value = this.getElementsByTagName("input")[0].value;
          clearAutocompletionList();
        });

        autocompletionListElem.appendChild(element);
      }
    }
  });

  queryInput.addEventListener("keydown", function(e) {
      var autocompletionElement = document.getElementById(this.id + "autocomplete-list");
      if (autocompletionElement) {
        const DOWN_KEYCODE = 40, UP_KEYCODE = 38, ESCAPE_KEYCODE = 27;
        var autocompletionList = autocompletionElement.getElementsByTagName("div");
        if (e.keyCode == DOWN_KEYCODE) {
          if (currentFocus >= 0) {
            autocompletionList[currentFocus].classList.remove("autocomplete-active");
          }
          currentFocus++;
          if (currentFocus >= autocompletionList.length) {
            currentFocus = 0;
          }

          autocompletionList[currentFocus].classList.add("autocomplete-active");
          queryInput.value = autocompletionList[currentFocus].getElementsByTagName("input")[0].value + " "
        } else if (e.keyCode == UP_KEYCODE) {
          if (currentFocus >= 0) {
            autocompletionList[currentFocus].classList.remove("autocomplete-active");
          }
          currentFocus--;
          if (currentFocus < 0) {
            currentFocus = (autocompletionList.length - 1);
          }

          autocompletionList[currentFocus].classList.add("autocomplete-active");
          queryInput.value = autocompletionList[currentFocus].getElementsByTagName("input")[0].value + " "
        } else if (e.keyCode == ESCAPE_KEYCODE) {
          clearAutocompletionList();
        }
      }
  });

  function clearAutocompletionList() {
    var autocompletionElement = document.getElementsByClassName("autocomplete-items");
    for (var i = 0; i < autocompletionElement.length; i++) {
      autocompletionElement[i].parentNode.removeChild(autocompletionElement[i]);
    }
  }

  document.addEventListener("click", function (e) {
    var target = e.target;
    if (target != queryInput) {
      clearAutocompletionList();
    }
  });

}

autocomplete(document.getElementById("query"), researchServices, bookServices, mangaServices, animeServices, musicServices);
