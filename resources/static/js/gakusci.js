'use strict';

const e = React.createElement;

function GakuQuery() {
  return e(
    'input',
    {id: "query", type: "text", name: "q"}
  );
}

function GakuSearchSubmit() {
  return e(
    'input',
    {id: "search", type: "submit"}
  );
}

function GakuForm() {
  return e(
    'form',
    {action: "/researches"},
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
ReactDOM.render(e(GakuForm), domContainer);
