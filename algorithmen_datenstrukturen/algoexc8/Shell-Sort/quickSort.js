function quicksort(arr, first, last){
  var x, i, j;

  // console.log(arr, first, last);

  if (first < last) {
    // 1. Divide: split up array in two subarrays
    x = arr[Math.floor((first + last)/2)];
    i = first;
    j = last;

    do {
      // console.log('check x on: ' + x);

      // find first element greater or equal to x
      while (arr[i] < x) {
        i++;
      }

      // find last element, who is smaller then x
      while (arr[j] > x) {
      // while (x < arr[j]) {
        j--;
      }

      // console.log(i, j);

      // swap the two false-ordered elements
      if (i < j) {
        // swap

        /*
        console.log('x=' + x);
        console.log('i=' + i);
        console.log('j=' + j);
        console.log('arr[i]=' + arr[i]);
        console.log('arr[j]=' + arr[j]);
        */

        var temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
      }
      i++;
      j--;

    // } while (i <= j);
    } while (i < j);

    quicksort(arr, first, j);
    quicksort(arr, i, last);
  }
}

function getRandomArray(length) {
  var arr = new Array();

  for(var i = 0; i < length; i++){
    arr[i] = Math.floor((Math.random()*100)+1);
  }
  return arr;
}

var arr = getRandomArray(5);
// var arr = new Array(39, 73, 12, 23, 35);
// var arr = new Array(3, 55, 69, 12); // [85, 38, 97, 98]
// var arr = new Array(85, 38, 97, 98);
// var arr = new Array(75, 72, 53, 57, 84, 61, 6, 46, 63);

console.log(arr);

// sort array by quicksort
quicksort(arr, 0, arr.length-1);

console.log(arr);

/*
var countTests = 100;

var timetotal = 0;
var startTime, timeDiff, arrayToSort;

for (var n = 1; n <= countTests; n++) {
  arrayToSort = getRandomArray(10000);

	startTime = new Date().getTime();
	quicksort(arrayToSort, 0, arrayToSort.length-1);
	timeDiff = new Date().getTime() - startTime;

  timetotal += timeDiff;

	console.log('Sorted Array in ' + timeDiff + ' Milisec');
}

var timeavg = timetotal / countTests;
console.log('timeavg: ' + timeavg);
*/


/*
function quickSort(items, left, right) {

  var index;

  if (items.length > 1) {

    index = partition(items, left, right);

    if (left < index - 1) {
      quickSort(items, left, index - 1);
    }

    if (index < right) {
      quickSort(items, index, right);
    }

  }

  return items;
}

function swap(items, firstIndex, secondIndex){
  var temp = items[firstIndex];
  items[firstIndex] = items[secondIndex];
  items[secondIndex] = temp;
}

function partition(items, left, right) {

  var pivot   = items[Math.floor((right + left) / 2)],
    i       = left,
    j       = right;


  while (i <= j) {

    while (items[i] < pivot) {
      i++;
    }

    while (items[j] > pivot) {
      j--;
    }

    if (i <= j) {
      swap(items, i, j);
      i++;
      j--;
    }
  }

  return i;
}

// first call
var result = quickSort(arr, 0, arr.length - 1);
console.log(arr);
*/