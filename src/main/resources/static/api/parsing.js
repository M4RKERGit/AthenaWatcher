var xhrHW = new XMLHttpRequest();
var urlHW = "/api/hwinfo";

var xhrSYS = new XMLHttpRequest();
var urlSYS = "/api/servinfo";

xhrHW.onreadystatechange = function()
{
    if (xhrHW.readyState === 4 && xhrHW.status === 200)
    {
        var jsonData = JSON.parse(xhrHW.responseText);
        showHW(jsonData);
    }
};

xhrSYS.onreadystatechange = function()
{
    if (xhrSYS.readyState === 4 && xhrSYS.status === 200)
    {
        var jsonData = JSON.parse(xhrSYS.responseText);
        showSYS(jsonData);
    }
};

xhrHW.open("GET", urlHW, true);
xhrHW.send();

xhrSYS.open("GET", urlSYS, true);
xhrSYS.send();

function showHW(data)
{
    var output = "";

    output += "CPU Manufacturer: " + data.cpu.manufacturer + "<br>";
    output += "CPU Model: " + data.cpu.modelName + "<br>";
    output += "CPU Frequency: " + data.cpu.cpuFreq + "<br>";
    output += "CPU Cores: " + data.cpu.cores + "<br>";
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

    output += "Service name: " + data.serviceName + "<br>";
    output += data.loaded + "<br>";
    output += data.activity + "<br>";
    output += data.PID + "<br>";
    output += data.memory + "<br>";

    document.getElementById("serviceJSON").innerHTML = output;
}