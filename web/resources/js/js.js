


function hide(name)
{
    $(name).css({
        display: "none"
    });


}

function show(nam)
{
    $(nam).css({
        display: "table-row"
    });


}


/* global SPAN, dataMessage, none */

$(function () {
    $(".accordion").accordion({
        active: false,
        collapsible: true
    });
    $(".accordion").accordion({
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
    var dp_now = new Date();
    var dp_today = new Date(dp_now.getFullYear(), dp_now.getMonth(), dp_now.getDate());
    $('.datepicker').datepicker({
        dateFormat: 'dd-mm-yy',
        minDate: dp_today,
        maxDate: '+3m'
    });


    $('.datepicker1').datepicker({
        dateFormat: 'dd-mm-yy'
    });

    // min and max selectable time
    $('.timepicker').timepicker({
        'minTime': '16:00',
        'maxTime': '23:30',
        'timeFormat': 'H:i'
    }
    );
});





