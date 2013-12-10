function sort(array, first, last,funcH){
	var e = null;
	var j = 0;
	var n = last - first;
	var tmpH = funcWrapper(funcH,tmpH,n);
	
	while( tmpH > 0){
		for (var i = first; i < (last - tmpH);i++){
			e = array[i + tmpH]
			j = i;
			while( (j >= first) && (e < array[j])){
				array[j + tmpH] = array[j];
				j = j - tmpH;
			}
			array[j+tmpH] = e;
		}
		tmpH = funcWrapper(funcH,tmpH,tmpH);
	}
}

function funcWrapper(funcH, lastH,n){
	return Math.floor(funcH(lastH,n));
}

var testCount = 100;
var sortArray = new Array();

for(var i = 0;i < 100;i++){
	sortArray[i] = Math.floor((Math.random()*100)+1);
}

//TODO: Knuth, und weitere: http://de.wikipedia.org/wiki/Shellsort
var steps = new Array();
steps[0] = function(lastH, h){return (h / 2)}; 
steps[1] = function(lastH, h){return (2^h)}; 
steps[2] = function(lastH, h){return ((2^h) - 1)}; 
steps[3] = function(lastH, h){if(lastH == null){return 1}else{return (3*lastH + 1)}};
//steps[3] = 


for(var i = 0; i <= steps.length - 1;i++){
	var timetotal = 0;
	var avgTime = 0;
	
	console.log('\n-----\n');
	
	for(var x = 0; x < testCount;x++){
		var arrayToSort = sortArray.slice();

		var myh = steps[i];
		var startTime = new Date().getTime();
		sort(arrayToSort,0,arrayToSort.length, myh);
		var timeDiff = (new Date().getTime() - startTime);
		
		timetotal += timeDiff
		
		console.log('h: ' + myh + ', Sorted Array in ' + timeDiff + ' Milisec., Array: ' + arrayToSort);
	}
	
	avgTime = timetotal / testCount;
	
	console.log('\nh: ' + myh + ', Sorted Array in ' + avgTime + ' Milisec. (avg), Array: ' + arrayToSort);
}

