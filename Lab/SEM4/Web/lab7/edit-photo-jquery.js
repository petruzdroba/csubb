$(document).ready(function () {
    let originalData = {};
    let isDirty      = false; // checks for unsaved changes
    let currentId    = null;

    function fetchPhoto(id) {
        $.ajax({
            url:      'api/photo_get.php',
            method:   'GET',
            data:     { id: id },
            dataType: 'json',
            success: function (data) {
                originalData = { trail_name: data.trail_name, description: data.description };
                $('#trail-name').val(data.trail_name);
                $('#description').val(data.description);
                $('#edit-form').show();
                isDirty = false;
                // disable save button until changes are made
                $('#btn-save').prop('disabled', true);
                $('#save-msg').text('');
            }
        });
    }

    function savePhoto(id, callback) {
        $.ajax({
            url:         'api/photo_update.php',
            method:      'POST',
            contentType: 'application/json',
            data:        JSON.stringify({
                id:          parseInt(id),
                trail_name:  $('#trail-name').val(),
                description: $('#description').val()
            }),
            dataType: 'json',
            success: function (data) {
                if (data.success) {
                    originalData = { trail_name: $('#trail-name').val(), description: $('#description').val() };
                    //reset value
                    isDirty      = false;
                    $('#btn-save').prop('disabled', true);
                    $('#save-msg').text('Saved successfully.');
                    $('#photo-select option[value="' + id + '"]')
                        .text('#' + id + ' - ' + $('#trail-name').val());
                    // execute callback if succ
                    if (typeof callback === 'function') callback();
                }
            },
            error: function () {
                $('#save-msg').text('Error saving.');
            }
        });
    }

    function hasChanges() {
        return $('#trail-name').val() !== originalData.trail_name ||
               $('#description').val() !== originalData.description;
    }

    $('#photo-select').on('change', function () {
        const newId = $(this).val();
        const oldId = currentId;

        if (!newId) {
            $('#edit-form').hide();
            return;
        }

        // if we switch and have unsaved
        if (isDirty && oldId) {
            const save = confirm('You have unsaved changes. Save before continuing?');
            if (save) {
                savePhoto(oldId, function () {
                    // save cur changes and fetch the swithed
                    currentId = newId;
                    fetchPhoto(newId);
                });
                return;
            }
        }

        currentId = newId;
        fetchPhoto(newId);
    });

    $('#trail-name, #description').on('input', function () {
        isDirty = hasChanges();
        $('#btn-save').prop('disabled', !isDirty);
        $('#save-msg').text('');
    });

    $('#btn-save').on('click', function () {
        // we dont do anythhing after success
        savePhoto(currentId, null);
    });
});