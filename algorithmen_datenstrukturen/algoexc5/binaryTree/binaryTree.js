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
    var htmlList = '<li>-</li>';

    if(tree !== null){

      htmlList = '<li>' + tree.data + '</li>';

      // get only empty nodes, if there an setted node on same dephts
      if (tree.rightSubtree !== null || tree.leftSubtree !== null) {
        htmlList += this.buildTreeNode(tree.rightSubtree);
        htmlList += this.buildTreeNode(tree.leftSubtree);
      }

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
    if (this.search(this.rootNode, value) !== null) {
      return true
    }
    return false;
  }

  /**
* search a subtree by value
* @param subtree
* @param value
* @returns {object} founded subtree, parent (null if not found)
*/
  this.search = function(subtree, value, parent) {
    var foundSubtree = null;
    // check not found, if subtree is null
    if (subtree == null) {
      return foundSubtree;
    }

    // check continuing
    if (subtree.data !== value) {
      if (value < subtree.data){
        // if the value is smaller, search in left subtree
        foundSubtree = this.search(subtree.leftSubtree, value, subtree);
      } else {
        // if the value is greater, search in right subtree
        foundSubtree = this.search(subtree.rightSubtree, value, subtree);
      }
    } else {
      // value is found
      foundSubtree = {
        subtree: subtree,
        parent: parent
      }
    }
    return foundSubtree;
  }

  /**
* deletes a node by value
* @param value
* @returns {boolean} delete was successfully
*/
  this.delete = function(value) {
    var search = this.search(this.rootNode, value);

    if (search !== null) {
      // node was found - there are 3 cases.

      // case 1: subtree has no children
      if (search.subtree.leftSubtree == null && search.subtree.rightSubtree == null) {

        console.log(search.parent);
        console.log(search.parent.leftSubtree == search.subtree);

        // reset parent node link
        if (search.parent.leftSubtree == search.subtree) {
          // reset left subtree
          search.parent.leftSubtree = null;
        } else {
          // reset right subtree
          search.parent.rightSubtree = null;
        }
        search.subtree = undefined;

        console.log('node with one child deleted.');

        // update tree view
        this.showTreeNode();
        return true;
      }

      // case 3: subtree had 2 children
      if (search.subtree.leftSubtree !== null && search.subtree.rightSubtree !== null) {
        // set parent subtree on my next child
        // search next higher value. 1 times right; n times left
        var nextVal = search.subtree.rightSubtree;
        var lastVal = search.subtree;
        while (nextVal.leftSubtree !== null) {
          lastVal = nextVal;
          nextVal = nextVal.leftSubtree;
        }

        // make a (deep) copy of nextVal
        var nextValOrig = jQuery.extend(true, {}, nextVal); // is never changing
        var nextValOrig2 = jQuery.extend(true, {}, nextVal); // get new replace of deleted node

        // delete next val (Important via lastVal, in other case, reference to null doesn't work)
        lastVal.leftSubtree = null;

        // delete moved node
        if (lastVal.leftSubtree == nextValOrig) {
          lastVal.leftSubtree = nextValOrig.rightSubtree;
        } else {
          lastVal.rightSubtree = nextValOrig.rightSubtree;
        }

        // set new links to new nextVal
        nextValOrig2.leftSubtree = search.subtree.leftSubtree;
        nextValOrig2.rightSubtree = search.subtree.rightSubtree;

        // replace old node with new found node
        search.subtree = nextValOrig2;

        this.showTreeNode();
        return true;
      }


      // case 2 (else / default): subtree had 1 child
      if (search.subtree.leftSubtree !== null && search.subtree.rightSubtree == null) {
        // if only left subtree is setted
        if (search.parent.leftSubtree == search.subtree) {
          // set parent left subtree to the only left child
          search.parent.leftSubtree = search.subtree.leftSubtree;
        } else {
          // set parent right subtree to the only left child
          search.parent.rightSubtree = search.subtree.leftSubtree;
        }
      } else {
        // then must be only right setted
        if (search.parent.leftSubtree == search.subtree) {
          // set parent left subtree to the only left child
          search.parent.leftSubtree = search.subtree.rightSubtree;
        } else {
          // set parent right subtree to the only left child
          search.parent.rightSubtree = search.subtree.rightSubtree;
        }
      }

      search.subtree = undefined;

      this.showTreeNode();
      return true;

    } else {
      // node wasn't found
      return false;
    }
  }

  this.addNodes = function(nodes) {
    nodes.forEach(function (value, index, ar){
      tm.addNode(value);
    });
    this.showTreeNode();
  }
  
  this.isTreeBalanced = function(){
	return this.needsBalancing(this.rootNode);
  }
  
  this.needsBalancing = function(tree){
	if(tree !== null){
		var diff = this.countSubtreeElements(tree.leftSubtree).countDepths - this.countSubtreeElements(tree.rightSubtree).countDepths;
		
		if(diff > 1 || diff < -1){
			return true;
		}else{
			if( this.needsBalancing(tree.leftSubtree) || this.needsBalancing(tree.rightSubtree)){
				return true;
			}else{
				return false;
			}
		}
	}else{
		return false;
	}
  }
  
  this.balanceTree = function(){
	this.rootNode = this.balanceTreeNode(this.rootNode);
  }
  
  this.getNormalizedNodeDepth = function(node){
	var depth = this.countSubtreeElements(node).countDepths;
	
	return depth + 1;
  }
  
  this.balanceTreeNode = function(node){
	if(!(node == null) || (node != null && node.leftSubtree == null && node.rightSubtree == null)){
		node.leftSubtree = this.balanceTreeNode(node.leftSubtree);
		node.rightSubtree = this.balanceTreeNode(node.rightSubtree);
		
		var leftSize = this.getNormalizedNodeDepth(node.leftSubtree);
		var rightSize = this.getNormalizedNodeDepth(node.rightSubtree);

		while ((leftSize - rightSize) > 1 || (leftSize - rightSize) < -1){
			//If tree is left heavy
			if((leftSize - rightSize) > 1){
				var treeSubLeftSize = (node.leftSubtree == null ? 0 : this.getNormalizedNodeDepth(node.leftSubtree.leftSubtree));
				var treeSubRightSize = (node.leftSubtree == null ? 0: this.getNormalizedNodeDepth(node.leftSubtree.rightSubtree));
				
				if((treeSubRightSize - treeSubLeftSize) > 0){
					node.leftSubtree = this.rotateLeft(node.leftSubtree);
					node = this.rotateRight(node);
					console.log(node.data);
				}else{
					node = this.rotateRight(node);
				}
			//If tree is right heavy
			}else if((rightSize - leftSize) > 1){
				var treeSubLeftSize = (node.rightSubtree == null ? 0 : this.getNormalizedNodeDepth(node.rightSubtree.leftSubtree));
				var treeSubRightSize = (node.rightSubtree == null ? 0 :  this.getNormalizedNodeDepth(node.rightSubtree.rightSubtree));
				if((treeSubLeftSize - treeSubRightSize) > 0){
					node.rightSubtree = this.rotateRight(node.rightSubtree);
					node = this.rotateLeft(node);
					console.log(node.data);
				}else{
						node = this.rotateLeft(node);
				}
			}
			
			leftSize = (node == null ? 0 : this.getNormalizedNodeDepth(node.leftSubtree));
			rightSize = (node == null ? 0 : this.getNormalizedNodeDepth(node.rightSubtree));
		}
	}
	
	return node;
  }
  
  this.rotateLeft = function(node){
	if(node !== null && node.rightSubtree !== null){
		var tmpNode = node.rightSubtree;
		
		node.rightSubtree = tmpNode.leftSubtree;
		tmpNode.leftSubtree = node;
		
		return tmpNode;
	}
	return null;
  }
  
  this.rotateRight = function(node){
	if(node !== null && node.leftSubtree !== null){
		var tmpNode = node.leftSubtree;
		
		node.leftSubtree = tmpNode.rightSubtree;
		tmpNode.rightSubtree = node;
		
		return tmpNode;
	}
	return null;
  }
}


// onload
$(function() {
  // global var, because direct access over js console
  tm = new treeManager();
	tm.addNode(3);
	tm.addNode(1);
	tm.addNode(8);
	tm.addNode(10);
	tm.addNode(2); 
	tm.addNode(4); 
	tm.addNode(6); 
	tm.addNode(7); 
	tm.addNode(9); 
	tm.addNode(5); 


  /*tm.addNode(5);
  tm.addNode(3);
  tm.addNode(8);
  tm.addNode(11);
  tm.addNode(4);

  tm.addNode(2);
  tm.addNode(1);
  tm.addNode(14);
  
  tm.addNode(7);
  tm.addNode(13);

  tm.addNode(10);

  tm.addNode(12);
  tm.addNode(20);
  tm.addNode(-1);
  tm.addNode(25);
  tm.addNode(-3);
  tm.addNode(22);
  tm.addNode(19);*/
 

  var counter = tm.getElementCount();
  console.log('Anzahl Elemente: ' + counter.countElements);
  console.log('Baum Tiefe: ' + counter.countDepths);


  console.log('Wert existiert: ' + tm.exist(1));
  console.log('Ausbalanciert: ' + tm.isTreeBalanced());
  //Balancing: http://me-lrt.de/08-avl-baum-java-selbstbalanciert
  //http://www.uni-bonn.de/~ochsendo/data/Men.AVL.pdf
  tm.balanceTree();
  tm.showTreeNode();
});