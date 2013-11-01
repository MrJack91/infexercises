function storage(htmlId) {
  this.htmlId = htmlId;
  this.data = {};

  this.initialize = function () {
    this.data = {};
  };

  this.write = function (index, val) {
    // in value can be everything, only cast to bin, if a number
    if (!isNaN(val) ) {
      val = miniPower.main.normalizeBin(val, miniPower.vars.defaultWordLength).val;
    }
    this.data[index] = val;
  };

  this.read = function (index) {
    if (this.data[index] !== undefined) {
      return this.data[index];
    } else {
      return '0';
    }
  };

  this.show = function (from, to) {
    var html = '';
    for (i = from; i <= to; i++) {
      var val = this.read(i);
      var valDec = miniPower.main.bin2dec(val);
      var valDecNeg = miniPower.main.bin2decNegative(val);

      var htmlDec = valDec;
      if (valDec !== valDecNeg) {
        htmlDec += ' [' + valDecNeg + ']';
      }
      // '<td>' + miniPower.main.bin2dec(val) + ' / ' + miniPower.main.bin2decNegative(val) + '</td>' +
      html += '' +
        '<tr>' +
          '<td>' + i + '</td>' +
          '<td>' + miniPower.main.formatBin(val) + '</td>' +
          '<td>' + htmlDec + '</td>' +
        '</tr>'
    }

    $('#'+this.htmlId + ' tbody').html(html);
  };


}