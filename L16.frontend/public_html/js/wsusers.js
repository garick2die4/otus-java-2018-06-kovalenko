var ws;

init = function ()
{
    ws = new WebSocket("ws://" + window.location.host + "/l16.frontend/sock");
    ws.onopen = function (event)
    {

    }
    ws.onmessage = function (event)
    {
        var textarea = document.getElementById("messages");
        textarea.value = event.data + "\n";
    }
    ws.onclose = function (event) {

    }
};

function addField()
{
    var container = document.getElementById("phones_container");
    var lastId = "phone" + (container.childNodes.length + 1)
    var phonediv = document.createElement("div")
    phonediv.id = lastId

    var p = document.createElement("p")
    p.appendChild(document.createTextNode("Phone: "))

    var input = document.createElement("input")
    input.setAttribute("type", "tel")
    input.setAttribute("name", "phone")
    p.appendChild(input)

    phonediv.appendChild(p);

    container.appendChild(phonediv);
}
        
function sendGetTotalCount()
{
    var j = {"command" : "getTotalUsersCount"};
   	ws.send(JSON.stringify(j));
}

function sendGetUserId()
{
	var id = document.getElementById("userId");
	var j = {"command" : "showUser", "id" : id.value};
	ws.send(JSON.stringify(j));
}

function addUser()
{
	var name = document.getElementById("userName");
	var age = document.getElementById("userAge");
	var address = document.getElementById("userAddress");
	var phones_count = document.getElementById("userAddress");
	var phonesElems = document.getElementsByName("phone");
	var phones = []; 
	for (var i = 0; i < phonesElems.length; i++)
    	phones[i] = phonesElems[i].value;
	var j = {command: "addUser", "name" : name.value, "age" : age.value, "address" : address.value, "phones" : phones};
	ws.send(JSON.stringify(j));
}