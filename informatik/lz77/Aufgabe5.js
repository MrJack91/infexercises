var PREVIEW_BUFFER_SIZE = 4;
var WORK_WINDOW_SIZE = 4;

function compress(inputText) {
  var workText = inputText;
  var workWindow = "";
  var previewBuffer = "";

  var finishedCompression = false;

  //Remove!! only for debug!!
  var count = 0;

  console.log("Starting compression...");
  while (!finishedCompression) {
    console.log("...");

    //Check if there is input text left
    if (workText.length > 0) {
      console.log("Process Text...");
      //Calculate number of bits to transfer to previewBuffer
      var prevBuffTransferCount = PREVIEW_BUFFER_SIZE - previewBuffer.length;

      previewBuffer = previewBuffer + workText.substring(0, prevBuffTransferCount);
      workText = workText.substring(prevBuffTransferCount, workText.length);
      console.log("Buffer: " + previewBuffer);
      console.log("Left Text: " + workText);

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
            currPos = iw;
            currLength = 1;
            //First character matches...

            //Are there any more?
            //Keep in boundary
            var max = previewBuffer.length;

            if (workWindow.length < max) {
              max = workWindow.length;
            }

            for (var x = 1; x < max; x++) {
              if (workWindow.charAt(iw + x) == previewBuffer.charAt(prevBufIndex + x)) {
                currLength++;
              } else {
                break;
              }
            }

            if (currLength >= lastLength) {
              lastLength = currLength;
              lastPos = currPos;
            }
          }
        }

        if (foundMatch) {
          //use lastLength + lastPos
        } else {
          //TODO: Add tripple: <0,0,previewBuffer.charAt(0)>
          previewBuffer = previewBuffer.substring(1, previewBuffer.length);
        }
      }
    } else {
      console.log("Finished compression...");
      finishedCompression = true;
    }

    count++;
    if (count > 20) {
      break;
    }
  }

  return null;
}

function decompress(inputText) {

}