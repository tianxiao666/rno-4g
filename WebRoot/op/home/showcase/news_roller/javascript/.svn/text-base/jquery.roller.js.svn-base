;(function($) {
	$.fn.roller = function(options) {
		var opts = $.extend({}, $.fn.roller.defaults, options),
			// 通过循环队列来管理滚动信息
			itemQueue = new Array();
			
		return this.each(function(index) {
			
			var $div = $(this).addClass('roller-container');
			// 用给定的数组进行初始化
			if(opts.items && Util.isArray(opts.items)) {
				for(var i = 0, len = opts.items.length; i < len; i++) {
					itemQueue.push($('<div class="roller-item"></div>').append(buildLink(opts.items[i])));
				}
			} else {
				//同样可以用页面元素进行初始化
			}
			
			// 把一开始要显示的条目加入容器中
			for(i = 0, len = opts.showNum; i < len; i++) {
				if(isUp()) {
					$div.append(itemQueue[i]);
				} else {
					$div.prepend(itemQueue[i]);
				}
			}
			// 把已经加入容器的条目副本 放到循环队列的尾部
			for(i = 0, len = opts.showNum; i < len; i++) {
				var temp = itemQueue.shift();
				itemQueue.push(temp.clone());
			}
			
			// 取得一个滚动元素
			var _item = $('.roller-item:first', this),
				// 取得它的外围高度 包括margin
				_outHeight = _item.outerHeight(true),
				// 容器的内容总高度
				totalHeight = _outHeight * parseInt(opts.showNum, 10);
			
			// 保存初始marginTop值
			opts.orginal_marginTop = parseInt(_item.css('margin-top'), 10);
			if(isUp()) {
				opts.anim_marginTop = opts.orginal_marginTop - _outHeight - parseInt($div.css('padding-top'), 10);
			} else {
				opts.anim_marginTop = opts.orginal_marginTop + _outHeight;
			}
			
			// 初始化容器样式和事件
			$div.css({
				'height': totalHeight + 'px',
				'overflow': 'hidden'
			}).hover(function() {
				// 鼠标进入时 停止滚动	
				opts.hold = true;
			}, function() {
				// 鼠标离开  重新开始滚动
				opts.hold = false;
				startRolling($(this));
			}).trigger('mouseleave');
			
		});
		
		/**
		 * 滚动方向判断
		 */
		function isUp() {
			if(opts.direction === 'up') return true;
			if(opts.direction === 'down') return false;
			throw new Error('direction should be "up" or "down"');
		}
		
		/**
		 * 生成一个jQuery封装的<a/>
		 */
		function buildLink(item) {
			var html = item.html;
			delete item.html;
			return $('<a></a>').attr(item).html(html);
		}
		
		function startRolling($div) {
			setTimeout(function() {
				// 是否停止滚动
				if(!opts.hold) { 
					var first = null,
						_funSelf = arguments.callee;
					
					// 当前第一个元素
					first = $div.find('.roller-item:first');
						
					first.animate({marginTop: opts.anim_marginTop}, 
						opts.interval, 
						function() {
							// 从队列中取出下一个条目
							var temp = itemQueue.shift();
							// 把它的副本放到队列的尾部
							itemQueue.push(temp.clone());
							
							if(isUp()) { 
								// 移除当前第一个元素
								first.remove();
								// 把刚取出的条目append到容器中
								$div.append(temp.hide());
							} else { 
								// 移除当前最后一个元素
								$div.find('.roller-item:last').remove();
								// 让当前第一个元素的marginTop恢复成初始值
								first.css('margin-top', opts.orginal_marginTop + 'px');
								// 把刚取出的条目prepend到容器中
								$div.prepend(temp.hide());
							}
							
							temp.fadeIn(opts.interval - 50);
							// 触发下一个循环
							setTimeout(_funSelf, opts.interval);
					});
				}
			}, opts.interval);
		};
	};
	
	//工具方法集合
	var Util = {
		toString: function(v) {
			return Object.prototype.toString.apply(v);
		}, 	
		// 判断是否是Array
		isArray : function(v){
			return Util.toString(v) === '[object Array]';
		}
	};
	
	// 滚动新闻默认配置
	$.fn.roller.defaults = {
		interval: 1000,  // 滚动间隔
		showNum: 5, 	 // 一次显示新闻数
		hold: false,     // 是否停止滚动
		direction: 'up'  // 滚动方向
	};
})(jQuery);