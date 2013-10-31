function akku(htmlId) {
  this.reg = new register(htmlId);
  this.carryBit = false;

  this.initialize = function () {
    this.reg.initialize();
    this.setCarryBit(0);
  };

  this.getBinary = function () {
    return this.reg.getBinary();
  }

  this.setBinary = function (value) {
    // this.carryBit = this.reg.setBinary(value);
    this.reg.setBinary(value);
  };


  this.setCarryBit = function (value) {
    value = parseInt(value);
    if (value === 1) {
      this.carryBit = true;
      return true;
    } else {
      this.carryBit = false;
      return true;
    }
    return false;
  };

  this.show = function() {
    this.reg.show();
    if (this.carryBit) {
      $('#' + this.reg.htmlId + ' .carrybit').html('1');
      $('#' + this.reg.htmlId + ' .headRight').addClass('label-danger');
      $('#' + this.reg.htmlId + ' .headRight').removeClass('label-default');
    } else {
      $('#' + this.reg.htmlId + ' .carrybit').html('0');
      $('#' + this.reg.htmlId + ' .headRight').addClass('label-default');
      $('#' + this.reg.htmlId + ' .headRight').removeClass('label-danger');
    }
  };

  this.addRegNr = function (regNr) {
    regNr = parseInt(regNr);

    var newVal = miniPower.main.binAdd(this.getBinary(), miniPower.main.getRnrObject(regNr).getBinary());
    this.setBinary(newVal);
  }

  this.not = function () {
    var val = this.getBinary();
    var valNew = miniPower.main.binNot(val);
    this.setBinary(valNew);
    return valNew;
  }

  this.addDirect = function (val1) {
    // val1 can be in 15bit -> convert to a 16bit int (with sign)
    if (val1.length < miniPower.vars.defaultWordLength) {
      var sign = val1.charAt(0).toString();
      var notSign = Math.abs(sign-1).toString();
      var fillWith = miniPower.vars.defaultWordLength-val1.length-1; // fill rest with notSign
      val1 = sign + notSign.repeat(fillWith) + val1;
    }

    // cast to bin
    var newVal = miniPower.main.binAdd(this.getBinary(), val1);
    this.setBinary(newVal);
  }

  this.reset = function () {
    this.setBinary('0');
  }

}