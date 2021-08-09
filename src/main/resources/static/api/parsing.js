Notification.requestPermission(function(permission)
{
    console.log('Результат запроса прав:', permission);
});

let not = null;
let xhr = new XMLHttpRequest();
xhr.onreadystatechange = function()
{
    if (xhr.readyState === 4 && xhr.status === 200)
    {
        if (xhr.responseText.includes('Success'))
        {
            if (not != null) not.close();
            not = new Notification('Executed', {body: xhr.responseText, dir: 'auto'});
            callSYS();
            return;
        }

        var jsonData = JSON.parse(xhr.responseText);
        switch(jsonData.infoType)
        {
            case('HARDWARE'):
                showHW(jsonData);
                break;
            case('SERVICE'):
                showSYS(jsonData);
                break;
            default:
                break;
        }
    }
};

const urlHW = "/api/hwinfo";
const urlSYS = "/api/servinfo";

function callSYS()
{
    xhr.open("GET", urlSYS, false);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send();
}

function callHW()
{
    xhr.open("GET", urlHW, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send();
}

callSYS();
callHW();
setInterval(callHW, 5000);

function sendServiceCommand(number, command)
{
    let servName;
    switch (number)
    {
        case 1:
            servName = document.getElementById("fserviceJSON").innerHTML.split(' ')[2].split("<br>")[0];
            break;
        case 2:
            servName = document.getElementById("sserviceJSON").innerHTML.split(' ')[2].split("<br>")[0];
            break;
        case 3:
            servName = document.getElementById("tserviceJSON").innerHTML.split(' ')[2].split("<br>")[0];
            break;
    }
    let toSend = servName + ' ' + command;
    xhr.open('POST', '', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(toSend);
}