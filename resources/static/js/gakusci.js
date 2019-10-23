'use strict';

function GakuQuery() {
  return React.createElement(
    'input',
    {id: "query", type: "text", name: "q"}
  );
}

function GakuSearchSubmit() {
  return React.createElement(
    'input',
    {id: "search", type: "submit"}
  );
}

function GakuForm() {
  return React.createElement(
    'form',
    {action: "/search"},
    GakuQuery(),
    GakuSearchSubmit()
  );
}

/*

<form action="/search">
  <input id="query" type="text" name="q"/>
  <input id="search" type="submit"/>
</form>

*/
const domContainer = document.querySelector('#search_form');
ReactDOM.render(React.createElement(GakuForm), domContainer);
