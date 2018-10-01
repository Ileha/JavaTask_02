var ul = document.getElementById("main_ul");

ul.ondblclick = function(event) {
    var target = event.target;
    if (target.tagName != "LI") return;
    if (IsLeaf(target)) {
//        DBlOpenLeaf(target);
    }
    else {
        DBlOpenNode(target);
    }     
}
ul.onclick = function(event) {
   var target = event.target;
    if (target.tagName != "LI") return;
    if (IsLeaf(target)) {
        OpenLeaf(target);
    }
    else {
        
    }
}

function IsLeaf(target) {
    var node = target.childElementCount;
    if (node == 0) return true; // нет детей
    else { return false; }
}
function DBlOpenNode(target) {
    var node = target.getElementsByTagName('ul')[0];
    node.style.display = node.style.display ? '' : 'none';
}
function OpenLeaf(target) {
    target.classList.toggle("selected");
}