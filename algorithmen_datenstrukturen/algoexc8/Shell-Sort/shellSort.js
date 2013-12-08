function sort(array, first, last,h){
	var e = null;
	var j = 0;
	var n = last - first + 1;
	
	while( h > 0){
		for (var i = first; i < (last - h);i++){
			e = array[i + h];
			j = i;
			while( (j >= first) && (e < array[j])){
				array[j + h] = array[j];
				j = j - h;
			}
			array[j+h] = e;
		}
		h = h / 2;
	}
}
var sortArray = new Array();

for(i = 0;i < 100;i++){
	sortArray[i] = Math.floor((Math.random()*100)+1);
}

//TODO: Knuth, und weitere: http://de.wikipedia.org/wiki/Shellsort
var steps = new Array();
steps[0] = sortArray.length / 2; 
steps[1] = 2^sortArray.length;
steps[2] = (2^sortArray.length) - 1;
//steps[3] = 


for( i = 0; i <= steps.length - 1;i++){
	var arrayToSort = sortArray.slice();

	var myh = steps[i];
	var startTime = new Date().getTime();
	sort(arrayToSort,0,arrayToSort.length, myh);
	var timeDiff = (new Date().getTime() - startTime);
	
	console.log('h: ' + myh + ', Sorted Array in ' + timeDiff + ' Milisec., Array: ' + arrayToSort);
}

