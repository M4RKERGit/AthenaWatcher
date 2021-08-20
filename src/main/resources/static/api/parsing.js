Notification.requestPermission(function(permission)
{
    console.log('Результат запроса прав:', permission);
});

let not = null;
let xhrHW = new XMLHttpRequest();
let xhrSYS = new XMLHttpRequest();
let xhrCMD = new XMLHttpRequest();
const urlHW = "/api/hwinfo";
const urlSYS = "/api/servinfo";

xhrHW.onreadystatechange = function()
{
    if (xhrHW.readyState === 4 && xhrHW.status === 200)
    {
        let jsonData = JSON.parse(xhrHW.responseText);
        if (jsonData.infoType === 'HARDWARE') showHW(jsonData);
    }
};

xhrSYS.onreadystatechange = function()
{
    if (xhrSYS.readyState === 4 && xhrSYS.status === 200)
    {
        let jsonData = JSON.parse(xhrSYS.responseText);
        if (jsonData.infoType === 'SERVICE') showSYS(jsonData);
    }
};

xhrCMD.onreadystatechange = function()
{
    console.log(xhrCMD.responseText);
    if (xhrCMD.responseText === 'refresh true')
    {
        document.getElementById("refreshSwitch").setAttribute('value', ' Stop refreshing ');
        callSYS();
        callHW();
    }
    if (xhrCMD.responseText === 'refresh false')
    {
        document.getElementById("refreshSwitch").setAttribute('value', ' Start refreshing ');
        callSYS();
        callHW();
    }
    if (xhrCMD.responseText.includes('Success'))
    {
        if (not != null) not.close();
        not = new Notification('Executed', {body: xhrCMD.responseText, dir: 'auto'});
        callSYS();
    }
}

function callSYS()
{
    xhrSYS.open("GET", urlSYS, true);
    xhrSYS.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhrSYS.send();
}

function callHW()
{
    xhrHW.open("GET", urlHW, true);
    xhrHW.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhrHW.send();
}

callHW();
callSYS();

setInterval(callHW, 3000);
setInterval(callSYS, 3000);

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
    xhrCMD.open('POST', '', true);
    xhrCMD.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhrCMD.send(toSend);
}

function sendCustom(command)
{
    xhrCMD.open('POST', '', true);
    xhrCMD.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhrCMD.send(command);
}