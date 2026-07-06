$(document).ready(function () {
    const K       = 3;
    let   offset  = 0;
    let   total   = 0;

    const isStaff = currentRole === 'staff';

    function cols() {
        return isStaff ? 6 : 5;
    }

    function fetchPage(newOffset) {
        // get request changed, use jquerys ajax 
        $.ajax({
            url:      'api/photos.php',
            method:   'GET',
            data:     { offset: newOffset },
            dataType: 'json',
            success: function (data) {
                total  = data.total;
                offset = data.offset;
                renderTable(data.records);
                updateNav();
            },
            error: function () {
                $('#photos-tbody').html('<tr><td colspan="' + cols() + '">Error loading data.</td></tr>');
            }
        });
    }

    function renderTable(records) {
        if (records.length === 0) {
            $('#photos-tbody').html('<tr><td colspan="' + cols() + '">No photos found.</td></tr>');
            return;
        }

        let rows = '';
        $.each(records, function (i, r) {
            const uploaderCell = isStaff
                ? '<td>' + $('<div>').text(r.uploader).html() + '</td>'
                : '';

            const canDelete = isStaff || (r.uploader_id === currentUserId);
            const deleteBtn = canDelete
                ? '<form method="POST" action="upload.php"'
                  + ' onsubmit="return confirm(\'Delete this photo?\')">'
                  + '<input type="hidden" name="action" value="delete">'
                  + '<input type="hidden" name="photo_id" value="' + r.id + '">'
                  + '<input type="hidden" name="csrf_token" value="' + csrfToken + '">'
                  + '<button type="submit" class="btn">Delete</button>'
                  + '</form>'
                : '—';

            // set text content to avoid xss
            const descCell = '<td>' + $('<div>').text(r.description).html() + '</td>';

            rows += '<tr>'
                + '<td>' + (offset + i + 1) + '</td>'
                + '<td>' + $('<div>').text(r.trail_name).html() + '</td>'
                + descCell
                + uploaderCell
                + '<td><img src="uploads/' + $('<div>').text(r.filename).html() + '"'
                +      ' style="height:60px;object-fit:cover;" alt="photo"></td>'
                + '<td>' + deleteBtn + '</td>'
                + '</tr>';
        });

        $('#photos-tbody').html(rows);
    }

    function updateNav() {
        const page  = Math.floor(offset / K) + 1;
        const pages = Math.ceil(total / K) || 1;
        $('#photos-info').text('Page ' + page + ' of ' + pages + ' (' + total + ' total)');

        $('#btn-prev').prop('disabled', offset === 0);
        $('#btn-next').prop('disabled', offset + K >= total);
    }

    $('#btn-prev').on('click', function () {
        if (offset - K >= 0) fetchPage(offset - K);
    });

    $('#btn-next').on('click', function () {
        if (offset + K < total) fetchPage(offset + K);
    });

    fetchPage(0);
});