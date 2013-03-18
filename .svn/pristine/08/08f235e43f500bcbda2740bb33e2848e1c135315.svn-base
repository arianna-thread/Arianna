var arianna = (function (arianna) {
	"use strict";
	arianna.drawer = (function (drawer) {
		drawer.state = undefined;
		drawer.container = undefined;
		drawer.drawPath = undefined;
		drawer.pois = [];
		drawer.tooltipCallback = undefined;
		drawer.currNode = undefined;
		drawer.linea = undefined;



		//drawer.tooltips = [];
		var drawCurrNode = function () {
				drawer.currNode = document.getElementById("currNode");
				drawer.currNode.setAttribute("cx", drawer.state.currNode.x);
				drawer.currNode.setAttribute("cy", drawer.state.currNode.y);
				drawer.currNode.setAttribute("r", 10);
			};
		var removePois = function () {
				var container = document.getElementById("mapContainer");
				drawer.pois.forEach(function (e) {
					container.removeChild(e.el);
				});
			};

		var setVisiblePoi = function (value) {
				if(value) {
					if(!this.alreadyCreated) {
						drawer.removeTooltips();
						this.tooltip = document.createElement("div");
						this.tooltip.classList.add("tooltip");
						var arrow = document.createElement("img");
						arrow.classList.add("rightArrow");
						arrow.setAttribute("alt", "alt");
						arrow.setAttribute("src", "rightArrow.png");
						this.tooltip.textContent = this.title;
						this.tooltip.appendChild(arrow);
						var lato = this.el.width;
						var x = this.x + lato / 2 - 20;
						var y = this.y - lato - 90;
						this.tooltip.style.left = x + "px";
						this.tooltip.style.top = y + "px";
						this.tooltip.onclick = drawer.tooltipCallback.bind(this);
						drawer.container.appendChild(this.tooltip);
						this.alreadyCreated = true;
					} else {
						//this.tooltip.style.display ="true";
						this.tooltip.classList.remove("hide");
					}
					this.isvisible = true;
				} else {
					//this.tooltip.style.display = "none";
					//that=this
					this.tooltip.fade = function () {
						var o = this.style.opacity;
						if(o <= 0) {
							return;
						}
						this.style.opacity = o - 0.1;
						setTimeout(fade, 5);

					};
					this.tooltip.fade();



					this.isvisible = false;
				}

			};


		drawer.removeTooltips = function () {
			this.pois.forEach(function (e) {
				if(e.visible) {
					e.visible = !e.visible;
				}
			});
		};



		var drawPoiFunc = function (cursor) {

				this.el = document.createElement("img");
				this.el.classList.add("cursor");
				this.el.classList.add(cursor);
				var source = cursor + ".png";
				this.el.setAttribute("src", source);

				var lato = 72;
				this.el.style.width = lato + "px";
				this.el.style.height = lato + "px";



				var x = this.x - lato / 2 - 20;
				var y = this.y - lato;
				this.el.style.left = x + "px";
				this.el.style.top = y + "px";
				this.el.onclick = this.toogle.bind(this);
				return this.el;

			};



		drawer.drawPois = function () {
			var fragment = document.createDocumentFragment();

			var child;

			this.pois.forEach(function (p) {
				if(p.uri === (drawer.state.curr || -1)) {
					child = p.draw("curr");
				} else if(p.uri === (drawer.state.succ || -1)) {
					child = p.draw("succ");
				} else {
					child = p.draw("cursor");
				}
				fragment.appendChild(child);
			});
			drawer.container.appendChild(fragment);
		};



		drawer.drawPath = function (color) {
			drawer.linea = document.getElementById("linea");
			var start = document.getElementById("start");
			var end = document.getElementById("end");
			var stringa = drawer.path.points.map(function (e) {
				return e.x + " " + e.y;
			}).join(", ");

			drawer.linea.setAttribute("points", stringa);
			drawer.linea.setAttribute("style", 'stroke-linejoin:round;fill:none;stroke:red;stroke-width:8');

			start.setAttribute("cx", drawer.path.points[0].x);
			start.setAttribute("cy", drawer.path.points[0].y);
			start.setAttribute("r", 10);

			end.setAttribute("cx", drawer.path.points[drawer.path.points.length - 1].x);
			end.setAttribute("cy", drawer.path.points[drawer.path.points.length - 1].y);
			end.setAttribute("r", 10);
		};


		var getNodeIndexByUri = function (uri) {
				var filtered = drawer.path.points.filter(function (el, index) {
					return uri == el.uri;
				});
				return drawer.path.points.indexOf(filtered[0]);
			};

		var animateBetweenNodes = function (nodo1, nodo2, callbackAnimate) {
				var diff_x = nodo1.x - nodo2.x;
				var diff_y = nodo1.y - nodo2.y;
				var dimstep = 2;
				var speed = 5;
				var direction_x = diff_x && diff_x / Math.abs(diff_x);
				var direction_y = diff_y && diff_y / Math.abs(diff_y);


				var move = function () {
						var x = Number(drawer.currNode.getAttribute('cx'));
						var y = Number(drawer.currNode.getAttribute('cy'));
						var inc_x = x + direction_x;
						var inc_y = y + direction_y;

						drawer.currNode.setAttribute("cx", inc_x);
						drawer.currNode.setAttribute("cy", inc_y);
						if(nodo1.x * direction_x <= (nodo2.x) * direction_x && nodo1.y * direction_x <= (nodo2.v) * direction_y) {
							setTimeout(move, time);
						} else {
							callbackAnimate();
						}
					};
				move();

			};



		var animate = function (oldstate) {
				var start = getNodeIndexByUri(oldstate.curr);
				var end = getNodeIndexByUri(drawer.state.curr);
				//drawer.animationNode = document.getElementById("animationNode");
				var i;
				var direction;
				start < end ? direction = 1 : direction = -1;
				i = start;
				var nodoCurr = drawer.path.points[i];
				var dest = drawer.path.points[i + direction];
				var step = function () {
						i += direction;
						nodoCurr = drawer.path.points[i];
						dest = drawer.path.points[i + direction];
						if(nodoCurr.uri == drawer.pois[end].uri) {
							return;
						}
						animateBetweenNodes(nodoCurr, dest, step);
					};
				animateBetweenNodes(nodoCurr, dest, step);
			};


		drawer.setState = function (state) {

			var oldState = drawer.state;
			if(drawer.state) {
				drawer.state = state;
				removePois();
				animate(oldState);
				drawer.drawPois();

			}

			drawer.state = state;

			//drawer.drawPois (callback);
			//drawCurrNode();
		};



		drawer.init = function (idmap, path, pois, stato, callback) {
			drawer.container = document.getElementById("mapContainer");
			pois.forEach(function (p) {
				// for(i in pois) {
				var myPoi = Object.create(p);
				myPoi.draw = drawPoiFunc;
				myPoi.tooltipAlreadyCreated = false;
				myPoi.isvisible = false;
				myPoi.toogle = function () {
					this.visible = !this.visible;
				};
				Object.defineProperty(myPoi, "visible", {
					set: setVisiblePoi,
					get: function () {
						return this.isvisible;

					}
				});
				drawer.pois.push(myPoi);
			});
			drawer.tooltipCallback = callback;
			drawer.path = path;
			drawer.setState(stato);
			drawCurrNode();
		};

		return drawer;
	}(arianna.drawer || {}));

	return arianna;
}(arianna || {}));



window.onload = function () {
	var p0 = {
		uri: 1,
		x: 80,
		y: 830,
		title: "paolo"

	};

	var p1 = {
		uri: 2,
		x: 308,
		y: 830,
		title: "dani"
	};
	var p2 = {
		uri: 3,
		x: 308,
		y: 392,
		title: "mauri"

	};
	var p3 = {
		uri: 4,
		x: 185,
		y: 392,
		title: "mauri"
	};
	var p4 = {
		uri: 5,
		x: 430,
		y: 490,
		title: "gesu"
	};
	var p = [p0, p1, p2, p3, p4];

	var path = {
		points: p
	};


	stato1 = {
		curr: 1,
		succ: 5,
		currNode: p0

	};
	stato2 = {
		curr: 2,
		succ: 4,
		currNode: p1


	};

	stato3 = {
		curr: 3,
		succ: 4,
		currNode: p2


	};

	stato4 = {
		curr: 4,
		succ: 5,
		currNode: p3


	};
	stato5 = {
		curr: 5,
		succ: 2,
		currNode: p4


	};

	//document.body.innerHTML = AndroidFunction.returnPath();
	//console.info("puppa");
	//console.log("puppa");
	//console.warn("puppa");
	daJava = '{"points":[{"title":"pirla","uri":"1","x":80,"y":830},{"title":"pirla","uri":"2","x":308,"y":830},{"title":"pirla","uri":"3","x":308,"y":392},{"title":"pirla","uri":"4","x":185,"y":392},{"title":"pirla","uri":"5","x":185,"y":175},{"title":"pirla","uri":"16","x":308,"y":175},{"title":"pirla","uri":"7","x":308,"y":275},{"title":"pirla","uri":"8","x":550,"y":275},{"title":"pirla","uri":"9","x":550,"y":392},{"title":"pirla","uri":"10","x":430,"y":392},{"title":"pirla","uri":"11","x":430,"y":490}]}';
	//
	// document.body.innerHTML = daJava;
	//daJava = AndroidFunction.returnPath();
	//document.write (daJava);
	//a = JSON.parse( AndroidFunction.returnPath());
	a = JSON.parse(daJava);
	//document.write (a.points);
	//b=JSON.parse(AndroidFunction.returnPois());
	arianna.drawer.init("map", a, p, stato1, function () {
		var uri = this.title;
		//AndroidFunction.startDetail(uri);

	});
	arianna.drawer.drawPath();
	// mydrawer.drawPois(function () {
	// 	this.style.opacity = 0;
	// });
	arianna.drawer.drawPois();
	// AndroidFunction.startDetail();
};