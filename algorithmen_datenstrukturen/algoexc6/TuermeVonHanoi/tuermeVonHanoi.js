
function solveTowersOfHanoi(from, to, n){
	if( n  == 0){
		return "";
	}else{
		var tmpStack = 'B';
		
		if(from.indexOf('A') == -1 && to.indexOf('A') == -1){
			tmpStack = 'A';
		}else if (from.indexOf('C') == -1 && to.indexOf('C') == -1){
			tmpStack = 'C';
		}
		
		return solveTowersOfHanoi(from,tmpStack,(n-1)) + " " + from + to + " " + solveTowersOfHanoi(tmpStack,to,(n-1));
	}
}


console.log(solveTowersOfHanoi('A','C',3));