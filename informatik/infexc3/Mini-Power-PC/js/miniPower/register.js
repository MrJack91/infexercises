function register(htmlId) {
  this.htmlId = htmlId;
  this.valueBin = '0';

  this.initialize = function() {
    this.setBinary('0');
    this.show();
  };

  /**
   *
   * @param value
   * @returns {boolean} is binary cutted
   */
  this.setBinary = function (value) {
    // normalize value
    var val = miniPower.main.normalizeBin(value, miniPower.vars.defaultWordLength);
    this.valueBin = val.val;
    return val.isCut;
  };

  this.getBinary = function () {
    return this.valueBin;
  }

  this.show = function() {
    var valueShow = miniPower.main.formatBin(this.getBinary());
    $('#' + this.htmlId + ' .binaryValue').html(valueShow);
    $('#' + this.htmlId + ' .decValue').html(miniPower.main.bin2decNegative(this.getBinary()));
  };

  this.reset = function () {
    this.setBinary('0');
  }
}