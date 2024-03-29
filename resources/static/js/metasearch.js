'use strict';

var researchServices = ["!arxiv", "!hal", "!libgen", "!scihub", "!theses", "!thesis"]
var bookServices = ["!goodreads", "!openlib", "!penguin"]
var mangaServices = ["!manga", "!kitsumanga"]
var animeServices = ["!anidb", "!anime", "!anilist", "!kitsuanime"]

function autocomplete(inp, research, books, mangas, animes) {
  /* The autocomplete function takes:
    - the text field element (the input)
    - several array of possible autocompleted values, depending of the domain (research, books, ...)
   */
  var currentFocus;
  // execute a function when someone writes in the text field
  inp.addEventListener("input", function(e) {
      var a, b, i, text = this.value;
      /*close any already open lists of autocompleted values*/
      closeAllLists();
      if (!text) { return false;}
      currentFocus = -1;

      // Select the list of services related to the type of search
      var tpapers = document.getElementById("type_papers")
      var tbooks = document.getElementById("type_books")
      var tmanga = document.getElementById("type_manga")
      var tanime = document.getElementById("type_anime")
      var arr;

      if (tpapers.checked) {
        arr = research
      } else if (tbooks.checked) {
        arr = books
      } else if (tmanga.checked) {
        arr = mangas
      } else if (tanime.checked) {
        arr = animes
      }

      // Display the autocompletion list
      a = document.createElement("DIV");
      a.setAttribute("id", this.id + "autocomplete-list");
      a.setAttribute("class", "autocomplete-items");

      this.parentNode.appendChild(a);
      for (i = 0; i < arr.length; i++) {
        if (arr[i].substr(0, text.length).toUpperCase() == text.toUpperCase()) {
          b = document.createElement("DIV");
          b.innerHTML = "<strong>" + arr[i].substr(0, text.length) + "</strong>";
          b.innerHTML += arr[i].substr(text.length);
          b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";

          // I integrate this listener in order to update the selected entry in the autocompletion list
          b.addEventListener("click", function(e) {
            inp.value = this.getElementsByTagName("input")[0].value;
            closeAllLists();
           });
          a.appendChild(b);
        }
      }
  });
  // execute a function presses a key on the keyboard
  inp.addEventListener("keydown", function(e) {
      var x = document.getElementById(this.id + "autocomplete-list");
      if (x) x = x.getElementsByTagName("div");
      if (e.keyCode == 40) {    // DOWN key pressed
        currentFocus++;
        addActive(x);
        inp.value = x[currentFocus].getElementsByTagName("input")[0].value + " "
      } else if (e.keyCode == 38) { // UP key pressed
        currentFocus--;
        addActive(x);
        inp.value = x[currentFocus].getElementsByTagName("input")[0].value + " "
      } else if (e.keyCode == 27) { // ESCAPE key pressed
        closeAllLists();
      }
  });

  function addActive(x) {
    /*a function to classify an item as "active":*/
    if (!x) return false;
    removeActive(x);
    if (currentFocus >= x.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (x.length - 1);
    x[currentFocus].classList.add("autocomplete-active");
  }

  function removeActive(x) {
    /*a function to remove the "active" class from all autocomplete items:*/
    for (var i = 0; i < x.length; i++) {
      x[i].classList.remove("autocomplete-active");
    }
  }

  function closeAllLists(elmnt) {
    /*close all autocomplete lists in the document,
    except the one passed as an argument:*/
    var x = document.getElementsByClassName("autocomplete-items");
    for (var i = 0; i < x.length; i++) {
      if (elmnt != x[i] && elmnt != inp) {
        x[i].parentNode.removeChild(x[i]);
      }
    }
  }

// execute a function when someone clicks in the document:
document.addEventListener("click", function (e) {
    closeAllLists(e.target);
});

}

autocomplete(document.getElementById("query"), researchServices, bookServices, mangaServices, animeServices);
