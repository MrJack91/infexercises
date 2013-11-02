function subtree() {
  this.leftSubtree = null;
  this.rightSubtree = null;

  this.data = null;
}

function treeManager() {
  this.addNode = function (data) {
		if(rootNode == null){
			rootNode = new subtree();
			rootNode.data = data;
		}else{
			preNode = null;
			subTree = rootNode;
			
			while(subTree != null){
				preNode = subTree;
				if(data < subTree.data){
					subTree = subTree.leftSubtree;
				}else{
					subTree = subTree.rightSubtree;
				}
			}
			
			node = new subtree();
			node.data = data;
			
			if(data < preNode.data){
				preNode.leftSubtree = node;
			}else{
				preNode.rightSubtree = node;
			}
			
		}
	}
}

function displayTreeNode(tree){
	if(tree != null){
		//TODO: Print value: tree.data
		displayTreeNode(tree.leftSubtree);
		displayTreeNode(tree.rightSubtree);
	}
}

console.log('hi');

var rootNode = null;
var trMgm = null;

trMgm = new treeManager();

trMgm.addNode(5);
trMgm.addNode(3);
trMgm.addNode(8);
console.log(rootNode);
console.log(rootNode.leftSubtree);
console.log(rootNode.rightSubtree);
console.log(rootNode.data);

displayTreeNode(rootNode);