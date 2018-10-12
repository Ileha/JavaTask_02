function GetNodeByParent(id_parent) {
    postToServer(JSON.stringify({
        cmd:"GetByParent", 
        data:{
            p_id:id_parent
        }
    }));
}

var ws = new WebSocket("ws://"+ "10.0.1.249:8080" +"/mywebapp/chat");
ws.onopen = function(){
    console.log("connect");
    GetNodeByParent(1);
};
ws.onmessage = function(message) {
    var arr = JSON.parse(message.data);
    if (arr.err != undefined) {
        console.log(arr.err.message);
    }
    else {
        CommandBank[arr.cmd].execute(arr.data);
    }
};
function postToServer(message) {
    ws.send(message);
}
function closeConnect() {
    ws.close();
}