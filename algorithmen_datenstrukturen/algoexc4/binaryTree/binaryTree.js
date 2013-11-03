function subtree() {
  this.leftSubtree = null;
  this.rightSubtree = null;

  this.data = null;
}

function treeManager() {

  // the root node itself
  this.rootNode = null;

  /**
   * add a node to the tree
   * @param data
   */
  this.addNode = function (data) {
		if (this.rootNode == null) {
      this.rootNode = new subtree();
      this.rootNode.data = data;
		}else{
			var preNode = null;
			var subTree = this.rootNode;
			
			while(subTree !== null){
				preNode = subTree;
				if(data < subTree.data){
					subTree = subTree.leftSubtree;
				}else{
					subTree = subTree.rightSubtree;
				}
			}
			
			var node = new subtree();
			node.data = data;
			
			if(data < preNode.data){
				preNode.leftSubtree = node;
			}else{
				preNode.rightSubtree = node;
			}
			
		}
	}

  /**
   * print the tree into html element with id treeContainer
   * @param tree
   */
  this.showTreeNode = function(){
    $('#treeContainer').html(this.buildTreeNode(this.rootNode));
  }

  /**
   * Builds the tree in html
   * @param tree optional can be a subtree
   * @returns {string} html
   */
  this.buildTreeNode = function(tree){
    var htmlList = '';

    if(tree !== null){

      htmlList = '<li>' + tree.data + '</li>';

      // first print right (90 degress)
      htmlList += this.buildTreeNode(tree.rightSubtree);
      htmlList += this.buildTreeNode(tree.leftSubtree);
    }
    // append new ul
    return '<ul>' + htmlList + '</ul>';
  }
}


// onload
$(function() {
  var tm = new treeManager();

  tm.addNode(5);
  tm.addNode(3);
  tm.addNode(8);


  tm.addNode(2);
  tm.addNode(1);
  tm.addNode(4);
  tm.addNode(7);
  tm.addNode(5);

  tm.addNode(3);

  tm.addNode(8);
  tm.addNode(8);
  tm.addNode(8);
  tm.addNode(8);
  tm.addNode(8);
  tm.addNode(8);

  /*
  console.log(rootNode);
  console.log(rootNode.leftSubtree);
  console.log(rootNode.rightSubtree);
  console.log(rootNode.data);
  */

  tm.showTreeNode();
});
