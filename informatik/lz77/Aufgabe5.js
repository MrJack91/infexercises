

var lz77 = {

  PREVIEW_BUFFER_SIZE: 16,

  //TODO build workwindows size
  WORK_WINDOW_SIZE: 16,

  compress: function (inputText) {
    var workText = inputText;
    var workWindow = '';
    var previewBuffer = '';

    console.log('workText: ' + workText);

    var finishedCompression = false;

    //Remove!! only for debug!!
    var count = 0;

    // console.log("Starting compression...");
    while (!finishedCompression) {
      console.log("...");

      //Check if there is input text left
      if (workText.length > 0 || previewBuffer.length > 0) {
        // console.log("Process Text...");
        //Calculate number of bits to transfer to previewBuffer
        var prevBuffTransferCount = lz77.PREVIEW_BUFFER_SIZE - previewBuffer.length + 1;

        previewBuffer = previewBuffer + workText.substring(0, prevBuffTransferCount);
        workText = workText.substring(prevBuffTransferCount, workText.length);
        // console.log("Buffer: " + previewBuffer);
        // console.log("Left Text: " + workText);

        //Still work to do....
        if (previewBuffer.length > 0) {
          var foundMatch = false;

          var prevBufIndex = 0;

          var lastPos = -1;
          var lastLength = -1;

          var currPos = -1;
          var currLength = -1;

          //Iterate over workWindow
          var iw = 0;
          for (iw = 0; iw < workWindow.length; iw++) {
            if (workWindow.charAt(iw) == previewBuffer.charAt(prevBufIndex)) {
              foundMatch = true;

              currPos = iw;
              currLength = 1;
              //First character matches...

              //Are there any more?
              //Keep in boundary
              var max = previewBuffer.length;

              if (workWindow.length < max) {
                max = workWindow.length;
              }

              var x;
              for (x = 1; x < max; x++) {
                if (workWindow.charAt(iw + x) == previewBuffer.charAt(prevBufIndex + x)) {
                  currLength++;
                } else {
                  break;
                }
              }

              var newCurrLength = currLength;
              // check if there any repeat in the previewbuffer (goes over the workwindow)
              if (x == max) {
                // search repeat of pattern (must be at the end of the workwindow)
                for (var n = 0; n < previewBuffer.length; n++) {
                  // compare workwindow and previewpuffer
                  if (workWindow.charAt(currPos + (n%currLength)) == previewBuffer.charAt(n)) {
                    newCurrLength++;
                  } else {
                    break;
                  }
                }
              }

              if (newCurrLength >= lastLength) {
                lastLength = newCurrLength;
                lastPos = currPos;
              }
            }
          }

          console.log('lastLength: ' + lastLength);
          console.log('lastPos: ' + lastPos);

          console.log('previewBuffer: ' + previewBuffer);

          if (foundMatch) {
            //only for print
            var pos = workWindow.length - lastPos;
            var len = lastLength;

            // handle overcut (repeat over workwindow)
            var posNextChar;
            if (len > pos) {
              posNextChar = len-pos;
            } else {
              posNextChar = len;
            }
            var nextChar = previewBuffer.charAt(posNextChar);

            // special case at the end, no next char available
            if (nextChar.length == 0) {
              nextChar = '-';
            }

            this.printTripel(pos, len, nextChar);

            // add text to workwindow
            workWindow += previewBuffer.substring(0, lastLength);

            // recalc previewbuffer
            previewBuffer = previewBuffer.substring(lastLength, previewBuffer.length);
          } else {
            this.printTripel(0, 0, previewBuffer.charAt(0));

            // add text to workwindow
            workWindow += previewBuffer.charAt(0);

            previewBuffer = previewBuffer.substring(1, previewBuffer.length);
          }


          console.log('workWindow: ' + workWindow);
        }
      } else {
        // console.log("Finished compression...");
        finishedCompression = true;
      }

      count++;
      if (count > 100) {
        break;
      }
    }

    return null;
  },

  decompress: function (compressedText) {
	
  },

  parseTripel: function(text){
	var tripelText = text;
	
	if(tripelText.charAt(0) == '<'){
		tripelText = tripelText.substring(1,tripelText.length);
	}
	
	if(tripelText.charAt(tripelText.length - 1) == '>'){
	tripelText = tripelText.substring(0,tripelText.length - 2);
	}
	
	var split = tripelText.split(",");
	
	if(split.length == 3){
		var pos = split[0].trim();
		var length = split[1].trim();
		var nextChar = split[2].trim();
		
		return new Tripel(pos,length,nextChar);
	}
	
	return null;
  },

  printTripel: function(pos, length, nextChar) {
    console.log('<' + pos + ', ' + length + ', ' + nextChar + '>');
  }
}

 var Tripel = function(pos,length,nextChar){
	this.pos = pos;
	this.length = length;
	this.nextChar = nextChar;
  }



// lz77.compress('ACA');
//lz77.compress('AAAAC');
lz77.compress('AAAAC12355jf8j23890jf2jfj2389 m9fh72hzf2zf7238f23gdgadasdjasdh9d128hd1289dh1298dh128dh128d891hd8');