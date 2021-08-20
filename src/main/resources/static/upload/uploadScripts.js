let xhrFL = new XMLHttpRequest();
xhrFL.onreadystatechange = function()
{
    if (xhrFL.readyState === 4 && xhrFL.status === 200)
    {
        let jsonData = JSON.parse(xhrFL.responseText);
        console.log(jsonData)
        showFL(jsonData);
    }
};

xhrFL.open("GET", "/upload/files", true);
xhrFL.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
xhrFL.send();

function showFL(jsonData)
{
    let output = "";
    for (let i = 0; i < (jsonData.length); i++)
    {
        output += "<a href=files/" + jsonData[i] + " target=\"_blank\">\n" + jsonData[i] + "\n</a>";
        output += "<br>";
    }
    document.getElementById("files").innerHTML = output;
}