let xhr = new XMLHttpRequest();
sendCMD('neofetch --stdout')

xhr.onreadystatechange = function()
{
    let brow = document.getElementById('browser');
    brow.innerHTML = xhr.responseText;
    brow.scroll({top: 99999999});
}

function sendCMD(val)
{
    if (val === 'click') val = document.getElementById('txtLine').value;
    document.getElementById('txtLine').value = '';
    xhr.open('POST', '', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(val);
}