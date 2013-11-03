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
  this.buildTreeNode = function(tree) {
    var htmlList = '<li></li>';

    if(tree !== null){

      htmlList = '<li>' + tree.data + '</li>';

      htmlList += this.buildTreeNode(tree.rightSubtree);
      htmlList += this.buildTreeNode(tree.leftSubtree);

    }
    // append new ul
    return '<ul>' + htmlList + '</ul>';
  }

  this.getElementCount = function() {
    return this.countSubtreeElements(this.rootNode);
  }

  this.countSubtreeElements = function(subtree) {
    var countElements = 0;
    var countDepths = -1; // only max node connection. amount of trees -1
    var maxCountDepths = 0;

    if (subtree !== null) {
      // increment one valid node
      countElements++;

      // increment depths (0/1)
      // countDepths++;

      // count both subtrees
      var countLeft = this.countSubtreeElements(subtree.leftSubtree);
      var countRight = this.countSubtreeElements(subtree.rightSubtree);

      // add all founded elements
      countElements += countLeft.countElements + countRight.countElements;

      // save the max depths
      // only count the greater value
      var countSubtreeDepths = countLeft.countDepths;
      if (countSubtreeDepths < countRight.countDepths) {
        countSubtreeDepths = countRight.countDepths;
      }
      countDepths = countSubtreeDepths + 1;
    }
    return {
      countElements: countElements,
      countDepths: countDepths,
      maxCountDepths: maxCountDepths
    };
  }

  this.exist = function(value) {
    return this.searchSubtree(this.rootNode, value);
  }

  this.searchSubtree = function(subtree, value) {
    var exists = false;
    // check not found, if subtree is null
    if (subtree == null) {
      console.log('end is found');
      return false;
    }

    // check continuing
    if (subtree.data !== value) {
      if (value < subtree.data){
        // if the value is smaller, search in left subtree
        exists = this.searchSubtree(subtree.leftSubtree, value);
      } else {
        // if the value is greater, search in right subtree
        exists = this.searchSubtree(subtree.rightSubtree, value);
      }
    } else {
      // value is found
      exists = true
    }
    return exists;
  }
}


// onload
$(function() {
  // global var, because direct access over js console
  tm = new treeManager();

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



  var counter = tm.getElementCount();
  console.log('Anzahl Elemente: ' + counter.countElements);
  console.log('Baum Tiefe: ' + counter.countDepths);


  console.log('Wert existiert: ' + tm.exist(1));

  tm.showTreeNode();
});
