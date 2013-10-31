function command(htmlId, start) {
  this.htmlId= htmlId;
  this.storage;
  this.start = start;
  this.pointer = start;
  this.commandCounter = 0;

  this.initialize = function() {
    this.storage = miniPower.storage;
    this.commandCounter = 0;
    this.pointer = this.start;
  };

  /**
   *
   * @param index
   * @param mnemonic
   * @param binary
   * @param parts
   */
  this.add = function (index, mnemonic, binary, parts) {
    val = {
      mnemonic: mnemonic,
      binary: miniPower.main.normalizeBin(binary).val,
      binaryShow: miniPower.main.formatBin(binary),
      parts: {
        mnemonicCommand: parts.mnemonicCommand,
        reg: parts.reg,
        adr: parts.adr,
        value: parts.value
      }
    }
    this.storage.write(index, val);
  };

  this.addMnemonic = function (index, mnemonic) {
    // mnemonic command
    var singleOpWord = mnemonic.split(' ');
    var mnemonicCommand = singleOpWord[0].toUpperCase();
    // binary template
    var binaryTemplate = miniPower.main.loadBinaryTemplate(mnemonicCommand);
    // binary template placeholders
    // var results = miniPower.main.regexSearch(/\[(.[^\[]*)_([0-9]+)\]/gi, binaryTemplate);
    var results = miniPower.main.regexSearch(/\[(.[^\[]*)_([0-9]+)\]/gi, binaryTemplate);
    // cut mnemonic word from commend, and split by coma
    singleOpWord.splice(0,1);
    var params = singleOpWord.join('').split(',');

    // set all notused bits
    if (results['notUsed'] !== undefined) {
      var count = parseInt(results['notUsed']);
      binaryTemplate = binaryTemplate.replace('[notUsed_' + results['notUsed'] + ']', '0'.repeat(count));
      delete results['notUsed'];
    }

    /*
    console.log(mnemonic);
    console.log(params);
    */

    // save all parts of mnemonics command
    var parts = {};
    parts['mnemonicCommand'] = mnemonicCommand;

    // go through every word
    for (i = 0; i < params.length; i++) {
      // clean number
      var number = params[i].replace(/[\s#]+/gi, '');
      if (number.length == 0) {
        break;
      }
      number = parseInt(number);

      // get the first key
      var firstKey = miniPower.main.getFirstKey(results);
      // replace dynamic placeholders with normalized binary
      switch (firstKey) {
        case 'reg':
        case 'adr':
          var numberBin = miniPower.main.dec2bin(number);
          var count = parseInt(results[firstKey]);

          /*
          console.log('command: ' + firstKey);
          console.log('count: ' + count);
          console.log('number: ' + number);
          console.log('numberBin: ' + numberBin);

          console.log('binaryTemplate: ' + binaryTemplate);
          */


          numberBin = miniPower.main.normalizeBin(numberBin, count).val;
          binaryTemplate = binaryTemplate.replace('[' + firstKey + '_' + count + ']', numberBin);

          // save every parts
          parts[firstKey] = {
            'number': number,
            'numberBin': numberBin
          };

          // console.log('binaryTemplate: ' + binaryTemplate);

          delete results[firstKey];
          break;
        case 'value':
          var count = parseInt(results[firstKey]);
          var numberBin15 = miniPower.main.dec2binNegative(number, count);

          // console.log('numberBin15: ' + numberBin15);

          numberBin15 = miniPower.main.normalizeBin(numberBin15, count).val;

          /*
          console.log('command: ' + firstKey);
          console.log('count: ' + count);
          console.log('number: ' + number);
          console.log('numberBin15: ' + numberBin15);

          console.log('binaryTemplate: ' + binaryTemplate);
          */

          binaryTemplate = binaryTemplate.replace('[' + firstKey + '_' + count + ']', numberBin15);

          // numberBin16 = miniPower.main.dec2binNegative(number);
          // console.log('numberBin: ' + numberBin16);

          // save every parts
          parts[firstKey] = {
            'number': number,
            'numberBin': numberBin15
          };

          // console.log('binaryTemplate: ' + binaryTemplate);

          delete results[firstKey];
          break;
      }
    }
    this.add(this.pointer, mnemonic, binaryTemplate, parts);
    this.pointer++;
  };

  this.load = function (index) {
    var val = this.storage.read(index);
    if (typeof(val) !== "object") {
      val = {};
    }
    if (val['binary'] == undefined) {
      val['binary'] = '0';
    }
    if (val['mnemonic'] == undefined) {
      val['mnemonic'] = '';
    }
    return val;
  };

  this.show = function (pre, after) {
    var html = '';
    for (i = this.pointer - pre; i <= this.pointer+after; i++) {
      var val = this.load(i);

      var htmlClass = '';
      if (i == this.pointer) {
        htmlClass = 'alert-info';
      }
      html += '' +
        '<tr class="' + htmlClass + '">' +
          '<td>' + i + '</td>' +
          '<td>' + val.mnemonic + '</td>' +
          '<td>' + miniPower.main.formatBin(val.binary) + '</td>' +
        '</tr>'
      $('#'+this.htmlId + ' tbody').html(html);
    }
    $('#stepCountVal').html(this.commandCounter);
    $('#'+this.htmlId + ' .commandPointer').html(this.pointer);
  };

  this.getMnemonicsInBinary = function () {
    var binaryCode = '';
    for (commandX in this.storage.data) {
      binaryCode += this.storage.data[commandX].binaryShow + '\n';
    }
    return binaryCode;
  };

  /**
   *
   * @returns {boolean} going on
   */
  this.step = function () {
    var data = this.load(this.pointer);
    var retVal = true; // going on until the end is coming
    var nextCommandAdr = this.pointer + 1;
    switch (data.parts.mnemonicCommand) {
      /* CAT 1 */
      case 'CLR':
        miniPower.main.getRnrObject(data.parts.reg.number).reset();
        break;
      case 'ADD':
        miniPower.akku.addRegNr(data.parts.reg.number);
        break;
      case 'ADDD':
        miniPower.akku.addDirect(data.parts.value.numberBin);
        break;
      case 'INC':
        miniPower.akku.addDirect(miniPower.main.dec2binNegative(1));
        break;
      case 'DEC':
        var val1 = miniPower.main.dec2binNegative(-1)
        miniPower.akku.addDirect(val1);
        break;

      /* CAT 2 */
      case 'LWDD':
        var val = miniPower.storage.read(data.parts.adr.number);
        miniPower.main.getRnrObject(data.parts.reg.number).setBinary(val);
        // miniPower.akku.setCarryBit(false);
        break;
      case 'SWDD':
        var val = miniPower.main.getRnrObject(data.parts.reg.number).getBinary();
        miniPower.storage.write(data.parts.adr.number, val);
        // miniPower.akku.setCarryBit(false);
        break;

      /* CAT 3 */
      case 'SRA':
        var val1 = miniPower.akku.getBinary();
        var carryBit = val1.charAt(miniPower.vars.defaultWordLength-1);
        // convert to dec -> javascript support binary "right shift"
        // read without sign
        var valT1 = val1.substring(1);
        // calc to dec
        valT1 = miniPower.main.bin2dec(valT1);
        // shift this only 15 chars bit right
        valT1 = valT1 >> 1;
        // calc back to bin
        valT1 = miniPower.main.dec2bin(valT1);
        // normalize to 15 signs
        valT1 = miniPower.main.normalizeBin(valT1, miniPower.vars.defaultWordLength-1).val;
        // add first (=MSB) original Bit for sign before
        valT1 = val1.charAt(0) + valT1;
        // add the same bit like the sign
        valT1 = valT1.replaceAt(1, val1.charAt(0));
        // special case: if negative and carry bet = 1 ;calc additional -1
        if (val1.charAt(0) == '1' && carryBit == '1') {
          valT1 = miniPower.main.binAddDec(valT1, 1);
        }
        // set new 16bit to akku
        miniPower.akku.setBinary(valT1);
        miniPower.akku.setCarryBit(carryBit);

        break;
      case 'SLA':
        var val1 = miniPower.akku.getBinary();
        // convert to dec -> javascript support binary "left shift"
        // read from 3. position
        var valT1 = val1.substring(2);
        // calc to dec
        valT1 = miniPower.main.bin2dec(valT1);
        // shift this only 14 chars bit
        valT1 = valT1 << 1;
        // calc back to bin
        valT1 = miniPower.main.dec2bin(valT1);
        // normalize to 15 signs
        valT1 = miniPower.main.normalizeBin(valT1, miniPower.vars.defaultWordLength-1).val;
        // add first (=MSB) original Bit for sign before
        valT1 = val1.charAt(0) + valT1;
        // set new 16bit to akku
        miniPower.akku.setBinary(valT1);
        miniPower.akku.setCarryBit(val1.charAt(1));

        break;
      case 'SRL':
        var val1 = miniPower.akku.getBinary();
        // read last cutted bit
        var carryBit = val1.substring(val1.length-1);
        // convert to dec -> javascript support binary "right shift"
        val1 = miniPower.main.bin2dec(val1);
        val1 = val1 >> 1;
        val1 = miniPower.main.dec2bin(val1);
        miniPower.akku.setBinary(val1);
        miniPower.akku.setCarryBit(carryBit);
        break;
      case 'SLL':
        var val1 = miniPower.akku.getBinary();
        // convert to dec -> javascript support binary "left shift"
        var carryBit = val1.substring(0,1);
        val1 = miniPower.main.bin2dec(val1);
        val1 = val1 << 1;
        val1 = miniPower.main.dec2bin(val1);
        miniPower.akku.setBinary(val1);
        miniPower.akku.setCarryBit(carryBit);
        break;

      /* CAT 4 */
      case 'AND':
        var val1 = miniPower.akku.getBinary();
        var val2 = miniPower.main.getRnrObject(data.parts.reg.number).getBinary();
        // convert to dec -> javascript support binary AND
        val1 = miniPower.main.bin2dec(val1);
        val2 = miniPower.main.bin2dec(val2);
        var val3 = val1 & val2;
        val3 = miniPower.main.dec2bin(val3);
        miniPower.akku.setBinary(val3);
        break;
      case 'OR':
        var val1 = miniPower.akku.getBinary();
        var val2 = miniPower.main.getRnrObject(data.parts.reg.number).getBinary();
        // convert to dec -> javascript support binary OR
        val1 = miniPower.main.bin2dec(val1);
        val2 = miniPower.main.bin2dec(val2);
        var val3 = val1 | val2;
        val3 = miniPower.main.dec2bin(val3);
        miniPower.akku.setBinary(val3);
        break;
      case 'NOT':
        miniPower.akku.not();
        break;

      /* CAT 5 */
      case 'BZ':
        if (miniPower.main.bin2dec(miniPower.akku.getBinary()) == 0) {
          var val = miniPower.main.getRnrObject(data.parts.reg.number).getBinary();
          val = miniPower.main.bin2dec(val);
          nextCommandAdr = val;
        }
        break;
      case 'BNZ':
        if (miniPower.main.bin2dec(miniPower.akku.getBinary()) !== 0) {
          var val = miniPower.main.getRnrObject(data.parts.reg.number).getBinary();
          val = miniPower.main.bin2dec(val);
          nextCommandAdr = val;
        }
        break;
      case 'BC':
        if (miniPower.akku.carryBit) {
          var val = miniPower.main.getRnrObject(data.parts.reg.number).getBinary();
          val = miniPower.main.bin2dec(val);
          nextCommandAdr = val;
        }
        break;
      case 'B':
        var val = miniPower.main.getRnrObject(data.parts.reg.number).getBinary();
        val = miniPower.main.bin2dec(val);
        nextCommandAdr = val;
        break;
      case 'BZD':
        if (miniPower.main.bin2dec(miniPower.akku.getBinary()) == 0) {
          nextCommandAdr = data.parts.adr.number;
        }
        break;
      case 'BNZD':
        if (miniPower.main.bin2dec(miniPower.akku.getBinary()) !== 0) {
          nextCommandAdr = data.parts.adr.number;
        }
        break;
      case 'BCD':
        if (miniPower.akku.carryBit) {
          nextCommandAdr = data.parts.adr.number;
        }
        break;
      case 'BD':
        nextCommandAdr = data.parts.adr.number;
        break;

      /* CAT 6 */
      case 'END':
        // stop going on. It's the happy end. ;)
        nextCommandAdr--;
        retVal = false;
        break;
    }
    this.pointer = nextCommandAdr;

    this.commandCounter++;
    return retVal;
  }
}