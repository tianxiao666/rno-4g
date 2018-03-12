//选项卡 1.切换条id 2.切换条标签类型 3.切换事件
function tab(target,tag,etype)
{
    var list = document.getElementById(target).getElementsByTagName(tag);
    for(var i = 0;i<list.length;i++)
	{
        list[i][etype]=function()
		{
            for(var i=0;i<list.length;i++)
			{
                if(list[i]==this){
                    list[i].className = "selected";
                    document.getElementById(target+"_"+i).style.display = "block";
				}
                else{
                    list[i].className="";
                    document.getElementById(target+"_"+i).style.display = "none";
                }
            }
        }
    }
}