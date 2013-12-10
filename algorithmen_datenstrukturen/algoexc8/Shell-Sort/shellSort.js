function sort(array, first, last, funcH){
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

/**
 *
 * @param array
 * @param first
 * @param last
 * @param hFactor factor to get next h (will be cut with math.floor())
 */
function shellsort(array, first, last, hFactor){
  var j, n, h, e;

  n = last- first + 1;
  h = calcStepH(n, hFactor);

  while (h > 0) {
    for (var i = first; i < (last - h); i++) {
      e = array[i + h];
      j = i;
      while (j >= first && e < array[j]) {
        array[j + h] = array[j];
        j = j - h;
      }
      array[j + h] = e;
    }
    h = calcStepH(h, hFactor);
  }
}

function calcStepH(oldStep, hFactor) {
  return Math.floor(oldStep * hFactor);
}


$(document).ready(function() {
  var countTests = 1;

  var timetotal = 0;
  var startTime, timeDiff, arrayToSort;

  for (var n = 1; n <= countTests; n++) {
    arrayToSort = getRandomArray(10);

    startTime = new Date().getTime();
    shellsort(arrayToSort, 0, arrayToSort.length-1, 0.5);
    timeDiff = new Date().getTime() - startTime;

    timetotal += timeDiff;

    console.log(arrayToSort);
    // console.log('Sorted Array in ' + timeDiff + ' Milisec');
  }

  var timeavg = timetotal / countTests;
  console.log('timeavg: ' + timeavg);


});

/*
var testCount = 1;
var sortArray = new Array();

for(var i = 0;i < 100;i++){
	sortArray[i] = Math.floor((Math.random()*100)+1);
}

//TODO: Knuth, und weitere: http://de.wikipedia.org/wiki/Shellsort
var steps = new Array();
steps[0] = function(lastH, h) {return (h / 2)};
steps[1] = function(lastH, h) {return (2^h)};
steps[2] = function(lastH, h) {return ((2^h) - 1)};
steps[3] = function(lastH, h) {if(lastH == null){return 1}else{return (3*lastH + 1)}};


for (var i = 0; i <= steps.length - 1;i++){
	var timetotal = 0;
	var avgTime = 0;
	
	console.log('\n-----\n');
	
	for(var x = 0; x < testCount;x++){
		var arrayToSort = sortArray.slice();

		var myh = steps[i];
		var startTime = new Date().getTime();
    shellsort(arrayToSort,0,arrayToSort.length, myh);
		var timeDiff = (new Date().getTime() - startTime);
		
		timetotal += timeDiff
		
		console.log('h: ' + myh + ', Sorted Array in ' + timeDiff + ' Milisec., Array: ' + arrayToSort);
	}
	
	avgTime = timetotal / testCount;
	
	console.log('\nh: ' + myh + ', Sorted Array in ' + avgTime + ' Milisec. (avg), Array: ' + arrayToSort);
}
*/