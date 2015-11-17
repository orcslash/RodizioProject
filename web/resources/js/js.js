function hide(name)
{
       $(name).css({
           display:"none"
       });
        
    
}
/* global SPAN, dataMessage, none */

$(function () {
    $("#accordion").accordion({
        active: false,
        collapsible: true
    });
    $("#accordion").accordion({
        heightStyle: "content",
        autoHeight: false,
        clearStyle: true
    });
});

/**
 * Init datepicker and time picker, set date and time format
 */
$(function ()
{
    $('.datepicker').datepicker(
            {
                dateFormat: "dd-mm-yy"
            });

    // min and max selectable time
    $('.timepicker').timepicker({
        'minTime': '16:00',
        'maxTime': '23:30',
        'timeFormat': 'H:i'
    }
    );
});





