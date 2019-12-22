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

function GakuSearchTypeSet(tvalue) {
  return e(
    'input',
    {id: "type_all", type: "checkbox", name: 'type', value: tvalue}
  );
}

function GakuTypeChoice() {
  return e('div',)
}

function GakuForm() {
  return e(
    'form',
    {action: "/researches"},
    GakuQuery(),
    GakuSearchSubmit(),
    e('br'),
    GakuSearchTypeSet("all"),
    'AllSources'
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
