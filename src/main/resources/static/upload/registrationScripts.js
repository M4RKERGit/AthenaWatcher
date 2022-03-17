let xhrRG = new XMLHttpRequest();
xhrRG.onreadystatechange = function ()
{
    if (xhrRG.readyState === 4 && xhrRG.status === 200)
    {
        if (xhrRG.responseText === "Successfully registered")
        {
            const elem = document.getElementById("regData");
            elem.parentNode.removeChild(elem);
            document.getElementById("welcomeHeader").textContent="Successfully registered";
        }
        else
        {
            document.getElementById("welcomeHeader").textContent="Try again, username and password lengths must be 8 or more";
        }
    }
}

function registerTry()
{
    let name, password, email, comment;
    name = document.getElementById("name").value;
    password = document.getElementById("password").value;
    email = document.getElementById("email").value;
    comment = document.getElementById("comment").value;
    let toSend = new Map();
    toSend.set("name", name).set("password", password).set("email", email).set("comment", comment);
    xhrRG.open('POST', '/upload/registration', true);
    xhrRG.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
    xhrRG.send(JSON.stringify(Object.fromEntries(toSend)));
}