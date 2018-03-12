function outPut(s) {
	document.writeln(s)
}
// 全局变量 
var i=0;
// 定义外部函数
function outer() {
	// 访问全局变量
	outPut(i); // 0
	// 定义一个类部函数
	function inner() {
		outPut(i); // undefiend
		var i = 1;
		outPut(i); // 1
	}
	inner();
	outPut(i); // 0
}
outer();