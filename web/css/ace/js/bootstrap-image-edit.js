/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-8-6
 * Time: 下午11:07
 * To change this template use File | Settings | File Templates.
 */

(function ($) {
    var Image = function (options) {
        this.init('image', options, Image.defaults);

        if('on_error' in options.image) {
            this.on_error = options.image['on_error'];
            delete options.image['on_error']
        }
        if('on_success' in options.image) {
            this.on_success = options.image['on_success'];
            delete options.image['on_success']
        }
        if('max_size' in options.image) {
            this.max_size = options.image['max_size'];
            delete options.image['max_size']
        }

        this.initImage(options, Image.defaults);
    };

    $.fn.editableutils.inherit(Image, $.fn.editabletypes.abstractinput);

    $.extend(Image.prototype, {
        initImage: function(options, defaults) {
            this.options.image = $.extend({}, defaults.image, options.image);
            this.name = this.options.image.name || 'editable-image-input';
        },

        /**
         Renders input from tpl

         @method render()
         **/
        render: function() {
            var self = this;
            this.$input = this.$tpl.find('input[type=hidden]:eq(0)');
            this.$file = this.$tpl.find('input[type=file]:eq(0)');

            this.$file.attr({'name':this.name});
            this.$input.attr({'name':this.name+'-hidden'});


            this.options.image.before_change = this.options.image.before_change || function(files, dropped) {
                var file = files[0];
                if(typeof file === "string") {
                    //files is just a file name here (in browsers that don't support FileReader API)
                    if(! (/\.(jpe?g|png|gif)$/i).test(file) ) {
                        if(self.on_error) self.on_error(1);
                        return false;
                    }
                }
                else {//file is a File object
                    var type = $.trim(file.type);
                    if( ( type.length > 0 && ! (/^image\/(jpe?g|png|gif)$/i).test(type) )
                        || ( type.length == 0 && ! (/\.(jpe?g|png|gif)$/i).test(file.name) )//for android default browser!
                        )
                    {
                        if(self.on_error) self.on_error(1);
                        return false;
                    }
                    if( self.max_size && file.size > self.max_size ) {
                        if(self.on_error) self.on_error(2);
                        return false;
                    }
                }

                if(self.on_success) self.on_success();
                return true;
            }
            this.options.image.before_remove = this.options.image.before_remove || function() {
                self.$input.val(null);
                return true;
            }

            /*this.$file.ace_file_input(this.options.image).on('change', function(){
                var $rand = (self.$file.val() || self.$file.data('ace_input_files')) ? Math.random() + "" + (new Date()).getTime() : null;
                self.$input.val($rand)//set a random value, so that selected file is uploaded each time, even if it's the same file, because inline editable plugin does not update if the value is not changed!
            }).closest('.ace-file-input').css({'width':'100%'});*/
            this.$file.image_input(this.options.image);
        }

    });


    Image.defaults = $.extend({}, $.fn.editabletypes.abstractinput.defaults, {
        tpl: '<span><input type="hidden" /></span><span><input type="file" /></span>',
        inputclass: '',
        image:
        {
            style: 'well',
            btn_choose: '上传图片',
            btn_change: null,
            no_icon: 'icon- fa fa-picture-o',
            thumbnail: 'large'
        }
    });

    $.fn.editabletypes.image = Image;

}(window.jQuery));


(function ($) {
    var hasFileList = 'FileList' in window;//file list enabled in modern browsers
    var hasFileReader = 'FileReader' in window;

    var Image_Input = function(element , settings) {
        var self = this;
        this.settings = $.extend({}, $.fn.image_input.defaults, settings);

        this.$element = $(element);
        this.element = element;
        this.disabled = false;
        this.can_reset = true;

        this.$element.on('change.ace_inner_call', function(e , ace_inner_call){
            if(ace_inner_call === true) return;//this change event is called from above drop event
            return handle_on_change.call(self);
        });

        this.$element.wrap('<div class="ace-file-input" />');

        this.apply_settings();
    };
    Image_Input.error = {
        'FILE_LOAD_FAILED' : 1,
        'IMAGE_LOAD_FAILED' : 2,
        'THUMBNAIL_FAILED' : 3
    };
    Image_Input.prototype.apply_settings = function() {
        var self = this;
        var remove_btn = !!this.settings.icon_remove;


        this.well_style = this.settings.style == 'well';

        if(this.well_style) this.$element.parent().addClass('ace-file-multiple');
        else this.$element.parent().removeClass('ace-file-multiple');

        this.$element.parent().find(':not(input[type=file])').remove();//remove all except our input, good for when changing settings
        this.$element.after('<label class="file-label" data-title="'+this.settings.btn_choose+'"><span class="file-name" data-title="'+this.settings.no_file+'">'+(this.settings.no_icon ? '<i class="'+this.settings.no_icon+'"></i>' : '')+'</span></label>');
        this.$label = this.$element.next();

        this.$label.on('click', function(){//firefox mobile doesn't allow 'tap'!
            if(!this.disabled && !self.element.disabled && !self.$element.attr('readonly'))
                self.$element.click();
        })

        if(remove_btn) this.$label.next('a').on("click", function(){
            if(! self.can_reset ) return false;

            var ret = true;
            if(self.settings.before_remove) ret = self.settings.before_remove.call(self.element);
            if(!ret) return false;
            return self.reset_input();
        });

    }

    Image_Input.prototype.reset_input = function() {
        this.$label.attr({'data-title':this.settings.btn_choose, 'class':'file-label'})
            .find('.file-name:first').attr({'data-title':this.settings.no_file , 'class':'file-name'})
            .find('[class*="icon-"]').attr('class', this.settings.no_icon)
            .prev('img').remove();
        if(!this.settings.no_icon) this.$label.find('[class*="fa fa-"]').remove();

        this.$label.find('.file-name').not(':first').remove();

        if(this.$element.data('ace_input_files')) {
            this.$element.removeData('ace_input_files');
            this.$element.removeData('ace_input_method');
        }

        //this.reset_input_field();

        return false;
    }

    var handle_on_change = function() {
        var ret = true;
        if(this.settings.before_change) ret = this.settings.before_change.call(this.element, this.element.files || [this.element.value]/*make it an array*/, false);//false means files have been selected, not dropped
        if(!ret || ret.length == 0) {
            if(!this.$element.data('ace_input_files')) this.reset_input_field();//if nothing selected before, reset because of the newly unacceptable (ret=false||length=0) selection
            return false;
        }


        //user can return a modified File Array as result
        var files = !hasFileList ? null ://for old IE, etc
            ((ret instanceof Array || ret instanceof FileList) ? ret : this.element.files);
        this.$element.data('ace_input_method', 'select');


        if(files && files.length > 0) {//html5
            this.$element.data('ace_input_files', files);
        }
        else {
            var name = $.trim( this.element.value );
            if(name && name.length > 0) {
                files = []
                files.push(name);
                this.$element.data('ace_input_files', files);
            }
        }

        if(!files || files.length == 0) return false;
        this.show_file_list(files);

        return true;
    }

    Image_Input.prototype.show_file_list = function($files) {
        var files = typeof $files === "undefined" ? this.$element.data('ace_input_files') : $files;
        if(!files || files.length == 0) return;

        //////////////////////////////////////////////////////////////////

        if(this.well_style) {
            this.$label.find('.file-name').remove();
            if(!this.settings.btn_change) this.$label.addClass('hide-placeholder');
        }
        this.$label.attr('data-title', this.settings.btn_change).addClass('selected');

        for (var i = 0; i < files.length; i++) {
            var filename = typeof files[i] === "string" ? files[i] : $.trim( files[i].name );
            var index = filename.lastIndexOf("\\") + 1;
            if(index == 0)index = filename.lastIndexOf("/") + 1;
            filename = filename.substr(index);

            var fileIcon = 'icon-file';
            if((/\.(jpe?g|png|gif|svg|bmp|tiff?)$/i).test(filename)) {
                fileIcon = 'fa fa-picture-o';
            }
            else if((/\.(mpe?g|flv|mov|avi|swf|mp4|mkv|webm|wmv|3gp)$/i).test(filename)) fileIcon = 'icon-film';
            else if((/\.(mp3|ogg|wav|wma|amr|aac)$/i).test(filename)) fileIcon = 'icon-music';


            if(!this.well_style) this.$label.find('.file-name').attr({'data-title':filename}).find('[class*="icon-"]').attr('class', fileIcon);
            else {
               this.$label.append('<span class="file-name"  ><i class="'+fileIcon+'"></i></span>');
                var type = $.trim(files[i].type);
                var can_preview = hasFileReader && this.settings.thumbnail
                    &&
                    ( (type.length > 0 && type.match('image')) || (type.length == 0 && fileIcon == 'icon-picture') )//the second one is for Android's default browser which gives an empty text for file.type
                if(can_preview) {
                    var self = this;
                    $.when(preview_image.call(this, files[i])).fail(function(result){
                        //called on failure to load preview
                        if(self.settings.preview_error) self.settings.preview_error.call(self, filename, result.code);
                    });
                }
            }

        }

        return true;
    }
    var preview_image = function(file) {
        var self = this;
        var $span = self.$label.find('.file-name:last');//it should be out of onload, otherwise all onloads may target the same span because of delays

        var deferred = new $.Deferred
        var reader = new FileReader();
        reader.onload = function (e) {
            $span.prepend("<img id='editImg'  style='display:none;' />");
            var img = $span.find('img:last').get(0);

            $(img).one('load', function() {
                //if image loaded successfully
                var size = 50;
                if(self.settings.thumbnail == 'large') size = self.$label.width();
                else if(self.settings.thumbnail == 'fit') size =self.$label.width();

                $span.addClass( 'large');

                var thumb = get_thumbnail(img, size, file.type);
                if(thumb == null) {
                    //if making thumbnail fails
                    $(this).remove();
                    deferred.reject({code:Image_Input.error['THUMBNAIL_FAILED']});
                    return;
                }

                var w = thumb.w, h = thumb.h;
                if(self.settings.thumbnail == 'small') {w=h=size;};
                $(img).css({'background-image':'url('+thumb.src+')' , width:w, height:h})
                    .data('thumb', thumb.src)
                    .attr({src:'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg=='})
                    .show()
                addJcrop();
                ///////////////////
                deferred.resolve();
            }).one('error', function() {
                    //for example when a file has image extenstion, but format is something else
                    $span.find('img').remove();
                    deferred.reject({code:Image_Input.error['IMAGE_LOAD_FAILED']});
                });

            img.src = e.target.result;
        }
        reader.onerror = function (e) {
            deferred.reject({code:Image_Input.error['FILE_LOAD_FAILED']});
        }
        reader.readAsDataURL(file);

        return deferred.promise();
    }

    var get_thumbnail = function(img, size, type) {

        var w = img.width, h = img.height;

        h = parseInt(size/w * h);
        w = size;


        var dataURL
        try {
            var canvas = document.createElement('canvas');
            canvas.width = size; canvas.height = parseInt(size/w * h);
            var context = canvas.getContext('2d');
            context.drawImage(img, 0, 0, img.width, img.height, 0, 0, w, h);
            dataURL = canvas.toDataURL(/*type == 'image/jpeg' ? type : 'image/png', 10*/)
        } catch(e) {
            dataURL = null;
        }

        //there was only one image that failed in firefox completely randomly! so let's double check it
        if(!( /^data\:image\/(png|jpe?g|gif);base64,[0-9A-Za-z\+\/\=]+$/.test(dataURL)) ) dataURL = null;
        if(! dataURL) return null;

        return {src: dataURL, w:w, h:h};
    }
    var addJcrop = function(){
        var jcrop_api,
            boundx,
            boundy,

        // Grab some information about the preview pane
            $preview = $('#preview-pane'),
            $pcnt = $('#preview-pane .preview-container'),
            $pimg = $('#preview-pane .preview-container img'),

            xsize = $pcnt.width(),
            ysize = $pcnt.height();

        console.log('init',[xsize,ysize]);
        $('#editImg').Jcrop();
        /*$('#editImg').Jcrop({
            onChange: updatePreview,
            onSelect: updatePreview,
            minSize: [ 80, 80 ],
            aspectRatio: 1
        },function(){
            // Use the API to get the real image size
            var bounds = this.getBounds();
            boundx = bounds[0];
            boundy = bounds[1];
            // Store the API in the jcrop_api variable
            jcrop_api = this;
            jcrop_api.setSelect([20,20,200,200]);
            jcrop_api.setOptions({ bgFade: true });
            jcrop_api.ui.selection.addClass('jcrop-selection');
            // Move the preview into the jcrop container for css positioning
            $preview.appendTo(jcrop_api.ui.holder);
        });*/

        function updatePreview(c)
        {
            if (parseInt(c.w) > 0)
            {
                var rx = xsize / c.w;
                var ry = ysize / c.h;

                $pimg.css({
                    width: Math.round(rx * boundx) + 'px',
                    height: Math.round(ry * boundy) + 'px',
                    marginLeft: '-' + Math.round(rx * c.x) + 'px',
                    marginTop: '-' + Math.round(ry * c.y) + 'px'
                });
            }
        };
    }

    $.fn.image_input =function (option,value) {
        var retval;

        var $set = this.each(function () {
            var $this = $(this);
            var data = $this.data('ace_file_input');
            var options = typeof option === 'object' && option;

            if (!data) $this.data('ace_file_input', (data = new Image_Input(this, options)));
            if (typeof option === 'string') retval = data[option](value);
        });

        return (retval === undefined) ? $set : retval;
    };


    $.fn.image_input.defaults = {
        style:false,
        no_file:'没有图片文件 ...',
        no_icon:'fa fa-cloud-upload',
        btn_choose:'选择',
        btn_change:'修改',
        icon_remove:'fa fa-times',
        droppable:false,
        thumbnail:false,//large, fit, small
        width: '100%',

        //callbacks
        before_change:null,
        before_remove:null,
        preview_error:null
    }


}(window.jQuery));
