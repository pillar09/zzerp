/**
 * @license                                     
 * jQuery Tools 1.2.6 Selectinput - <input type="select" /> for humans
 * 
 * NO COPYRIGHTS OR LICENSES. DO WHAT YOU LIKE.
 * 
 * http://flowplayer.org/tools/form/selectinput/
 *
 * Since: Mar 2010
 * Date: ${date} 
 */
(function($, undefined) {	
		
	/* TODO: 
		 preserve today highlighted
	*/
	
	$.tools = $.tools || {version: '1.2.6'};
	
	var instances = [], 
		 tool, 
		 
	tool = $.tools.selectinput = {
		
		conf: { 
			offset: [0, 0],
			speed: 0,
			trigger: 0,
			toggle: 0,
			editable: 0
		}
		
		
	};
		 
	
	function Selectinput(input, conf)  { 
		// variables
		var self = this,  
			 css = 'calroot',
			 root = $("#"+css),
//			 title = root.find("#" + css.title),
			 trigger,
			 value = input.attr("select-value") || conf.value || input.val(), 
			 opened = false,
			 original;
//		alert("select");
//		input.addClass(css.input);
		
		var fire = input.add(self);
		// construct layout
		if (!root.length) {
			// root
			root = $('<div></div>')
				.hide().css({position: 'absolute'}).attr("id", css.root);			
			
			$("body").append(root);
		}	
		
				
		// trigger icon
		if (conf.trigger) {
			trigger = $("<a/>").attr("href", "#").addClass(css.trigger).click(function(e)  {
				conf.toggle ? self.toggle() : self.show();
				return e.preventDefault();
			}).insertAfter(input);	
		}
		
		
		// layout elements
		
//{{{ pick
			 			 
		function select(date, conf, e) {  
			
			// current value	
			alert("select");
			
			// beforChange
			e = e || $.Event("api");
			e.type = "beforeChange";
			
			fire.trigger(e, [date]);
			if (e.isDefaultPrevented()) { return; }
			
			// formatting			
//			input.val(format(date, conf.format, conf.lang));
			
      // change
			e.type = "change";
			fire.trigger(e);
			// store value into input
			input.data("select", date);
			
			self.hide(e); 
		}
//}}}
		
		
//{{{ onShow

		function onShow(ev) {
			alert("onShow");
			ev.type = "onShow";
			fire.trigger(ev);
			
			// click outside selectinput
			$(document).bind("click.d", function(e) {					
				var el = e.target;
				if (!$(el).parents("#" + css).length && el != input[0] && (!trigger || el != trigger[0])) {
					self.hide(e);
				}
				
			}); 
		}
//}}}


		$.extend(self, {

      
			/**
			*   @public
			*   Show the calendar
			*/					
			show: function(e) {
				if (input.attr("readonly") || input.attr("disabled") || opened) { return; }
				alert("show");
				
				// onBeforeShow
				e = e || $.Event();
				e.type = "onBeforeShow";
				fire.trigger(e);
				if (e.isDefaultPrevented()) { return; }
			
				$.each(instances, function() {
					this.hide();	
				});
				
				opened = true;
				
				// set date
				self.setValue(value);				 
				
				// show calendar
				var pos = input.offset();

				root.css({ 
					top: pos.top + input.outerHeight({margins: true}) + conf.offset[0], 
					left: pos.left + conf.offset[1] 
				});
				
				if (conf.speed) {
					root.show(conf.speed, function() {
						onShow(e);			
					});	
				} else {
					root.show();
					onShow(e);
				}
				
				return self;
			}, 

      /**
      *   @public
      *
      *   Set the value of the selectinput
      */
			setValue: function(year, month, day)  {
				
				//TODO
				return self;
			}, 
	//}}}
	
			destroy: function() {
				input.add(document).unbind("click.d").unbind("keydown.d");
				root.add(trigger).remove();
				input.removeData("selectinput").removeClass(css.input);
				if (original)  { input.replaceWith(original); }
			},
			
			hide: function(e) {				 
				
				if (opened) {  
					
					// onHide 
					e = $.Event();
					e.type = "onHide";
					fire.trigger(e);
					
					$(document).unbind("click.d").unbind("keydown.d");
					
					// cancelled ?
					if (e.isDefaultPrevented()) { return; }
					
					// do the hide
					root.hide();
					opened = false;
				}
				
				return self;
			},
			
			toggle: function(){
			  return self.isOpen() ? self.hide() : self.show();
			},
			
			getConf: function() {
				return conf;	
			},
			
			getInput: function() {
				return input;	
			},
			
			getCalendar: function() {
				return root;	
			},
			
			getValue: function(dateFormat) {
				return dateFormat ;	
			},
			
			isOpen: function() {
				return opened;	
			}
			
		}); 
		
		// callbacks	
		$.each(['onBeforeShow','onShow','change','onHide'], function(i, name) {
				
			// configuration
			if ($.isFunction(conf[name]))  {
				$(self).bind(name, conf[name]);	
			}
			
			// API methods				
			self[name] = function(fn) {
				if (fn) { $(self).bind(name, fn); }
				return self;
			};
		});

		if (!conf.editable) {
			
			// show selectinput & assign keyboard shortcuts
			input.bind("focus.d click.d", self.show).keydown(function(e) {
	
				var key = e.keyCode;
				
				// open selectinput with navigation keyw
				if (!opened &&  $(KEYS).index(key) >= 0) {
					self.show(e);
					return e.preventDefault();
				} 
				
				// allow tab
				return e.shiftKey || e.ctrlKey || e.altKey || key == 9 ? true : e.preventDefault();   
				
			});
		}
		
		// initial value 		
//		if (parseDate(input.val())) {
//			select(value, conf);
//		}
		
	} 
	
	
	$.fn.selectinput = function(conf) {   
		// already instantiated
		if (this.data("selectinput")) { return this; } 
		alert("selectinput");
		// configuration
		conf = $.extend(true, {}, tool.conf, conf);		
		
		// CSS prefix
		var s='';
//		$.each(conf.css, function(key, val) {
//			if (!val && key != 'prefix') { 
//				conf.css[key] = (conf.css.prefix || '') + (val || key);
//				s+= key+"="+conf.css[key]+"\n";
//			}
//		});		
//		alert(s);
//		conf.css['root'] = 'calroot';
//		conf.css['body'] = 'calbody';
		var els;
		
		this.each(function() {			
			var el = new Selectinput($(this), conf);
			instances.push(el);
			var input = el.getInput().data("selectinput", el);
			els = els ? els.add(input) : input;	
		});		
		alert("selectinput22");
		return els ? els : this;		
	}; 
	
	
}) (jQuery);