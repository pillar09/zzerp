/**
 * @license                                     
 * jQuery Tools 1.2.6 Datetimeinput - <input type="datetime" /> for humans
 * 
 * NO COPYRIGHTS OR LICENSES. DO WHAT YOU LIKE.
 * 
 * http://flowplayer.org/tools/form/datetimeinput/
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
		 
		 // h=72, j=74, k=75, l=76, down=40, left=37, up=38, right=39
		 KEYS = [75, 76, 38, 39, 74, 72, 40, 37],
		 LABELS = {};
	
	tool = $.tools.datetimeinput = {
		
		conf: { 
			format: 'yyyy-mm-dd HH:MM',
			selectors: false,
			yearRange: [-5, 5],
			lang: 'en',
			offset: [0, 0],
			speed: 0,
			firstDay: 0, // The first day of the week, Sun = 0, Mon = 1, ...
			min: undefined,
			max: undefined,
			trigger: 0,
			toggle: 0,
			editable: 0,
			
			css: {
				
				prefix: 'cal',
				input: 'date',
				
				// ids
				root: 0,
				head: 0,
				title: 0, 
				prev: 0,
				next: 0,
				month: 0,
				year: 0, 
				days: 0,
				
				body: 0,
				weeks: 0,
				today: 0,		
				current: 0,
				
				// classnames
				week: 0, 
				off: 0,
				sunday: 0,
				focus: 0,
				disabled: 0,
				trigger: 0,
				foot:0,
				time:0,
				hour:0,
				minute:0
				
			}  
		},
		
		localize: function(language, labels) {
			$.each(labels, function(key, val) {
				labels[key] = val.split(",");		
			});
			LABELS[language] = labels;	
		}
		
	};
	
	tool.localize("en", {
		months: 		 'January,February,March,April,May,June,July,August,September,October,November,December', 
		shortMonths: 'Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec',  
		days: 		 'Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday', 
		shortDays: 	 'Sun,Mon,Tue,Wed,Thu,Fri,Sat'	  
	});

	
//{{{ private functions
		

	// @return amount of days in certain month
	function dayAm(year, month) {
		return new Date(year, month + 1, 0).getDate();
	}
 
	function zeropad(val, len) {
		val = '' + val;
		len = len || 2;
		while (val.length < len) { val = "0" + val; }
		return val;
	}  
	
	// thanks: http://stevenlevithan.com/assets/misc/date.format.js 
  //var Re = /d{1,4}|m{1,4}|yy(?:yy)?|"[^"]*"|'[^']*'/g, tmpTag = $("<a/>");
	var Re = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,tmpTag = $("<a/>");
	function format(date, fmt, lang) {
		
	  var d = date.getDate(),
			D = date.getDay(),
			m = date.getMonth(),
			y = date.getFullYear(),
			H = date.getHours(),	//Returns the hour (from 0-23)
			M = date.getMinutes(),	//Returns the minutes (from 0-59)
			s = date.getSeconds(),
			
			flags = {
				d:    d,
				dd:   zeropad(d),
				ddd:  LABELS[lang].shortDays[D],
				dddd: LABELS[lang].days[D],
				m:    m + 1,
				mm:   zeropad(m + 1),
				mmm:  LABELS[lang].shortMonths[m],
				mmmm: LABELS[lang].months[m],
				yy:   String(y).slice(2),
				yyyy: y,
				h:    H % 12 || 12,
				hh:   zeropad(H % 12 || 12),
				H:    H,
				HH:   zeropad(H),
				M:    M,
				MM:   zeropad(M),
				s:    s,
				ss:   zeropad(s)
			};

		var ret = fmt.replace(Re, function ($0) {
			return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
		});
		
		// a small trick to handle special characters
		return tmpTag.html(ret).html();
		
	}
	
	function integer(val) {
		return parseInt(val, 10);	
	} 

	function isSameDay(d1, d2)  {
		return d1.getFullYear() === d2.getFullYear() && 
			d1.getMonth() == d2.getMonth() &&
			d1.getDate() == d2.getDate(); 
	}
	
	function parseDate(val) {
		
		if (val === undefined) { return; }
		if (val.constructor == Date) { return val; } 
		
		if (typeof val == 'string') {
			var dt = val.split(' ');
			// rfc3339?
			if(dt.length == 2){
				var eld = dt[0].split("-");		
				var elt = dt[1].split(":");
				if (eld.length == 3 & elt.length >= 2) {
					return new Date(integer(eld[0]), integer(eld[1]) -1, integer(eld[2]), integer(elt[0]), integer(elt[1]));
				}
			}
			
			
			// invalid offset
			if ( !(/^-?\d+$/).test(val) ) { return; }
			
			// convert to integer
			val = integer(val);
		}
		
		var date = new Date;
		date.setDate(date.getDate() + val);
		return date; 
	}
	
//}}}
		 
	
	function Datetimeinput(input, conf)  { 

		// variables
		var self = this,  
			 now = new Date,
			 yearNow = now.getFullYear(),
			 css = conf.css,
			 labels = LABELS[conf.lang],
			 root = $("#" + css.root),
			 title = root.find("#" + css.title),
			 trigger,
			 pm, nm, 
			 currYear, currMonth, currDay, currHours, currMinutes, currSeconds,
			 hourSelector,minuteSelector,
			 value = input.attr("data-value") || conf.value || input.val(), 
			 min = input.attr("min") || conf.min,  
			 max = input.attr("max") || conf.max,
			 opened,
			 original,
			 time = root.find("#" + css.time);

		// zero min is not undefined 	 
		if (min === 0) { min = "0"; }
		
		// use sane values for value, min & max		
		value = parseDate(value) || now;  
		
		min   = parseDate(min || new Date(yearNow + conf.yearRange[0], 1, 1));
		max   = parseDate(max || new Date( yearNow + conf.yearRange[1]+ 1, 1, -1));
		
		
		// check that language exists
		if (!labels) { throw "Datetimeinput: invalid language: " + conf.lang; }
		
		// Replace built-in date input: NOTE: input.attr("type", "text") throws exception by the browser
		if (input.attr("type") == 'date') {
			var original = input.clone(),
          def = original.wrap("<div/>").parent().html(),
          clone = $(def.replace(/type/i, "type=text data-orig-type"));
          
			if (conf.value) clone.val(conf.value);   // jquery 1.6.2 val(undefined) will clear val()
			
			input.replaceWith(clone);
			input = clone;
		}
		
		input.addClass(css.input);
		
		var fire = input.add(self);
			 
		// construct layout
		if (!root.length) {
			
			// root
			root = $('<div><div><a/><div/><a/></div><div><div/><div/></div><div><div/></div></div>')
				.hide().css({position: 'absolute'}).attr("id", css.root);			
						
			// elements
			root.children()
				.eq(0).attr("id", css.head).find("a").eq(0).attr("id", css.prev).end().eq(1).attr("id", css.next).end().end().end()
				.eq(1).attr("id", css.body).children()
					.eq(0).attr("id", css.days).end()
					.eq(1).attr("id", css.weeks).end().end().end()
				.eq(2).attr("id", css.foot).find("a");
					//.eq(0).attr("class", css.prev).end()
					//.eq(1).attr("class", css.next).end()
					//.eq(2).attr("class", css.prev).end()
					//.eq(3).attr("class", css.next).end().end();		 				  
			
			// title
			title = root.find("#" + css.head).find("div").attr("id", css.title);
			time = root.find("#" + css.foot).find("div").attr("id", css.time);
			
			// year & month selectors
			if (conf.selectors) {				
				var monthSelector = $("<select/>").attr("id", css.month),
					 yearSelector = $("<select/>").attr("id", css.year);				
				title.html(yearSelector.add(monthSelector));
			}			
			
			hourSelector = $("<select/>").attr("id", css.hour),
			minuteSelector = $("<select/>").attr("id", css.minute);
			time.append("时间：").append(hourSelector).append(" : ").append(minuteSelector);
			
			// day titles
			var days = root.find("#" + css.days); 
			
			// days of the week
			for (var d = 0; d < 7; d++) { 
				days.append($("<span/>").text(labels.shortDays[(d + conf.firstDay) % 7]));
			}
			
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
		var weeks = root.find("#" + css.weeks);
		yearSelector = root.find("#" + css.year);
		monthSelector = root.find("#" + css.month);
		hourSelector = root.find("#" + css.hour);
		minuteSelector = root.find("#" + css.minute);
			 
		
//{{{ pick
			 			 
		function select(date, conf, e) {  
			
			// current value
			value 	 = date;
			currYear  = date.getFullYear();
			currMonth = date.getMonth();
			currDay  = date.getDate();

			//date = new Date(currYear,currMonth,currDay,currHours,currMinutes);
			
			// beforChange
			e = e || $.Event("api");
			e.type = "beforeChange";
			
			fire.trigger(e, [date]);
			if (e.isDefaultPrevented()) { return; }
			
			// formatting			
			input.val(format(date, conf.format, conf.lang));
			
      // change
			e.type = "change";
			fire.trigger(e);
			
			// store value into input
			input.data("datetime", date);
			self.hide(e); 
		}
//}}}
		
		
//{{{ onShow

		function onShow(ev) {
			
			ev.type = "onShow";
			fire.trigger(ev);
			
			$(document).on("keydown.d", function(e) {
					
				if (e.ctrlKey) { return true; }				
				var key = e.keyCode;			 
				
				// backspace clears the value
				if (key == 8) {
					input.val("");
					return self.hide(e);	
				}
				
				// esc or tab key
				if (key == 27 || key == 9) { return self.hide(e); }						
					
				if ($(KEYS).index(key) >= 0) {
					
					if (!opened) { 
						self.show(e); 
						return e.preventDefault();
					} 
					
					var days = $("#" + css.weeks + " a"), 
						 el = $("." + css.focus),
						 index = days.index(el);
					 
					el.removeClass(css.focus);
					
					if (key == 74 || key == 40) { index += 7; }
					else if (key == 75 || key == 38) { index -= 7; }							
					else if (key == 76 || key == 39) { index += 1; }
					else if (key == 72 || key == 37) { index -= 1; }
					
					
					if (index > 41) {
						 self.addMonth();
						 el = $("#" + css.weeks + " a:eq(" + (index-42) + ")");
					} else if (index < 0) {
						 self.addMonth(-1);
						 el = $("#" + css.weeks + " a:eq(" + (index+42) + ")");
					} else {
						 el = days.eq(index);
					}
					
					el.addClass(css.focus);
					return e.preventDefault();
					
				}
			 
				// pageUp / pageDown
				if (key == 34) { return self.addMonth(); }
				if (key == 33) { return self.addMonth(-1); }
				
				// home
				if (key == 36) { return self.today(); }
				
				// enter
				if (key == 13) {
					if (!$(e.target).is("select")) {
						$("." + css.focus).click();
					}
				}
				
				return $([16, 17, 18, 9]).index(key) >= 0;  				
			});
			
			
			// click outside datetimeinput
			$(document).bind("click.d", function(e) {					
				var el = e.target;
				
				if (!$(el).parents("#" + css.root).length && el != input[0] && (!trigger || el != trigger[0])) {
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
				
				// onBeforeShow
				e = e || $.Event();
				e.type = "onBeforeShow";
				fire.trigger(e);
				if (e.isDefaultPrevented()) { return; }
			
				$.each(instances, function() {
					this.hide();	
				});
				
				opened = true;
				
				// month selector
				monthSelector.unbind("change").change(function() {
					self.setValue(yearSelector.val(), $(this).val());		
				});
				
				// year selector
				yearSelector.unbind("change").change(function() {
					self.setValue($(this).val(), monthSelector.val());		
				});
				
				// month selector
				minuteSelector.unbind("change").change(function() {
					self.setValue(currYear,currMonth,currDay, hourSelector.val(), $(this).val());		
				});
				
				// year selector
				hourSelector.unbind("change").change(function() {
					self.setValue(currYear,currMonth,currDay, $(this).val(), minuteSelector.val());		
				});
				
				// prev / next month
				pm = root.find("#" + css.prev).unbind("click").click(function(e) {
					if (!pm.hasClass(css.disabled)) {	
					  self.addMonth(-1);
					}
					return false;
				});
				
				nm = root.find("#" + css.next).unbind("click").click(function(e) {
					if (!nm.hasClass(css.disabled)) {
						self.addMonth();
					}
					return false;
				});	 
				
				// set date
				self.setValue(value);				 
				
				// show calendar
				var pos = input.offset();

				// iPad position fix
				if (/iPad/i.test(navigator.userAgent)) {
					pos.top -= $(window).scrollTop();
				}
				
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
      *   Set the value of the datetimeinput
      */
			setValue: function(year, month, day, hour, minute)  {
				
				var date = integer(month) >= -1 ? new Date(integer(year), 
						integer(month), 
						integer(day == undefined || isNaN(day) ? currDay : day),
						integer(hour == undefined || isNaN(hour) ? currHours : hour),
						integer(minute == undefined || isNaN(minute) ? currMinutes : minute)
						) : 
					year || value;				

				if (date < min) { date = min; }
				else if (date > max) { date = max; }

				// date given as ISO string
				if (typeof year == 'string') { date = parseDate(year); }
				
				year = date.getFullYear();
				month = date.getMonth();
				day = date.getDate(); 
				hour = date.getHours();
				minute = date.getMinutes();
				
				
				// roll year & month
				if (month == -1) {
					month = 11;
					year--;
				} else if (month == 12) {
					month = 0;
					year++;
				} 
				
				if (!opened) { 
					select(date, conf);
					return self; 
				} 				
				
				currMonth = month;
				currYear = year;
				currDay = day;
				currHours = hour;
				currMinutes = minute-minute%10;

				// variables
				var tmp = new Date(year, month, 1 - conf.firstDay), begin = tmp.getDay(),
					 days = dayAm(year, month),
					 prevDays = dayAm(year, month - 1),
					 week;	 
				
				// minute selector
				minuteSelector.empty();
				minuteSelector.append($("<option/>").html(0).attr("value", 0));
				minuteSelector.append($("<option/>").html(10).attr("value", 10));
				minuteSelector.append($("<option/>").html(20).attr("value", 20));
				minuteSelector.append($("<option/>").html(30).attr("value", 30));
				minuteSelector.append($("<option/>").html(40).attr("value", 40));
				minuteSelector.append($("<option/>").html(50).attr("value", 50));
				
				// hour selector
				hourSelector.empty();		
				var hourNow = now.getHours();
				
				for (var i = 0;  i < 24; i++) {
					hourSelector.append($("<option/>").text(i));
				}		
				
				minuteSelector.val(currMinutes);
				hourSelector.val(currHours);
				
				// selectors
				if (conf.selectors) { 
					
					// month selector
					monthSelector.empty();
					$.each(labels.months, function(i, m) {					
						if (min < new Date(year, i + 1, 1) && max > new Date(year, i, 0)) {
							monthSelector.append($("<option/>").html(m).attr("value", i));
						}
					});
					
					// year selector
					yearSelector.empty();		
					var yearNow = now.getFullYear();
					
					for (var i = yearNow + conf.yearRange[0];  i < yearNow + conf.yearRange[1]; i++) {
						if (min < new Date(i + 1, 0, 1) && max > new Date(i, 0, 0)) {
							yearSelector.append($("<option/>").text(i));
						}
					}		
					
					monthSelector.val(month);
					yearSelector.val(year);
					
				// title
				} else {
					title.html(labels.months[month] + " " + year);	
				} 	   
					 
				// populate weeks
				weeks.empty();				
				pm.add(nm).removeClass(css.disabled); 
				
				// !begin === "sunday"
				for (var j = !begin ? -7 : 0, a, num; j < (!begin ? 35 : 42); j++) { 
					
					a = $("<a/>");
					
					if (j % 7 === 0) {
						week = $("<div/>").addClass(css.week);
						weeks.append(week);			
					}					
					
					if (j < begin)  { 
						a.addClass(css.off); 
						num = prevDays - begin + j + 1;
						date = new Date(year, month-1, num);
						
					} else if (j >= begin + days)  {
						a.addClass(css.off);	
						num = j - days - begin + 1;
						date = new Date(year, month+1, num);
						
					} else  { 
						num = j - begin + 1;
						date = new Date(year, month, num);  
						
						// current date
						if (isSameDay(value, date)) {
							a.attr("id", css.current).addClass(css.focus);
							
						// today
						} else if (isSameDay(now, date)) {
							a.attr("id", css.today);
						}	 
					}
					
					// disabled
					if (min && date < min) {
						a.add(pm).addClass(css.disabled);						
					}
					
					if (max && date > max) {
						a.add(nm).addClass(css.disabled);						
					}
					
					a.attr("href", "#" + num).text(num).data("datetime", date);					
					
					week.append(a);
				}
				
				// date picking					
				weeks.find("a").click(function(e) {
					var el = $(this); 
					if (!el.hasClass(css.disabled)) {  
						$("#" + css.current).removeAttr("id");
						el.attr("id", css.current);	 
						var storeDate = el.data("datetime");
						storeDate.setHours(currHours);
						storeDate.setMinutes(currMinutes);
						select(storeDate, conf, e);
					}
					return false;
				});

				// sunday
				if (css.sunday) {
					weeks.find(css.week).each(function() {
						var beg = conf.firstDay ? 7 - conf.firstDay : 0;
						$(this).children().slice(beg, beg + 1).addClass(css.sunday);		
					});	
				} 
				
				return self;
			}, 
	//}}}
	
			setMin: function(val, fit) {
				min = parseDate(val);
				if (fit && value < min) { self.setValue(min); }
				return self;
			},
		
			setMax: function(val, fit) {
				max = parseDate(val);
				if (fit && value > max) { self.setValue(max); }
				return self;
			}, 
			
			today: function() {
				return self.setValue(now);	
			},
			
			addDay: function(amount) {
				return this.setValue(currYear, currMonth, currDay + (amount || 1));		
			},
			
			addMonth: function(amount) {
			  var targetMonth        = currMonth + (amount || 1),
		            daysInTargetMonth  = dayAm(currYear, targetMonth),
		            targetDay          = currDay <= daysInTargetMonth ? currDay : daysInTargetMonth;
		       
		      return this.setValue(currYear, targetMonth, targetDay);
			},
			
			addYear: function(amount) {
				return this.setValue(currYear + (amount || 1), currMonth, currDay);	
			},						
			
			destroy: function() {
				input.add(document).unbind("click.d").unbind("keydown.d");
				root.add(trigger).remove();
				input.removeData("datetimeinput").removeClass(css.input);
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
				return dateFormat ? format(value, dateFormat, conf.lang) : value;	
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
			
			// show datetimeinput & assign keyboard shortcuts
			input.bind("focus.d click.d", self.show).keydown(function(e) {
	
				var key = e.keyCode;
		
				// open datetimeinput with navigation keyw
				if (!opened &&  $(KEYS).index(key) >= 0) {
					self.show(e);
					return e.preventDefault();
				} 
				
				// allow tab
				return e.shiftKey || e.ctrlKey || e.altKey || key == 9 ? true : e.preventDefault();   
				
			});
		}
		
		// initial value 		
		if (parseDate(input.val())) {
			select(value, conf);
		}
		
	} 
	
	$.expr[':'].date = function(el) {
		var type = el.getAttribute("type");
		return type && type == 'datetime' || !!$(el).data("datetimeinput");
	};
	
	
	$.fn.datetimeinput = function(conf) {   
		// already instantiated
		if (this.data("datetimeinput")) { return this; } 
		
		// configuration
		conf = $.extend(true, {}, tool.conf, conf);		
		
		// CSS prefix
		$.each(conf.css, function(key, val) {
			if (!val && key != 'prefix') { 
				conf.css[key] = (conf.css.prefix || '') + (val || key);
			}
		});		
	
		var els;
		
		this.each(function() {									
			var el = new Datetimeinput($(this), conf);
			instances.push(el);
			var input = el.getInput().data("datetimeinput", el);
			els = els ? els.add(input) : input;	
		});		
	
		return els ? els : this;		
	}; 
	
	
}) (jQuery);
 
	
