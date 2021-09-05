let xhr = new XMLHttpRequest();
console.log('Loaded xhr');

xhr.onreadystatechange = function()
{
    console.log('Got back: ' + xhr.responseText);
    document.getElementById('browser').innerHTML = xhr.responseText;
}

function sendCMD()
{
    console.log('SendCMD call');
    let val = document.getElementById('txtLine').value;
    xhr.open('POST', '', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(val);
}