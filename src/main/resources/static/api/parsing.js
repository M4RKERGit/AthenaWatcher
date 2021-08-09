var xhrHW = new XMLHttpRequest();
var urlHW = "/api/hwinfo";
xhrHW.onreadystatechange = function()
{
    if (xhrHW.readyState === 4 && xhrHW.status === 200)
    {
        var jsonData = JSON.parse(xhrHW.responseText);
        showHW(jsonData);
    }
};
xhrHW.open("GET", urlHW, true);
xhrHW.send();



var xhrSYS = new XMLHttpRequest();
var urlSYS = "/api/servinfo";
xhrSYS.onreadystatechange = function()
{
    if (xhrSYS.readyState === 4 && xhrSYS.status === 200)
    {
        var jsonData = JSON.parse(xhrSYS.responseText);
        showSYS(jsonData);
    }
};
xhrSYS.open("GET", urlSYS, true);
xhrSYS.send();

function showHW(data)
{
    var output = "";

    output += "CPU Manufacturer: " + data.cpu.manufacturer + "<br>";
    output += "CPU Model: " + data.cpu.modelName + "<br>";
    output += "CPU Frequency: " + data.cpu.cpuFreq + "<br>";
    output += "CPU Cores (Logical): " + data.cpu.cores + "<br>";
    output += "FPU Tech: " + data.cpu.FPU + "<br>";
    output += "CPU Temp: " + data.cpu.curTemp + "<br>";
    output += "CPU Critical Temp: " + data.cpu.critTemp + "<br>";
    document.getElementById("cpuJSON").innerHTML = output;

    var output = "";

    output += "GPU Description: " + data.gpu.description + "<br>";
    output += "GPU Model: " + data.gpu.product + "<br>";
    output += "GPU Vendor: " + data.gpu.vendor + "<br>";
    output += "GPU Clock: " + data.gpu.clock + "<br>";
    output += "GPU Temp: " + data.gpu.curTemp + "<br>";
    output += "GPU Critical Temp: " + data.gpu.critTemp + "<br>";
    output += "GPU Consumption: " + data.gpu.power + "<br>";
    document.getElementById("gpuJSON").innerHTML = output;

    var output = "";

    output += "RAM Total: " + data.memory.ram.total + "<br>";
    output += "RAM Used: " + data.memory.ram.used + "<br>";
    output += "RAM Free: " + data.memory.ram.free + "<br>";
    output += "RAM Shared: " + data.memory.ram.shared + "<br>";
    output += "RAM Cache: " + data.memory.ram.cache + "<br>";
    output += "RAM Available: " + data.memory.ram.available + "<br>";
    document.getElementById("memoryJSON").innerHTML = output;
}

function showSYS(data)
{
    var output = "";

    output += "Service name: " + data["serviceList"][0].serviceName + "<br>";
    if (data["serviceList"][0].defined === false) output += "Service undefined<br>";
    else
    {
        output += data["serviceList"][0].loaded + "<br>";
        output += data["serviceList"][0].activity + "<br>";
        output += data["serviceList"][0].PID + "<br>";
        output += data["serviceList"][0].memory + "<br>";
    }
    document.getElementById("fserviceJSON").innerHTML = output;
    document.getElementById("fserviceLOG").innerHTML = data["serviceList"][0].log;

    var output = "";

    output += "Service name: " + data["serviceList"][1].serviceName + "<br>";
    if (data["serviceList"][1].defined === false) output += "Service undefined<br>";
    else
    {
        output += data["serviceList"][1].loaded + "<br>";
        output += data["serviceList"][1].activity + "<br>";
        output += data["serviceList"][1].PID + "<br>";
        output += data["serviceList"][1].memory + "<br>";
    }
    document.getElementById("sserviceJSON").innerHTML = output;
    document.getElementById("sserviceLOG").innerHTML = data["serviceList"][1].log;

    var output = "";

    output += "Service name: " + data["serviceList"][2].serviceName + "<br>";
    if (data["serviceList"][2].defined === false) output += "Service undefined<br>";
    else
    {
        output += data["serviceList"][2].loaded + "<br>";
        output += data["serviceList"][2].activity + "<br>";
        output += data["serviceList"][2].PID + "<br>";
        output += data["serviceList"][2].memory + "<br>";
    }
    document.getElementById("tserviceJSON").innerHTML = output;
    document.getElementById("tserviceLOG").innerHTML = data["serviceList"][2].log;
}

function sendServiceCommand(number, command)
{
    var xhr = new XMLHttpRequest();
    var servName;
    switch (number)
    {
        case 1:
            servName = document.getElementById("fserviceJSON").innerHTML.split(' ')[2].split("<br>")[0];
            console.log(servName);
            break;
        case 2:
            servName = document.getElementById("sserviceJSON").innerHTML.split(' ')[2].split("<br>")[0];
            console.log(servName);
            break;
        case 3:
            servName = document.getElementById("tserviceJSON").innerHTML.split(' ')[2].split("<br>")[0];
            console.log(servName);
            break;
    }
    var toSend = servName + ' ' + command;
    xhr.open('POST', '', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.send(toSend);
}