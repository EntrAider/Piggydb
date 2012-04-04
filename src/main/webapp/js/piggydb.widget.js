piggydb.namespace("piggydb.widget", {
	
});



/**
 *  The base class for HTML widgets
 */
piggydb.widget.Widget = function(jQueryElement) {
	this.element = jQueryElement;
}
piggydb.widget.Widget.prototype = {
		
};



/**
 *  Facebox based on facebox (http://famspam.com/facebox)
 */
piggydb.widget.Facebox = function(id) {
	piggydb.widget.Widget.call(this, jQuery('\
<div id="' + id + '" class="facebox"> \
  <div class="popup"> \
    <table> \
      <tbody> \
        <tr> \
          <td class="tl"/><td class="b"/><td class="tr"/> \
        </tr> \
        <tr> \
          <td class="b"/> \
          <td class="body"> \
           <div class="header"> \
             <a href="#" class="close"> \
             <img src="images/large-delete.gif" title="close" class="close_image" alt="X"/></a> \
           </div> \
           <div class="content"></div> \
          </td> \
          <td class="b"/> \
        </tr> \
        <tr> \
          <td class="bl"/><td class="b"/><td class="br"/> \
        </tr> \
      </tbody> \
    </table> \
  </div> \
</div>'));
	
  this.id = id;
  this.body = this.element.find('.body');
  this.content = this.element.find('.content');
};
piggydb.widget.Facebox.prototype = jQuery.extend({
	
  show: function(url) {
    this.init();  
    this.loading();
    
    var outer = this;
    jQuery.get(url, function(data) { 
    	outer.reveal(data);
	  });
  },
  
  showImage: function(url) {
    this.init();  
    this.loading();
   
    var maxWidth = jQuery(window).width() - 80;
    var maxHeight = jQuery(window).height() - 120;
    var image = new Image();
    var outer = this;
    image.onload = function() {
    	outer.reveal('<div class="image"><img src="' + image.src + '" /></div>');
      var width = image.width < maxWidth ? image.width : maxWidth;
      var height = image.height < maxHeight ? image.height : maxHeight;
      if (width < maxWidth && height == maxHeight) width = Math.min(width + 15, maxWidth);
      if (height < maxHeight && width == maxWidth) height = Math.min(height + 15, maxHeight);
      outer.content.css({
	      'width':  width,
	      'height': height
	    });   
    };
    image.src = url;
  },
  
  init: function() {
    jQuery('#' + this.id).remove();
    this.content.empty();
    this.element.find(".close").click(this.onCloseClick);
    this.element.appendTo(jQuery('body'));
  },
  
  loading: function () {
	  if (this.element.find('.loading').length == 1) return true;

	  this.body.children().hide().end().
	  	append('<div class="loading"><img src="images/load.gif"/></div>'); 
	  this.element.show();
	},
	
	reveal: function (data) {
    this.content.append(data);
    this.element.find('.loading').remove();
    this.body.children().fadeIn('normal');
	},

  onCloseClick: function () {
    var element = jQuery(this).closest(".facebox");
    element.fadeOut(function() {
    	element.find('.loading').remove();
	  });
	  return false;
	}		
}, piggydb.widget.Widget.prototype);

