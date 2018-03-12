/**
 * @license Highcharts JS v2.2.1 (2012-03-15)
 * Exporting module
 *
 * (c) 2010-2011 Torstein Hønsi
 *
 * License: www.highcharts.com/license
 */

// JSLint options:
/*global Highcharts, document, window, Math, setTimeout */

(function () { // encapsulate

// create shortcuts
var HC = Highcharts,
	Chart = HC.Chart,
	addEvent = HC.addEvent,
	removeEvent = HC.removeEvent,
	createElement = HC.createElement,
	discardElement = HC.discardElement,
	css = HC.css,
	merge = HC.merge,
	each = HC.each,
	extend = HC.extend,
	math = Math,
	mathMax = math.max,
	doc = document,
	win = window,
	hasTouch = doc.documentElement.ontouchstart !== undefined,
	M = 'M',
	L = 'L',
	DIV = 'div',
	HIDDEN = 'hidden',
	NONE = 'none',
	PREFIX = 'highcharts-',
	ABSOLUTE = 'absolute',
	PX = 'px',
	UNDEFINED,
	defaultOptions = HC.getOptions();


// Buttons and menus are collected in a separate config option set called 'navigation'.
// This can be extended later to add control buttons like zoom and pan right click menus.
defaultOptions.navigation = {
	menuStyle: {
		border: '1px solid #A0A0A0',
		background: '#FFFFFF'
	},
	menuItemStyle: {
		padding: '0 5px',
		background: NONE,
		color: '#303030',
		fontSize: hasTouch ? '14px' : '11px'
	},
	menuItemHoverStyle: {
		background: '#4572A5',
		color: '#FFFFFF'
	},

	buttonOptions: {
		align: 'right',
		backgroundColor: {
			linearGradient: [0, 0, 0, 20],
			stops: [
				[0.4, '#F7F7F7'],
				[0.6, '#E3E3E3']
			]
		},
		borderColor: '#B0B0B0',
		borderRadius: 3,
		borderWidth: 1,
		//enabled: true,
		height: 20,
		hoverBorderColor: '#909090',
		hoverSymbolFill: '#81A7CF',
		hoverSymbolStroke: '#4572A5',
		symbolFill: '#E0E0E0',
		//symbolSize: 12,
		symbolStroke: '#A0A0A0',
		//symbolStrokeWidth: 1,
		symbolX: 11.5,
		symbolY: 10.5,
		verticalAlign: 'top',
		width: 24,
		y: 10
	}
};



// Add the export related options
defaultOptions.exporting = {
	//enabled: true,
	//filename: 'chart',
	type: 'image/png',
	url: 'http://export.highcharts.com/',
	width: 800,
	buttons: {
		exportButton: {
			//enabled: true,
			symbol: 'exportIcon',
			x: -10,
			symbolFill: '#A8BF77',
			hoverSymbolFill: '#768F3E',
			_id: 'exportButton',
			_titleKey: 'exportButtonTitle',
			menuItems: [{
				textKey: 'downloadPNG',
				onclick: function () {
					this.exportChart();
				}
			}, {
				textKey: 'downloadJPEG',
				onclick: function () {
					this.exportChart({
						type: 'image/jpeg'
					});
				}
			}, {
				textKey: 'downloadPDF',
				onclick: function () {
					this.exportChart({
						type: 'application/pdf'
					});
				}
			}, {
				textKey: 'downloadSVG',
				onclick: function () {
					this.exportChart({
						type: 'image/svg+xml'
					});
				}
			}
			// Enable this block to add "View SVG" to the dropdown menu
			/*
			,{

				text: 'View SVG',
				onclick: function () {
					var svg = this.getSVG()
						.replace(/</g, '\n&lt;')
						.replace(/>/g, '&gt;');

					doc.body.innerHTML = '<pre>' + svg + '</pre>';
				}
			} // */
			]

		},
		printButton: {
			//enabled: true,
			symbol: 'printIcon',
			x: -36,
			symbolFill: '#B5C9DF',
			hoverSymbolFill: '#779ABF',
			_id: 'printButton',
			_titleKey: 'printButtonTitle',
			onclick: function () {
				this.print();
			}
		}
	}
};




/**
 * Crisp for 1px stroke width, which is default. In the future, consider a smarter,
 * global function.
 */
function crisp(arr) {
	var i = arr.length;
	while (i--) {
		if (typeof arr[i] === 'number') {
			arr[i] = Math.round(arr[i]) - 0.5;		
		}
	}
	return arr;
}

// Create the export icon
HC.Renderer.prototype.symbols.exportIcon = function (x, y, width, height) {
	return crisp([
		M, // the disk
		x, y + width,
		L,
		x + width, y + height,
		x + width, y + height * 0.8,
		x, y + height * 0.8,
		'Z',
		M, // the arrow
		x + width * 0.5, y + height * 0.8,
		L,
		x + width * 0.8, y + height * 0.4,
		x + width * 0.4, y + height * 0.4,
		x + width * 0.4, y,
		x + width * 0.6, y,
		x + width * 0.6, y + height * 0.4,
		x + width * 0.2, y + height * 0.4,
		'Z'
	]);
};
// Create the print icon
HC.Renderer.prototype.symbols.printIcon = function (x, y, width, height) {
	return crisp([
		M, // the printer
		x, y + height * 0.7,
		L,
		x + width, y + height * 0.7,
		x + width, y + height * 0.4,
		x, y + height * 0.4,
		'Z',
		M, // the upper sheet
		x + width * 0.2, y + height * 0.4,
		L,
		x + width * 0.2, y,
		x + width * 0.8, y,
		x + width * 0.8, y + height * 0.4,
		'Z',
		M, // the lower sheet
		x + width * 0.2, y + height * 0.7,
		L,
		x, y + height,
		x + width, y + height,
		x + width * 0.8, y + height * 0.7,
		'Z'
	]);
};


// Add the buttons on chart load
Chart.prototype.callbacks.push(function (chart) {
	var n,
		exportingOptions = chart.options.exporting,
		buttons = exportingOptions.buttons;

	if (exportingOptions.enabled !== false) {

		for (n in buttons) {
		}

		// Destroy the export elements at chart destroy
		addEvent(chart, 'destroy', chart.destroyExport);
	}

});


}());
