var stateenum = Object.freeze(
    {
        open: 1,
        close: 2,
        loading: 3
    }
);
var highlight = undefined;

dom_node[1].ondblclick = function(event) {
    var target = event.target;
    if (target.root_id === undefined) return;
    if (!IsLeaf(target)) {
        if (!target.load) {
            SetFolderState(stateenum.loading, target);
            GetNodeByParent(target.root_id);
            setTimeout(DBlOpenNode, 2000, target);
        }
        else {
            DBlOpenNode(target);
        }
    }
};

document.onclick = function(event) {
    var target = event.target;
    TrashHighlight();
    if (target.root_id === undefined) return;
    SetHighlight(target);
};

function TrashHighlight() {
    if (highlight === undefined) { return; }
    highlight.classList.remove("selected");
}
function SetHighlight(target) {
    highlight = target.text;
    highlight.classList.add("selected");
}

function IsLeaf(target) {
    return (target.t === "f");
}
function DBlOpenNode(target) {
    target.contain.style.display = target.contain.style.display ? '' : 'none';
    if (!target.contain.style.display) {
        SetFolderState(stateenum.open, target);
    }
    else{
        SetFolderState(stateenum.close, target);
    }
}
function SetFolderState(state, target) {    
    target.classList.toggle("node_open", 1===state);
    target.classList.toggle("node_icon", 2===state);
    target.classList.toggle("node_load", 3===state);
}

//function OpenLeaf(target) {
//    target.classList.toggle("selected");
//}