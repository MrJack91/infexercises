function isPalindrom(aWord){
	if(aWord.length < 2){
		return true;
	}else{
		if(aWord.substring(0,1) == aWord.substring(aWord.length -1,aWord.length)){
			if(isPalindrom(aWord.substring(1,aWord.length -1))){
				return true;
			}
		}
	}
	return false;
}

console.log(isPalindrom('Test'));
console.log(isPalindrom('sugus'));
console.log(isPalindrom('Das ist ein längerer test'));
console.log(isPalindrom('Anna'));
console.log(isPalindrom('Ich denke das ist ein PalindrommordnilaP nie tsi sad ekned hcI'));