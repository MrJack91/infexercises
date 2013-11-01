const POINTER_COMMAND = 100;
const POINTER_STORAGE = 500;

var miniPower;
var goTroughInterval;

var miniPowerTemplate = {
  main : {},
  storage : new storage('storage'),
  command : new command('opCodeView', POINTER_COMMAND),
  register : {
    r1 : new register('reg1'),
    r2 : new register('reg2'),
    r3 : new register('reg3')
  },
  akku : new akku('akku'),
  vars: {}
};

miniPowerTemplate.vars.commandMapping = {
  "CLR":  "0000[reg_2]101",
  "ADD":  "0000[reg_2]111[notUsed_7]",
  "ADDD": "1[value_15]",
  "INC":  "00000001[notUsed_8]",
  "DEC":  "00001000[notUsed_8]",

  "LWDD": "010[notUsed_1][reg_2][adr_10]",
  "SWDD": "011[notUsed_1][reg_2][adr_10]",

  "SRA":  "00000101[notUsed_8]",
  "SLA":  "00001000[notUsed_8]",
  "SRL":  "00001001[notUsed_8]",
  "SLL":  "00001100[notUsed_8]",

  "AND":  "0000[reg_2]100[notUsed_7]",
  "OR":   "0000[reg_2]110[notUsed_7]",
  "NOT":  "000000001[notUsed_7]",

  "BZ":   "0001[reg_2]10[notUsed_8]",
  "BNZ":  "0001[reg_2]01[notUsed_8]",
  "BC":   "0001[reg_2]11[notUsed_8]",
  "B":    "0001[reg_2]00[notUsed_8]",
  "BZD":  "00110[notUsed_1][adr_10]",
  "BNZD": "00101[notUsed_1][adr_10]",
  "BCD":  "00111[notUsed_1][adr_10]",
  "BD":   "00100[notUsed_1][adr_10]",

  "END":   "0000000000000000"
};

miniPowerTemplate.vars.defaultWordLength = 16;

miniPowerTemplate.main = {
  /**
   * parse binary from mnemonics
   */
  parseBinary : function (inputCommandsId, inputStorageId) {
    try {
      // commands import
      var inputCommand = $('#'+inputCommandsId).val();
      $('#'+inputCommandsId).removeClass('alert-danger');
      var singleOp = inputCommand.split('\n');
      $(singleOp).each(function(index, value) {
        miniPower.command.addMnemonic(POINTER_COMMAND, value);
      });
      miniPower.command.pointer = POINTER_COMMAND;
      miniPower.command.show(5,10);
      $('#opCodeBin').val(miniPower.command.getMnemonicsInBinary());
      $('#'+inputCommandsId).addClass('alert-success');
    } catch (err) {
      console.log(err.message);
      $('#'+inputCommandsId).removeClass('alert-success');
      $('#'+inputCommandsId).addClass('alert-danger');
    }

    try {
      // storage import
      $('#'+inputStorageId).removeClass('alert-danger');
      var inputStorage = $('#'+inputStorageId).val();
      var singleStorage = inputStorage.split('\n');
      $(singleStorage).each(function(index, value){
        value = parseInt(value);
        if (isNaN(value)) {
          value = 0;
        }
        value = miniPower.main.dec2binNegative(value);
        miniPower.storage.write(POINTER_STORAGE + index, value);
      });
      // show next 29 entries
      miniPower.storage.show(POINTER_STORAGE, POINTER_STORAGE+29);
      $('#'+inputStorageId).addClass('alert-success');
    } catch (err) {
      console.log(err.message + ': ' + err.lineNumber);
      $('#'+inputStorageId).removeClass('alert-success');
      $('#'+inputStorageId).addClass('alert-danger');
    }

    // save inputs to data store
    localStorage.setItem('opCode', inputCommand);
    localStorage.setItem('storageInput', inputStorage);
  },

  loadBinaryTemplate : function (mnemonics) {
    mnemonics = mnemonics.toUpperCase();
    return miniPower.vars.commandMapping[mnemonics];
  },

  bin2dec : function (val) {
    return parseInt(val, 2);
  },

  dec2bin : function (val) {
    return parseInt(Math.abs(val)).toString(2);
  },

  bin2decNegative : function (val) {
    // normalize
    val = miniPower.main.normalizeBin(val, miniPower.vars.defaultWordLength).val;
    var startIndex = val.length - miniPower.vars.defaultWordLength;
    var valReturn;
    if (val.charAt(startIndex) == '1') {
      // in cause of a negative number

      // problem -1
      var valDec = miniPower.main.bin2dec(val);
      valDec--;
      val = miniPower.main.dec2bin(valDec);
      val = miniPower.main.binNot(val);

      valReturn = 0 - miniPower.main.bin2dec(val);
    } else {
      valReturn = miniPower.main.bin2dec(val);
    }

    return valReturn;
  },

  dec2binNegative : function (val, len) {
    if (len == undefined) {
      len = miniPower.vars.defaultWordLength;
    }

    val = parseInt(val);

    var valAbs = miniPower.main.dec2bin(Math.abs(val));
    var valReturnNorm = miniPower.main.normalizeBin(valAbs, len);

    var valReturn = valReturnNorm.val;

    if (val < 0) {
      // negative - build 2er complement

      valReturn = miniPower.main.binNot(valReturn);

      var valDec = miniPower.main.bin2dec(valReturn);
      valDec++;
      valReturn = miniPower.main.dec2bin(valDec);
    }
    return valReturn;
  },

  binAdd : function(val1, val2) {

    // console.log('add val1 (binary): ' + val1);
    // console.log('add val2 (binary): ' + val2);

    val1 = miniPower.main.bin2decNegative(val1);
    val2 = miniPower.main.bin2decNegative(val2);

    // console.log('add val1 (dec): ' + val1);
    // console.log('add val2 (dec): ' + val2);

    var val3 = val1 + val2;

    // console.log(val3);

    // set carry flag
    if (val3 > 32767 || val3 < -32767) {
      // need to overwrite carrybit. (temporary write to akku. overwrite it correct)
      miniPower.akku.setCarryBit(1);
    } else {
      miniPower.akku.setCarryBit(0);
    }

    // console.log('add val3: (dec)' + val3);

    val3 = miniPower.main.dec2binNegative(val3);

    // console.log('add val3 (binary): ' + val3);

    return val3;
  },

  binAddDec : function(val1, val2) {
    // value 1 in binary --> convert to dec
    // console.log('binAddDec val1 (binary): ' + val1);
    // val1 = miniPower.main.bin2decNegative(val1);
    val2 = miniPower.main.dec2binNegative(val2);


    // console.log('binAddDec val1 (dec): ' + val1);
    // console.log('binAddDec val2: ' + val2);

    var val3 = miniPower.main.binAdd(val1, val2);

    return val3;
  },

  binNot : function (val) {
    var valNew = '';
    for (var x = 0; x < val.length; x++)
    {
      var c = val.charAt(x);
      if (c == '1') {
        valNew += '0';
      } else {
        valNew += '1';
      }
    }
    return valNew;
  },

  getFirstKey : function (obj) {
    var first;
    for (first in obj) break;
    return first;
  },

  /**
   * format in 4'er binary blocks
   * @param val
   * @returns {Number}
   */
  formatBin : function (val) {
    val = miniPower.main.normalizeBin(val, miniPower.vars.defaultWordLength).val;
    pos = 4;
    while (val.length > pos) {
      val = val.insert(pos, ' ');
      pos = pos + 5; // +1 because added space
    }
    return val;
  },

  normalizeBin : function (val, len) {
    // cast to string
    val = val.toString();

    // clear spaces
    val = val.replace(' ', '');

    var isCat;
    // normalize to 16 chars
    if (val.length <= len) {
      isCat = false;
      val = '0'.repeat(len-val.length)+val;
    } else {
      // cut bit in front
      isCat = true;
      val = val.substring(val.length-len);
    }
    return {
      val: val,
      isCut: isCat
    };
  },

  initialize : function () {
    miniPower.main.setControlButtonsActive(true);
    miniPower = jQuery.extend(true, {}, miniPowerTemplate);

    miniPower.akku.initialize();
    miniPower.register.r1.initialize();
    miniPower.register.r2.initialize();
    miniPower.register.r3.initialize();

    miniPower.storage.initialize();
    miniPower.storage.show(POINTER_STORAGE, POINTER_STORAGE+29);

    miniPower.command.initialize();
    miniPower.command.show(5, 10);
  },

  regexSearch : function (regex, subject) {
    var results = {}
    while (match = regex.exec(subject)) {
      results[match[1]] = match[2];
    }
    return results;
  },

  /**
   * load an register via number (direct form mnemonics)
   *  00: akku;
   *  01: reg1;
   *  02: reg2;
   *  03: reg3;
   * @param number
   */
  getRnrObject : function (number) {
    switch (parseInt(number)) {
      case 0:
        return miniPower.akku;
        break;
      case 1:
        return miniPower.register.r1;
        break;
      case 2:
        return miniPower.register.r2;
        break;
      case 3:
        return miniPower.register.r3;
        break;
      default:
        break;
    }
  },

  showAll : function () {
    miniPower.command.show(5,10);
    miniPower.storage.show(POINTER_STORAGE, POINTER_STORAGE+29);
    miniPower.register.r1.show();
    miniPower.register.r2.show();
    miniPower.register.r3.show();
    miniPower.akku.show();
  },

  goOneStep : function () {
    if (!miniPower.command.step()) {
      // end command found -> deactivate buttons
      miniPower.main.setControlButtonsActive(false);
      return false;
    }
    return true;
  },

  setControlButtonsActive : function (active) {
    if (active) {
      $('#btnModeStep').removeAttr('disabled');
      $('#btnModeSlow').removeAttr('disabled');
      $('#btnModeFast').removeAttr('disabled');
    } else {
      $('#btnModeStep').attr('disabled', '');
      $('#btnModeSlow').attr('disabled', '');
      $('#btnModeFast').attr('disabled', '');
    }
  },

  goOnLoopWithSteps : function (e, htmlId, initHtmlText, repeatTime, show, callback) {
    e.preventDefault();

    if (goTroughInterval == undefined) {
      // start running - interval
      $('#'+htmlId).html('Stop');
      miniPower.main.setControlButtonsActive(0);
      $('#'+htmlId).removeAttr('disabled');

      goTroughInterval = setInterval(function () {
        if (!miniPower.main.goOneStep()) {
          miniPower.main.stopInterval(htmlId, initHtmlText, callback);
          miniPower.main.setControlButtonsActive(0);
        }
        if (show) {
          miniPower.main.showAll();
        }
      }, repeatTime);
    } else {
      miniPower.main.stopInterval(htmlId, initHtmlText, callback);
      miniPower.main.setControlButtonsActive(1);
    }
  },

  stopInterval : function (htmlId, initHtmlText, callback) {
    // exit running - stop interval
    $('#'+htmlId).html(initHtmlText);
    // user abort slow mode
    clearInterval(goTroughInterval);
    goTroughInterval = undefined;
    callback();
  }
}

// running instanz
miniPower = jQuery.extend(true, {}, miniPowerTemplate);

String.prototype.insert = function (index, string) {
  if (index > 0)
    return this.substring(0, index) + string + this.substring(index, this.length);
  else
    return string + this;
};

String.prototype.repeat = function(n) {
  if (n > 0) {
    n = n || 1;
    return Array(n+1).join(this);
  }
  return '';
};

String.prototype.replaceAt=function(index, character) {
  return this.substr(0, index) + character + this.substr(index+character.length);
}

// INIT
miniPower.main.initialize();

$('#btnModeStep').bind('click', function (e) {
  e.preventDefault();
  miniPower.main.goOneStep();
  miniPower.main.showAll();
  return false;
})

$('#btnModeSlow').bind('click', function (e) {
  miniPower.main.goOnLoopWithSteps(e, 'btnModeSlow', 'Slow', 1000, true, function () {

  });
  return false;
})

$('#btnModeFast').bind('click', function (e) {
  miniPower.main.goOnLoopWithSteps(e, 'btnModeFast', 'Fast', 0, false, function () {
    miniPower.main.showAll();
  });

  return false;
})

$('#btnReload').bind('click', function (e) {
  e.preventDefault();
  miniPower.main.initialize();
  miniPower.main.showAll();
  miniPower.main.parseBinary('opCode', 'storageInput');
})

/*
// set test values
// $('#opCode').val('LWDD 0, #500\nSWDD 0, #520\nLWDD 1, #501\nSWDD 1, #521\nCLR 0\nCLR 1\nEND');
// ADD
// $('#opCode').val('LWDD 0, #500\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nADDD #10\nINC\nINC\nINC\nINC\nINC\nDEC\nADD 1\nEND');
// ADDD
// $('#opCode').val('LWDD 0, #500\nADDD #-10\nINC\nINC\nINC\nINC\nINC\nDEC\nADD 1\nEND');

// $('#opCode').val('LWDD 0, #501\nLWDD 1, #501\nBNZ 1\nEND\nEND\nEND\nEND\nEND\nCLR 0\nCLR 1\nEND');
// $('#opCode').val('LWDD 0, #500\nSLL\nSLL\nSLL\nSLL\nSLL\nSLL\nSLL\nSLL\nEND');
// inf 3 aufagbe
$('#opCode').val('LWDD 0, #504    ; hello test\nSLA\n\nSLA\nSLA\nSWDD 0, #506\nLWDD 0, #502\nSLA\nSLA\nLWDD 1, #500\nADD 1\nLWDD 1, #506\nADD 1\nSWDD 0, #506\nEND');
// $('#opCode').val('LWDD 0, #504\nSRA\nSRA\nSRA\nSRA\nSRA\nSRA\nSRA\nSRA\nSRA\nSWDD 0, #506\nLWDD 0, #502\nSLA\nSLA\nLWDD 1, #500\nADD 1\nLWDD 1, #506\nADD 1\nSWDD 0, #506\nEND');
// $('#opCode').val('LWDD 0, #500\nINC\nINC\nINC\nINC\nINC\nINC\nINC\nINC\nINC\nINC\nINC\nINC\nINC\nINC\nINC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nDEC\nEND');
// $('#opCode').val('LWDD 0, #500\nADDD #40000\nEND');


// $('#storageInput').val('14\n0\n7\n0\n66');
$('#storageInput').val('25\n0\n-14\n0\n-123');
// $('#storageInput').val('-125\n0\n10000\n0\n16');
// $('#storageInput').val('1000\n0\n10000\n0\n-2000');
// $('#storageInput').val('32765\n-32765\n0\n10000\n0\n-2000');
*/

$('#opCode').val(localStorage.getItem('opCode'));
$('#storageInput').val(localStorage.getItem('storageInput'));

miniPower.main.parseBinary('opCode', 'storageInput');


