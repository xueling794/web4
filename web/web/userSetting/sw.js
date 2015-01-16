/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-8-10
 * Time: 下午2:07
 * To change this template use File | Settings | File Templates.
 */
var input = document.getElementById("img_input");
var temp_img =document.getElementById('temp_img');
var display_img =document.getElementById('display_img');
var preview_img =document.getElementById('preview_img');
var jcropApi = null;

var boundx, boundy;

input.addEventListener( 'change',readFile,false );
function readFile(){
    temp_img.src = null;
    display_img.src = null;

    var file = this.files[0];

    if(jcropApi != null){
        jcropApi.destroy();
    }

    if(!/image\/\w+/.test(file.type)){
        alert("Type error");
        return false;
    }
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function(e){
        console.log(e);
        temp_img.src = this.result;
        var w = temp_img.width, h = temp_img.height;

        h = parseInt(display_img.width/w * h);
        w = display_img.width;


        var dataURL
        try {
            var canvas = document.createElement('canvas');
            canvas.width = w; canvas.height = h;
            var context = canvas.getContext('2d');
            context.drawImage(temp_img, 0, 0, temp_img.width, temp_img.height, 0, 0, w, h);
            dataURL = canvas.toDataURL(file.type);
            display_img.src = dataURL;
            //preview_img.src =dataURL;
            $("#display_img").css("height",h);
            $("#temp_img").css("display","none");
            $('#display_img').Jcrop({
                onChange: updatePreview,
                onSelect: updatePreview,
                bgFade:     true,
                bgOpacity: .2,
                setSelect: [ 10, 10, 190, 190 ],
                allowSelect:false,
                minSize: [ 80, 80 ],
                aspectRatio: 1
            },function(){
                var bounds = this.getBounds();
                boundx = bounds[0];
                boundy = bounds[1];
                jcropApi = this;
                //$preview.appendTo(jcropApi.ui.holder);
            });

            console.log(dataURL.length);
        } catch(e) {
            dataURL = null;
        }
        console.log(dataURL);
        /*result.innerHTML = '<img src="'+this.result+'" alt=""/>';
        img_area.innerHTML = '<div class="sitetip">img explore:</div><img src="'+this.result+'" alt=""/>';*/
    }
}
function updatePreview(c)
{
    var canvas = document.createElement('canvas');
    canvas.width = c.w; canvas.height = c.h;
    var context = canvas.getContext('2d');
    context.drawImage(display_img, c.x, c.y, c.w, c.h, 0, 0, 200, 200);
    var preDataURL = canvas.toDataURL();
    preview_img.src = preDataURL;
};
