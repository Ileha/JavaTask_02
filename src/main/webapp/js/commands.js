var CommandBank = {};
var dom_node = {};

function addNode(node, id, name, type) {
    var obj = document.createElement('li');
    var span = document.createElement('span');
    obj.text = span;
    obj.appendChild(span);
    dom_node[id] = obj;
    obj.root_id = id;
    obj.t = type;
    if (obj.t == "f") {
        obj.classList.add('item_icon');
    }
    else {
        obj.classList.add('node_icon');
        obj.contain = document.createElement('ul');
        obj.contain.style.display = 'none';
        obj.appendChild(obj.contain);
    }
    obj.text.innerHTML = name;
    obj.load = false;
    node.contain.appendChild(obj);
}
function addNodeContainer(contain, id, name) {
    addNode(contain, id, name, "d");
}

function addNodeEnd(contain, id, name) {
    addNode(contain, id, name, "f");
}

CommandBank["Delete"] = {
    /*
        {
            cmd:"Delete",
            data:{
                id
            }
        }
    */
    execute: function(input) {
        if (dom_node[input.id] !== undefined) {
            this.disposeRec(dom_node[input.id]);
        }
    },
    
    disposeRec: function(input) {
        if (input.load == true && input.t == "d") {
            for (var i = 0; i < input.contain.children.length; i++) {
                this.disposeRec(input.contain.children[i]);
            }
        }
        input.parentElement.removeChild(input);
        delete(dom_node[input.root_id]);
    }
};
CommandBank["InsertContainer"] = {
    /*
    {
        cmd:"InsertEnd", 
        data:{
                p_id,
                name,
                new_id
            }
    }
    */
    execute: function(input) {
        if (dom_node[input.p_id] != undefined && dom_node[input.p_id].load === true) {
            addNodeContainer(dom_node[input.p_id], input.new_id, input.name);
        }
    }
};
CommandBank["InsertEnd"] = {
    execute: function(input) {
        if (dom_node[input.p_id] != undefined && dom_node[input.p_id].load === true) {
            addNodeEnd(dom_node[input.p_id], input.new_id, input.name);
        }
    }
};
CommandBank["MoveTo"] = {
    /*
    {
        cmd:"MoveTo",
        data:{
            id,
            name,
            type,
            p_id
        }
    }
     */
    execute: function(input) {
        var what_move;
        if (dom_node[input.id] != undefined) {
            what_move = dom_node[input.id]; 
        }
        
        if (dom_node[input.p_id] != undefined && dom_node[input.p_id].load === true) {
            if (what_move != undefined) {
                what_move.parentElement.removeChild(what_move);
                dom_node[input.p_id].contain.appendChild(what_move);
            }
            else {
                addNode(dom_node[input.p_id], input.id, input.name, input.type);
            }
        }
        else {
            if (what_move != undefined) {
                CommandBank["Delete"].execute({id:what_move.root_id})
            }
        }
        
        
//        if (dom_node[input.p_id] != undefined && dom_node[input.p_id].load === true) {
//            what_move = dom_node[input.id];
//            dom_node[input.id].parentElement.removeChild(input);
//        }
//        else if(dom_node[input.id] != undefined) {
//            
//        }
//        
//        if (dom_node[input.id] != undefined) {
//           
//        }
            
        
    }
};
CommandBank["Rename"] = {
    /*
    {
        cmd:"Rename",
        data:{
            name="name", 
            id=arg1
        }
    }
    */
    execute: function(input) {
        if (dom_node[input.id] != undefined) {
            dom_node[input.id].text.innerHTML = input.name;
        }
    }
};
CommandBank["SetChild"] = {
    /*
    {
        "data":{
            "parent":1,
            "array":[
                {"name":"dir_1","id":2,"type":"d"},
                {"name":"file_1","id":5,"type":"f"}
            ]
        },
        "cmd":"SetChild"
    }
    */
    execute: function(input) {
        var parent = dom_node[input.parent];
        input.array.forEach(function(element) {
            addNode(parent, element.id, element.name, element.type);
        });
        parent.load = true;
    }
};

dom_node[1] = document.getElementById("main_panel");
dom_node[1].root_id = 1;
dom_node[1].t = "d";
dom_node[1].load = false;
dom_node[1].contain = dom_node[1].children[0];