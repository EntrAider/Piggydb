// 
// A modified version of Namespace.js - https://github.com/smith/namespacedotjs
//

var piggydb = {};

piggydb.namespace = (function() {

	var _separator = '.';

	var _toArray = function(obj) {
		// checks if it's an array
		if (typeof(obj) == 'object' && obj.sort) {
			return obj;
		}
		return Array(obj);
	};

	//
	// Creates an object following the specified namespace identifier.
	//
	var _namespace = function(identifier) {
		var module = arguments[1] || false;
		var ns = window;
		
		if (identifier !== '') {
			var parts = identifier.split(_separator);
			for (var i = 0; i < parts.length; i++) {
				if (!ns[parts[i]]) {
					ns[parts[i]] = {};
				}
				ns = ns[parts[i]];
			}
		}
		
		if (module) {
			for (var propertyName in module) {
				if (module.hasOwnProperty(propertyName)) {
					ns[propertyName] = module[propertyName];
				}
			}
		}
		
		return ns;
	};

	//
	// Imports properties from the specified namespace to the global space (ie. under window)
	// 
	// The identifier string can contain the * wildcard character as its last segment (eg: com.test.*) 
	// which will import all properties from the namespace.
	// 
	// If not, the targeted namespace will be imported (ie. if com.test is imported, the test object 
	// will now be global). If the targeted object is not found, it will be included using include().
	//
	_namespace.import = function(identifier) {
		var identifiers = _toArray(identifier);
		var parts, target, ns;
		
		for (var i = 0; i < identifiers.length; i++) {
			identifier = identifiers[i];
		
			parts = identifier.split(_separator);
			target = parts.pop();
			ns = _namespace(parts.join(_separator));
		
			if (target == '*') {
				// imports all objects from the identifier, can't use include() in that case
				for (var objectName in ns) {
					if (ns.hasOwnProperty(objectName)) {
						window[objectName] = ns[objectName];
					}
				}
			} 
			else {
				// imports only one object
				if (ns[target]) {
					// the object exists, import it
					window[target] = ns[target];
				}
			}
		}
	};

	return _namespace;
})();


//
// Examples
//

piggydb.namespace("piggydb.util", {
	log: function(message) {
		jQuery("#console").append(message + "<br/>");
	}
});

