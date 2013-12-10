$(document).ready(function() {
  var countTests = 5;

  var timetotal = 0;
  var startTime, timeDiff, arrayToSort;

  for (var n = 1; n <= countTests; n++) {
    arrayToSort = getRandomArray(10);

    startTime = new Date().getTime();
    quicksort(arrayToSort, 0, arrayToSort.length-1, false);
    timeDiff = new Date().getTime() - startTime;

    timetotal += timeDiff;

    // console.log(arrayToSort);
    console.log('Sorted Array in ' + timeDiff + ' Milisec');
  }

  var timeavg = timetotal / countTests;
  console.log('timeavg: ' + timeavg);
});

function quicksort(arr, first, last, debug){
  var x, i, j;

  if (debug) {
    console.log(arr, first, last);
  }

  if (first < last) {
    // 1. Divide: split up array in two subarrays
    x = arr[Math.floor((first + last) / 2)];
    i = first;
    j = last;

    do {
      if (debug) {
        console.log('check x on: ' + x);
      }

      // find first element greater or equal to x
      while (arr[i] < x) {
        i++;
      }

      // find last element, who is smaller then x
      while (arr[j] > x) {
        // while (x < arr[j]) {
        j--;
      }

      if (debug) {
        console.log(i, j);
      }

      // swap the two false-ordered elements
      if (i <= j) {

        if (debug) {
          console.log('x=' + x);
          console.log('i=' + i);
          console.log('j=' + j);
          console.log('arr[i]=' + arr[i]);
          console.log('arr[j]=' + arr[j]);
        }

        // swap
        var temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

        i++;
        j--;
      }

      // } while (i <= j);
    } while (i < j);

    quicksort(arr, first, j, debug);
    quicksort(arr, i, last, debug);
  }
}