var menu_node = document.getElementById("meny_node");
var menu_end = document.getElementById("meny_end");
var current = undefined;
var result_cut = undefined;

var pop_up_window = document.getElementById("pop_up_window");
pop_up_window.text = document.getElementById("window_text");
pop_up_window.button_ok = document.getElementById("window_OK");
pop_up_window.button_cancel = document.getElementById("window_Cancel");
pop_up_window.enter_data = document.getElementById("window_data");
pop_up_window.swich = function () {
    pop_up_window.style.display = pop_up_window.style.display ? '' : 'none';
    pop_up_window.enter_data.value = "";
};

document.addEventListener('contextmenu', onContextMenu, false);

var choose=undefined;

function showMenu(x, y, menu){
    menu.style.left = x + 'px';
    menu.style.top = y + 'px';
    menu.classList.add('show-meny');
}

function hideMenu(menu){
    menu.classList.remove('show-meny');
}

function onContextMenu(e){
    if (current != undefined) {
        hideMenu(current); 
        current = undefined;
    }
    var disable = false;
    TrashHighlight();
    if(e.target.root_id == undefined) {
        choose = dom_node[1];  
        disable = true;
    }
    else {
        choose = e.target;
        SetHighlight(choose);
    }
    
    var elements = document.querySelectorAll('.menu>ul>li>a.ondisabled');
    for (var i = 0; i < elements.length; i++) {
        elements[i].classList.toggle("disabled", disable);
    }
    elements = document.querySelectorAll('.menu>ul>li>a.onempty');
    for (var i = 0; i < elements.length; i++) {
        elements[i].classList.toggle("disabled", result_cut==undefined);
    }
    
    if (choose.t == "d") { current = menu_node; }
    else { current = menu_end; }
    e.preventDefault();
    showMenu(e.pageX, e.pageY, current);
    document.addEventListener('mousedown', onMouseDown, false);
}

function onMouseDown(e) {
    //console.log(e.target.root_id);
    if (e.target.onclick != undefined) { e.target.onclick(); };
    hideMenu(current);
    document.removeEventListener('mousedown', onMouseDown);
}

function rename() {
    //{cmd:"Rename",data:{name, id}}
    var obj = document.createElement('input');
    obj.value = choose.text.innerText;
    var first_test =  obj.value;
    obj.type = "text";
    
    button_ok = document.createElement('button');
    button_ok.innerText = "OK";
    button_ok.onclick = function () {
        postToServer(JSON.stringify({
            cmd:"Rename", 
            data:{
                name:obj.value,
                id:choose.root_id
            }
        }));
        choose.text.removeChild(obj);
        choose.text.removeChild(button_ok);
        choose.text.removeChild(button_cancel);
        choose.text.innerText = first_test;
        choose.text.removeAttribute("reduct");
    };
    button_cancel = document.createElement('button');
    button_cancel.innerText = "Cancel";
    button_cancel.onclick = function () {
        choose.text.removeChild(obj);
        choose.text.removeChild(button_ok);
        choose.text.removeChild(button_cancel);
        choose.text.innerText = first_test;
        choose.text.removeAttribute("reduct");
        
    };
    choose.text.setAttribute("reduct", ""); 
    choose.text.innerHTML = "";
    choose.text.appendChild(obj);
    choose.text.appendChild(button_ok);
    choose.text.appendChild(button_cancel);
}

function menuAddNodeEnd() {
    //{cmd:"InsertEnd", data:{p_id,name}}
    pop_up_window.text.innerHTML = "enter file name";
    pop_up_window.button_ok.onclick = function () {
        postToServer(JSON.stringify({
            cmd:"InsertEnd", 
            data:{
                p_id:choose.root_id,
                name:pop_up_window.enter_data.value
            }
        }));
        pop_up_window.swich();
    };
    pop_up_window.button_cancel.onclick = function () {
        pop_up_window.swich();
    };
    pop_up_window.swich();
}

function menuAddNode() {
    //{cmd:"InsertContainer", data:{p_id,name}}
    pop_up_window.text.innerHTML = "enter folder name";
    pop_up_window.button_ok.onclick = function () {
        postToServer(JSON.stringify({
            cmd:"InsertContainer", 
            data:{
                p_id:choose.root_id,
                name:pop_up_window.enter_data.value
            }
        }));
        pop_up_window.swich();
    };
    pop_up_window.button_cancel.onclick = function () {
        pop_up_window.swich();
    };
    pop_up_window.swich();
}

function DeleteNode() {
    //{cmd:"Delete",data:{id}}
    postToServer(JSON.stringify({
        cmd:"Delete", 
        data:{
            id:choose.root_id
        }
    }));
}

function CutNode() {
    result_cut = choose;
}
function MoveNode() {
    //{cmd:"MoveTo",data:{id, p_id}}
    postToServer(JSON.stringify({
        cmd:"MoveTo", 
        data:{
            id:result_cut.root_id,
            p_id: choose.root_id
        }
    }));
    result_cut = undefined;
}